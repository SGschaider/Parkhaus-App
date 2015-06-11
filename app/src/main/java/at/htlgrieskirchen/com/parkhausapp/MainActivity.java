package at.htlgrieskirchen.com.parkhausapp;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;


public class MainActivity extends Activity {

    protected MapView mapView;
    protected IMapController mapController;

    private boolean useOnlineMap = true;
    private int zoom = 12;
    private GeoPoint linz = new GeoPoint(48.351054, 14.249727);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map);
        initMap();

        ParkhausDbHelper dbHelper = new ParkhausDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
    }

    private void initMap()
    {
        mapController = mapView.getController();
        mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
        mapView.setMultiTouchControls(false);
        mapView.setBuiltInZoomControls(true);
        mapView.setUseDataConnection(useOnlineMap);

        mapController.setZoom(zoom);
        mapController.setCenter(linz);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuKarte){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
