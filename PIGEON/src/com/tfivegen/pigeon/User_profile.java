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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tfivegen.pigeon.listviewadaper.Application;
import com.tfivegen.pigeon.listviewadaper.ApplicationAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class User_profile extends Activity {
	ProgressDialog progress;
	List<Application> apps;
	Dialog screenDialog;
	String job_title,details;
	static final int ID_SCREENDIALOG = 1;
	ListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
	}

	
	public void sell(View view)
	{
		Toast.makeText(getApplicationContext(),"sell clicked", Toast.LENGTH_LONG).show();	
		Intent Post_page = new Intent(User_profile.this, PostActivity.class);
        startActivity(Post_page);
	}
	public void mypost(View view)
	{
		Toast.makeText(getApplicationContext(),"mypost clicked", Toast.LENGTH_LONG).show();	
		progress = new ProgressDialog(this);
		read_post_thread task = new read_post_thread();
		task.execute();
		
	}
	public void mypost_click_finish(String json_result)
	{
		try {
            // convert json string to json array
            JSONArray aJson = new JSONArray(json_result);
            // create apps list
            apps = new ArrayList<Application>();
            
            for(int i=0; i<aJson.length(); i++) {
                JSONObject json = aJson.getJSONObject(i);
                Application app = new Application();
                app.setTitle(json.getString("job_name"));
                app.setDescript(json.getString("description"));
                app.setPrice(Integer.parseInt(json.getString("price")));
                app.setEmpid(Integer.parseInt(json.getString("employer_id")));  
                app.SetView(Integer.parseInt(json.getString("view")));
                app.setLatitude(Double.parseDouble(json.getString("latitude")));
                app.setLongitude(Double.parseDouble(json.getString("longitude")));
                apps.add(app);
            }
            
            //notify the activity that fetch data has been complete
        } catch (JSONException e){
            return;
        }
		//mbox(json_result);
		showDialog(ID_SCREENDIALOG);
	}
	
	protected Dialog onCreateDialog(int id) {
	     // TODO Auto-generated method stub
	     
	     screenDialog = null;
	     switch(id){
	     case(ID_SCREENDIALOG):
	      screenDialog = new Dialog(this);
	      screenDialog.setContentView(R.layout.activity_data_list);
	      list=(ListView)screenDialog.findViewById(R.id.list);
	      ApplicationAdapter adapter = new ApplicationAdapter(this, apps);
	      list.setAdapter(adapter);
	      list.setOnItemClickListener(new OnItemClickListener(){
	        	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	        	    screenDialog.dismiss();
	        	    screenDialog.cancel();
	        		Intent in=new Intent(getApplicationContext(),Jobdetail.class);
	        	    Bundle extras = new Bundle();
	        		extras.putString("name", apps.get(position).getTitle());
	        		extras.putString("descript", apps.get(position).getDescript());
	        	    extras.putLong("price", apps.get(position).getPrice());
	        	    extras.putInt("view", apps.get(position).getView());
	        	    extras.putDouble("latitude", apps.get(position).getLatitude());
	        	    extras.putDouble("longtitude",apps.get(position).getLongitude());
	        	    in.putExtras(extras);
	        	    startActivity(in); //เปิดหน้าใหม่*/
	        	  }
	        	});
	     }
	     return screenDialog;
	    }
	    
	    @Override
	    protected void onPrepareDialog(int id, Dialog dialog) {
	     // TODO Auto-generated method stub
	     switch(id){
	     case(ID_SCREENDIALOG):
	     	dialog.setTitle("Job Details");
	     	Button myButton = new Button(this);
	     	myButton.setText("Cancel");
	     	myButton.setBackgroundColor(Color.parseColor("#ff4c67"));
	     	myButton.setTextColor(Color.parseColor("#ffffff"));
	     	RelativeLayout datlis = (RelativeLayout)screenDialog.findViewById(R.id.datalist01);
	     	LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	     	lp.addRule(RelativeLayout.BELOW, list.getId());
	     	datlis.addView(myButton, lp);
	     	myButton.setOnClickListener(dismissscreen);
	     	
	      break;
	     }
	    }
	    
	    private Button.OnClickListener dismissscreen= new Button.OnClickListener(){
	    	@Override
		     public void onClick(View arg0) {
		      // TODO Auto-generated method stub
		      screenDialog.dismiss();
		     }};
	     
	public void logout(View view)
	{
		try
		{
			SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("user_username", null);
			editor.putString("user_password", null);
			editor.commit();
		}
		catch(Exception ex)
		{
			mbox(ex.toString());
		}
		
		check_login.logout();
		finish();
		Toast.makeText(getApplicationContext(),"Logout...", Toast.LENGTH_LONG).show();	
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_profile, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
/****************************************************************************************************************/
	// Sub class
	
		class read_post_thread extends AsyncTask<Void,Void,Integer> 
		{
			String result = "nothing";
			protected Integer doInBackground(Void... params)
			{	
				
				try 
				{
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost("http://pigeon.meximas.com/pigeon/customer_post.php");
					
					List<NameValuePair> pairs = new ArrayList<NameValuePair>();
					pairs.add(new BasicNameValuePair("employer_id", check_login.id));
					
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
		    	mypost_click_finish(result);
		    }
		}
}

