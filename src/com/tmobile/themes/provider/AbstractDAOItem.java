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

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * AbstractDAOItem provides an easy way to access data in a {@link Cursor}.
 * For T-Mobile internal use.
 * @author T-Mobile USA
 *
 */
public abstract class AbstractDAOItem {
    protected Cursor mCursor;

    /**
     * Creates an {@link AbstractDAOItem} for the specified {@link Cursor}.
     * @param c a valid {@link Cursor} for the {@link AbstractDAOItem} type.
     */
    public AbstractDAOItem(Cursor c) {
        if (c == null || c.getCount() == 0) {
            throw new IllegalArgumentException("Cursor cannot be null or empty");
        }
        mCursor = c;
    }

    /**
     * @return The underlying {@link Cursor} for this {@link AbstractDAOItem}.
     */
    public Cursor getCursor() {
        return mCursor;
    }

    /**
     * Closes the underlying {@link Cursor}.
     */
    public void close() {
        mCursor.close();
    }

    /**
     * Move the underlying {@link Cursor} to the specified position.
     * @param position the position to move to.
     */
    public void setPosition(int position) {
        mCursor.moveToPosition(position);
    }

    /**
     * @return the position of the underlying {@link Cursor}.
     */
    public int getPosition() {
        return mCursor.getPosition();
    }

    /**
     * @return the count of the underlying {@link Cursor}.
     */
    public int getCount() {
        return mCursor.getCount();
    }

    /**
     * @param uriString the String to parse.
     * @return the parsed {@link Uri} or null.
     */
    protected static Uri parseUriNullSafe(String uriString) {
        return (uriString != null ? Uri.parse(uriString) : null);
    }

    /**
     * @param context the Callers {@link Context}.
     * @return The {@link Uri} for the underlying {@link Cursor}.
     */
    public abstract Uri getUri(Context context);

    protected abstract static class Creator<T extends AbstractDAOItem> {
        /**
         * Creates an {@link AbstractDAOItem} for the specified {@link Uri} with the {@link Cursor} positioned to the first entry.
         * @param context the {@link Context} of the caller.
         * @param uri the {@link Uri} of the item(s).
         * @return an {@link AbstractDAOItem} positioned to the first entry in the {@link Cursor}.
         */
        public T newInstance(Context context, Uri uri) {
            if (uri != null) {
                Cursor c = context.getContentResolver().query(uri, null, null, null, null);
                return newInstance(c);
            }
            return null;
        }

        /**
         * Creates an {@link AbstractDAOItem} for the specified {@link Cursor} positioned to the first entry.
         * @param c a valid {@link Cursor} for the {@link AbstractDAOItem} type.
         * @return an {@link AbstractDAOItem} positioned to the first entry in the {@link Cursor}.
         */
        public T newInstance(Cursor c) {
            if (c != null) {
                if (c.moveToFirst() == true) {
                    return init(c);
                } else {
                    c.close();
                }
            }
            return null;
        }

        public abstract T init(Cursor c);
    }
}
