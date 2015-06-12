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
            insertParkhaus(stmt, 100, "Bahnhofparkhaus Linz", "Bahnhofplatz 9", 5.00, new GeoPoint(48.289365, 14.290229));
            insertParkhaus(stmt, 80, "Garage am Bahnhof ", "Bahnhofplatz 4", 4.50, new GeoPoint(48.289857, 14.290204));
            insertParkhaus(stmt, 150, "Garage Bahnhof-Wissensturm Linz", "Kärntnerstraßte 18", 6.00, new GeoPoint(48.290706, 14.289123));
            insertParkhaus(stmt, 65, "Blumauergarage", "Weingartshofstraße 26", 3.50,  new GeoPoint(48.292547, 14.288645));
            insertParkhaus(stmt, 75, "AREV Immobilien Gesellschft m.b.H.", "Bockgasse 2b", 4.00, new GeoPoint(48.292547, 14.288645));
            insertParkhaus(stmt, 200, "Raiffeisengarage Südbahnhof", "Khevenhüllerstraße 6", 6.50, new GeoPoint(48.299201, 14.303134));
            insertParkhaus(stmt, 150, "Raiffeisen Markt Südbahnhof", "Markplatz 1", 6.00, new GeoPoint(48.301359, 14.300339));
            insertParkhaus(stmt, 100, "Öpark Garagen-gmbh","Mozartstraße 12-14", 5.50, new GeoPoint( 48.301267, 14.292405));
            insertParkhaus(stmt, 400, "Tiefgarage Seilerstätte", "Seilerstätte 1", 6.75, new GeoPoint(48.300518, 14.289523));
            insertParkhaus(stmt, 650, "City Parkhaus Linz - CIP", "Bethlehemstraße 12", 7.55, new GeoPoint(48.303446, 14.290606));
            insertParkhaus(stmt, 600, "Hauptplatz Tiefgarage Linz", "Hauptplatz", 8.80, new GeoPoint( 48.305723, 14.286610));
            insertParkhaus(stmt, 90,  "Garage Diakonissenkrankenhaus", "Weißenwolffstraße", 4.00, new GeoPoint(48.304261, 14.302122));
            insertParkhaus(stmt, 350, "Tiefgarage Neues Rathaus", "Flußgasse 3", 5.70, new GeoPoint(48.309199, 14.282259));

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
