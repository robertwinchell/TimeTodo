package com.Timetodo;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
	private int hour1 = 0;
	private int minute1 = 0;

	SharedPreferences Todo_Prefs;

	boolean check;

	LinearLayout lnr_reset, lnr_task_list;
	Button btn_reset;

	ScrollView scr_title;
	private TimePicker timePicker1;
	TimePickerDialog tpd;

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
		btn_reset = (Button) findViewById(R.id.btn_reset);
		btn_reset.setOnClickListener(this);

		linf = (LayoutInflater) getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		linf = LayoutInflater.from(Home.this);

		scr_title = (ScrollView) findViewById(R.id.scr_title);

		lnr_reset = (LinearLayout) findViewById(R.id.lnr_reset);

		lnr_task_list = (LinearLayout) findViewById(R.id.lnr_task_list);

		// showDialog(TIME_DIALOG_ID);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Todo_Prefs = PreferenceManager.getDefaultSharedPreferences(this);

		lnr_task_list.removeAllViews();
		Share.quick_task_home.clear();
		Share.quick_task_home_type.clear();

		if (!Todo_Prefs.getString("cuttent_time", "").equals("")) {

			startTime = Long
					.parseLong(Todo_Prefs.getString("cuttent_time", ""));

			timeSwapBuff = Todo_Prefs.getLong("time", 0);
			customHandler.postDelayed(updateTimerThread, 0);

			lnr_reset.setVisibility(View.VISIBLE);

			check = Todo_Prefs.getBoolean("check", false);

		} else {
			startTime = 0;
			timeSwapBuff = 0;

			lnr_reset.setVisibility(View.GONE);
		}
		if (!Todo_Prefs.getString("name", "").equalsIgnoreCase("")) {

			Share.quick_task_home.add(Todo_Prefs.getString("name", ""));
			Share.quick_task_home_type.add(Todo_Prefs.getString("type", ""));

			if (Share.quick_task_home.size() > 0) {

				for (int i = 0; i < Share.quick_task_home.size(); i++) {

					row(Share.quick_task_home.get(i),
							Share.quick_task_home_type.get(i));

				}
			}

		}

	}

	private void row(final String name, final String type) {
		// TODO Auto-generated method stub

		final View v = linf.inflate(R.layout.row_task, null);

		try {

			LinearLayout lnr_task_select = (LinearLayout) v
					.findViewById(R.id.lnr_task_select);
			lnr_task_select.setVisibility(View.GONE);

			TextView txt_task = (TextView) v.findViewById(R.id.txt_task);

			txt_task.setText(name);
			LinearLayout lnr_task_delete = (LinearLayout) v
					.findViewById(R.id.lnr_task_delete);

			LinearLayout lnr_task_row = (LinearLayout) v
					.findViewById(R.id.lnr_task_row);

			LinearLayout lnr_task_edit = (LinearLayout) v
					.findViewById(R.id.lnr_task_edit);
			lnr_task_edit.setVisibility(View.GONE);
			lnr_task_row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					scr_title.requestDisallowInterceptTouchEvent(true);

					Intent intent = new Intent(Home.this, Task_map.class);

					Bundle bundle = new Bundle();
					bundle.putString("id", "1");
					bundle.putString("name", name);
					bundle.putString("des", "");
					bundle.putString("type", type);
					intent.putExtras(bundle);
					startActivityForResult(intent, 9);

				}
			});

			lnr_task_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
							Home.this);

					alertDialog2.setTitle("Confirm Delete...");

					alertDialog2
							.setMessage("Are you sure you want delete this task?");

					alertDialog2.setIcon(R.drawable.btn_close);

					alertDialog2.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.dismiss();

									for (int i = 0; i < Share.quick_task_home
											.size(); i++) {

										if (name.equalsIgnoreCase(Share.quick_task_home
												.get(i))) {

											Share.quick_task_home.remove(i);

											SharedPreferences.Editor editor = Todo_Prefs
													.edit();
											editor.putString("name", "");
											editor.putString("type", "");

											editor.commit();

											Log.e("",
													"msg "
															+ Share.quick_task_home
																	.size());
											onResume();

										}

									}

								}
							});

					alertDialog2.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.cancel();
								}
							});

					alertDialog2.show();

				}
			});

		} catch (Exception e) {
			// TODO: handle exception
		}
		lnr_task_list.addView(v);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == btn_todo) {

			Intent i = new Intent(Home.this, Add_task_list.class);
			startActivity(i);

		} else if (v == btn_calender) {

			Intent intent = new Intent(Home.this, Cale.class);
			startActivityForResult(intent, 9);

		} else if (v == btn_reset) {

			long startTime1 = 0;

			startTime = startTime1;
			SharedPreferences.Editor editor = Todo_Prefs.edit();
			editor.putString("cuttent_time", "");
			editor.putBoolean("check", false);

			editor.commit();

			timeSwapBuff = 0;

			txt_time.setText("00:00:00");

			lnr_reset.setVisibility(View.GONE);
			// startTime = startTime1;

			Intent i = new Intent(Home.this, Home.class);
			PendingIntent pi = PendingIntent.getBroadcast(
					getApplicationContext(), 12345, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis(), pi);

			onResume();

		} else if (v == btn_time) {

			long startTime1 = 0;
			startTime1 = SystemClock.uptimeMillis();

			check = true;

			SharedPreferences.Editor editor = Todo_Prefs.edit();
			editor.putString("cuttent_time", String.valueOf(startTime1));
			editor.putBoolean("check", false);

			editor.commit();

			startTime = startTime1;

			final Calendar c = Calendar.getInstance();
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);

			tpd = new TimePickerDialog(Home.this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {

							// set current time into timepicker
							// timePicker1.setCurrentHour(hourOfDay);
							// timePicker1.setCurrentMinute(minute);

							Log.e("", "msg of" + hourOfDay);
							hour1 = hourOfDay;
							minute1 = minute;
							
							long totaltime = 0;

							totaltime = (hour1) * 60;

							totaltime = (totaltime + minute1) * 60000;
							Log.e("", "msg  " + totaltime);
							String time = String.valueOf(hour1) + ":" + String.valueOf(minute1);

							Share.time = time;

							Calendar calendar = Calendar.getInstance();

							int hours = calendar.get(Calendar.HOUR_OF_DAY);
							int min = calendar.get(Calendar.MINUTE);

							int currenttime = hours * 60;

							currenttime = (currenttime + min) * 60000;
							Log.e("", "msg  " + currenttime);
							timeSwapBuff = totaltime - currenttime;

							SharedPreferences.Editor editor1 = Todo_Prefs.edit();

							editor1.putLong("time", timeSwapBuff);
							editor1.commit();

							customHandler.postDelayed(updateTimerThread, 0);

							lnr_reset.setVisibility(View.VISIBLE);
							
						}
					}, hour, minute, false);
			tpd.show();

			

		}
	}

	private Runnable updateTimerThread = new Runnable() {

		public void run() {

			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

			// Log.e("", "msg timemili==" + timeInMilliseconds);

			// Log.e("", "msg time swapbuffer  ===" + timeSwapBuff);
			updatedTime = timeSwapBuff - timeInMilliseconds;

			// Log.e("", "msg update" + updatedTime);

			if (updatedTime < (6 * 60000)) {

				// Log.e("", "msg push notification");

				if (updatedTime > (5 * 60000)) {

					if (check) {

						// Log.e("", "msg update" + updatedTime);

						int diff = (int) (updatedTime - (5 * 60000));

						// diff = 60000 + diff;

						Log.e("", "msg update" + diff);
						Intent i = new Intent(Home.this, OnAlarmReceiver.class);
						PendingIntent pi = PendingIntent.getBroadcast(
								getApplicationContext(), 12345, i,
								PendingIntent.FLAG_UPDATE_CURRENT);
						AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						alarmManager.set(AlarmManager.RTC_WAKEUP,
								System.currentTimeMillis() + diff, pi);

						SharedPreferences.Editor editor = Todo_Prefs.edit();

						editor.putBoolean("check", false);

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
				lnr_reset.setVisibility(View.GONE);
				customHandler.removeCallbacks(updateTimerThread);

			} else {

				txt_time.setText(String.format("%02d", hour) + ":"
						+ String.format("%02d", mins) + ":"
						+ String.format("%02d", secs));

				customHandler.postDelayed(this, 0);
			}

		}

	};

	// btnChangeTime.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	//
	//
	//
	// }
	//
	// });

	// private TimePickerDialog.OnTimeSetListener timePickerListener = new
	// TimePickerDialog.OnTimeSetListener() {
	// public void onTimeSet(TimePicker view, int selectedHour,
	// int selectedMinute) {
	//
	// hour = selectedHour;
	// minute = selectedMinute;
	//
	// long totaltime = 0;
	//
	// totaltime = (hour) * 60;
	//
	// totaltime = (totaltime + minute) * 60000;
	//
	// String time = String.valueOf(hour) + ":" + String.valueOf(minute);
	//
	// Share.time = time;
	//
	// Calendar calendar = Calendar.getInstance();
	//
	// int hours = calendar.get(Calendar.HOUR_OF_DAY);
	// int min = calendar.get(Calendar.MINUTE);
	//
	// int currenttime = hours * 60;
	//
	// currenttime = (currenttime + min) * 60000;
	//
	// timeSwapBuff = totaltime - currenttime;
	//
	// SharedPreferences.Editor editor = Todo_Prefs.edit();
	//
	// editor.putLong("time", timeSwapBuff);
	// editor.commit();
	//
	// customHandler.postDelayed(updateTimerThread, 0);
	//
	// lnr_reset.setVisibility(View.VISIBLE);
	//
	// }
	//
	// };

}
