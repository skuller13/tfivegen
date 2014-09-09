package com.tfivegen.pigeon;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

public class JobTask extends TabActivity {
	
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_job_task);
      
      TabHost tabHost = getTabHost();
      TabSpec Menu = tabHost.newTabSpec("Menu"); //สร้างแท็บใหม่ขึ้นมา (Tab เมนู)
      Intent newIntent = new Intent(this, DataList.class); //ตั้ง่คาหน้า
      Menu.setIndicator("").setContent(newIntent); //setContent คือ ให้ไปหน้านั้นเมื่อกดปุ่ม
      
      TabSpec MyLocation = tabHost.newTabSpec("Location"); //สร้างแท็บใหม่ขึ้นมาอีก (Tab หน้าที่ไปตำแหน่งของเรา)
      Intent new1Intent = new Intent(this, MyLocation.class);//ตั้งค่าหน้า
      MyLocation.setIndicator("").setContent(new1Intent); //setContent คือ ให้ไปหน้านั้นเมื่อกดปุ่ม
      
      tabHost.addTab(MyLocation); //เพิ่มแท็บลงไปในหน้ามือถือ
      tabHost.addTab(Menu); //เพิ่มแท็บลงไปในหน้ามือถือ
      
      tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_find); //ใส่รูปให้แท็บ
      tabHost.getTabWidget().setCurrentTab(0); //ตั้งค่าหน้าแท็บที่กำลังกด (0 คือแท็บแรก)
      tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_list); //ใส่รูปให้แท็บ
   }
}