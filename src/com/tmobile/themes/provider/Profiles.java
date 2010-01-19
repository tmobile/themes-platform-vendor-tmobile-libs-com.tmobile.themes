package com.tmobile.themes.provider;

import com.tmobile.themes.ProfileManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class Profiles {

    public static final String AUTHORITY = "com.tmobile.profilemanager.profiles";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/profiles");
    private static final Uri BASE_SET_ACTIVE_PROFILE_URI =
        Uri.parse("content://" + AUTHORITY + "/set_active_profile");

    public static final Uri RESTORE_CONTENT_URI =
        Uri.parse("content://" + AUTHORITY + "/restore_backup");

    private Profiles(){}

    /* package */ static Uri makeSetActiveProfileUri(Context context, long profileId) {
        return BASE_SET_ACTIVE_PROFILE_URI.buildUpon()
            .appendEncodedPath(String.valueOf(profileId))
            .build();
    }

    public static Uri getProfileUri(Context context, long profileId) {
        return CONTENT_URI.buildUpon()
            .appendPath(String.valueOf(profileId)).build();
    }

    public static Cursor listProfiles(Context context) {
        return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
    }

    public static Cursor getActiveProfile(Context context) {
        Cursor c = context.getContentResolver().query(CONTENT_URI,
                null, ProfileColumns.IS_ACTIVE + "=1",
                null, null);
        if (c.getCount() == 0) {
            Cursor allProfiles = listProfiles(context);
            try {
                ProfileItem item = ProfileItem.getInstance(allProfiles);
                if (item != null) {
                    markActiveProfile(context, item.getId());
                } else {
                    throw new IllegalStateException("No profiles in provider.");
                }
            } finally {
                allProfiles.close();
            }
            c.requery();
//            if (c.getCount() == 0) {
//                Log.e(ProfileManager.TAG, "BUG: markActiveProfile failed?");
//            }
        }
        return c;
    }

    public static void markActiveProfile(Context context, long profileId) {
        /*
         * This is a special URI we use only to mark active profile. This is
         * necessary to make the operations atomic in terms of the cursor
         * notification. If we notify the cursor first to set all profiles
         * inactive, we risk a requery race condition revealing no active
         * profiles. Making these two operations atomically paired prevents this
         * issue as we only notify after the new active profile has been marked.
         */
        context.getContentResolver().update(makeSetActiveProfileUri(context, profileId),
                null, null, null);
    }

    public static void deleteProfile(Context context, int profileId) {
        context.getContentResolver().delete(
                CONTENT_URI, ProfileColumns._ID + " = " + profileId,
                null);
    }

    /**
     * Request a profile change by broadcasting to the ProfileManager. Must hold
     * permission {@link Constants#PERMISSION_CHANGE_PROFILE}.
     */
    public static void changeProfile(Context context, Uri profileUri) {
        context.sendOrderedBroadcast(new Intent(ProfileManager.ACTION_CHANGE_PROFILE,
                profileUri), null);
    }

    public interface ProfileColumns {
        public static final String CONTENT_TYPE = "vnd.tmobile.cursor.dir/profile";
        public static final String CONTENT_ITEM_TYPE = "vnd.tmobile.cursor.item/profile";

        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String THEME_URI = "theme_uri";
        public static final String APPEARANCE_URI = "appearance_uri";
        public static final String RINGTONE_URI = "ringtone_uri";
        public static final String NOTIFICATION_URI = "notification_uri";
        public static final String WALLPAPER_URI = "wallpaper_uri";
        public static final String LOCK_WALLPAPER_URI = "lock_wallpaper_uri";
        public static final String SCENE_ID = "scene_id";
        public static final String IS_ACTIVE = "is_active";

        public static final String DEFAULT_SORT_ORDER = "name ASC";
    }

}
