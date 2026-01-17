/*
 * Copyright 2016 Hippo Seven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hippo.unifile;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressLint("SoonBlockedPrivateApi")
final class TrickRandomAccessFile extends RandomAccessFile {

    private static final String TAG = TrickRandomAccessFile.class.getSimpleName();

    private static final Field FIELD_FD;
    private static final Method METHOD_CLOSE;

    static {
        Field field;
        try {
            field = RandomAccessFile.class.getDeclaredField("fd");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "Can't get field RandomAccessFile.fd : " + e);
            field = null;
        }
        FIELD_FD = field;

        Method method;
        try {
            Class<?> clazz = Class.forName("libcore.io.IoUtils");
            method = clazz.getMethod("close", FileDescriptor.class);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Can't get class libcore.io.IoUtils: " + e);
            method = null;
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "Can't get method libcore.io.IoUtils.close(FileDescriptor): " + e);
            method = null;
        }
        METHOD_CLOSE = method;
    }

    private ParcelFileDescriptor mPfd;
    private AssetFileDescriptor mAfd;

    private TrickRandomAccessFile(String mode) throws FileNotFoundException {
        // /dev/random is only a temp file to create TrickRandomAccessFile object
        super("/dev/random", mode);
    }

    @Override
    public void close() throws IOException {
        if (mPfd != null) {
            mPfd.close();
            mPfd = null;
        }
        if (mAfd != null) {
            mAfd.close();
            mAfd = null;
        }
        super.close();
    }

    private static void checkReflection() throws IOException {
        // Check reflection stuff
        if (FIELD_FD == null || METHOD_CLOSE == null) {
            throw new IOException("Can't get reflection stuff");
        }
    }

    @NonNull
    static RandomAccessFile create(ParcelFileDescriptor pfd, String mode) throws IOException {
        if (pfd == null) {
            throw new IOException("ParcelFileDescriptor is null");
        }

        checkReflection();

        try {
            FileDescriptor fd = pfd.getFileDescriptor();
            if (fd == null) {
                throw new IOException("Can't get FileDescriptor");
            }

            TrickRandomAccessFile file = create(fd, mode);
            file.mPfd = pfd;
            return file;
        } catch (IOException e) {
            // Close ParcelFileDescriptor if failed
            pfd.close();
            throw e;
        }
    }

    @NonNull
    static RandomAccessFile create(AssetFileDescriptor afd, String mode) throws IOException {
        if (afd == null) {
            throw new IOException("AssetFileDescriptor is null");
        }

        checkReflection();

        try {
            FileDescriptor fd = afd.getFileDescriptor();
            if (fd == null) {
                throw new IOException("Can't get FileDescriptor");
            }

            TrickRandomAccessFile file = create(fd, mode);
            file.mAfd = afd;
            return file;
        } catch (IOException e) {
            // Close AssetFileDescriptor if failed
            afd.close();
            throw e;
        }
    }

    @NonNull
    private static TrickRandomAccessFile create(FileDescriptor fd, String mode) throws IOException {
        // Create TrickRandomAccessFile object
        TrickRandomAccessFile file;
        try {
            file = new TrickRandomAccessFile(mode);
        } catch (FileNotFoundException e) {
            throw new IOException("Can't create TrickRandomAccessFile");
        }

        // Close old FileDescriptor
        try {
            Object obj = FIELD_FD.get(file);
            if (obj instanceof FileDescriptor) {
                METHOD_CLOSE.invoke(null, (FileDescriptor) obj);
            }
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Failed to invoke libcore.io.IoUtils.close(FileDescriptor): " + e);
            file.close();
            throw new IOException(e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e(TAG, "Failed to invoke libcore.io.IoUtils.close(FileDescriptor): " + e);
            file.close();
            throw new IOException(e.getMessage());
        }

        // Set new FileDescriptor
        try {
            FIELD_FD.set(file, fd);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            file.close();
            throw new IOException(e.getMessage());
        }

        return file;
    }
}
