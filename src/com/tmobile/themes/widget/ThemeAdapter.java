package com.tmobile.themes.widget;

import com.tmobile.themes.provider.ThemeItem;
import com.tmobile.themes.provider.Themes;
import com.tmobile.themes.provider.Themes.ThemeColumns;

import android.app.Activity;
import android.content.Context;
import android.content.res.CustomTheme;
import android.database.Cursor;

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
