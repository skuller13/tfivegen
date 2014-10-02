package com.tfivegen.pigeon;

import android.app.Activity;
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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class PostActivity extends Activity {
	ProgressDialog progress;
	TextView job_name; 
	TextView price;
	TextView description;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
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
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post, menu);
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
					pairs.add(new BasicNameValuePair("job_name", job_name.getText().toString()));
					pairs.add(new BasicNameValuePair("price", price.getText().toString()));
					pairs.add(new BasicNameValuePair("description", description.getText().toString()));
					pairs.add(new BasicNameValuePair("latitude","0.000000"));
					pairs.add(new BasicNameValuePair("longitude","0.000000"));
					pairs.add(new BasicNameValuePair("username","root"));
					pairs.add(new BasicNameValuePair("password","1234"));
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
}
