package com.tmobile.themes.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * AbstractDAOItem provides an easy way to access data in a {@link Cursor}.
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
     * @return the count of the underlyong {@link Cursor}.
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
