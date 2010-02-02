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
