package weiyuan.androidweartest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;


public class MainActivity extends Activity {
	
	
	static final String EXTRA_EVENT_ID = "event";
	final int mId = 1;
	static final String location = "Mackwell Electronics";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void createNotification(View view)
	{
		System.out.println("notification");
		
		
		
		//create PendingIntent from an intent
		Intent resultIntent = new Intent(this, NotificationActivity.class); // default touch intent
		Intent mapIntent = new Intent(Intent.ACTION_VIEW);
		
		Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
		mapIntent.setData(geoUri);
		PendingIntent mapPendingIntent = PendingIntent.getActivity(this, 0, mapIntent, 0);
		
		
		
		resultIntent.putExtra(EXTRA_EVENT_ID, mId);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0 , resultIntent, 0);

		
		
		
		//create mBuild
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		
		
		//set properties for mBuilder
			.setSmallIcon(R.drawable.ic_launcher)
			.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
			.setContentText("First Notification")
			.setContentTitle("Hello")
			.setContentIntent(resultPendingIntent)  //PendingIntent for action touch
			.addAction(R.drawable.ic_launcher, "Map", mapPendingIntent)
			;
			
		
		
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
						
		stackBuilder.addParentStack(NotificationActivity.class);
		
		stackBuilder.addNextIntent(resultIntent);
		
		
		
		
		//two ways of create notification manager
		//this is a recommended for android wear device, which will not loose any new feature
		NotificationManagerCompat notificationManager =
		        NotificationManagerCompat.from(this);
		
		
		// Build the notification and issues it with notification manager.
		notificationManager.notify(mId, mBuilder.build());
		
		
		//this is for normal device notification
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		
		
		
		
		
		// mId allows you to update the notification later on.
		mNotificationManager.notify(2, mBuilder.build());
		
		
	}
	
	
	/**
	 * Add a notification with some new features to Android Wear
	 * Big Style and Pages
	 * @param view Button pressed
	 */
	public void newFeatureNotification(View view)
	{
		Intent openIntent = new Intent(this,NotificationActivity.class);
		PendingIntent openPendingIntent = PendingIntent.getActivity(this, 0, openIntent, 0);
		
		// big Style page
		BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
		bigStyle.bigText(location);
		
		
		//style for second page
		BigTextStyle secondPageStyle = new NotificationCompat.BigTextStyle()
			.setBigContentTitle("Page 2")
			.setSummaryText("Summary")
			.bigText(location);
		
		
		//notification for second page
		Notification secondPageNotification = new NotificationCompat.Builder(this)
			.setStyle(secondPageStyle)
			.build();
		
		//first builder
		NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentText("Hello Android Wear")
			.setContentTitle("Hello")
			.setStyle(bigStyle)
			.setContentIntent(openPendingIntent);
		
		
		//final notification, build upon first builder and add sedond page by addPage(notification)
		//Notification twoPageNotification = new .Builder(mNotificationBuilder)
		//	.addPage(secondPageNotification)
		//	.build();
		
		
		
		//create notification manager
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
		
		
		//send notification
		//notificationManager.notify(0,twoPageNotification);
		
		
		
		
	}


	
}
