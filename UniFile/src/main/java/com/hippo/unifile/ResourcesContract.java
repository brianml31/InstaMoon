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

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

final class ResourcesContract {
    private ResourcesContract() {}

    public static OpenResourceResult openResource(Context context, Uri uri) {
        String authority = uri.getAuthority();
        Resources r;
        if (TextUtils.isEmpty(authority)) {
            return null;
        } else {
            try {
                r = context.getPackageManager().getResourcesForApplication(authority);
            } catch (PackageManager.NameNotFoundException ex) {
                return null;
            }
        }
        List<String> path = uri.getPathSegments();
        if (path == null) {
            return null;
        }
        int len = path.size();
        int id;
        String name;
        if (len == 1) {
            try {
                id = Integer.parseInt(path.get(0));
            } catch (NumberFormatException e) {
                return null;
            }
            try {
                name = r.getResourceEntryName(id);
            } catch (Resources.NotFoundException e) {
                return null;
            }
        } else if (len == 2) {
            name = path.get(1);
            id = r.getIdentifier(path.get(1), path.get(0), authority);
        } else {
            return null;
        }
        if (id == 0 || name == null) {
            return null;
        }
        OpenResourceResult res = new OpenResourceResult();
        res.r = r;
        res.p = authority;
        res.id = id;
        res.name = name;
        return res;
    }

    public static class OpenResourceResult {
        public Resources r;
        public String p;
        public int id;
        public String name;
    }
}
