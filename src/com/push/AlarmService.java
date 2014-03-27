package com.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.Timetodo.Home;
import com.Timetodo.R;

public class AlarmService extends WakeIntentService {
	public AlarmService() {
		super("AlarmService");
	}

	@Override
	void doReminderWork(Intent intent) {

		String name = intent.getStringExtra("name");
		String days = intent.getStringExtra("days");

		Log.e("", "name==" + name + days);

		// Notification noti = new Notification.Builder(this)
		// .setContentTitle("New mail from " + "test@gmail.com")
		// .setContentText("Subject")
		// .setSmallIcon(R.drawable.icon)
		// .setContentIntent(pIntent)
		// .addAction(R.drawable.icon, "Call", pIntent)
		// .addAction(R.drawable.icon, "More", pIntent)
		// .addAction(R.drawable.icon, "And more", pIntent).build();
		//

		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent notificationIntent = new Intent(this, Home.class);

		// Intent notificationIntent = new Intent(this, );
		PendingIntent pi = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification note = new Notification(R.drawable.ic_launcher, "Alarm",
				System.currentTimeMillis());

		note.setLatestEventInfo(this, "TimeToDo reminder",
				"Today task time will be end in 5 minutes", pi);

		note.defaults |= Notification.DEFAULT_ALL;
		note.flags |= Notification.FLAG_AUTO_CANCEL;
		int id = 123456789;
		manager.notify(id, note);

	}
}