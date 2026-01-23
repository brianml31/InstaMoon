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
 * Created by Hippo on 11/19/2016.
 */

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;

class Utils {
    private Utils() {}

    @Nullable
    static String getTypeForName(String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }

        final int lastDot = name.lastIndexOf('.');
        if (lastDot >= 0) {
            final String extension = name.substring(lastDot + 1).toLowerCase();
            final String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (!TextUtils.isEmpty(mime)) {
                return mime;
            }
        }

        return "application/octet-stream";
    }

    /**
     * A normal Unix pathname does not contain consecutive slashes and does not end
     * with a slash. The empty string and "/" are special cases that are also
     * considered normal.
     */
    static String normalize(String pathname) {
        int n = pathname.length();
        char[] normalized = pathname.toCharArray();
        int index = 0;
        char prevChar = 0;
        for (int i = 0; i < n; i++) {
            char current = normalized[i];
            // Remove duplicate slashes.
            if (!(current == '/' && prevChar == '/')) {
                normalized[index++] = current;
            }

            prevChar = current;
        }

        // Omit the trailing slash, except when pathname == "/".
        if (prevChar == '/' && n > 1) {
            index--;
        }

        return (index != n) ? new String(normalized, 0, index) : pathname;
    }

    // Invariant: Both |parent| and |child| are normalized paths.
    static String resolve(String parent, String child) {
        if (child.length() == 0 || child.equals("/")) {
            return parent;
        }

        if (child.charAt(0) == '/') {
            if (parent.equals("/")) return child;
            return parent + child;
        }

        if (parent.equals("/")) return parent + child;
        return parent + '/' + child;
    }
}
