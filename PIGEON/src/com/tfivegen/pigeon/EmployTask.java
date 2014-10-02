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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EmployTask extends Activity {
	ProgressDialog progress;
	TextView username; 
	TextView password; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employ_task);
		// login check
				if(check_login.id != null)
				{
					Toast.makeText(getApplicationContext(),"Auto Login success!", Toast.LENGTH_LONG).show();	
					Intent Post_page = new Intent(EmployTask.this, User_profile.class);
		            startActivity(Post_page);
				}
	}
	public void login_click(View view)
	{
		progress = new ProgressDialog(this);
		username = (TextView)findViewById(R.id.username_register);
		password = (TextView)findViewById(R.id.password_text);
		
		login_thread task = new login_thread();
		task.execute();
		
	}
	public void register_click(View view)
	{
		Intent Register_page = new Intent(EmployTask.this, RegisterActivity.class);
        startActivity(Register_page);
	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
	}*/
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
   {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) 
        {
        	this.finish();
            return false; //I have tried here true also
        }
        return super.onKeyDown(keyCode, event);
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
	
		class login_thread extends AsyncTask<Void,Void,Integer> 
		{
			String result = "nothing";
			protected Integer doInBackground(Void... params)
			{	
				try 
				{
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost("http://pigeon.meximas.com/pigeon/login.php");
					
					List<NameValuePair> pairs = new ArrayList<NameValuePair>();
					pairs.add(new BasicNameValuePair("username", username.getText().toString()));
					pairs.add(new BasicNameValuePair("password", password.getText().toString()));
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
		    	try 
		    	{
					JSONObject c = new JSONObject(result);
					check_login.id = c.getString("id");
					check_login.username = c.getString("username");
					check_login.password = c.getString("password");
					check_login.email = c.getString("email");
					check_login.phone = c.getString("phone");
					check_login.error_status = c.getString("error_status");
					
					if(check_login.error_status.equals("0"))
					{
						Toast.makeText(getApplicationContext(),"Login success!", Toast.LENGTH_LONG).show();	
						Intent Post_page = new Intent(EmployTask.this, User_profile.class);
			            startActivity(Post_page);
			            finish();
					}
					else
					{
						Toast.makeText(getApplicationContext(),"Error : " + check_login.error_status, Toast.LENGTH_LONG).show();	
					}
					
				} 
		    	catch (JSONException e) 
				{
					mbox( e.toString() + "\n" + result);
				}
		    }
		}
}
