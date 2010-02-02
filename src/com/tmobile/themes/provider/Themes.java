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

import com.tmobile.themes.ThemeManager;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.CustomTheme;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

/**
 * A simple helper class the provides an easy way of working with themes.
 * @author T-Mobile USA
 */
public class Themes {
    public static final String AUTHORITY = "com.tmobile.thememanager.themes";

    public static final Uri CONTENT_URI =
        Uri.parse("content://" + AUTHORITY);

    private Themes() {}

    /**
     * Creates a theme {@link Uri} for the given theme package and id.
     * @param context the context of the caller.
     * @param packageName the package of the theme.
     * @param themeId the id of the theme.
     * @return
     */
    public static Uri getThemeUri(Context context, String packageName, String themeId) {
        if (TextUtils.isEmpty(packageName) && TextUtils.isEmpty(themeId)) {
            return ThemeColumns.CONTENT_URI.buildUpon()
                    .appendEncodedPath("system").build();
        } else {
            return ThemeColumns.CONTENT_URI.buildUpon()
                    .appendPath(packageName)
                    .appendPath(themeId).build();
        }
    }

    /**
     * Gets the {@link Uri} for the default theme.
     * @param context the context of the caller.
     * @return the default theme {@link Uri}.
     */
    public static Uri getDefaultThemeUri(Context context) {
        CustomTheme def = CustomTheme.getDefault();
        return getThemeUri(context, def.getThemePackageName(), def.getThemeId());
    }

    /**
     * Gets a {@link Cursor} for all themes in the provider. Uses the default Projection.
     * @param context the context of the caller.
     * @return a {@link Cursor} for all themes in the provider or null if provider is empty.
     */
    public static Cursor listThemes(Context context) {
        return listThemes(context, null);
    }

    /**
     * Gets a {@link Cursor} for all themes in the provider using the specified projection.
     * @param context the context of the caller.
     * @param projection the Projection for the {@link Cursor}.
     * @return a {@link Cursor} for all themes in the provider using the specified projection or null if provider is empty.
     */
    public static Cursor listThemes(Context context, String[] projection) {
        return context.getContentResolver().query(ThemeColumns.CONTENT_PLURAL_URI,
                projection, null, null, null);
    }

    /**
     * Gets a {@link Cursor} for themes in the provider filter by the specified package name.
     * @param context the context of the caller.
     * @param packageName the package for which to filter.
     * @return a {@link Cursor} for themes in the provider filter by the specified package name or null if provider is empty.
     */
    public static Cursor listThemesByPackage(Context context, String packageName) {
        return context.getContentResolver().query(ThemeColumns.CONTENT_PLURAL_URI,
                null, ThemeColumns.THEME_PACKAGE + " = ?",
                new String[] { packageName }, null);
    }

    /**
     * Gets a {@link Cursor} for the currently applied theme.
     * @param context the context of the caller.
     * @return a {@link Cursor} for the currently applied theme.
     */
    public static Cursor getAppliedTheme(Context context) {
        return context.getContentResolver().query(ThemeColumns.CONTENT_PLURAL_URI,
                null, ThemeColumns.IS_APPLIED + "=1", null, null);
    }

    /**
     * Deletes a non system theme with the specified package and id.
     * @param context the context of the caller.
     * @param packageName
     * @param themeId
     */
    public static void deleteTheme(Context context, String packageName,
            String themeId) {
        context.getContentResolver().delete(
                ThemeColumns.CONTENT_PLURAL_URI, ThemeColumns.THEME_PACKAGE + " = ? AND " +
                    ThemeColumns.THEME_ID + " = ?",
                new String[] { packageName, themeId });
    }

    /**
     * Deletes non system themes with the specified package.
     * @param context the context of the caller.
     * @param packageName the package for the themes to be deleted.
     */
    public static void deleteThemesByPackage(Context context, String packageName) {
        context.getContentResolver().delete(
                ThemeColumns.CONTENT_PLURAL_URI, ThemeColumns.THEME_PACKAGE + " = ?",
                new String[] { packageName });
    }

    /**
     * Marks a theme as being the applied theme.
     * @param context the context of the caller.
     * @param packageName the package of the theme to apply.
     * @param themeId the id of the theme to apply.
     */
    public static void markAppliedTheme(Context context, String packageName, String themeId) {
        ContentValues values = new ContentValues();
        values.put(ThemeColumns.IS_APPLIED, 0);
        context.getContentResolver().update(ThemeColumns.CONTENT_PLURAL_URI, values, null, null);
        values.put(ThemeColumns.IS_APPLIED, 1);
        context.getContentResolver().update(ThemeColumns.CONTENT_PLURAL_URI, values,
                ThemeColumns.THEME_PACKAGE + " = ? AND " +
                    ThemeColumns.THEME_ID + " = ?",
                new String[] { packageName, themeId });
    }

    /**
     * Request a theme change by broadcasting to the ThemeManager. Must hold
     * permission {@link Constants#PERMISSION_CHANGE_THEME}.
     */
    public static void changeTheme(Context context, Uri themeUri) {
        changeTheme(context, new Intent(ThemeManager.ACTION_CHANGE_THEME, themeUri));
    }

    /**
     * Changes to the style of the given style {@link Uri}.
     * @param context the context of the caller.
     * @param styleUri the {@link Uri} of the style to apply.
     */
    public static void changeStyle(Context context, Uri styleUri) {
        changeTheme(context, new Intent(ThemeManager.ACTION_CHANGE_THEME).setDataAndType(styleUri,
                ThemeColumns.STYLE_CONTENT_ITEM_TYPE));
    }

    /**
     * Alternate API to {@link #changeTheme(Context, Uri)} which allows you to
     * customize the intent that is delivered. This is used to access more
     * advanced functionality like conditionalizing certain parts of the theme
     * that is going to be applied.
     * @param context the context of the caller.
     * @param intent the Intent with extras the specify the conditions to apply.
     */
    public static void changeTheme(Context context, Intent intent) {
        context.sendOrderedBroadcast(intent, Manifest.permission.CHANGE_CONFIGURATION);
    }

    public interface ThemeColumns {
        public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/theme");

        public static final Uri CONTENT_PLURAL_URI =
            Uri.parse("content://" + AUTHORITY + "/themes");

        public static final String CONTENT_TYPE = "vnd.tmobile.cursor.dir/theme";
        public static final String CONTENT_ITEM_TYPE = "vnd.tmobile.cursor.item/theme";

        public static final String STYLE_CONTENT_TYPE = "vnd.tmobile.cursor.dir/style";
        public static final String STYLE_CONTENT_ITEM_TYPE = "vnd.tmobile.cursor.item/style";

        public static final String _ID = "_id";
        public static final String THEME_ID = "theme_id";
        public static final String THEME_PACKAGE = "theme_package";

        public static final String IS_APPLIED = "is_applied";

        public static final String NAME = "name";
        public static final String STYLE_NAME = "style_name";
        public static final String AUTHOR = "author";
        public static final String IS_DRM = "is_drm";

        public static final String WALLPAPER_NAME = "wallpaper_name";
        public static final String WALLPAPER_URI = "wallpaper_uri";

        public static final String LOCK_WALLPAPER_NAME = "lock_wallpaper_name";
        public static final String LOCK_WALLPAPER_URI = "lock_wallpaper_uri";

        public static final String RINGTONE_NAME = "ringtone_name";
        public static final String RINGTONE_URI = "ringtone_uri";
        public static final String NOTIFICATION_RINGTONE_NAME = "notif_ringtone_name";
        public static final String NOTIFICATION_RINGTONE_URI = "notif_ringtone_uri";

        public static final String THUMBNAIL_URI = "thumbnail_uri";
        public static final String PREVIEW_URI = "preview_uri";

        public static final String IS_SYSTEM = "system";
    }
}
