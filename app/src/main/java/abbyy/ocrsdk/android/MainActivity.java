package abbyy.ocrsdk.android;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	private final int TAKE_PICTURE = 0;
	private final int SELECT_FILE = 1; 
	
	private String resultUrl = "result.txt";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
	public void captureImageFromSdCard( View view ) {
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    	intent.setType("image/*");

    	startActivityForResult(intent, SELECT_FILE);
    }
	
	public static final int MEDIA_TYPE_IMAGE = 1;

	private static Uri getOutputMediaFileUri(){
	      return Uri.fromFile(getOutputMediaFile());
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(){
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "TextWord");
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            return null;
	        }
	    }

	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "image.jpg" );

	    return mediaFile;
	}
    
    public void captureImageFromCamera( View view) {
    	Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    	Uri fileUri = getOutputMediaFileUri(); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        startActivityForResult(intent, TAKE_PICTURE);
    } 
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		
		String imageFilePath = null;

		switch (requestCode) {
		case TAKE_PICTURE:
			imageFilePath = getOutputMediaFileUri().getPath();
			break;
		case SELECT_FILE: {
			Uri imageUri = data.getData();

			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cur = managedQuery(imageUri, projection, null, null, null);
			cur.moveToFirst();
			imageFilePath = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
			}
			break;
		}

		//Remove output file
		deleteFile(resultUrl);
		
        Intent results = new Intent( this, ResultsActivity.class);
    	results.putExtra("IMAGE_PATH", imageFilePath);
    	results.putExtra("RESULT_PATH", resultUrl);
    	startActivity(results);
    } 
    
}
