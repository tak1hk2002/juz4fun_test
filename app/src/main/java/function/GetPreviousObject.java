package function;

import java.io.Serializable;

/**
 * Created by LAM on 23/4/2015.
 * GetPreviousObject
 *      id (int) => The drawable value of the pic in Ranking.java
 *      latitude (double) => the place number, just like the coordinates
 *      longitude (double) => the place number
 *
 *
 */
//Temporarily store an object and pass it to next activity
@SuppressWarnings("serial")
public class GetPreviousObject implements Serializable {

    public GetPreviousObject(int id, double latitude, double longitude, String companyName ){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.companyName = companyName;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public double getLatitude() {
        return this.latitude;
    }
    public double getLongitude(){
        return this.longitude;
    }
    public String getCompanyName() {
        return this.companyName;
    }
    public void setLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private int id;
    private double latitude, longitude;
    private String companyName;


}
