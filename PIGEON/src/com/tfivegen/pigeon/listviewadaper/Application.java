package com.tfivegen.pigeon.listviewadaper;

public class Application {
    private String jobname;
    private int price;
    private int empid;
    private String desc;
    private int view;
    private double longitude,latitude;
    
    public String getTitle() {
        return jobname;
    }
    public void setTitle(String jobname) {
        this.jobname = jobname;
    }
    public long getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getEmpid() {
        return empid;
    }
    public void setEmpid(int empid) {
        this.empid = empid;
    }
    
    public String getDescript() {
    	return desc;
    }
    
    public void setDescript(String descript) {
    	this.desc=descript;
    }
    
    public int getView() {
    	return view;
    }
    
    public void SetView(int viewer) {
    	this.view=viewer;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
