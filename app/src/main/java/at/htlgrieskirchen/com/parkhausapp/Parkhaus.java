package at.htlgrieskirchen.com.parkhausapp;

import org.osmdroid.util.GeoPoint;

/**
 * Created by sgschaider on 11.06.2015.
 */
public class Parkhaus
{
    int id;
    int maxAnzahlParkplaetze;
    String name;
    String standort;
    GeoPoint geoPoint;
    double preis;

    public Parkhaus() {
    }

    public Parkhaus(int id, int maxAnzahlParkplaetze, String name, String standort, GeoPoint geoPoint, double preis) {
        this.id = id;
        this.maxAnzahlParkplaetze = maxAnzahlParkplaetze;
        this.name = name;
        this.standort = standort;
        this.geoPoint = geoPoint;
        this.preis = preis;
    }

    @Override
    public String toString() {
        return "Parkhaus{" +
                "preis=" + preis +
                ", geoPoint=" + geoPoint +
                ", standort='" + standort + '\'' +
                ", name='" + name + '\'' +
                ", maxAnzahlParkplaetze=" + maxAnzahlParkplaetze +
                ", id=" + id +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxAnzahlParkplaetze() {
        return maxAnzahlParkplaetze;
    }

    public void setMaxAnzahlParkplaetze(int maxAnzahlParkplaetze) {
        this.maxAnzahlParkplaetze = maxAnzahlParkplaetze;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStandort() {
        return standort;
    }

    public void setStandort(String standort) {
        this.standort = standort;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }
}
