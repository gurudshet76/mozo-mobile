package com.carmozo.driverapp.UiUtilities;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by shreyasgs on 27-09-2015.
 */
public class DriverJobLists implements Serializable {
    private String jobId;
    private String customerId;
    private String jobTitle;
    private String customerName;
    private String customerMobileNo;
    private long scheduleTime;
    private String pickupPoint;
    //private LatLng startLatLong;
    private String destAddress;
    //private LatLng endLatLong;
    private double startLat;
    private double startLng;
    private double endLat;
    private double endLng;
    private String subOrderId;
    private String logisticsId;
    private int statusId;

    private static final long serialVersionUID = 1L;

    public DriverJobLists() {

    }

    public DriverJobLists(String jobId,
                          String customerId,
                          String jobTitle,
                          String customerName,
                          String customerMobileNo,
                          long scheduleTime, String pickupPoint, LatLng startLatLong,
                          String destAddress, LatLng endLatLong, int statusId) {
        this.jobId = jobId;
        this.customerId = customerId;
        this.jobTitle = jobTitle;
        this.customerName = customerName;
        this.customerMobileNo = customerMobileNo;
        this.scheduleTime = scheduleTime;
        this.pickupPoint = pickupPoint;
        //this.startLatLong = startLatLong;
        this.destAddress = destAddress;
        //this.endLatLong =  endLatLong;
        this.statusId = statusId;
    }

    public void setJobId (String jobId){
        this.jobId = jobId ;
    }

    public String getJobId () {
        return this.jobId ;
    }

    public void setCustomerId (String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId (){
        return this.customerId ;
    }


    public void setJobTitle (String jobTitle) {
        this.jobTitle = jobTitle ;
    }

    public String getJobTitle () {
        return this.jobTitle ;
    }

    public void setCustomerName (String customerName){
        this.customerName = customerName ;
    }

    public String getCustomerName () {
        return this.customerName ;
    }

    public void setCustomerMobileNo (String customerMobileNo) {
        this.customerMobileNo = customerMobileNo ;
    }

    public String getCustomerMobileNo () {

        return this.customerMobileNo ;
    }

    public void setScheduleTime (long scheduleTime) {
        this.scheduleTime = scheduleTime ;
    }

    public long getScheduleTime () {
        return this.scheduleTime ;
    }

    public void setPickupPoint (String pickupPoint) {
        this.pickupPoint = pickupPoint ;
    }

    public String getPickupPoint () {
        return this.pickupPoint ;
    }

    public void setStartLatLong (LatLng startLatLong){
        this.startLat = startLatLong.latitude;
        this.startLng = startLatLong.longitude;
        //this.startLatLong = startLatLong ;
    }

    public LatLng getStartLatLong () {
        //return this.startLatLong;
        LatLng sLatLong = new LatLng(startLat, startLng);
        return sLatLong;
    }

    public void setDestAddress (String destAddress) {
        this.destAddress = destAddress;
    }

    public String getDestAddress () { return this.destAddress; }

    public void setEndLatLong (LatLng endLatLong) {
        this.endLat = endLatLong.latitude;
        this.endLng = endLatLong.longitude;
        //this.endLatLong = endLatLong;
    }

    public LatLng getEndLatLong () {
        LatLng eLatLong = new LatLng(endLat, endLng);
        //return this.endLatLong ;
        return eLatLong;
    }

    public void setSubOrderId (String subOrderId) { this.subOrderId = subOrderId; }

    public String getSubOrderId () { return this.subOrderId; }

    public void setLogisticsId (String logisticsId) { this.logisticsId = logisticsId; }

    public String getLogisticsId ( ) { return this.logisticsId; }

    public void setStatusId (int statusId) { this.statusId = statusId; }

    public int getStatusId (){ return this.statusId; }

}
