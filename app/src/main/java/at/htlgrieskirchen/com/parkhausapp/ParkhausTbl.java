package at.htlgrieskirchen.com.parkhausapp;

/**
 * Created by sgschaider on 28.05.2015.
 */
public class ParkhausTbl
{
    public static final String TABLE_NAME = "Parkhaus";
    public final static String Id = "Id";
    public final static String AnzahlParkplaetze = "AnzahlParkplaetze";
    public final static String Name = "Name";
    public final static String Standort = "Standort";
    public final static String Preis = "Preis";
    public final static String Latitude = "Latitude"; //Großer Wert
    public final static String Longitude = "Longitude"; //Kleiner Wert
    public final static String[] ALL_COLUMNS = new String[] {Id + " AS _id", AnzahlParkplaetze, Name, Standort, Preis, Latitude, Longitude};

    public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME +
                    "(" +
                    Id + " INTEGER PRIMARY KEY, " +
                    AnzahlParkplaetze + " INT NOT NULL," +
                    Name + " TEXT NOT NULL," +
                    Standort + " TEXT NOT NULL," +
                    Preis + " REAL NOT NULL," +
                    Latitude + " REAL NOT NULL," +
                    Longitude + " REAL NOT NULL" +
                    ")";

    public static final String STMT_DELETE = "DELETE FROM " + TABLE_NAME;
    public static final String STMT_INSERT =
            "INSERT INTO " + TABLE_NAME +
                    "(" + AnzahlParkplaetze + "," + Name + "," + Standort + "," + Preis + "," + Latitude + "," + Longitude + ") "+
                    "VALUES (?,?,?,?,?,?)";
}
