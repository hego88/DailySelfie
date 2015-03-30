package com.coursera.hricigor.DailySelfie;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.coursera.hricigor.DailySelfie.SwipeDismissListViewTouchListener.DismissCallbacks;
import com.hricigor.dailyselfie.R;

public class SelfieListActivity extends ListActivity implements
		OnItemClickListener, DismissCallbacks {
	private static final String TAG = SelfieListActivity.class.getSimpleName();

	// Alarm
	private AlarmManager mAlarmManager;
	private Intent mNotificationReceiverIntent;
	private PendingIntent mNotificationReceiverPendingIntent;

	// For taking picture
	public static final int REQUEST_IMAGE_CAPTURE = 2101988;
	public static final String SELFIE_BITMAP_IMAGE = "bitmap_image";

	private static String mCurrentPhotoPath;
	private static String mImageFileName;

	// Geting storage for pictures
	private static File mStorageDir;

	// Opens detail Activity
	private static Intent mDetailIntent;

	// Adapter and list view for selfies;
	private SelfieViewAdapter mListAdapter;
	private ListView mListView;

	// Listener for swiping and deleting selfie
	private SwipeDismissListViewTouchListener mTouchListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		mListAdapter = new SelfieViewAdapter(getApplicationContext());
		mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		mStorageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		mListView = getListView();

		mListAdapter.addAll();

		mListView.setAdapter(mListAdapter);

		setupAlarm();
	}

	@Override
	protected void onStart() {

		// Swipe to delete selfie
		mTouchListener = new SwipeDismissListViewTouchListener(mListView, this);
		mListView.setOnTouchListener(mTouchListener);

		// Setting this scroll listener is required to ensure that during
		// ListView scrolling,
		// we don't look for swipes.
		mListView.setOnScrollListener(mTouchListener.makeScrollListener());

		// Click to open new activity
		mListView.setOnItemClickListener(this);
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.selfie_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Add picture action button
		if (item.getItemId() == R.id.action_new_selfie) {
			Log.i(TAG, "onOptionsItemSelected: New selfie");
			takePicture();
		}

		// Delete all pictures action button
		if (item.getItemId() == R.id.action_delete_all) {
			mListAdapter.deleteAll();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Log.i(TAG, "onActivityResult: Image retrived");
			mListAdapter.add(new Selfie(mImageFileName, mCurrentPhotoPath));
		}

	}

	@Override
	public boolean canDismiss(int position) {
		return true;

	}

	@Override
	public void onDismiss(ListView listView, int[] reverseSortedPositions) {
		for (int position : reverseSortedPositions) {
			mListAdapter.deleteSelfie(position);
		}
		mListAdapter.notifyDataSetChanged();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Selfie temSelfie = (Selfie) mListAdapter.getItem(position);

		mDetailIntent = new Intent(SelfieListActivity.this,
				SelfieDetailsActivity.class);
		mDetailIntent.putExtra(SELFIE_BITMAP_IMAGE,
				temSelfie.getSelfiePicture());
		startActivity(mDetailIntent);
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		mImageFileName = "SELFIE_" + getDate();
		mStorageDir.mkdirs();

		File selfieFile = File.createTempFile(mImageFileName, /* prefix */".jpg",/* suffix */
				mStorageDir/* directory */);

		mCurrentPhotoPath = selfieFile.getAbsolutePath();
		return selfieFile;
	}

	private void takePicture() {
		Log.i(TAG, "takePicture");

		Intent takeSelfieIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takeSelfieIntent.resolveActivity(getPackageManager()) != null) {

			// Create the File where the photo should go
			File selfieFile = null;

			try {
				selfieFile = createImageFile();
			} catch (IOException ex) {
				Log.e(TAG, "Can't make selfieFile", ex);
			}

			// Continue only if the File was successfully created
			if (selfieFile != null) {
				takeSelfieIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(selfieFile));
				startActivityForResult(takeSelfieIntent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}

	private void setupAlarm() {
		Log.i(TAG, "setupAlarm");

		mNotificationReceiverIntent = new Intent(getApplicationContext(),
				SelfieBroadcastReciver.class);
		mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, mNotificationReceiverIntent, 0);

		mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
				AlarmManager.INTERVAL_DAY, mNotificationReceiverPendingIntent);
	}

	private String getDate() {
		Log.i(TAG, "getDate");

		return new SimpleDateFormat("HH:mm:ss_dd.MM.yyyy").format(new Date());
	}

}
