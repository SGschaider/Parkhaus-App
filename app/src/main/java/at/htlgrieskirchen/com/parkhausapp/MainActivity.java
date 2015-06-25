package at.htlgrieskirchen.com.parkhausapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
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
public class MainActivity extends Activity implements LocationListener {

    protected MapView mapView;
    protected IMapController mapController;

    private boolean useOnlineMap = true;
    private int zoom = 12;
    private GeoPoint linz = new GeoPoint(48.351054, 14.249727);

    ParkhausDbHelper dbHelper = null;
    SQLiteDatabase db = null;

    private static LocationManager locMan = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ParkhausDbHelper(this);
        db = dbHelper.getReadableDatabase();

        locMan = (LocationManager) getSystemService(LOCATION_SERVICE);

        mapView = (MapView) findViewById(R.id.map);
        initMap();
        addMarkers();
        meinStandort();
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
                        showRouteConfirmation(item.getPoint());
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

    private void showRouteConfirmation(final GeoPoint geoPoint)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.routeConfirmation))
               .setCancelable(true)
               .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       routeBerechnen(geoPoint);
                   }
               }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
               });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void routeBerechnen(GeoPoint endPoint)
    {
        Location loc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc == null)
        {
            Toast.makeText(this, "Route kann nicht berechnet werden - Ihr Standort ist nicht bekannt!", Toast.LENGTH_LONG).show();
            return;
        }

        //mit Polyline
        RoadManager roadManager = new OSRMRoadManager();

        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(new GeoPoint(loc.getLatitude(), loc.getLongitude())); //startPoint
        waypoints.add(endPoint); //endPoint

        Road road = roadManager.getRoad(waypoints);
        org.osmdroid.bonuspack.overlays.Polyline roadOverlay = RoadManager.buildRoadOverlay(road, this);
        mapView.getOverlays().add(roadOverlay);
        mapView.invalidate();

        /*
        RoadManager roadManager = new MapQuestRoadManager("_Hwu5Npb7EaSxje7DBtTnTQgwIBG0XW3M_");
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(new GeoPoint(48.314800, 14.214778)); //startPoint
        waypoints.add(new GeoPoint(48.311490, 14.282241)); //endPoint
        roadManager.addRequestOption("routeType=bicycle");

        Road road = roadManager.getRoad(waypoints);
        org.osmdroid.bonuspack.overlays.Polyline roadOverlay = RoadManager.buildRoadOverlay(road, this);
        mapView.getOverlays().add(roadOverlay);
        mapView.invalidate(); */
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
            OverlayItem overlayItem = new OverlayItem(list.get(i).getName(), list.get(i).getStandort(), list.get(i).getGeoPoint());
            overlayItem.setMarker(getResources().getDrawable(R.drawable.overlayicon));
            items[i] = overlayItem;
        }
        return  items;
    }

    private void meinStandort()
    {
        Location loc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        GeoPoint geoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
        if(loc == null)
        {
            Toast.makeText(this, "Aktueller Standort konnten nicht abgerufen werden!", Toast.LENGTH_LONG).show();
            return;
        }

        OverlayItem ich = new OverlayItem("Ich", "Mein Standort", geoPoint);
        ich.setMarker(getResources().getDrawable(R.drawable.meinstandort));
        OverlayItem[] item = new OverlayItem[] {ich};

        ItemizedOverlayWithFocus<OverlayItem> itemList = new ItemizedOverlayWithFocus<OverlayItem>(Arrays.asList(item), null, new DefaultResourceProxyImpl(this));
        itemList.setFocusItemsOnTap(true);
        mapView.getOverlays().add(itemList);
        mapView.invalidate();
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


    @Override
    protected void onResume() {
        super.onResume();
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locMan.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null) return;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}
}
