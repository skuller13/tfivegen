package com.tfivegen.pigeon;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.tfivegen.pigeon.EmployTask.login_thread;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PostActivity extends Activity implements android.location.LocationListener {
	ProgressDialog progress;
	TextView job_name; 
	TextView price;
	TextView description;
	Double longitude,latitude;
	public static int MIN_TIME=1000*60*1,MIN_DISTANCE=10,MAP_ZOOM=13;
	
	LocationManager lm;
	Location l;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		l=getLocation();
	}
	
	public void post_click(View view)
	{
		progress = new ProgressDialog(this);
		job_name = (TextView)findViewById(R.id.job_name_text);
		price = (TextView)findViewById(R.id.price_text);
		description = (TextView)findViewById(R.id.description_text);
		
		insert_post_thread task = new insert_post_thread();
		task.execute();
	}
	
	public void mbox(String message)
	{
		  	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
		    dlgAlert.setMessage(message);
		    dlgAlert.setTitle("Message");              
		    dlgAlert.setPositiveButton("OK", null);
		    dlgAlert.setCancelable(true);
		    dlgAlert.create().show();		    
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
	
/****************************************************************************************************************/
	
	// Sub class
	
		class insert_post_thread extends AsyncTask<Void,Void,Integer> 
		{
			String result = "nothing";
			protected Integer doInBackground(Void... params)
			{	
				try 
				{
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost("http://pigeon.meximas.com/pigeon/insert_post.php");
					
					List<NameValuePair> pairs = new ArrayList<NameValuePair>();
					pairs.add(new BasicNameValuePair("user_id", check_login.id));
					pairs.add(new BasicNameValuePair("username", check_login.username));
					pairs.add(new BasicNameValuePair("password", check_login.password));
					
					pairs.add(new BasicNameValuePair("job_name", job_name.getText().toString()));
					pairs.add(new BasicNameValuePair("price", price.getText().toString()));
					pairs.add(new BasicNameValuePair("description", description.getText().toString()));
					pairs.add(new BasicNameValuePair("latitude",String.valueOf(l.getLatitude())));
					pairs.add(new BasicNameValuePair("longitude",String.valueOf(l.getLongitude())));

					post.setEntity(new UrlEncodedFormEntity(pairs));
					HttpResponse response = client.execute(post);
					result = EntityUtils.toString(response.getEntity());
					
				} 
				catch (ClientProtocolException e) 
				{		
					result = e.toString();
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					result = e.toString();
					e.printStackTrace();
				}		
							
				return null;	
			}
			protected void onPreExecute()
			{
				progress.setMessage("Connecting...");
				progress.show();
			}
		    protected void onProgressUpdate() 
		    {
		    	
		    }		
		    protected void onPostExecute(Integer n)
		    {
		    	
		    	progress.dismiss();
		    	if(result.equals("done"))
		    	{
		    		//Intent Post_page = new Intent(PostActivity.this, EmployTask.class);
		            //startActivity(Post_page);
		    		finish();
		    	}
		    	else
		    	{
		    		mbox(result);
		    	}
		    	
		    }
		}

@Override
public void onLocationChanged(Location location) {
	// TODO Auto-generated method stub
	
}

@Override
public void onProviderDisabled(String provider) {
	// TODO Auto-generated method stub
	
}

@Override
public void onProviderEnabled(String provider) {
	// TODO Auto-generated method stub
	
}

@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
	// TODO Auto-generated method stub
	
}
		
		
}
