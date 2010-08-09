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

import com.tmobile.themes.provider.Profiles.ProfileColumns;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * <p>Simple data access object designed to wrap a cursor returned from any of the
 * Profiles class APIs.  Can be used efficiently with a custom CursorAdapter.</p>
 * 
 * <h2>Usage</h2>
 * <p>Here is an example of looping through a Cursor with ProfileItem:</p>
 * <pre class="prettyprint">
 *      Cursor c = Profiles.listProfiles(myContext);
 *      ProfileItem item = ProfileItem(c);
 *      if (item != null) {
 *          try {
 *              do {
 *                  //Do something with the item
 *              } while (c.moveToNext());
 *          } finally {
 *              item.close();
 *          }
 *      }
 * </pre>
 */
public class ProfileItem extends AbstractDAOItem {
    private int mColumnId;
    private int mColumnName;
    private int mColumnDescription;
    private int mColumnThemeUri;
    private int mColumnAppearanceUri;
    private int mColumnRingtoneUri;
    private int mColumnNotificationUri;
    private int mColumnWallpaperUri;
    private int mColumnLockWallpaperUri;
    private int mColumnCallFowardingEnabled;
    private int mColumnEmailNotificationsOn;
    private int mColumnSceneId;
    private int mColumnIsActive;
    private int mColumnIsRestrictedScene;
    private int mColumnPreloadId;
    private int mColumnAutoswitchType;
    private int mColumnStartTime;
    private int mColumnEndTime;
    private int mColumnEncodedDow;
    private int mColumnLocationGeocode;
    private int mColumnLocationName;

    private static final AbstractDAOItem.Creator<ProfileItem> CREATOR =
            new AbstractDAOItem.Creator<ProfileItem>() {
        @Override
        public ProfileItem init(Cursor c) {
            return new ProfileItem(c);
        }
    };

    /**
     * {@inheritDoc}
     */
    public static ProfileItem getInstance(Context context, Uri uri) {
        return CREATOR.newInstance(context, uri);
    }

    /**
     * {@inheritDoc}
     */
    public static ProfileItem getInstance(Cursor c) {
        return CREATOR.newInstance(c);
    }

    /**
     * {@inheritDoc}
     */
    public ProfileItem(Cursor c) {
        super(c);
        mColumnId = c.getColumnIndex(ProfileColumns._ID);
        mColumnName = c.getColumnIndex(ProfileColumns.NAME);
        mColumnDescription = c.getColumnIndex(ProfileColumns.DESCRIPTION);
        mColumnThemeUri = c.getColumnIndex(ProfileColumns.THEME_URI);
        mColumnAppearanceUri = c.getColumnIndex(ProfileColumns.APPEARANCE_URI);
        mColumnRingtoneUri = c.getColumnIndex(ProfileColumns.RINGTONE_URI);
        mColumnNotificationUri = c.getColumnIndex(ProfileColumns.NOTIFICATION_URI);
        mColumnWallpaperUri = c.getColumnIndex(ProfileColumns.WALLPAPER_URI);
        mColumnLockWallpaperUri = c.getColumnIndex(ProfileColumns.LOCK_WALLPAPER_URI);
        mColumnCallFowardingEnabled  = c.getColumnIndex(ProfileColumns.CALL_FORWARDING_ENABLED);
        mColumnEmailNotificationsOn = c.getColumnIndex(ProfileColumns.EMAIL_NOTIFICATION_ON);
        mColumnSceneId = c.getColumnIndex(ProfileColumns.SCENE_ID);
        mColumnIsActive = c.getColumnIndex(ProfileColumns.IS_ACTIVE);
        mColumnIsRestrictedScene = c.getColumnIndex(ProfileColumns.IS_RESTRICTED);
        mColumnPreloadId = c.getColumnIndex(ProfileColumns.PRELOAD_ID);
        mColumnAutoswitchType = c.getColumnIndex(ProfileColumns.AUTOSWITCH_TYPE);
        mColumnStartTime = c.getColumnIndex(ProfileColumns.AUTOSWITCH_TIME_START);
        mColumnEndTime = c.getColumnIndex(ProfileColumns.AUTOSWITCH_TIME_END);
        mColumnEncodedDow = c.getColumnIndex(ProfileColumns.AUTOSWITCH_ENCODED_DOW);
        mColumnLocationGeocode = c.getColumnIndex(ProfileColumns.AUTOSWITCH_LOCATION_GEOCODE);
        mColumnLocationName = c.getColumnIndex(ProfileColumns.AUTOSWITCH_LOCATION_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Uri getUri(Context context) {
        return Profiles.getProfileUri(context, getId());
    }

    /**
     * @return the profile id
     */
    public int getId() {
        return mCursor.getInt(mColumnId);
    }

    /**
     * @return the profile name
     */
    public String getName() {
        return mCursor.getString(mColumnName);
    }

    /**
     * @return the profile description
     */
    public String getDescription() {
        return mCursor.getString(mColumnDescription);
    }

    /**
     * @return the theme Uri
     */
    public Uri getThemeUri() {
        return parseUriNullSafe(mCursor.getString(mColumnThemeUri));
    }
    
    /**
     * @return the appearance Uri
     */
    public Uri getAppearanceUri() {
        return parseUriNullSafe(mCursor.getString(mColumnAppearanceUri));
    }

    /**
     * @return the ringtone Uri
     */
    public Uri getRingtoneUri() {
        return parseUriNullSafe(mCursor.getString(mColumnRingtoneUri));
    }

    /**
     * @return the notification Uri
     */
    public Uri getNotificationUri() {
        return parseUriNullSafe(mCursor.getString(mColumnNotificationUri));
    }

    /**
     * @return the wallpaper Uri
     */
    public Uri getWallpaperUri() {
        return parseUriNullSafe(mCursor.getString(mColumnWallpaperUri));
    }

    /**
     * @return the lock screen wallpaper Uri
     */
    public Uri getLockWallpaperUri() {
        return parseUriNullSafe(mCursor.getString(mColumnLockWallpaperUri));
    }

    /**
     * @return true if call forwarding is enabled
     */
    public boolean callForwardingEnabled() {
        return mCursor.getInt(mColumnCallFowardingEnabled) == 1;
    }

    /**
     * @return true if email notifications are on
     */
    public boolean emailNotificationsOn() {
        return mCursor.getInt(mColumnEmailNotificationsOn) == 1;
    }

    /**
     * @return the scene id
     */
    public int getSceneId() {
        return mCursor.getInt(mColumnSceneId);
    }

    /**
     * @return true if the profile is active
     */
    public boolean isActive() {
        return mCursor.getInt(mColumnIsActive) != 0;
    }

    public boolean isRestrictedScene() {
        return mCursor.getInt(mColumnIsRestrictedScene) != 0;
    }

    public boolean isPreload() {
        return mCursor.getInt(mColumnPreloadId) > 0;
    }

    public int getPreloadId() {
        return mCursor.getInt(mColumnPreloadId);
    }

    public int getAutoswitchType() {
        return mCursor.getInt(mColumnAutoswitchType);
    }

    public int getEncodedDow() {
        return mCursor.getInt(mColumnEncodedDow);
    }

    public int getStartTime() {
        return mCursor.getInt(mColumnStartTime);
    }

    public int getEndTime() {
        return mCursor.getInt(mColumnEndTime);
    }

    public String getLocationGeocode() {
        return mCursor.getString(mColumnLocationGeocode);
    }

    public String getLocationName() {
        return mCursor.getString(mColumnLocationName);
    }

    /**
     * @param profile the profile to compare
     * @return true if the ids match
     */
    public boolean equals(ProfileItem profile) {
        if (profile == null) {
            return false;
        }
        return (profile.getId() == getId());
    }
}
