package com.tfivegen.pigeon;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tfivegen.pigeon.listviewadaper.Application;
import com.tfivegen.pigeon.listviewadaper.FetchDataListener;
import com.tfivegen.pigeon.listviewadaper.FetchDataTask;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

public class MyLocation extends Activity implements android.location.LocationListener,FetchDataListener {
	public static LocationManager lm;
	public static String provider;
	public static Location l;
	private GoogleMap googleMap;
	protected String latitude,longitude; 
	public static int MIN_TIME=1000*60*1,MIN_DISTANCE=10,MAP_ZOOM=13;
	public static int mode=0;
	public static LatLng MyPos;
	public static double latitude_pos,longitude_pos;
	private ProgressDialog dialog;
	
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_my_location);
      Bundle extras= getIntent().getExtras();
      retrievedata(extras);
      try {
    	  		l=getLocation(); //เข้าฟังก์ชัน getLocation เพื่อรับตำแหน่งปัจจุบัน
    	  		if(mode!=2){
    	  			MyPos=new LatLng(l.getLatitude(),l.getLongitude()); //MyPos เอาไว้เก็บค่า 2 ค่านั่นคือ ละติจูด และ ลองจิจูด นั่นเอง...!
    	  			initView();
    	  		}
    	  		else{
    	  			MyPos=new LatLng(latitude_pos,longitude_pos);
    	  			Toast.makeText(getApplicationContext(), String.valueOf(latitude_pos)+":"+String.valueOf(longitude_pos), Toast.LENGTH_SHORT).show();
    	  		}
    	  			
  	  			if (googleMap == null) 
  	  				googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap(); //เช็คว่ามีแผนที่อยู่แล้วหรือไม่ ถ้าไม่ ก็จะรับแผนที่เข้ามาใส่ใน R.id.map
  	  			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //ตั้งค่าแผนที่ให้มีแค่ ถนน กับอาคาร (ปกติจะเห็นภูมิปรเทศด้วย)
  	  			googleMap.addMarker(new MarkerOptions()
  	  			.position(MyPos)
  	  			.title("You are here")
  	  			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))); 
  	  			//เพิ่มจุดบนแผนที่
         
  	  			CameraPosition cameraPosition = new CameraPosition.Builder()
  	  				.target(MyPos)
  	  				.zoom(MAP_ZOOM)
  	  				.build(); //ตั้งค่าระดับแหน่งการซูม,ตำแหน่งละลอง
  	  			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); //กล้องจับไปตามค่าที่ตั้งไว้ข้างบน
      		} catch (Exception e) {
      			e.printStackTrace(); //Try,catch อันนี้เป็นประมาณว่า ดักจับ Error ถ้าเกิด Error ขึ้นจะรายงานบน debug log
      	}
   }
   
   public void retrievedata(Bundle ext){
	   mode=ext.getInt("mode");
	   latitude_pos=ext.getDouble("latitude");
	   longitude_pos=ext.getDouble("longitude");
   }
   
   private void buildAlertMessageNoGps() { //ฟังก์ชั่นนี้เอาไว้ถามผู้ใช้เมื่อไม่มีการเปิด device รับตำแหน่งใดๆ
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
	           .setCancelable(false)
	           .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)); //เปิดsettingตำแหน่ง
	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                    dialog.cancel(); //ปุ่มยกเลิก
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show(); //แสดงหน้าตั้งค่าตำแหน่ง
  }
   
   @Override
   public void onLocationChanged(Location location) {
   }
   
   public Location getLocation() {
	    lm = (LocationManager)this.getSystemService(LOCATION_SERVICE);
	    boolean GPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER); //เช็คว่า GPS เปิดไว้มั้ย
	    boolean network = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER); //เช็คว่า ตำแหน่งจาก network เปิดไว้มั้ย
	    Location location;
	    if (!GPS && !network) {
	    	buildAlertMessageNoGps(); //ถ้าไม่มีตัวรับตำแหน่งเลย ให้เข้าฟังก์ชันนี้
	    } else {
	    	if (GPS) { //ถ้า GPS หาตำแหน่งได้ ให้รับ location จาก GPS
	            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME, MIN_DISTANCE, this);
	            location = lm
	                    .getLastKnownLocation(LocationManager.GPS_PROVIDER); //รับตำแหน่งจาก GPS
	            if (location != null) {
	                return location;
	            }
	        }
	    	else if (network) { //ถ้า network หาตำแหน่งให้ได้ ให้รับ location จาก network เลย
	            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME, MIN_DISTANCE, this);
	            location = lm
	                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //รับตำแหน่งจาก network
	            if (location != null) {
	                return location; //ส่งค่า location กลับไป
	            }
			}
	    }
	    return null;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	
	}

	private void initView() {
    // show progress dialog
		dialog = ProgressDialog.show(this, "", "Loading...");
		String url = "http://pigeon.meximas.com/pigeon/read1.php";
		FetchDataTask task = new FetchDataTask(this);
		task.execute(url);
	}

	@Override
	public void onFetchComplete(List<Application> data) {
		if(dialog != null)  dialog.dismiss();
		for(int i=0;i<data.size();i++)
		{
			MyPos=new LatLng(data.get(i).getLatitude(),data.get(i).getLongitude());
			googleMap.addMarker(new MarkerOptions()
				.position(MyPos)
				.title(data.get(i).getTitle())
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
		}
	}

	@Override
	public void onFetchFailure(String msg) {
		// TODO Auto-generated method stub 
	}
}