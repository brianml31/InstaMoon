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

/*
 * Created by Hippo on 11/23/2016.
 */

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class ResourceFile extends UniFile {

    private final Resources mR;
    private final String mP;
    private final int mId;
    private final String mName;

    ResourceFile(Resources r, String p, int id, String name) {
        super(null);
        mR = r;
        mP = p;
        mId = id;
        mName = name;
    }

    @Override
    public UniFile createFile(String displayName) {
        return null;
    }

    @Override
    public UniFile createDirectory(String displayName) {
        return null;
    }

    @NonNull
    @Override
    public Uri getUri() {
        return new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(mP)
                .path(Integer.toString(mId))
                .build();
    }

    @Nullable
    @Override
    public String getName() {
        return mName;
    }

    @Nullable
    @Override
    public String getType() {
        // Can't get type, just return application/octet-stream
        return "application/octet-stream";
    }

    @Nullable
    @Override
    public String getFilePath() {
        return null;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public boolean isFile() {
        return true;
    }

    @Override
    public long lastModified() {
        return -1;
    }

    @Override
    public long length() {
        return -1;
    }

    @Override
    public boolean canRead() {
        return true;
    }

    @Override
    public boolean canWrite() {
        return false;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Nullable
    @Override
    public UniFile[] listFiles() {
        return null;
    }

    @Nullable
    @Override
    public UniFile[] listFiles(FilenameFilter filter) {
        return null;
    }

    @Nullable
    @Override
    public UniFile findFile(String displayName) {
        return null;
    }

    @Override
    public boolean renameTo(String displayName) {
        return false;
    }

    @NonNull
    @Override
    public OutputStream openOutputStream() throws IOException {
        throw new IOException("Can't open OutputStream from resource file.");
    }

    @NonNull
    @Override
    public OutputStream openOutputStream(boolean append) throws IOException {
        throw new IOException("Can't open OutputStream from resource file.");
    }

    @NonNull
    @Override
    public InputStream openInputStream() throws IOException {
        try {
            return mR.openRawResource(mId);
        } catch (Resources.NotFoundException e) {
            throw new IOException("Can't open InputStream");
        }
    }

    @NonNull
    @Override
    public UniRandomAccessFile createRandomAccessFile(String mode) throws IOException {
        if (!"r".equals(mode)) {
            throw new IOException("Unsupported mode: " + mode);
        }

        AssetFileDescriptor afd;
        try {
            afd = mR.openRawResourceFd(mId);
        } catch (Resources.NotFoundException e) {
            throw new IOException("Can't open AssetFileDescriptor");
        }
        if (afd == null) {
            throw new IOException("Can't open AssetFileDescriptor");
        }

        return new RawRandomAccessFile(TrickRandomAccessFile.create(afd, mode));
    }
}
