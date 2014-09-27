package com.tfivegen.pigeon;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
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
	Button btnScreenDialog_OK;
	String job_title,details;
	
    int loader = R.drawable.loader;
    public ImageView image;
    public String image_url = "http://pigeon.meximas.com/pigeon/job_image/tew_01.jpg";
    public ImageLoader imgLoader;
	
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
      image = (ImageView)screenDialog.findViewById(R.id.imagexy);
      imgLoader = new ImageLoader(screenDialog.getContext());
      imgLoader.DisplayImage(image_url, loader, image);
      
     }
     return screenDialog;
    }
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
     // TODO Auto-generated method stub
     switch(id){
     case(ID_SCREENDIALOG):
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
     
     @Override
     public void onFetchFailure(String msg) {
         // dismiss the progress dialog
         if(dialog != null)  dialog.dismiss();
         // show failure message
         Toast.makeText(this, msg, Toast.LENGTH_LONG).show();        
     }
}