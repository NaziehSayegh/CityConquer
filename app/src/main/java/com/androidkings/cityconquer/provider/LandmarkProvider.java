package com.androidkings.cityconquer.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LandmarkProvider extends ContentProvider {

    private static final String AUTHORITY = "com.androidkings.cityconquer.provider";
    private static final String LANDMARKS_PATH = "landmarks";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + LANDMARKS_PATH);

    private static final int LANDMARKS = 1;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, LANDMARKS_PATH, LANDMARKS);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        if (uriMatcher.match(uri) == LANDMARKS) {
            MatrixCursor cursor = new MatrixCursor(
                    new String[]{"id", "name", "city", "latitude", "longitude", "xp"});

            // Jerusalem landmarks
            cursor.addRow(new Object[]{1, "Al-Aqsa Mosque", "Jerusalem", 31.7761, 35.2358, 100});
            cursor.addRow(new Object[]{2, "Church of Holy Sepulchre", "Jerusalem", 31.7784, 35.2297, 100});
            cursor.addRow(new Object[]{3, "Western Wall", "Jerusalem", 31.7767, 35.2345, 100});
            cursor.addRow(new Object[]{4, "Damascus Gate", "Jerusalem", 31.7828, 35.2298, 75});
            cursor.addRow(new Object[]{5, "Tower of David", "Jerusalem", 31.7762, 35.2285, 75});

            return cursor;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}