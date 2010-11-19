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

import com.tmobile.themes.provider.AbstractDAOItem;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;

/**
 * Re-usable adapter which fills itself with all currently installed visual
 * themes/profiles. Includes a convenient inner-class which can represent all types of
 * visual themes/profiles with helpful accessors. AbstractDAOItemAdapter will automatically manage the cursor.
 */
public abstract class AbstractDAOItemAdapter<T extends AbstractDAOItem> extends CursorAdapter {
    protected T mDAOItem;
    private final LayoutInflater mInflater;
    private final Context mContext;

    /*
     * These values support an optional feature called automatic marking. See
     * setAutomaticMarking for more info.
     */
    private boolean mMarking;
    private Uri mExistingUri;
    private int mMarkedPosition = -1;

    public AbstractDAOItemAdapter(Activity context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        allocInternal(c);
    }

    protected Context getContext() {
        return mContext;
    }

    protected LayoutInflater getInflater() {
        return mInflater;
    }

    protected abstract void onAllocInternal(Cursor c);

    private void allocInternal(Cursor c) {
        if (c != null && c.getCount() > 0) {
            onAllocInternal(c);
        } else {
            mDAOItem = null;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        allocInternal(getCursor());
        super.notifyDataSetChanged();
        if (mMarking) {
            markCurrentOrExistingItem(mExistingUri, false);
        }
    }

    @Override
    public void notifyDataSetInvalidated() {
        mDAOItem = null;
        super.notifyDataSetInvalidated();
    }

    /**
     * Get the currently applied item backed by this DAO item.
     *
     * @return The currently applied item or null if none exist.
     */
    protected abstract T getCurrentlyAppliedItem(Context context);

    /**
     * Enable or disable automatic support for marking a particular item. Users
     * of this feature should also extend {@link #onMarkChanged(int)}.
     *
     * @param enabled If true, automatic marking support will be enabled;
     *            otherwise it will be disabled. This feature is off by default.
     * @param existingUri The item to mark if non-null; otherwise, the currently
     *            applied item will be taken from
     *            {@link #getCurrentlyAppliedItem}.
     */
    public void setUseAutomaticMarking(boolean enabled, Uri existingUri) {
        mMarking = enabled;
        mExistingUri = existingUri;

        if (enabled) {
            /*
             * Mark, but do not call notifyDataSetChanged as this will trigger
             * an unnecessary extra call to markCurrentOrExistingItem.
             */
            markCurrentOrExistingItem(existingUri, false);
        } else {
            mMarkedPosition = -1;
        }
    }

    /**
     * Mark the applied item's position.
     *
     * @param existingUri uri to select, or null to use the currently applied
     *            item.
     * @param notifyChange if true, {@link #notifyDataSetChanged} will be called
     *            on mark change.
     *
     * @see #getMarkedPosition
     */
    private int markCurrentOrExistingItem(Uri existingUri, boolean notifyChange) {
        int position = findExistingOrCurrentItem(getContext(), existingUri);
        if (mMarkedPosition != position) {
            int oldPosition = mMarkedPosition;
            mMarkedPosition = position;
            onMarkChanged(oldPosition);
            if (notifyChange) {
                notifyDataSetChanged();
            }
        }
        return position;
    }

    /**
     * @return the previously marked position or -1 if no mark has been set.
     *
     * @throws IllegalStateException if called with automatic marking disabled.
     */
    public int getMarkedPosition() {
        if (!mMarking) {
            throw new IllegalStateException("getMarkedPosition() called without automatic marking support.");
        }
        return mMarkedPosition;
    }

    /**
     * Called when the marked item has changed (or has been reset to -1). The
     * default implementation does nothing.
     *
     * @param oldMarkedPosition Position of the old mark. Use
     *            {@link #getMarkedPosition()} to access the current new mark.
     */
    protected void onMarkChanged(int oldMarkedPosition) {
        /* Nothing... */
    }

    /**
     * Utility function to work out which theme item should be shown as checked.
     *
     * <p>This method is implemented with way too much allocation.</p>
     *
     * @param existingUri Requested existing URI if provided via Intent extras.
     */
    public int findExistingOrCurrentItem(Context context, Uri existingUri) {
        Uri needle = getExistingOrCurrentUri(context, existingUri);
        if (needle != null) {
            return findItem(context, needle);
        } else {
            return -1;
        }
    }

    private Uri getExistingOrCurrentUri(Context context, Uri existingUri) {
        if (existingUri != null) {
            return existingUri;
        } else {
            T current = getCurrentlyAppliedItem(context);
            if (current != null) {
                try {
                    return current.getUri(context);
                } finally {
                    current.close();
                }
            } else {
                return null;
            }
        }
    }

    /**
     * Gets the position of the item in the Adapter.
     * @param context the context of the caller.
     * @param uri the Uri to find.
     * @return the position of the item in the Adapter or -1 of not in Adapter.
     */
    public int findItem(Context context, Uri uri) {
        if (uri == null) return -1;
        int n = getCount();
        while (n-- > 0) {
            T item = getDAOItem(n);
            if (uri.equals(item.getUri(context)) == true) {
                return n;
            }
        }
        return -1;
    }

    /**
     * Gets an {@link AbstractDAOItem} with it's cursor positioned at the given postion.
     * @param position the position to get.
     * @return the item or null if the position is invalid.
     */
    public T getDAOItem(int position) {
        if (position >= 0 && getCount() >= 0) {
            mDAOItem.setPosition(position);
            return mDAOItem;
        }
        return null;
    }
}
