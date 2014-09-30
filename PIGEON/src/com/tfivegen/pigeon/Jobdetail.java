package com.tfivegen.pigeon;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tfivegen.pigeon.listviewadaper.Application;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class Jobdetail extends Activity {
	TextView jname,jdesc,jprice,jview,jphone;
	String image_url="http://pigeon.meximas.com/pigeon/job_image/tew_01.jpg";
	List<Application> data;
	ImageView image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobdetail);
		Bundle extras=getIntent().getExtras();
		setview();
		setdata(extras);
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
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(image_url, image);
	}
	
	public void ImageLoading(){
   	 DisplayImageOptions.Builder optionBuilder = new DisplayImageOptions.Builder();
		// จากนั้นก็เซ็ตออปชันต่างๆ ตามรายการข้างล่างนี้
		// ตั้งว่าจะโชว์รูปอะไร เมื่อ ImageView ไม่มีภาพ
		optionBuilder.showImageForEmptyUri(R.drawable.ic_launcher);
		// จะให้โชว์รูปอะไร ถ้าโหลดรูปภาพมาแสดงไม่ได้
      	optionBuilder.showImageOnFail(R.drawable.ic_launcher);
	        // ตั้งให้ cache รูปลง memory
	        optionBuilder.cacheInMemory(true);
	        // ตั้งให้ cache รูปลงเครื่อง
	        optionBuilder.cacheOnDisk(true);
		// จากนั้นก็ build ค่าทั้งหมด ใส่ตัวแปร options 
		DisplayImageOptions options = optionBuilder.build();
		
		
		ImageLoaderConfiguration.Builder loaderBuilder = 
				new ImageLoaderConfiguration.Builder(getApplicationContext());
		
		// ตั้งค่า option โดยใช้ options ที่ได้ตั้งค่าไว้ด้านบน
		loaderBuilder.defaultDisplayImageOptions(options);
		// ตั้งค่าให้ cache รูปมีขนาด 240x240 
		loaderBuilder.diskCacheExtraOptions(800, 800, null);
		
		ImageLoaderConfiguration config = loaderBuilder.build();
		
		// ตั้งค่าออปชันเริ่มต้นที่ได้ประกาศไว้ทั้งหมด ให้กับ ImageLoader
		ImageLoader.getInstance().init(config);
    }
}
