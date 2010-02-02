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
 *      Cursor c = Themes.listThemes(myContext);
 *      ThemeItem item = ThemeItem(c);
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

    /** @deprecated */
    public int getResourceId(Context context) {
        return CustomTheme.getStyleId(context, getPackageName(),
                getThemeId());
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
     * @return the preview image uri, or null if this theme doesn't specify one.
     */
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

    /** 
     * @return true if this theme is currently applied
     */
    public boolean isApplied() {
        return mCursor.getInt(mColumnIsApplied) != 0;
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
