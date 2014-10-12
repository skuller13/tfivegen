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

import com.tfivegen.pigeon.PostActivity.insert_post_thread;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class User_profile extends Activity {
	ProgressDialog progress;
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
		mbox(json_result);
	}
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

