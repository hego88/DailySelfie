package com.coursera.hricigor.DailySelfie;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hricigor.dailyselfie.R;

public class SelfieViewAdapter extends BaseAdapter {
	private static final String TAG = SelfieViewAdapter.class.getSimpleName();

	
	private ArrayList<Selfie> mSelfieList = new ArrayList<Selfie>();
	private File mStorageDir = Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	
	private LayoutInflater mInflater = null;
	private Context mContext;
	

	public SelfieViewAdapter(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mSelfieList.size();
	}

	@Override
	public Object getItem(int position) {
		return mSelfieList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i(TAG, "getView");
		
		View newView = convertView;
		ViewHolder holder;
		
		
		Selfie tempSelfie = mSelfieList.get(position);
		
		if (null == convertView) {
			holder = new ViewHolder();
			newView = mInflater.inflate(R.layout.selfie_item, null);
			holder.selfieImage = (ImageView) newView.findViewById(R.id.selfie_picture_image_view);
			
			holder.selfieTime = (TextView) newView.findViewById(R.id.selfie_time);
			holder.selfieText = (TextView) newView.findViewById(R.id.selfie_name_text_view);
			newView.setTag(holder);

		} else {
			holder = (ViewHolder) newView.getTag();
		}
		holder.selfieImage.setImageBitmap(tempSelfie.getSelfieThumb());
		holder.selfieTime.setText(tempSelfie.getSelfieTime());
		holder.selfieText.setText(tempSelfie.getSelfieName());

		return newView;
	}

	public void addAll() {
		if (mContext.getExternalFilesDir(null) != null) {
			for (File file : mStorageDir.listFiles()) {
				if (file.getName().startsWith("SELFIE_")) {
					add(new Selfie(file.getName(), file.getAbsolutePath()));
				}
			}
		}
	}

	// Adding selfie one by one and to list and notifing about data change
	public void add(Selfie listItem) {
		mSelfieList.add(listItem);
		notifyDataSetChanged();
	}

	// Deleting all images and clearing ListView for any data
	public void deleteAll() {

		for (Selfie selfie : mSelfieList) {
			new File(selfie.getSelfiePath()).delete();
		}
		mSelfieList.clear();
		notifyDataSetChanged();
	}

	public void deleteSelfie(int position) {
		new File(mSelfieList.get(position).getSelfiePath()).delete();
		mSelfieList.remove(position);
	}
	
	// View holder for this View
	static class ViewHolder {
		ImageView selfieImage;
		TextView selfieTime;
		TextView selfieText;
	}

}
