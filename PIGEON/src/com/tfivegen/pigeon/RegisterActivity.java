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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterActivity extends Activity {
	public Boolean result_of_activity = false;
	TextView username; 
	TextView password_1; 
	TextView password_2; 
	TextView email; 
	TextView phone;
	public register_thread task;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}
	
	public void register_click(View view)
	{
		username = (TextView)findViewById(R.id.username);
		password_1 = (TextView)findViewById(R.id.password_1);
		password_2 = (TextView)findViewById(R.id.password_2);
		email = (TextView)findViewById(R.id.email);
		phone = (TextView)findViewById(R.id.phone);
		
		if(password_1.getText().toString().equals(password_2.getText().toString()) != true)
		{
			mbox("Password not match.");
		}
		else
		{
			task = new register_thread();
			task.execute();
		}
	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	*/
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

	public Boolean result_of_activity()
	{
		return result_of_activity = true;
	}
	
/****************************************************************************************************************/
	
	// Sub class
	
		class register_thread extends AsyncTask<Void,Void,Integer> 
		{
			String result = "nothing";
			protected Integer doInBackground(Void... params)
			{	
				try 
				{
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost("http://pigeon.meximas.com/pigeon/register.php");
					
					List<NameValuePair> pairs = new ArrayList<NameValuePair>();
					pairs.add(new BasicNameValuePair("username", username.getText().toString()));
					pairs.add(new BasicNameValuePair("password", password_1.getText().toString()));
					pairs.add(new BasicNameValuePair("email", email.getText().toString()));
					pairs.add(new BasicNameValuePair("phone", phone.getText().toString()));
					post.setEntity(new UrlEncodedFormEntity(pairs,"UTF-8"));
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
				
			}
		    protected void onProgressUpdate() 
		    {
		    	
		    }
		   
		    protected void onPostExecute(Integer n)
		    {
		    	try 
		    	{
					JSONObject c = new JSONObject(result);
					String error_message = c.getString("error_message");
					if(error_message.equals("0"))
					{
						Toast.makeText(getApplicationContext(),"Success... you were member.", Toast.LENGTH_LONG).show();	
						finish();
					}
					else
					{
						Toast.makeText(getApplicationContext(),error_message, Toast.LENGTH_LONG).show();	
					}
					
		    	} 
		    	catch (JSONException e) 
		    	{
					e.printStackTrace();
				}
		    	
	
		    	
		    }
		}
}
