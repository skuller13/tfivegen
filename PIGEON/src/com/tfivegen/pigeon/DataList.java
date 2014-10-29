package com.tfivegen.pigeon;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tfivegen.pigeon.listviewadaper.Application;
import com.tfivegen.pigeon.listviewadaper.ApplicationAdapter;
import com.tfivegen.pigeon.listviewadaper.FetchDataListener;
import com.tfivegen.pigeon.listviewadaper.FetchDataTask;

public class DataList extends Activity implements FetchDataListener {
	private ProgressDialog dialog;
	ListView list;
	 
	Dialog screenDialog;
	static final int ID_SCREENDIALOG = 1;
	 
	TextView TextOut,desc; 
	View screen;
	Button btnScreenDialog_OK,viewjob;
	String job_title,details;
	int loader=R.drawable.loader;
	Application databundle;
	
    public ImageView image;
    public String image_url = "http://pigeon.meximas.com/pigeon/job_image/tew_01.jpg";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_list);
        list=(ListView)findViewById(R.id.list);
        initView();
	}
	
	private void initView() {
        // show progress dialog
        dialog = ProgressDialog.show(this, "", "Loading...");
        String url = "http://pigeon.meximas.com/pigeon/read.php";
        FetchDataTask task = new FetchDataTask(this);
        task.execute(url);
    }
    
    @Override
    public void onFetchComplete(final List<Application> data) {
        // dismiss the progress dialog
        if(dialog != null)  dialog.dismiss();
        // create new adapter
        ApplicationAdapter adapter = new ApplicationAdapter(this, data);
        // set the adapter to list
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        		job_title=data.get(position).getTitle();
        		details=data.get(position).getDescript();
        		databundle=data.get(position);
        		showDialog(ID_SCREENDIALOG);
        	  }
        	});
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
     // TODO Auto-generated method stub
     
     screenDialog = null;
     switch(id){
     case(ID_SCREENDIALOG):
      screenDialog = new Dialog(this);
      screenDialog.setContentView(R.layout.dialog);
      TextOut = (TextView)screenDialog.findViewById(R.id.textout);
      desc = (TextView)screenDialog.findViewById(R.id.description);
      btnScreenDialog_OK = (Button)screenDialog.findViewById(R.id.okdialogbutton);
      btnScreenDialog_OK.setOnClickListener(btnScreenDialog_OKOnClickListener);
      viewjob = (Button)screenDialog.findViewById(R.id.gotopage);
      viewjob.setOnClickListener(viewjobdetail);
      image= new ImageView(DataList.this);
      image=(ImageView)screenDialog.findViewById(R.id.imagexy);
      ImageLoading();
      
     }
     return screenDialog;
    }
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
     // TODO Auto-generated method stub
     switch(id){
     case(ID_SCREENDIALOG):
    	ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(image_url, image);
     	dialog.setTitle("Job Details");
     	TextOut.setText(job_title);
     	desc.setText(details);
      break;
     }
    }
     
    private Button.OnClickListener btnScreenDialog_OKOnClickListener
     = new Button.OnClickListener(){
     
     @Override
     public void onClick(View arg0) {
      // TODO Auto-generated method stub
      screenDialog.dismiss();
     }};
     
     private Button.OnClickListener viewjobdetail
     = new Button.OnClickListener(){
     
     @Override
     public void onClick(View arg0) {
      // TODO Auto-generated method stub
      screenDialog.dismiss();
      Intent in=new Intent(getApplicationContext(),Jobdetail.class); //ตั้งลิ้งหน้าใหม่
      Bundle extras = new Bundle();
      extras.putString("name", databundle.getTitle());
      extras.putString("descript", databundle.getDescript());
      extras.putLong("price", databundle.getPrice());
      extras.putInt("view", databundle.getView());
      extras.putInt("employee_id", databundle.getEmpid());
      in.putExtras(extras);
      startActivity(in); //เปิดหน้าใหม่
     }};
     
     @Override
     public void onFetchFailure(String msg) {
         if(dialog != null)  dialog.dismiss();// dismiss the progress dialog
         Toast.makeText(this, msg, Toast.LENGTH_LONG).show();  // show failure message
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
 		loaderBuilder.diskCacheExtraOptions(240, 240, null);
 		
 		ImageLoaderConfiguration config = loaderBuilder.build();
 		
 		// ตั้งค่าออปชันเริ่มต้นที่ได้ประกาศไว้ทั้งหมด ให้กับ ImageLoader
 		ImageLoader.getInstance().init(config);
     }
}