package com.timetodo;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.push.OnAlarmReceiver;
import com.share.Share;

public class Home extends Activity implements OnClickListener {

	Button btn_todo, btn_calender, btn_time;
	TextView txt_time;
	LayoutInflater linf;
	String time;

	static final int TIME_DIALOG_ID = 1;
	private long startTime = 0L;
	private Handler customHandler = new Handler();
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	double time1;

	private int hour = 0;
	private int minute = 0;

	SharedPreferences Todo_Prefs;

	boolean check;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		btn_todo = (Button) findViewById(R.id.btn_todo);
		btn_todo.setOnClickListener(this);

		btn_calender = (Button) findViewById(R.id.btn_calender);
		btn_calender.setOnClickListener(this);
		btn_time = (Button) findViewById(R.id.btn_time);
		btn_time.setOnClickListener(this);
		txt_time = (TextView) findViewById(R.id.txt_time);

		linf = (LayoutInflater) getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		linf = LayoutInflater.from(Home.this);

		Todo_Prefs = PreferenceManager.getDefaultSharedPreferences(this);

		// startTime = Todo_Prefs.getString("cuttent_time", 0);

		if (!Todo_Prefs.getString("cuttent_time", "").equals("")) {

			startTime = Long
					.parseLong(Todo_Prefs.getString("cuttent_time", ""));
			// timeSwapBuff = (long) (10 * 60000);
			timeSwapBuff = Todo_Prefs.getLong("time", 0);
			customHandler.postDelayed(updateTimerThread, 0);

		} else {
			startTime = 0;
			timeSwapBuff = 0;
		}

		Log.e("", "st==" + startTime);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == btn_todo) {

			Intent i = new Intent(this, Add_task.class);

			startActivityForResult(i, 8);

		} else if (v == btn_calender) {

			Intent intent = new Intent(Home.this, Cale.class);
			startActivityForResult(intent, 9);

		} else if (v == btn_time) {

			long startTime1 = 0;
			startTime1 = SystemClock.uptimeMillis();

			check = true;
			SharedPreferences.Editor editor = Todo_Prefs.edit();
			editor.putString("cuttent_time", String.valueOf(startTime1));

			editor.commit();

			startTime = startTime1;
			// final View v1 = linf.inflate(R.layout.adduser, null);
			// AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);

			// final AlertDialog dialog = builder.create();
			// dialog.setView(v1, 0, 0, 0, 0);
			// dialog.show();
			//
			// Button ok = (Button) v1.findViewById(R.id.btn_ok);
			// ok.setText("Ok");
			//
			// final TextView txt_name = (TextView)
			// v1.findViewById(R.id.txt_name);
			// txt_name.setText("How much time do you have?");
			//
			// Button cancel = (Button) v1.findViewById(R.id.btn_cancel);
			//
			// final EditText edt_fname = (EditText) v1
			// .findViewById(R.id.edt_fname);
			//
			// ok.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			//
			// // Calendar calendar = Calendar.getInstance();
			// // long startTime1 = calendar.getTimeInMillis();
			//
			// long startTime1 = 0;
			// startTime1 = SystemClock.uptimeMillis();
			//
			// Log.e("", "starttime=" + startTime1);
			// Log.e("", "st==" + startTime1);
			// time = edt_fname.getText().toString();
			//
			// Share.time = time;
			// time1 = Integer.parseInt(Share.time);
			// Log.e("", "msg time in milisecond" + time1);
			//
			// timeSwapBuff = (long) (time1 * 60000);
			// SharedPreferences.Editor editor = Todo_Prefs.edit();
			// editor.putString("cuttent_time", String.valueOf(startTime1));
			// editor.putLong("time", timeSwapBuff);
			// editor.commit();
			//
			// startTime = startTime1;
			//
			// // time1 = time1 + startTime;
			//
			// Log.e("", "msg time in milisecond" + timeSwapBuff);
			// // txt_time.setText(String.valueOf((time1)));
			// customHandler.postDelayed(updateTimerThread, 0);
			// // txt_time.setText(String.valueOf(time1));
			// dialog.dismiss();
			//
			// }
			// });
			//
			// cancel.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			//
			// dialog.cancel();
			//
			// }
			// });

			showDialog(TIME_DIALOG_ID);

		}
	}

	private Runnable updateTimerThread = new Runnable() {

		public void run() {

			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

			updatedTime = timeSwapBuff - timeInMilliseconds;

			if (updatedTime < (6 * 60000)) {

				// Log.e("", "msg push notification");

				if (updatedTime > (5 * 60000)) {

					if (check) {

						updatedTime = (6 * 60000) - updatedTime;

						Log.e("", "msg UPDATE" + updatedTime);

						int diff = 60000 - (int) updatedTime;
						Log.e("", "msg push notification");

						Calendar calendar = Calendar.getInstance();

						calendar.add(Calendar.MILLISECOND, diff);

						Intent i = new Intent(getApplicationContext(),
								OnAlarmReceiver.class);
						i.putExtra("name", "name");
						i.putExtra("days", "dayws");
						PendingIntent pi = PendingIntent.getBroadcast(
								getApplicationContext(), 12345, i,
								PendingIntent.FLAG_UPDATE_CURRENT);

						AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						alarmManager.set(AlarmManager.RTC_WAKEUP,
								System.currentTimeMillis() + diff, pi);

						Log.e("", "diffff --------" + diff);
						Log.e("",
								"diffff --------" + calendar.getTimeInMillis());

						check = false;

					}
				}
			}

			int secs = (int) (updatedTime / 1000);

			int mins = secs / 60;
			int hour = mins / 60;
			secs = secs % 60;
			mins = mins % 60;

			// int milliseconds = (int) (updatedTime % 1000);

			txt_time.setText(String.valueOf((time1)));

			txt_time.setText(String.format("%02d", hour) + ":"
					+ String.format("%02d", mins) + ":"
					+ String.format("%02d", secs));

			if (updatedTime < 0) {

				txt_time.setText("00:00:00");
				customHandler.removeCallbacks(updateTimerThread);

			} else {

				txt_time.setText(String.format("%02d", hour) + ":"
						+ String.format("%02d", mins) + ":"
						+ String.format("%02d", secs));

				customHandler.postDelayed(this, 0);
			}

		}

	};

	@Override
	protected Dialog onCreateDialog(int id) {
		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		switch (id) {

		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);

		default:
			return null;
		}
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {

			hour = selectedHour;
			minute = selectedMinute;

			long totaltime = 0;

			totaltime = (hour) * 60;

			totaltime = (totaltime + minute) * 60000;
			// Log.e("", "msg totaltime in milisecond==========" + totaltime);

			String time = String.valueOf(hour) + ":" + String.valueOf(minute);

			Share.time = time;

			Calendar calendar = Calendar.getInstance();

			int hours = calendar.get(Calendar.HOUR_OF_DAY);
			int min = calendar.get(Calendar.MINUTE);

			int currenttime = hours * 60;

			currenttime = (currenttime + min) * 60000;

			timeSwapBuff = totaltime - currenttime;

			SharedPreferences.Editor editor = Todo_Prefs.edit();

			editor.putLong("time", timeSwapBuff);
			editor.commit();

			// Log.e("", "timeSwapBuff===========" + timeSwapBuff);

			customHandler.postDelayed(updateTimerThread, 0);
		}

	};

}
