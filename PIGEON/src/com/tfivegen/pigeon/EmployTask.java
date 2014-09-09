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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class EmployTask extends Activity {
	ProgressDialog progress;
	TextView username; 
	TextView password; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employ_task);
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
		    	if(result.equals("done"))
		    	{
		    		Intent Post_page = new Intent(EmployTask.this, PostActivity.class);
		            startActivity(Post_page);
		    	}
		    	else
		    	{
		    		mbox(result);
		    	}
		    }
		}
}
