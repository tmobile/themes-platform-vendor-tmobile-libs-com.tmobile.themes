package com.tmobile.themes;

import android.content.Intent;

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
    public static final String EXTRA_RINGTONE_URI = "com.tmobile.intent.extra.theme.RINGTONE_URI";
    public static final String EXTRA_NOTIFICATION_RINGTONE_URI = "com.tmobile.intent.extra.theme.NOTIFICATION_RINGTONE_URI";

    
}
