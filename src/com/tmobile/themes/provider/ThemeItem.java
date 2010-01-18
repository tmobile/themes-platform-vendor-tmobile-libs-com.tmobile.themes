package com.tmobile.themes.provider;

import com.tmobile.themes.provider.Themes.ThemeColumns;

import android.content.Context;
import android.content.res.CustomTheme;
import android.database.Cursor;
import android.net.Uri;

/**
 * Simple data access object designed to wrap a cursor returned from any of the
 * Themes class APIs.  Can be used efficiently with a custom CursorAdapter.
 */
public class ThemeItem extends AbstractDAOItem {
    private int mColumnId;
    private int mColumnThemeId;
    private int mColumnThemePackage;
    private int mColumnName;
    private int mColumnStyleName;
    private int mColumnAuthor;
    private int mColumnIsDRM;
    private int mColumnWallpaperName;
    private int mColumnWallpaperUri;
    private int mColumnLockWallpaperUri;
    private int mColumnRingtoneName;
    private int mColumnRingtoneUri;
    private int mColumnNotifRingtoneName;
    private int mColumnNotifRingtoneUri;
    private int mColumnThumbnailUri;
    private int mColumnIsSystem;
    private int mColumnIsApplied;
    private int mColumnPreviewUri;

    private static final AbstractDAOItem.Creator<ThemeItem> CREATOR =
            new AbstractDAOItem.Creator<ThemeItem>() {
        @Override
        public ThemeItem init(Cursor c) {
            return new ThemeItem(c);
        }
    };

    /**
     * {@inheritDoc}
     */
    public static ThemeItem getInstance(Context context, Uri uri) {
        return CREATOR.newInstance(context, uri);
    }

    /**
     * {@inheritDoc}
     */
    public static ThemeItem getInstance(Cursor c) {
        return CREATOR.newInstance(c);
    }

    /**
     * {@inheritDoc}
     */
    public ThemeItem(Cursor c) {
        super(c);
        mColumnId = c.getColumnIndex(ThemeColumns._ID);
        mColumnThemeId = c.getColumnIndex(ThemeColumns.THEME_ID);
        mColumnThemePackage = c.getColumnIndex(ThemeColumns.THEME_PACKAGE);
        mColumnName = c.getColumnIndex(ThemeColumns.NAME);
        mColumnStyleName = c.getColumnIndex(ThemeColumns.STYLE_NAME);
        mColumnAuthor = c.getColumnIndex(ThemeColumns.AUTHOR);
        mColumnIsDRM = c.getColumnIndex(ThemeColumns.IS_DRM);
        mColumnWallpaperName = c.getColumnIndex(ThemeColumns.WALLPAPER_NAME);
        mColumnWallpaperUri = c.getColumnIndex(ThemeColumns.WALLPAPER_URI);
        mColumnLockWallpaperUri = c.getColumnIndex(ThemeColumns.LOCK_WALLPAPER_URI);
        mColumnRingtoneName = c.getColumnIndex(ThemeColumns.RINGTONE_NAME);
        mColumnRingtoneUri = c.getColumnIndex(ThemeColumns.RINGTONE_URI);
        mColumnNotifRingtoneName = c.getColumnIndex(ThemeColumns.NOTIFICATION_RINGTONE_NAME);
        mColumnNotifRingtoneUri = c.getColumnIndex(ThemeColumns.NOTIFICATION_RINGTONE_URI);
        mColumnThumbnailUri = c.getColumnIndex(ThemeColumns.THUMBNAIL_URI);
        mColumnIsSystem = c.getColumnIndex(ThemeColumns.IS_SYSTEM);
        mColumnIsApplied = c.getColumnIndex(ThemeColumns.IS_APPLIED);
        mColumnPreviewUri = c.getColumnIndex(ThemeColumns.PREVIEW_URI);
    }

    public long getId() {
        return mCursor.getLong(mColumnId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Uri getUri(Context context) {
        return Themes.getThemeUri(context, getPackageName(), getThemeId());
    }

    public String getName() {
        return mCursor.getString(mColumnName);
    }

    /**
     * Access the name to be displayed for the theme when packages sans
     * wallpaper and ringtone. For different parts of the UI.
     */
    public String getStyleName() {
        return mCursor.getString(mColumnStyleName);
    }

    public String getAuthor() {
        return mCursor.getString(mColumnAuthor);
    }

    public boolean isDRMProtected() {
        return mCursor.getInt(mColumnIsDRM) != 0;
    }

    /** @deprecated */
    public int getResourceId(Context context) {
        return CustomTheme.getStyleId(context, getPackageName(),
                getThemeId());
    }

    public String getThemeId() {
        return mCursor.getString(mColumnThemeId);
    }

    public String getPackageName() {
        return mCursor.getString(mColumnThemePackage);
    }

    /**
     * Requests a unique identifier for a wallpaper. Useful to distinguish
     * different wallpaper items contained in a single theme package. Though
     * the result appears to be a filename, it should never be treated in
     * this way. It is merely useful as a unique key to feed a BitmapStore
     * surrounding this theme package.
     */
    public String getWallpaperIdentifier() {
        return mCursor.getString(mColumnWallpaperName);
    }

    public Uri getWallpaperUri(Context context) {
        return parseUriNullSafe(mCursor.getString(mColumnWallpaperUri));
    }

    public Uri getLockWallpaperUri(Context context) {
        return parseUriNullSafe(mCursor.getString(mColumnLockWallpaperUri));
    }

    public Uri getRingtoneUri(Context context) {
        return parseUriNullSafe(mCursor.getString(mColumnRingtoneUri));
    }

    public String getRingtoneName() {
        return mCursor.getString(mColumnRingtoneName);
    }

    public Uri getNotificationRingtoneUri(Context context) {
        return parseUriNullSafe(mCursor.getString(mColumnNotifRingtoneUri));
    }

    public String getNotificationRingtoneName() {
        return mCursor.getString(mColumnNotifRingtoneName);
    }

    public Uri getThumbnailUri() {
        return parseUriNullSafe(mCursor.getString(mColumnThumbnailUri));
    }

    public Uri getPreviewUri() {
        return parseUriNullSafe(mCursor.getString(mColumnPreviewUri));
    }

    /** @deprecated */
    public String getSoundPackName() {
        return null;
    }

    /**
     * Tests whether the theme item can be uninstalled. This condition
     * is true for all theme APKs not part of the system image.
     *
     * @return Returns true if the theme can be uninstalled.
     */
    public boolean isRemovable() {
        return mCursor.getInt(mColumnIsSystem) == 0;
    }

    public boolean isApplied() {
        return mCursor.getInt(mColumnIsApplied) != 0;
    }

    public boolean equals(CustomTheme theme) {
        if (theme == null) {
            return false;
        }
        if (getPackageName().equals(theme.getThemePackageName()) == false) {
            return false;
        }
        return theme.getThemeId().equals(getThemeId());
    }

    public String toString() {
        StringBuilder b = new StringBuilder();

        b.append('{');
        b.append("pkg=").append(getPackageName()).append("; ");
        b.append("themeId=").append(getThemeId()).append("; ");
        b.append("name=").append(getName()).append("; ");
        b.append("drm=").append(isDRMProtected());
        b.append('}');

        return b.toString();
    }
}
