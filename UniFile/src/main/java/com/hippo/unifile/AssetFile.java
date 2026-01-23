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
 * Created by Hippo on 11/16/2016.
 */

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

class AssetFile extends UniFile {

    private final AssetManager mAssetManager;
    private final String mPath;

    AssetFile(UniFile parent, AssetManager assetManager, String path) {
        super(parent);
        mAssetManager = assetManager;
        mPath = path;
    }

    @Override
    public UniFile createFile(String displayName) {
        UniFile file = findFile(displayName);
        if (file != null && file.isFile()) {
            return file;
        } else {
            return null;
        }
    }

    @Override
    public UniFile createDirectory(String displayName) {
        UniFile file = findFile(displayName);
        if (file != null && file.isDirectory()) {
            return file;
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public Uri getUri() {
        return new Uri.Builder()
                .scheme(ContentResolver.SCHEME_FILE)
                .authority("")
                .path("android_asset/" + mPath)
                .build();
    }

    @Nullable
    @Override
    public String getName() {
        final int index = mPath.lastIndexOf('/');
        if (index >= 0 && index < mPath.length() - 1) {
            return mPath.substring(index + 1);
        } else {
            return mPath;
        }
    }

    @Nullable
    @Override
    public String getType() {
        if (isDirectory()) {
            return null;
        } else {
            return Utils.getTypeForName(getName());
        }
    }

    @Nullable
    @Override
    public String getFilePath() {
        // Not supported
        return null;
    }

    @Override
    public boolean isDirectory() {
        try {
            String[] files = mAssetManager.list(mPath);
            return files != null && files.length > 0;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isFile() {
        InputStream is;
        try {
            is = openInputStream();
        } catch (IOException e) {
            return false;
        }
        IOUtils.closeQuietly(is);
        return true;
    }

    @Override
    public long lastModified() {
        // Not supported
        return -1;
    }

    @Override
    public long length() {
        // Not supported
        return -1;
    }

    @Override
    public boolean canRead() {
        return isFile();
    }

    @Override
    public boolean canWrite() {
        return false;
    }

    @Override
    public boolean delete() {
        // Not supported
        return false;
    }

    @Override
    public boolean exists() {
        return isDirectory() || isFile();
    }

    @Nullable
    @Override
    public UniFile[] listFiles() {
        try {
            String[] files = mAssetManager.list(mPath);
            if (files == null || files.length == 0) {
                return null;
            }

            int length = files.length;
            UniFile[] results = new UniFile[length];
            for (int i = 0; i < length; i++) {
                results[i] = new AssetFile(this, mAssetManager, Utils.resolve(mPath, files[i]));
            }
            return results;
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public UniFile[] listFiles(FilenameFilter filter) {
        if (filter == null) {
            return listFiles();
        }

        try {
            String[] files = mAssetManager.list(mPath);
            if (files == null || files.length == 0) {
                return null;
            }

            int length = files.length;
            final ArrayList<UniFile> results = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                String name = files[i];
                if (filter.accept(this, name)) {
                    results.add(new AssetFile(this, mAssetManager, Utils.resolve(mPath, files[i])));
                }
            }
            return results.toArray(new UniFile[results.size()]);
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public UniFile findFile(String displayName) {
        if (TextUtils.isEmpty(displayName)) {
            return null;
        }

        try {
            String[] files = mAssetManager.list(mPath);
            if (files == null || files.length == 0) {
                return null;
            }

            for (String f : files) {
                if (displayName.equals(f)) {
                    return new AssetFile(this, mAssetManager, Utils.resolve(mPath, displayName));
                }
            }

            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean renameTo(String displayName) {
        // Not supported
        return false;
    }

    @NonNull
    @Override
    public OutputStream openOutputStream() throws IOException {
        throw new IOException("Not support OutputStream for asset file.");
    }

    @NonNull
    @Override
    public OutputStream openOutputStream(boolean append) throws IOException {
        throw new IOException("Not support OutputStream for asset file.");
    }

    @NonNull
    @Override
    public InputStream openInputStream() throws IOException {
        return mAssetManager.open(mPath);
    }

    @NonNull
    @Override
    public UniRandomAccessFile createRandomAccessFile(String mode) throws IOException {
        if (!"r".equals(mode)) {
            throw new IOException("Unsupported mode: " + mode);
        }

        AssetFileDescriptor afd = mAssetManager.openFd(mPath);
        if (afd == null) {
            throw new IOException("Can't open AssetFileDescriptor");
        }

        return new RawRandomAccessFile(TrickRandomAccessFile.create(afd, mode));
    }
}
