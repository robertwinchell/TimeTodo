package com.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

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