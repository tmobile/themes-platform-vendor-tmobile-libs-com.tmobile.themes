package com.tmobile.themes.provider;


import com.tmobile.themes.provider.Profiles.ProfileColumns;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Simple data access object designed to wrap a cursor returned from any of the
 * Profiles class APIs.  Can be used efficiently with a custom CursorAdapter.
 */
public class ProfileItem extends AbstractDAOItem {
    private int mColumnId;
    private int mColumnName;
    private int mColumnThemeUri;
    private int mColumnAppearanceUri;
    private int mColumnRingtoneUri;
    private int mColumnNotificationUri;
    private int mColumnWallpaperUri;
    private int mColumnLockWallpaperUri;
    private int mColumnSceneId;
    private int mColumnIsActive;

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
        mColumnThemeUri = c.getColumnIndex(ProfileColumns.THEME_URI);
        mColumnAppearanceUri = c.getColumnIndex(ProfileColumns.APPEARANCE_URI);
        mColumnRingtoneUri = c.getColumnIndex(ProfileColumns.RINGTONE_URI);
        mColumnNotificationUri = c.getColumnIndex(ProfileColumns.NOTIFICATION_URI);
        mColumnWallpaperUri = c.getColumnIndex(ProfileColumns.WALLPAPER_URI);
        mColumnLockWallpaperUri = c.getColumnIndex(ProfileColumns.LOCK_WALLPAPER_URI);
        mColumnSceneId = c.getColumnIndex(ProfileColumns.SCENE_ID);
        mColumnIsActive = c.getColumnIndex(ProfileColumns.IS_ACTIVE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Uri getUri(Context context) {
        return Profiles.getProfileUri(context, getId());
    }

    public int getId() {
        return mCursor.getInt(mColumnId);
    }

    public String getName() {
        return mCursor.getString(mColumnName);
    }

    public Uri getThemeUri() {
        return parseUriNullSafe(mCursor.getString(mColumnThemeUri));
    }
    
    public Uri getAppearanceUri() {
        return parseUriNullSafe(mCursor.getString(mColumnAppearanceUri));
    }

    public Uri getRingtoneUri() {
        return parseUriNullSafe(mCursor.getString(mColumnRingtoneUri));
    }

    public Uri getNotificationUri() {
        return parseUriNullSafe(mCursor.getString(mColumnNotificationUri));
    }

    public Uri getWallpaperUri() {
        return parseUriNullSafe(mCursor.getString(mColumnWallpaperUri));
    }

    public Uri getLockWallpaperUri() {
        return parseUriNullSafe(mCursor.getString(mColumnLockWallpaperUri));
    }

    public int getSceneId() {
        return mCursor.getInt(mColumnSceneId);
    }

    public boolean isActive() {
        return mCursor.getInt(mColumnIsActive) != 0;
    }
    
    public boolean equals(ProfileItem profile) {
        if (profile == null) {
            return false;
        }
        return (profile.getId() == getId());
    }
}
