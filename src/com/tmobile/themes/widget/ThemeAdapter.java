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
