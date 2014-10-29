package com.tfivegen.pigeon;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tfivegen.pigeon.listviewadaper.Application;

public class Jobdetail extends Activity {
	ProgressDialog progress;
	TextView jname,jdesc,jprice,jview,jphone;
	String image_url="http://pigeon.meximas.com/pigeon/job_image/tew_01.jpg";
	List<Application> data;
	Double latitude=null,longitude=null;
	ImageView image;
	String phone = null,email = null,emp_id=null;
	Dialog screenDialog;
	static final int ID_SCREENDIALOG = 1;
	EditText messageText;
	Button cancel,send;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobdetail);
		Bundle extras=getIntent().getExtras();
		setview();
		setdata(extras);
		
		MyPhoneListener phoneListener = new MyPhoneListener();
		TelephonyManager telephonyManager = 
			(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		// receive notifications of telephony state changes 
		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
		
		progress = new ProgressDialog(this);
		insert_post_thread task = new insert_post_thread();
		task.execute();
		
	}
	
	public void setview(){
		jname=(TextView)findViewById(R.id.jname);
		jdesc=(TextView)findViewById(R.id.jdesc);
		jprice=(TextView)findViewById(R.id.jprice);
		jview=(TextView)findViewById(R.id.jview);
		jphone=(TextView)findViewById(R.id.jphone);
		image=(ImageView)findViewById(R.id.job_im);
	}
	
	public void setdata(Bundle ext){
		jname.setText("Title: "+ext.getString("name"));
		jdesc.setText(ext.getString("descript"));
		jprice.setText("Price: "+String.valueOf(ext.getLong("price")));
		jview.setText("View: "+String.valueOf(ext.getInt("view")));
		emp_id=String.valueOf(ext.getInt("employee_id"));
		latitude=ext.getDouble("latitude");
		longitude=ext.getDouble("longitude");
		ImageLoading();
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(image_url, image);
	}
	
	public void ImageLoading(){
   	 DisplayImageOptions.Builder optionBuilder = new DisplayImageOptions.Builder();
		optionBuilder.showImageForEmptyUri(R.drawable.ic_launcher);
      	optionBuilder.showImageOnFail(R.drawable.ic_launcher);
	    optionBuilder.cacheInMemory(true);
	    optionBuilder.cacheOnDisk(true);
		DisplayImageOptions options = optionBuilder.build();
		ImageLoaderConfiguration.Builder loaderBuilder = 
				new ImageLoaderConfiguration.Builder(getApplicationContext());
		loaderBuilder.defaultDisplayImageOptions(options);
		loaderBuilder.diskCacheExtraOptions(800, 800, null);
		ImageLoaderConfiguration config = loaderBuilder.build();
		ImageLoader.getInstance().init(config);
    }
	
	public void phonecall(View view){
		try {
			// set the data
			String uri = "tel:"+phone;
			Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
			
			startActivity(callIntent);
		}catch(Exception e) {
			Toast.makeText(getApplicationContext(),"Your call has failed...",
				Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	public void messageinstant(View view){
		showDialog(ID_SCREENDIALOG);
	}
	
	   @Override
	    protected Dialog onCreateDialog(int id) {
	     // TODO Auto-generated method stub
	     
	     screenDialog = null;
	     switch(id){
	     case(ID_SCREENDIALOG):
	      screenDialog = new Dialog(this);
	      screenDialog.setContentView(R.layout.message);
	      messageText=(EditText)screenDialog.findViewById(R.id.messagetext1);
	      send=(Button)screenDialog.findViewById(R.id.button1);
	      send.setOnClickListener(sendmessage);
	      cancel=(Button)screenDialog.findViewById(R.id.button2);
	      cancel.setOnClickListener(cancelmessage);
	     }
	     return screenDialog;
	    }
	    
	   public void emailaction(View view){
		   Intent i = new Intent(Intent.ACTION_SEND);  
		   i.setType("message/rfc822") ; // use from live device
		   
		   i.putExtra(Intent.EXTRA_EMAIL,  new String[] { email });  
		   i.putExtra(Intent.EXTRA_SUBJECT,"ต้องการทำงานตอนนี้!");  
		   //i.putExtra(Intent.EXTRA, value)
		   startActivity(Intent.createChooser(i, "Select email application."));
	   }
	   
	    @Override
	    protected void onPrepareDialog(int id, Dialog dialog) {
	     // TODO Auto-generated method stub
	     switch(id){
	     case(ID_SCREENDIALOG):
	     	dialog.setTitle("Send Instant Message");
	      break;
	     }
	    }
	    
	    private Button.OnClickListener sendmessage
	     = new Button.OnClickListener(){
	     
	     @Override
	     public void onClick(View arg0) {
	      // TODO Auto-generated method stub
	      screenDialog.dismiss();
	      String sms = messageText.getText().toString();
	    	try {
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(phone, null, sms, null, null);
				Toast.makeText(getApplicationContext(), "SMS Sent!",
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"SMS faild, please try again later!",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
	     }};
	     
	     private Button.OnClickListener cancelmessage
	     = new Button.OnClickListener(){
	     
	     @Override
	     public void onClick(View arg0) {
	      screenDialog.dismiss();
	     }};
	     
	     public void locate(View view){
	    	 Intent location_act=new Intent(Jobdetail.this,MyLocation.class);
	    	 Bundle extras = new Bundle();
	    	 extras.putInt("mode",2);
	    	 extras.putDouble("latitude", latitude);
	    	 extras.putDouble("longitude", longitude);
	    	 location_act.putExtras(extras);
	    	 startActivity(location_act);
	     }
	     
	/************************* Phone Class ****************/
	private class MyPhoneListener extends PhoneStateListener {
		 
		private boolean onCall = false;
 
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
 
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// phone ringing...
				Toast.makeText(Jobdetail.this, incomingNumber + " calls you", 
						Toast.LENGTH_LONG).show();
				break;
			
			case TelephonyManager.CALL_STATE_OFFHOOK:
				// one call exists that is dialing, active, or on hold
				Toast.makeText(Jobdetail.this, "on call...", 
						Toast.LENGTH_LONG).show();
				//because user answers the incoming call
				onCall = true;
				break;

			case TelephonyManager.CALL_STATE_IDLE:
				// in initialization of the class and at the end of phone call 
				
				// detect flag from CALL_STATE_OFFHOOK
				if (onCall == true) {
					Toast.makeText(Jobdetail.this, "restart app after call", 
							Toast.LENGTH_LONG).show();
 
					// restart our application
					Intent restart = getBaseContext().getPackageManager().
						getLaunchIntentForPackage(getBaseContext().getPackageName());
					restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(restart);
 
					onCall = false;
				}
				break;
			default:
				break;
			}
			
		}
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
					HttpPost post = new HttpPost("http://pigeon.meximas.com/pigeon/find_data.php");
					
					List<NameValuePair> pairs = new ArrayList<NameValuePair>();
					pairs.add(new BasicNameValuePair("member_id",emp_id));
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
		    	
				try {
					JSONObject c = new JSONObject(result);
					phone = c.getString("phone");
					email = c.getString("email");	
					if(phone.length()!=0)
						jphone.setText("Phone: "+phone);
					else{
						RelativeLayout root= (RelativeLayout)findViewById(R.id.job_det);
						RelativeLayout child=(RelativeLayout)findViewById(R.id.useraction);
						root.removeView(child);
					}
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}		    		
		    }
		}
}

