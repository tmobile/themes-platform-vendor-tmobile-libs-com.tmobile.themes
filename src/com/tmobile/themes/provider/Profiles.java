/*
TERMS AND CONDITIONS

This software code is (C) 2009 T-Mobile USA, Inc. All Rights Reserved.

Unauthorized redistribution or further use of this material is prohibited
without the express permission of T-Mobile USA, Inc. and will be prosecuted
to the fullest extent of the law.

Removal or modification of these Terms and Conditions from the source or
binary code of this software is prohibited.  In the event that redistribution
of the source or binary code for this software is approved by T-Mobile USA, Inc.,
these Terms and Conditions and the above copyright notice must be reproduced in
their entirety and in all circumstances.

No name or trademarks of T-Mobile USA, Inc., or of its parent company, Deutsche
Telekom AG or any Deutsche Telekom or T-Mobile entity, may be used to endorse
or promote products derived from this software without specific prior written
permission.

THIS SOFTWARE IS PROVIDED ON AN "AS IS" AND "WITH ALL FAULTS" BASIS AND WITHOUT
WARRANTIES OF ANY KIND.  ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS OR
WARRANTIES, INCLUDING ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE, OR NON-INFRINGEMENT CONCERNING THIS SOFTWARE, ITS SOURCE OR
BINARY CODE OR ANY DERIVATIVES THEREOF ARE HEREBY EXCLUDED.  T-MOBILE USA, INC.
AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.  IN
NO EVENT WILL T-MOBILE USA, INC. OR ITS LICENSORS BE LIABLE FOR LOST REVENUE, PROFIT
OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF T-MOBILE USA, INC. HAS BEEN
ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.  

THESE TERMS AND CONDITIONS APPLY SOLELY AND EXCLUSIVELY TO THE USE, MODIFICATION
OR DISTRIBUTION OF THIS SOFTWARE, ITS SOURCE OR BINARY CODE OR ANY DERIVATIVES
THEREOF, AND ARE SEPARATE FROM ANY WRITTEN WARRANTY THAT MAY BE PROVIDED WITH A
DEVICE YOU PURCHASE FROM T-MOBILE USA, INC., AND TO THE EXTENT PERMITTED BY LAW.  
*/

package com.tmobile.themes.provider;

import com.tmobile.themes.ProfileManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

/**
 * A simple helper class the provides an easy way of working with profiles.
 * @author T-Mobile USA
 */
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

    /**
     * Creates a {@link Uri} for a profile for the given profile id.
     * @param context the context of the caller.
     * @param profileId the profile id
     * @return a {@link Uri} for the given profile id
     */
    public static Uri getProfileUri(Context context, long profileId) {
        return CONTENT_URI.buildUpon()
            .appendPath(String.valueOf(profileId)).build();
    }

    /**
     * Gets a {@link Cursor} for all profiles in the provider.
     * @param context the context of the caller.
     * @return a {@link Cursor} for all profiles or null if no profiles exists.
     */
    public static Cursor listProfiles(Context context) {
        return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
    }

    /**
     * Gets a {@link Cursor} for the current active profile. If no profile is active,
     * it marks the first profile as active and returns it's {@link Cursor}.
     * @param context the Context of the caller.
     * @return a {@link Cursor} for the active profile or null if no profiles exists.
     */
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
                    return null;
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

    /**
     * Marks the given profile id as the active profile.
     * @param context the context of the caller.
     * @param profileId the profile id to mark active.
     */
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

    /**
     * Deletes the given profile Id.
     * @param context context the context of the caller.
     * @param profileId the profile id to delete.
     */
    public static void deleteProfile(Context context, int profileId) {
        context.getContentResolver().delete(
                CONTENT_URI, ProfileColumns._ID + " = " + profileId,
                null);
    }

    /**
     * Request a profile change by broadcasting to the ProfileManager. Must hold
     * permission {@link ProfileManager#PERMISSION_CHANGE_PROFILE}.
     * @param context context the context of the caller.
     * @param profileUri the profile {@link Uri} to change to.
     */
    public static void changeProfile(Context context, Uri profileUri) {
        context.sendOrderedBroadcast(new Intent(ProfileManager.ACTION_CHANGE_PROFILE,
                profileUri), null);
    }

    /**
     * The Columns in the profiles provider database
     */
    public interface ProfileColumns {
        public static final String CONTENT_TYPE = "vnd.tmobile.cursor.dir/profile";
        public static final String CONTENT_ITEM_TYPE = "vnd.tmobile.cursor.item/profile";

        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String THEME_URI = "theme_uri";
        public static final String APPEARANCE_URI = "appearance_uri";
        public static final String RINGTONE_URI = "ringtone_uri";
        public static final String NOTIFICATION_URI = "notification_uri";
        public static final String WALLPAPER_URI = "wallpaper_uri";
        public static final String LOCK_WALLPAPER_URI = "lock_wallpaper_uri";
        public static final String CALL_FORWARDING_ENABLED = "call_forwarding_enabled";
        public static final String EMAIL_NOTIFICATION_ON = "email_notification_on";
        public static final String SCENE_ID = "scene_id";
        public static final String IS_ACTIVE = "is_active";
        public static final String IS_RESTRICTED = "is_restricted";
        public static final String PRELOAD_ID = "preload_id";
        public static final String DEFAULT_SORT_ORDER = "is_active DESC, preload_id DESC, name";
    }

}
