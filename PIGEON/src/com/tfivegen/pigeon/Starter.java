package com.tfivegen.pigeon;

import android.app.Activity;
import android.content.Intent;
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
		Intent i=new Intent(this,EmployTask.class); //ตั้งลิ้งหน้าใหม่
		startActivity(i); //เปิดหน้าใหม่
	}
}
