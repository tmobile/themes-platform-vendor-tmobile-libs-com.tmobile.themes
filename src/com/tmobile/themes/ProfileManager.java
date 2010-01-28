package com.tmobile.themes;

import android.net.Uri;

public interface ProfileManager {

    public static final String PERMISSION_CHANGE_PROFILE = "com.tmobile.permission.CHANGE_PROFILE";

    public static final String ACTION_CHANGE_PROFILE = "com.tmobile.intent.action.CHANGE_PROFILE";

    public static final String ACTION_PROFILE_CHANGED = "com.tmobile.intent.action.PROFILE_CHANGED";
    
    public static final Uri SILENT_RINGTONE_URI = Uri.parse("content://com.tmobile.thememanager/ringtone/silent");

}
