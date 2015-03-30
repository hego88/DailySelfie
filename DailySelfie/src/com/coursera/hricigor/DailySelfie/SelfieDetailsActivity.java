package com.coursera.hricigor.DailySelfie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.hricigor.dailyselfie.R;

public class SelfieDetailsActivity extends Activity {
	private static final String TAG = SelfieDetailsActivity.class.getSimpleName();
	
	
	private ImageView mImageView;
	private Bitmap mImageBitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		
		// Set Content View
		setContentView(R.layout.selfie_details_activity);
		
		// Get Bitmap from Intent
		mImageBitmap = getIntent().getParcelableExtra(SelfieListActivity.SELFIE_BITMAP_IMAGE);
		
		// Get ImageView for Image
		mImageView = (ImageView) findViewById(R.id.details_image_view);
		
		if(mImageView != null && mImageBitmap != null) {
			mImageView.setImageBitmap(mImageBitmap);
		} else {
			Toast.makeText(getApplicationContext(), "Unable to show selfie", Toast.LENGTH_SHORT).show();
			finish();
		}
		
	}
}
