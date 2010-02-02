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

package com.tmobile.themes.widget;

import com.tmobile.themes.provider.ThemeItem;
import com.tmobile.themes.provider.Themes;
import com.tmobile.themes.provider.Themes.ThemeColumns;

import android.app.Activity;
import android.content.Context;
import android.content.res.CustomTheme;
import android.database.Cursor;

/**
 * Re-usable adapter which fills itself with all currently installed visual
 * themes. The Adapter will manager the cursor.
 * @author T-Mobile USA
 */
public abstract class ThemeAdapter extends AbstractDAOItemAdapter<ThemeItem> {
    public ThemeAdapter(Activity context) {
        super(context, loadThemes(context), true);
    }

    private static Cursor loadThemes(Activity context) {
        return context.managedQuery(ThemeColumns.CONTENT_PLURAL_URI,
                null, null, ThemeColumns.NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ThemeItem getCurrentlyAppliedItem(Context context) {
        return ThemeItem.getInstance(Themes.getAppliedTheme(context));
    }
    
    @Override
    protected void onAllocInternal(Cursor c) {
        mDAOItem = new ThemeItem(c);
    }

    /** @deprecated use {@link #getDAOItem(int)}. */
    public ThemeItem getTheme(int position) {
        return getDAOItem(position);
    }

    public int findItem(CustomTheme theme) {
        if (theme == null) return -1;
        int n = getCount();
        while (n-- > 0) {
            ThemeItem item = getDAOItem(n);
            if (item.equals(theme) == true) {
                return n;
            }
        }
        return -1;
    }
}
