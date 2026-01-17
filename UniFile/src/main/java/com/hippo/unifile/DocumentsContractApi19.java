/*
 * Copyright (C) 2014 The Android Open Source Project
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

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.List;

@TargetApi(Build.VERSION_CODES.KITKAT)
final class DocumentsContractApi19 {
    private DocumentsContractApi19() {}

    private static final String TAG = DocumentsContractApi19.class.getSimpleName();

    private static final String AUTHORITY_DOCUMENT_EXTERNAL_STORAGE = "com.android.externalstorage.documents";
    private static final String AUTHORITY_DOCUMENT_DOWNLOAD = "com.android.providers.downloads.documents";
    private static final String AUTHORITY_DOCUMENT_MEDIA = "com.android.providers.media.documents";

    private static final String PROVIDER_INTERFACE = "android.content.action.DOCUMENTS_PROVIDER";

    private static final String PATH_DOCUMENT = "document";
    private static final String PATH_TREE = "tree";

    public static boolean isContentUri(@Nullable Uri uri) {
        return uri != null && ContentResolver.SCHEME_CONTENT.equals(uri.getScheme());
    }

    public static boolean isDocumentsProvider(Context context, String authority) {
        final Intent intent = new Intent(PROVIDER_INTERFACE);
        final List<ResolveInfo> infos = context.getPackageManager()
                .queryIntentContentProviders(intent, 0);
        for (ResolveInfo info : infos) {
            if (authority.equals(info.providerInfo.authority)) {
                return true;
            }
        }
        return false;
    }

    // It is different from DocumentsContract.isDocumentUri().
    // It accepts uri like content://com.android.externalstorage.documents/tree/primary%3AHaHa as well.
    public static boolean isDocumentUri(Context context, Uri self) {
        if (isContentUri(self) && isDocumentsProvider(context, self.getAuthority())) {
            final List<String> paths = self.getPathSegments();
            if (paths.size() == 2) {
                return PATH_DOCUMENT.equals(paths.get(0)) || PATH_TREE.equals(paths.get(0));
            } else if (paths.size() == 4) {
                return PATH_TREE.equals(paths.get(0)) && PATH_DOCUMENT.equals(paths.get(2));
            }
        }
        return false;
    }

    public static String getName(Context context, Uri self) {
        return Contracts.queryForString(context, self, DocumentsContract.Document.COLUMN_DISPLAY_NAME, null);
    }

    private static String getRawType(Context context, Uri self) {
        return Contracts.queryForString(context, self, DocumentsContract.Document.COLUMN_MIME_TYPE, null);
    }

    public static String getType(Context context, Uri self) {
        final String rawType = getRawType(context, self);
        if (DocumentsContract.Document.MIME_TYPE_DIR.equals(rawType)) {
            return null;
        } else {
            return rawType;
        }
    }

    public static String getFilePath(Context context, Uri self) {
        if (self == null) {
            return null;
        }

        try {
            final String authority = self.getAuthority();
            if (AUTHORITY_DOCUMENT_EXTERNAL_STORAGE.equals(authority)) {
                // Get type and path
                final String docId = DocumentsContract.getDocumentId(self);
                final String[] split = docId.split(":");
                final String type = split[0];
                final String path = split[1];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + path;
                } else {
                    // Get the storage path
                    File[] cacheDirs = context.getExternalCacheDirs();
                    String storageDir = null;
                    for (File cacheDir : cacheDirs) {
                        final String cachePath = cacheDir.getPath();
                        int index = cachePath.indexOf(type);
                        if (index >= 0) {
                            storageDir = cachePath.substring(0, index + type.length());
                        }
                    }

                    if (storageDir != null) {
                        return storageDir + "/" + path;
                    } else {
                        return null;
                    }
                }
            } else if (AUTHORITY_DOCUMENT_DOWNLOAD.equals(authority)) {
                final String id = DocumentsContract.getDocumentId(self);

                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return Contracts.queryForString(context, contentUri, MediaStore.MediaColumns.DATA, null);
            } else if (AUTHORITY_DOCUMENT_MEDIA.equals(authority)) {
                // Get type and id
                final String docId = DocumentsContract.getDocumentId(self);
                final String[] split = docId.split(":");
                final String type = split[0];
                final String id = split[1];

                final Uri baseUri;
                if ("image".equals(type)) {
                    baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    baseUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    baseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    Log.d(TAG, "Unknown type in " + AUTHORITY_DOCUMENT_MEDIA + ": " + type);
                    return null;
                }

                final Uri contentUri = ContentUris.withAppendedId(baseUri, Long.valueOf(id));

                // Requires android.permission.READ_EXTERNAL_STORAGE or return null
                return Contracts.queryForString(context, contentUri, MediaStore.MediaColumns.DATA, null);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isDirectory(Context context, Uri self) {
        return DocumentsContract.Document.MIME_TYPE_DIR.equals(getRawType(context, self));
    }

    public static boolean isFile(Context context, Uri self) {
        final String type = getRawType(context, self);
        return !(DocumentsContract.Document.MIME_TYPE_DIR.equals(type) || TextUtils.isEmpty(type));
    }

    public static long lastModified(Context context, Uri self) {
        return Contracts.queryForLong(context, self, DocumentsContract.Document.COLUMN_LAST_MODIFIED, -1L);
    }

    public static long length(Context context, Uri self) {
        return Contracts.queryForLong(context, self, DocumentsContract.Document.COLUMN_SIZE, -1L);
    }

    public static boolean canRead(Context context, Uri self) {
        // Ignore if grant doesn't allow read
        if (context.checkCallingOrSelfUriPermission(self, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        // Ignore documents without MIME
        return !TextUtils.isEmpty(getRawType(context, self));
    }

    public static boolean canWrite(Context context, Uri self) {
        // Ignore if grant doesn't allow write
        if (context.checkCallingOrSelfUriPermission(self, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        final String type = getRawType(context, self);
        final int flags = Contracts.queryForInt(context, self, DocumentsContract.Document.COLUMN_FLAGS, 0);

        // Ignore documents without MIME
        if (TextUtils.isEmpty(type)) {
            return false;
        }

        // Deletable documents considered writable
        if ((flags & DocumentsContract.Document.FLAG_SUPPORTS_DELETE) != 0) {
            return true;
        }

        if (DocumentsContract.Document.MIME_TYPE_DIR.equals(type)
                && (flags & DocumentsContract.Document.FLAG_DIR_SUPPORTS_CREATE) != 0) {
            // Directories that allow create considered writable
            return true;
        } else if (!TextUtils.isEmpty(type)
                && (flags & DocumentsContract.Document.FLAG_SUPPORTS_WRITE) != 0) {
            // Writable normal files considered writable
            return true;
        }

        return false;
    }

    public static boolean delete(Context context, Uri self) {
        try {
            return DocumentsContract.deleteDocument(context.getContentResolver(), self);
        } catch (Exception e) {
            // Maybe user ejects tf card
            Log.e(TAG, "Failed to renameTo", e);
            return false;
        }
    }

    public static boolean exists(Context context, Uri self) {
        final ContentResolver resolver = context.getContentResolver();

        Cursor c = null;
        try {
            c = resolver.query(self, new String[] {
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID }, null, null, null);
            return null != c && c.getCount() > 0;
        } catch (Exception e) {
            // Log.w(TAG, "Failed query: " + e);
            return false;
        } finally {
            Contracts.closeQuietly(c);
        }
    }
}
