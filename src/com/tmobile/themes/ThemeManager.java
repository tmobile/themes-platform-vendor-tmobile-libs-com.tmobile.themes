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
