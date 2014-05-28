package com.Timetodo;

import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.push.OnAlarmReceiver;
import com.share.Share;
import com.webservices.Webservice;

public class Home extends Activity implements OnClickListener {

	Button btn_todo, btn_calender, btn_time;
	TextView txt_time;
	LayoutInflater linf;
	String time;

	String response = "";
	JSONArray jArray;
	JSONObject jObject, JObject1;

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

	boolean register;

	LinearLayout lnr_reset, lnr_task_list;
	Button btn_reset, btn_frds;

	ScrollView scr_title;

	String username, useremail;
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

		btn_frds = (Button) findViewById(R.id.btn_frds);
		btn_frds.setOnClickListener(this);

		linf = (LayoutInflater) getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		linf = LayoutInflater.from(Home.this);

		scr_title = (ScrollView) findViewById(R.id.scr_title);

		lnr_reset = (LinearLayout) findViewById(R.id.lnr_reset);

		lnr_task_list = (LinearLayout) findViewById(R.id.lnr_task_list);

		Account[] accounts = AccountManager.get(this).getAccountsByType(
				"com.google");
		useremail = accounts[0].name.toString();
		Log.e("My email id that i want", useremail);
		for (Account account : accounts) {
			String possibleEmail = account.toString();
			Log.e("Possible email id of user", possibleEmail);

		}

		// showDialog(TIME_DIALOG_ID);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();

		Todo_Prefs = PreferenceManager.getDefaultSharedPreferences(this);

		if (Todo_Prefs.getBoolean("register", true)) {

			View layout = linf.inflate(R.layout.adduser, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);

			final AlertDialog dialog = builder.create();
			dialog.setView(layout, 0, 0, 0, 0);
			dialog.show();
			Button ok = (Button) layout.findViewById(R.id.btn_ok);

			Button cancel = (Button) layout.findViewById(R.id.btn_cancel);

			final EditText edt_fname = (EditText) layout
					.findViewById(R.id.edt_fname);

			Log.e("", "user name======" + edt_fname.getText().toString());
			ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (edt_fname.getText().toString().equalsIgnoreCase("")) {

					} else {
						username = edt_fname.getText().toString();
						new get_register().execute("");
						dialog.dismiss();
					}

				}
			});

			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					dialog.cancel();

				}
			});
		}

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

			Share.meters = 0;

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

		} else if (v == btn_frds) {

			Intent intent = new Intent(Home.this, Friends_list.class);
			startActivityForResult(intent, 9);

		} else if (v == btn_reset) {

			long startTime1 = 0;

			startTime = startTime1;

			SharedPreferences.Editor editor = Todo_Prefs.edit();
			editor.putString("cuttent_time", "");
			editor.putBoolean("check", false);

			editor.commit();

			timeSwapBuff = 0;

			Share.meters = 0;

			txt_time.setText("00:00:00");

			lnr_reset.setVisibility(View.GONE);
			// startTime = startTime1;

			Intent i = new Intent(Home.this, Home.class);

			onResume();
			PendingIntent pi = PendingIntent.getBroadcast(
					getApplicationContext(), 12345, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis(), pi);

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

							// Log.e("", "msg of" + hourOfDay);
							hour1 = hourOfDay;
							minute1 = minute;

							long totaltime = 0;

							totaltime = (hour1) * 60;
							long minnn = totaltime + minute1;

							Log.e("", "time in  min" + minnn);

							totaltime = (totaltime + minute1) * 60000;
							Log.e("", "msg  " + totaltime);
							String time = String.valueOf(hour1) + ":"
									+ String.valueOf(minute1);

							Share.time = time;

							Calendar calendar = Calendar.getInstance();

							int hours = calendar.get(Calendar.HOUR_OF_DAY);
							int min = calendar.get(Calendar.MINUTE);

							int currenttime = hours * 60;

							currenttime = (currenttime + min) * 60000;
							Log.e("", "msg  " + min);
							timeSwapBuff = totaltime - currenttime;

							Log.e("", "msg time" + timeSwapBuff);

							if (timeSwapBuff > 0) {

								Share.meters = (int) (timeSwapBuff / 60000);

							} else {

								Share.meters = 0;

							}

							SharedPreferences.Editor editor1 = Todo_Prefs
									.edit();

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

						// Log.e("", "msg update" + diff);
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

			if (updatedTime < 0) {
				Share.meters = 0;
			} else {
				Share.meters = (int) (updatedTime / 60000);
			}

			int mins = secs / 60;
			int hour = mins / 60;
			secs = secs % 60;
			mins = mins % 60;

			// Share.meters = (mins + 1) * 108;

			// Log.e("", "meters" + Share.meters);

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

	private class get_register extends AsyncTask<String, Void, Boolean> {
		ProgressDialog pd;
		List<Object[]> l;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = ProgressDialog.show(Home.this, "", "Loading....", true, false);
			pd.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {

			Boolean success = false;

			response = null;
			String url = "http://api.vasundharavision.com/timetodo/?op=register&user_name="
					+ username
					+ "&user_email="
					+ useremail
					+ "&user_latitude="
					+ Share.cur_lat + "&user_longitude=" + Share.cur_lng;

			try {

				response = Webservice.webServiceCall(url);

				System.out.println("response" + response);
			} catch (Exception e) {
				System.out.println("Error : " + e.toString());
			}

			return success;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			super.onPostExecute(success);

			try {

				JObject1 = new JSONObject(response);
				String message = JObject1.getString("message");
				String success1 = JObject1.getString("success");

				Log.e("", "msg register status" + message);

				if (success1.equalsIgnoreCase("1")) {

					SharedPreferences.Editor editor = Todo_Prefs.edit();
					editor.putBoolean("register", false);
					editor.commit();
				}

				else if (message
						.equalsIgnoreCase("Email Address is already registered !")) {

					SharedPreferences.Editor editor = Todo_Prefs.edit();
					editor.putBoolean("register", false);
					editor.commit();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e("Error", e.toString());
			}
			pd.dismiss();
			onResume();
		}
	}

}
