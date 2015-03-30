package com.coursera.hricigor.DailySelfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hricigor.dailyselfie.R;

public class SelfieBroadcastReciver extends BroadcastReceiver {
	private static final String TAG = BroadcastReceiver.class.getSimpleName();
	private static final int MY_NOTIFICATION_ID = 1;

	// Notification Text Elements
	private static String tickerText;
	private static String contentTitle;
	private static String contentText;

	// Notification Action Elements
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "onRecive");

		tickerText = context.getString(R.string.notification_tickerText);
		contentTitle = context.getString(R.string.notification_contentTitle);
		contentText = context.getString(R.string.notification_contentText);

		mNotificationIntent = new Intent(context, SelfieListActivity.class);
		mContentIntent = PendingIntent.getActivity(context, 0,
				mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		Notification.Builder notificationBuilder = new Notification.Builder(
				context);
		notificationBuilder.setTicker(tickerText);
		notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
		notificationBuilder.setAutoCancel(true);
		notificationBuilder.setContentTitle(contentTitle);
		notificationBuilder.setContentText(contentText);
		notificationBuilder.setContentIntent(mContentIntent);

		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(MY_NOTIFICATION_ID,
				notificationBuilder.build());

	}

}
