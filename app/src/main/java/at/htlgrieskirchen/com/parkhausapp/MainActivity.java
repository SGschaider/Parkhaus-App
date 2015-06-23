package at.htlgrieskirchen.com.parkhausapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by thofer
 */
public class MainActivity extends Activity {

    protected MapView mapView;
    protected IMapController mapController;

    private boolean useOnlineMap = true;
    private int zoom = 12;
    private GeoPoint linz = new GeoPoint(48.351054, 14.249727);

    ParkhausDbHelper dbHelper = null;
    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ParkhausDbHelper(this);
        db = dbHelper.getReadableDatabase();

        mapView = (MapView) findViewById(R.id.map);
        initMap();
        addMarkers();
    }

    private void initMap()
    {
        mapController = mapView.getController();
        mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setUseDataConnection(useOnlineMap);

        mapController.setZoom(zoom);
        mapController.setCenter(linz);
    }

    private void addMarkers()
    {
        OverlayItem[] items = loadOverlayItems();

        ItemizedOverlayWithFocus<OverlayItem> itemList = new ItemizedOverlayWithFocus<OverlayItem>(Arrays.asList(items),
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        return false;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                }, new DefaultResourceProxyImpl(this));
        itemList.setFocusItemsOnTap(true);
        mapView.getOverlays().add(itemList);
    }

    private OverlayItem[] loadOverlayItems()
    {
        Cursor rows = db.query(ParkhausTbl.TABLE_NAME, ParkhausTbl.ALL_COLUMNS, null, null, null, null, null, null);
        int counter = 0;
        List<Parkhaus> list = new ArrayList<>();

        while (rows.moveToNext())
        {
            int id = rows.getInt(0);
            int anzahlParkplaetze = rows.getInt(1);
            String name = rows.getString(2);
            String standort = rows.getString(3);
            double preis = rows.getDouble(4);

            double latitude = rows.getDouble(5);
            double longitude = rows.getDouble(6);
            GeoPoint geoPoint = new GeoPoint(latitude, longitude);

            list.add(new Parkhaus(id, anzahlParkplaetze, name, standort, geoPoint, preis));
            counter++;
        }

        OverlayItem[] items = new OverlayItem[counter];
        for(int i = 0; i < counter; i++)
        {
            OverlayItem overlayItem = new OverlayItem("Parkhaus", list.get(i).getStandort(), list.get(i).getGeoPoint());
            overlayItem.setMarker(getResources().getDrawable(R.drawable.overlayicon));
            items[i] = overlayItem;
        }
        return  items;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuListe){
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
