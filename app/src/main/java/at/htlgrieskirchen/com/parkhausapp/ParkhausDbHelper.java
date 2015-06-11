package at.htlgrieskirchen.com.parkhausapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
