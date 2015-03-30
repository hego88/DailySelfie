package com.coursera.hricigor.DailySelfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

public class Selfie {
	
	// Only this fields, all the rest we get from them
	private String selfieName;
	private String selfiePath;

	// Constructor
	public Selfie(String selfieName, String selfiePath) {
		this.selfieName = selfieName;
		this.selfiePath = selfiePath;
	}

	// Getters for fields and to get picture and thumbnail from file path
	public String getSelfieName() {
		return "Selfie taken at " + selfieName.substring(16, 26);
	}
	
	public String getSelfiePath() {
		return selfiePath;
	}
	
	public Bitmap getSelfiePicture() {
		return ThumbnailUtils.extractThumbnail(
				BitmapFactory.decodeFile(getSelfiePath(), null), 300, 300);
	}
	
	public Bitmap getSelfieThumb() {
		return ThumbnailUtils.extractThumbnail(
				BitmapFactory.decodeFile(getSelfiePath(), null), 200, 200);
	}
	
	public String getSelfieTime() {
		return selfieName.substring(7, 15);
	}
	
	

}
