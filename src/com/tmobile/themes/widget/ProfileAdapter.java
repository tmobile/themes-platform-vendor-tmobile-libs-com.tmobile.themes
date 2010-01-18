package com.tmobile.themes.widget;

import com.tmobile.themes.provider.ProfileItem;
import com.tmobile.themes.provider.Profiles;
import com.tmobile.themes.provider.Profiles.ProfileColumns;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

public abstract class ProfileAdapter extends AbstractDAOItemAdapter<ProfileItem> {
    public ProfileAdapter(Activity context) {
        super(context, loadProfiles(context), true);
    }

    private static Cursor loadProfiles(Activity context) {
        return context.managedQuery(Profiles.CONTENT_URI,
                null, null, ProfileColumns.NAME);
    }

    @Override
    protected void onAllocInternal(Cursor c) {
        mDAOItem = new ProfileItem(c);
    }
    
    /** @deprecated use {@link #getDAOItem(int)}. */
    public ProfileItem getProfile(int position) {
        return getDAOItem(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ProfileItem getCurrentlyAppliedItem(Context context) {
        return ProfileItem.getInstance(Profiles.getActiveProfile(context));
    }
}
