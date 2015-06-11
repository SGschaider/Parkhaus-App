package at.htlgrieskirchen.com.parkhausapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import org.osmdroid.util.GeoPoint;

/**
 * Created by sgschaider on 28.05.2015.
 */
public class ParkhausDbHelper extends SQLiteOpenHelper
{
    private final static String DB_NAME = "parkhaus.db";
    private final static int DB_VERSION = 1;


    public ParkhausDbHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(ParkhausTbl.SQL_CREATE);
        seed(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(ParkhausTbl.SQL_DROP);
        onCreate(db);
    }

    private void seed(SQLiteDatabase db)
    {
        final SQLiteStatement stmt = db.compileStatement(ParkhausTbl.STMT_INSERT);
        db.beginTransaction();
        try{
            insertParkhaus(stmt, 100, "Bahnhofparkhaus Linz", "Bahnhofplatz 9", 5, new GeoPoint(48.289365, 14.290229));
            db.setTransactionSuccessful();
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
        finally
        {
            db.endTransaction();
        }
    }

    private void insertParkhaus(SQLiteStatement stmt, int anzahlParkplaetze, String name, String standort,double preis, GeoPoint geoPoint)
    {
        stmt.bindLong(1, anzahlParkplaetze);
        stmt.bindString(2, name);
        stmt.bindString(3, standort);
        stmt.bindDouble(4, preis);
        stmt.bindDouble(5, geoPoint.getLatitude());
        stmt.bindDouble(6, geoPoint.getLongitude());
        stmt.executeInsert();
    }
}
