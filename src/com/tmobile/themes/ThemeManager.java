/*
 * Copyright (C) 2010, T-Mobile USA, Inc.
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

package com.tmobile.themes;

import android.content.Intent;
import android.net.Uri;

public interface ThemeManager {

    /**
     * Commonly passed between activities.
     *
     * @see com.tmobile.thememanager.provider.ThemeItem
     */
    public static final String EXTRA_THEME_ITEM = "theme_item";

    /**
     * Permission required to send a broadcast to the ThemeManager requesting
     * theme change. This permission is not required to fire a chooser for
     * {@link #ACTION_SET_THEME}, which presents the ThemeManager's normal UI.
     */
    public static final String PERMISSION_CHANGE_THEME = "com.tmobile.permission.CHANGE_THEME";

    /**
     * Broadcast intent to use to change theme without going through the normal
     * ThemeManager UI.  Requires {@link #PERMISSION_SET_THEME}.
     */
    public static final String ACTION_CHANGE_THEME = "com.tmobile.intent.action.CHANGE_THEME";

    /**
     * Broadcast intent fired on theme change.
     */
    public static final String ACTION_THEME_CHANGED = "com.tmobile.intent.action.THEME_CHANGED";

    /**
     * Similar to {@link Intent#ACTION_SET_WALLPAPER}.
     */
    public static final String ACTION_SET_THEME = "com.tmobile.intent.action.SET_THEME";

    /**
     * URI for the item which should be checked in both the theme and style
     * choosers. If null, will use the current global theme.
     */
    public static final String EXTRA_THEME_EXISTING_URI = "com.tmobile.intent.extra.theme.EXISTING_URI";

    /**
     * URI for the profile in which the current theme is associated. If null, we will apply against the
     * currently applied profile.
     */
    public static final String EXTRA_THEME_PROFILE_URI = "com.tmobile.intent.extra.theme.THEME_PROFILE_URI";

    /**
     * Boolean indicating whether the "extended" theme change API should be
     * supported. This API is a convenience for profile change and is not used
     * during normal theme or style changes.
     * <p>
     * Without this set, {@link #EXTRA_WALLPAPER_URI},
     * {@link #EXTRA_DONT_SET_LOCK_WALLPAPER}, {@link #EXTRA_LOCK_WALLPAPER_URI},
     * {@link #EXTRA_RINGTONE_URI}, and {@link #EXTRA_NOTIFICATION_RINGTONE_URI}
     * will not be observed.
     */
    public static final String EXTRA_EXTENDED_THEME_CHANGE = "com.tmobile.intent.extra.theme.EXTENDED_THEME_CHANGE";

    public static final String EXTRA_WALLPAPER_URI = "com.tmobile.intent.extra.theme.WALLPAPER_URI";
    public static final String EXTRA_DONT_SET_LOCK_WALLPAPER = "com.tmobile.intent.extra.theme.DONT_SET_LOCK_WALLPAPER";
    public static final String EXTRA_LOCK_WALLPAPER_URI = "com.tmobile.intent.extra.theme.LOCK_WALLPAPER_URI";
    public static final String EXTRA_LIVE_WALLPAPER_COMPONENT = "com.tmobile.intent.extra.theme.LIVE_WALLPAPER_COMPONENT";
    public static final String EXTRA_RINGTONE_URI = "com.tmobile.intent.extra.theme.RINGTONE_URI";
    public static final String EXTRA_NOTIFICATION_RINGTONE_URI = "com.tmobile.intent.extra.theme.NOTIFICATION_RINGTONE_URI";

    /**
     * The Android ringtone manager returns a null Uri for silent.
     * Use this Uri in the local provider to better track silent.
     */
    public static final Uri SILENT_RINGTONE_URI = Uri.parse("content://com.tmobile.thememanager/ringtone/silent");
}
