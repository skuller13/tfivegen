package com.tfivegen.pigeon.listviewadaper;

public class Application {
    private String jobname,description;
    private int price;
    private int empid;
    //private String icon;
    
    public String getTitle() {
        return jobname;
    }
    public void setTitle(String jobname) {
        this.jobname = jobname;
    }
    public String getDescript() {
        return description;
    }
    public void setDescript(String text) {
        this.description = text;
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
    /*public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }*/
}
