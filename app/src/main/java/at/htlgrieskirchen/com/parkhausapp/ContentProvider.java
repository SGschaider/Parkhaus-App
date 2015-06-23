package at.htlgrieskirchen.com.parkhausapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by thofer
 */
public class ContentProvider extends android.content.ContentProvider {
//test
    ParkhausDbHelper database;

    private static final int PARKHAEUSER = 10;
    private static final int PARKHAUS_ID = 20;

    private static final String AUTHORITY = "at.htlgrieskirchen.com.parkhausapp.ContentProvider";

    private static final String BASE_PATH = "parkhaeuser";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/parkhaeuser";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/parkhaus";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, PARKHAEUSER);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PARKHAUS_ID);
    }

    @Override
    public boolean onCreate() {
        database = new ParkhausDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(ParkhausTbl.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case PARKHAEUSER:
                break;
            case PARKHAUS_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(ParkhausTbl.Id + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        //nix
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case PARKHAEUSER:
                id = sqlDB.insert(ParkhausTbl.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case PARKHAEUSER:
                rowsDeleted = sqlDB.delete(ParkhausTbl.TABLE_NAME, selection, selectionArgs);
                break;
            case PARKHAUS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(ParkhausTbl.TABLE_NAME, ParkhausTbl.Id + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(ParkhausTbl.TABLE_NAME, ParkhausTbl.Id + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case PARKHAEUSER:
                rowsUpdated = sqlDB.update(ParkhausTbl.TABLE_NAME, values, selection, selectionArgs);
                break;
            case PARKHAUS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(ParkhausTbl.TABLE_NAME, values, ParkhausTbl.Id + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(ParkhausTbl.TABLE_NAME, values, ParkhausTbl.Id + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = ParkhausTbl.ALL_COLUMNS;
        if (projection != null)
        {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns))
            {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
