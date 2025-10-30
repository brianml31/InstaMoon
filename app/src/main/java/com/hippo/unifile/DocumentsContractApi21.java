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
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
final class DocumentsContractApi21 {
    private DocumentsContractApi21() {}

    private static final String TAG = DocumentsContractApi21.class.getSimpleName();

    private static final String PATH_DOCUMENT = "document";
    private static final String PATH_TREE = "tree";

    public static boolean isTreeDocumentUri(Context context, Uri self) {
        if (DocumentsContractApi19.isContentUri(self) &&
                DocumentsContractApi19.isDocumentsProvider(context, self.getAuthority())) {
            final List<String> paths = self.getPathSegments();
            if (paths.size() == 2) {
                return PATH_TREE.equals(paths.get(0));
            } else if (paths.size() == 4) {
                return PATH_TREE.equals(paths.get(0)) && PATH_DOCUMENT.equals(paths.get(2));
            }
        }
        return false;
    }

    public static Uri createFile(Context context, Uri self, String mimeType,
            String displayName) {
        try {
            return DocumentsContract.createDocument(context.getContentResolver(), self, mimeType,
                    displayName);
        } catch (Exception e) {
            // Maybe user ejects tf card
            Log.e(TAG, "Failed to createFile", e);
            return null;
        }
    }

    public static Uri createDirectory(Context context, Uri self, String displayName) {
        return createFile(context, self, DocumentsContract.Document.MIME_TYPE_DIR, displayName);
    }

    public static Uri prepareTreeUri(Uri treeUri) {
        String documentId;
        try {
            documentId = DocumentsContract.getDocumentId(treeUri);
            if (documentId == null) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            // IllegalArgumentException will be raised
            // if DocumentsContract.getDocumentId() failed.
            // But it isn't mentioned the document,
            // catch all kinds of Exception for safety.
            documentId = DocumentsContract.getTreeDocumentId(treeUri);
        }
        return DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId);
    }

    public static Uri[] listFiles(Context context, Uri self) {
        final ContentResolver resolver = context.getContentResolver();
        final Uri childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(self,
                DocumentsContract.getDocumentId(self));
        final ArrayList<Uri> results = new ArrayList<>();

        Cursor c = null;
        try {
            c = resolver.query(childrenUri, new String[] {
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID }, null, null, null);
            if (null != c) {
                while (c.moveToNext()) {
                    final String documentId = c.getString(0);
                    final Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(self,
                            documentId);
                    results.add(documentUri);
                }
            }
        } catch (Exception e) {
            // Log.w(TAG, "Failed query: " + e);
        } finally {
            closeQuietly(c);
        }

        return results.toArray(new Uri[results.size()]);
    }

    public static Uri renameTo(Context context, Uri self, String displayName) {
        try {
            return DocumentsContract.renameDocument(context.getContentResolver(), self, displayName);
        } catch (Exception e) {
            // Maybe user ejects tf card
            Log.e(TAG, "Failed to renameTo", e);
            return null;
        }
    }

    private static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }
}
