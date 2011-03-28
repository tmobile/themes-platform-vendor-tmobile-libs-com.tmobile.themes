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

package com.tmobile.themes.provider;

import com.tmobile.themes.provider.Themes.ThemeColumns;

import android.content.Context;
import android.content.res.CustomTheme;
import android.database.Cursor;
import android.net.Uri;

/**
 * Simple data access object designed to wrap a cursor returned from any of the
 * Themes class APIs.  Can be used efficiently with a custom CursorAdapter.
 *
 * <h2>Usage</h2>
 * <p>Here is an example of looping through a Cursor with ThemeItem:</p>
 * <pre  class="prettyprint">
 *      ThemeItem item = new ThemeItem(Themes.listThemes(myContext));
 *      try {
 *          while (c.moveToNext()) {
 *              //Do something with the item
 *          }
 *      } finally {
 *          item.close();
 *      }
 * </pre>
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
    private int mColumnHasHostDensity;
    private int mColumnHasThemePackageScope;

    private static final AbstractDAOItem.Creator<ThemeItem> CREATOR =
            new AbstractDAOItem.Creator<ThemeItem>() {
        @Override
        public ThemeItem init(Cursor c) {
            return new ThemeItem(c);
        }
    };

    /**
     * @see AbstractDAOItem.Creator#newInstance(Context, Uri)
     */
    public static ThemeItem getInstance(Context context, Uri uri) {
        return CREATOR.newInstance(context, uri);
    }

    /**
     * @see AbstractDAOItem.Creator#newInstance(Cursor)
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
        mColumnHasHostDensity = c.getColumnIndex(ThemeColumns.HAS_HOST_DENSITY);
        mColumnHasThemePackageScope = c.getColumnIndex(ThemeColumns.HAS_THEME_PACKAGE_SCOPE);
    }

    /**
     * @return the id for this item's row in the provider
     */
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

    /**
     * @return the Theme name
     */
    public String getName() {
        return mCursor.getString(mColumnName);
    }

    /**
     * Access the name to be displayed for the theme when packages sans
     * wallpaper and ringtone. For different parts of the UI.
     * @return the style name
     */
    public String getStyleName() {
        return mCursor.getString(mColumnStyleName);
    }

    /**
     * @return the Theme author
     */
    public String getAuthor() {
        return mCursor.getString(mColumnAuthor);
    }

    /**
     * @return true if this theme contains DRM content
     */
    public boolean isDRMProtected() {
        return mCursor.getInt(mColumnIsDRM) != 0;
    }

    /**
     * @return the String Theme Id
     */
    public String getThemeId() {
        return mCursor.getString(mColumnThemeId);
    }

    /**
     * @return this theme's package
     */
    public String getPackageName() {
        return mCursor.getString(mColumnThemePackage);
    }

    /**
     * Requests a unique identifier for a wallpaper. Useful to distinguish
     * different wallpaper items contained in a single theme package. Though
     * the result appears to be a filename, it should never be treated in
     * this way. It is merely useful as a unique key to feed a BitmapStore
     * surrounding this theme package.
     * @return the wallpaper identifier
     */
    public String getWallpaperIdentifier() {
        return mCursor.getString(mColumnWallpaperName);
    }

    /**
     * If this theme specifies a wallpaper, get the Uri.
     * @param context the context of the caller
     * @return the wallpaper uri, or null if this theme doesn't specify one.
     */
    public Uri getWallpaperUri(Context context) {
        return parseUriNullSafe(mCursor.getString(mColumnWallpaperUri));
    }

    /**
     * If this theme specifies a lockscreen wallpaper, get the Uri.
     * @param context the context of the caller
     * @return the lockscreen wallpaper uri, or null if this theme doesn't specify one.
     */
    public Uri getLockWallpaperUri(Context context) {
        return parseUriNullSafe(mCursor.getString(mColumnLockWallpaperUri));
    }

    /**
     * If this theme specifies a ringtone, get the Uri.
     * @param context the context of the caller
     * @return the ringtone uri, or null if this theme doesn't specify one.
     */
    public Uri getRingtoneUri(Context context) {
        return parseUriNullSafe(mCursor.getString(mColumnRingtoneUri));
    }

    /**
     * @return the name of the ringtone or null if this theme doesn't specify one.
     */
    public String getRingtoneName() {
        return mCursor.getString(mColumnRingtoneName);
    }

    /**
     * If this theme specifies a notification ringtone, get the Uri.
     * @param context the context of the caller
     * @return the notification ringtone uri, or null if this theme doesn't specify one.
     */
    public Uri getNotificationRingtoneUri(Context context) {
        return parseUriNullSafe(mCursor.getString(mColumnNotifRingtoneUri));
    }

    /**
     * @return the name of the notification ringtone or null if this theme doesn't specify one.
     */
    public String getNotificationRingtoneName() {
        return mCursor.getString(mColumnNotifRingtoneName);
    }

    /**
     * A theme may specify a thumbnail to represent a theme.
     * @return the thumbnail uri, or null if this theme doesn't specify one.
     */
    public Uri getThumbnailUri() {
        return parseUriNullSafe(mCursor.getString(mColumnThumbnailUri));
    }

    /**
     * A theme may specify a preview image to represent a theme.
     *
     * @param orientation the screen orientation for which a preview image is
     * desired.  Orientation values come from {@link android.content.res.Configuration}
     * @return the preview image uri, or null if this theme doesn't specify one.
     */
    public Uri getPreviewUri(int orientation) {
        Uri uri = parseUriNullSafe(mCursor.getString(mColumnPreviewUri));
        if (null != uri) {
            uri = uri.buildUpon().appendQueryParameter(Themes.KEY_ORIENTATION,
                    String.valueOf(orientation)).build();
        }
        return uri;
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

    /**
     * @return true if this theme is currently applied
     */
    public boolean isApplied() {
        return mCursor.getInt(mColumnIsApplied) != 0;
    }

    /**
     * @return true if this theme has assets compiled for the current host's
     *         display ensity.
     */
    public boolean hasHostDensity() {
        return mCursor.getInt(mColumnHasHostDensity) != 0;
    }

    /**
     * @return true if this theme has assets compiled in the theme package scope
     *         (0x0a as opposed to 0x7f).
     */
    public boolean hasThemePackageScope() {
        return mCursor.getInt(mColumnHasThemePackageScope) != 0;
    }

    /**
     * Compares the internal T-Mobile theme object to this ThemeItem.
     * For internal use.
     * @param theme the CustomTheme object to compare
     * @return
     */
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
