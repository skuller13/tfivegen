package com.tfivegen.pigeon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;



public class Starter extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starter);
	}
	
	public void Findjob(View view){
		Intent in=new Intent(this,JobTask.class); //ตั้งลิ้งหน้าใหม่
		startActivity(in); //เปิดหน้าใหม่
	}
	
	public void Employee(View view){
		// login check	
			if(check_login.id != null)
			{
				Intent Post_page = new Intent(Starter.this, User_profile.class);
		        startActivity(Post_page);
			}
			else
			{
		Intent i=new Intent(this,EmployTask.class); //ตั้งลิ้งหน้าใหม่
		startActivity(i); //เปิดหน้าใหม่
			}
		
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
}
