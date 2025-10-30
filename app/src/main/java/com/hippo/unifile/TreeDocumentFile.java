/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hippo.unifile;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

class TreeDocumentFile extends UniFile {

    private static final String TAG = TreeDocumentFile.class.getSimpleName();

    private final Context mContext;
    private Uri mUri;

    TreeDocumentFile(UniFile parent, Context context, Uri uri) {
        super(parent);
        mContext = context.getApplicationContext();
        mUri = uri;
    }

    @Override
    public UniFile createFile(String displayName) {
        if (TextUtils.isEmpty(displayName)) {
            return null;
        }

        UniFile child = findFile(displayName);

        if (child != null) {
            if (child.isFile()) {
                return child;
            } else {
                Log.w(TAG, "Try to create file " + displayName + ", but it is not file");
                return null;
            }
        } else {
            // FIXME There's nothing about display name and extension mentioned in document.
            // But it works for com.android.externalstorage.documents.
            // The safest way is use application/octet-stream all the time,
            // But media store will not be updated.
            int index = displayName.lastIndexOf('.');
            if (index > 0) {
                String name = displayName.substring(0, index);
                String extension = displayName.substring(index + 1);
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                if (!TextUtils.isEmpty(mimeType)) {
                    final Uri result = DocumentsContractApi21.createFile(mContext, mUri, mimeType, name);
                    return (result != null) ? new TreeDocumentFile(this, mContext, result) : null;
                }
            }

            // Not dot in displayName or dot is the first char or can't get MimeType
            final Uri result = DocumentsContractApi21.createFile(mContext, mUri, "application/octet-stream", displayName);
            return (result != null) ? new TreeDocumentFile(this, mContext, result) : null;
        }
    }

    @Override
    public UniFile createDirectory(String displayName) {
        if (TextUtils.isEmpty(displayName)) {
            return null;
        }

        UniFile child = findFile(displayName);

        if (child != null) {
            if (child.isDirectory()) {
                return child;
            } else {
                return null;
            }
        } else {
            final Uri result = DocumentsContractApi21.createDirectory(mContext, mUri, displayName);
            return (result != null) ? new TreeDocumentFile(this, mContext, result) : null;
        }
    }

    @NonNull
    @Override
    public Uri getUri() {
        return mUri;
    }

    @Override
    public String getName() {
        return DocumentsContractApi19.getName(mContext, mUri);
    }

    @Override
    public String getType() {
        final String type = DocumentsContractApi19.getType(mContext, mUri);
        if (!TextUtils.isEmpty(type)) {
            return type;
        } else {
            return Utils.getTypeForName(getName());
        }
    }

    @Nullable
    @Override
    public String getFilePath() {
        return DocumentsContractApi19.getFilePath(mContext, mUri);
    }

    @Override
    public boolean isDirectory() {
        return DocumentsContractApi19.isDirectory(mContext, mUri);
    }

    @Override
    public boolean isFile() {
        return DocumentsContractApi19.isFile(mContext, mUri);
    }

    @Override
    public long lastModified() {
        return DocumentsContractApi19.lastModified(mContext, mUri);
    }

    @Override
    public long length() {
        if (isDirectory()) {
            return -1L;
        } else {
            return DocumentsContractApi19.length(mContext, mUri);
        }
    }

    @Override
    public boolean canRead() {
        return DocumentsContractApi19.canRead(mContext, mUri);
    }

    @Override
    public boolean canWrite() {
        return DocumentsContractApi19.canWrite(mContext, mUri);
    }

    @Override
    public boolean delete() {
        return DocumentsContractApi19.delete(mContext, mUri);
    }

    @Override
    public boolean exists() {
        return DocumentsContractApi19.exists(mContext, mUri);
    }

    @Override
    public UniFile[] listFiles() {
        if (!isDirectory()) {
            return null;
        }

        final Uri[] result = DocumentsContractApi21.listFiles(mContext, mUri);
        final UniFile[] resultFiles = new UniFile[result.length];
        for (int i = 0, n = result.length; i < n; i++) {
            Uri uri = result[i];
            resultFiles[i] = new TreeDocumentFile(this, mContext, uri);
        }
        return resultFiles;
    }

    @Override
    public UniFile[] listFiles(FilenameFilter filter) {
        if (filter == null) {
            return listFiles();
        }

        if (!isDirectory()) {
            return null;
        }

        final Uri[] uris = DocumentsContractApi21.listFiles(mContext, mUri);
        final ArrayList<UniFile> results = new ArrayList<>();
        for (Uri uri : uris) {
            String name = DocumentsContractApi19.getName(mContext, uri);
            if (name != null && filter.accept(this, name)) {
                results.add(new TreeDocumentFile(this, mContext, uri));
            }
        }
        return results.toArray(new UniFile[results.size()]);
    }

    @Override
    public UniFile findFile(String displayName) {
        if (TextUtils.isEmpty(displayName)) {
            return null;
        }

        if (!isDirectory()) {
            return null;
        }

        final Uri[] result = DocumentsContractApi21.listFiles(mContext, mUri);
        for (Uri uri : result) {
            final String name = DocumentsContractApi19.getName(mContext, uri);
            if (displayName.equals(name)) {
                return new TreeDocumentFile(this, mContext, uri);
            }
        }
        return null;
    }

    @Override
    public boolean renameTo(String displayName) {
        final Uri result = DocumentsContractApi21.renameTo(mContext, mUri, displayName);
        if (result != null) {
            mUri = result;
            return true;
        } else {
            return false;
        }
    }

    @NonNull
    @Override
    public OutputStream openOutputStream() throws IOException {
        if (isDirectory()) {
            throw new IOException("Can't open OutputStream from a directory");
        }

        OutputStream os;
        try {
            os = mContext.getContentResolver().openOutputStream(mUri);
        } catch (Exception e) {
            throw new IOException("Can't open OutputStream");
        }
        if (os == null) {
            throw new IOException("Can't open OutputStream");
        }
        return os;
    }

    @NonNull
    @Override
    public OutputStream openOutputStream(boolean append) throws IOException {
        if (isDirectory()) {
            throw new IOException("Can't open OutputStream from a directory");
        }

        OutputStream os;
        try {
            os = mContext.getContentResolver().openOutputStream(mUri, append ? "wa" : "w");
        } catch (Exception e) {
            throw new IOException("Can't open OutputStream");
        }
        if (os == null) {
            throw new IOException("Can't open OutputStream");
        }
        return os;
    }

    @NonNull
    @Override
    public InputStream openInputStream() throws IOException {
        if (isDirectory()) {
            throw new IOException("Can't open InputStream from a directory");
        }

        InputStream is;
        try {
            is = mContext.getContentResolver().openInputStream(mUri);
        } catch (Exception e) {
            throw new IOException("Can't open InputStream");
        }
        if (is == null) {
            throw new IOException("Can't open InputStream");
        }
        return is;
    }

    @NonNull
    @Override
    public UniRandomAccessFile createRandomAccessFile(String mode) throws IOException {
        // Check file
        if (!isFile()) {
            throw new IOException("Can't make sure it is file");
        }

        ParcelFileDescriptor pfd;
        try {
            pfd = mContext.getContentResolver().openFileDescriptor(mUri, mode);
        } catch (Exception e) {
            throw new IOException("Can't open ParcelFileDescriptor");
        }
        if (pfd == null) {
            throw new IOException("Can't open ParcelFileDescriptor");
        }

        return new RawRandomAccessFile(TrickRandomAccessFile.create(pfd, mode));
    }
}
