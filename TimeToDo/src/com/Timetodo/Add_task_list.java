package com.Timetodo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.database.DBAdapter;
import com.share.Share;

public class Add_task_list extends Activity implements OnClickListener {

	Button btn_addtask;
	LayoutInflater linf;
	LinearLayout rr;
	TextView btn_quicktask;
	final DBAdapter dba = new DBAdapter(this);

	boolean isChecked = true;
	String formattedDate = "";

	SharedPreferences Todo_Prefs;
	ArrayAdapter<String> VesselListAdapter, VesselListAdaptertask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_task);

		Todo_Prefs = PreferenceManager.getDefaultSharedPreferences(this);

		ImageView img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();

			}
		});
		btn_addtask = (Button) findViewById(R.id.btn_addtask);
		btn_addtask.setOnClickListener(this);
		btn_quicktask = (TextView) findViewById(R.id.btn_quicktask);
		btn_quicktask.setOnClickListener(this);

		linf = (LayoutInflater) getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		linf = LayoutInflater.from(Add_task_list.this);
		rr = (LinearLayout) findViewById(R.id.lnr_task_list);

		Share.quick_task.clear();

		Share.quick_task.add("I am Hungry");
		Share.quick_task.add("I Want go to Shop");
		Share.quick_task.add("I Want go to the Hospital ");
		Share.quick_task.add("I want to go to the ATM");
		Share.quick_task.add("I Want go to the Bank ");
		Share.quick_task.add("I Want to the Restaurant ");
		Share.quick_task.add("I Want go to the Mall ");
		Share.quick_task.add("I Want go to the Hotel ");
		Share.quick_task.add("I Want go to the Hostel ");
		Share.quick_task.add("I Want go to the Garden ");
		Share.quick_task.add("I Want go to the Beach ");

		Share.task_reminder.clear();

		VesselListAdaptertask = new ArrayAdapter<String>(Add_task_list.this,
				android.R.layout.select_dialog_singlechoice, Share.quick_task);

		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		formattedDate = df.format(c.getTime());
//		Log.e("day", "" + formattedDate);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		get_task();
		add_task_list();

	}

	public void get_task() {

		dba.open();

		Cursor crsrGetMatches = dba.gettask_list(formattedDate);

//		Log.e("", "length" + crsrGetMatches.getCount());

		Share.task_id.clear();
		Share.task_name.clear();
		Share.task_des.clear();
		Share.task_date.clear();
		Share.task_type.clear();
		Share.task_reminder.clear();
		Share.task_time.clear();

		if (crsrGetMatches.getCount() > 0) {

			// Log.e("", "COUNT:::" + crsrGetMatches.getCount());
			try {
				crsrGetMatches.moveToFirst();
				do {

					Share.task_id.add(crsrGetMatches.getString(0));
					Share.task_name.add(crsrGetMatches.getString(1));
					Share.task_des.add(crsrGetMatches.getString(2));
					Share.task_date.add(crsrGetMatches.getString(3));
					Share.task_time.add(crsrGetMatches.getString(4));
					Share.task_type.add(crsrGetMatches.getString(5));

//					Log.e("", "msg date======" + crsrGetMatches.getString(3));

				} while (crsrGetMatches.moveToNext());

			} catch (Exception e) {

			}
		}

		else {

		}

		crsrGetMatches.close();
		dba.close();

	}

	public void add_task_list() {

		rr.removeAllViews();

		for (int i = 0; i < Share.task_id.size(); i++) {

			String id = Share.task_id.get(i);
			String name = Share.task_name.get(i);
			String des = Share.task_des.get(i);
			String date = Share.task_date.get(i);
			String time = Share.task_time.get(i);
			String type = Share.task_type.get(i);

			row(id, name, des, date, time, type);
		}
	}

	private void row(final String id, final String name, final String des,
			final String date, final String time, final String type) {
		// TODO Auto-generated method stub

		final View v = linf.inflate(R.layout.row_task, null);

		LinearLayout lnr_task_row = (LinearLayout) v
				.findViewById(R.id.lnr_task_row);

		final CheckBox chk_select = (CheckBox) v.findViewById(R.id.chk_select);
		chk_select.setChecked(isChecked);

		TextView txt_task = (TextView) v.findViewById(R.id.txt_task);
		ImageView img_task_delete = (ImageView) v
				.findViewById(R.id.img_task_delete);
		LinearLayout lnr_task_delete = (LinearLayout) v
				.findViewById(R.id.lnr_task_delete);
		LinearLayout lnr_task_edit = (LinearLayout) v
				.findViewById(R.id.lnr_task_edit);

		try {
			txt_task.setText(name);

//			chk_select
//					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//						@Override
//						public void onCheckedChanged(CompoundButton buttonView,
//								boolean isChecked) {
//							// TODO Auto-generated method stub
//
//							
//
//						}
//					});
			
			chk_select.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Share.quick_task_home.clear();
					Share.quick_task_home_type.clear();
					Share.quick_task_home.add(name);
					Share.quick_task_home_type.add(type);

					SharedPreferences.Editor editor = Todo_Prefs.edit();
					editor.putString("name", name);
					editor.putString("type", type);
					editor.putString("id", id);

					Log.e("", "id saved" + id);

					editor.commit();
					get_task();
					add_task_list();
				}
			});

			Log.e("", "out id" + id);
			if (Todo_Prefs.getString("id", "").equals(id)) {
				
				Log.e("", "in=="+id);
				chk_select.setChecked(true);
			}

			else {
				chk_select.setChecked(false);
			}

			lnr_task_row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					// scr_title.requestDisallowInterceptTouchEvent(true);

					Intent intent = new Intent(Add_task_list.this,
							Task_map.class);

					Bundle bundle = new Bundle();
					bundle.putString("id", id);
					bundle.putString("name", name);
					bundle.putString("des", des);
					bundle.putString("type", type);
					intent.putExtras(bundle);
					startActivityForResult(intent, 9);

				}
			});

			lnr_task_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// scr_title.requestDisallowInterceptTouchEvent(true);
					AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
							Add_task_list.this);

					// Setting Dialog Title
					alertDialog2.setTitle("Confirm Delete...");

					// Setting Dialog Message
					alertDialog2
							.setMessage("Are you sure you want delete this file?");

					// Setting Icon to Dialog
					alertDialog2.setIcon(R.drawable.btn_close);

					// Setting Positive "Yes" Btn
					alertDialog2.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Write your code here to execute after
									// dialog
									dialog.dismiss();
									remove_task(id);
								}
							});
					// Setting Negative "NO" Btn
					alertDialog2.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Write your code here to execute after

									dialog.cancel();
								}
							});

					// Showing Alert Dialog
					alertDialog2.show();

				}
			});

			lnr_task_edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Share.task_edit = true;

					Intent edit = new Intent(Add_task_list.this, Task_add.class);

					Bundle bundle = new Bundle();
					bundle.putString("id", id);
					bundle.putString("name", name);
					bundle.putString("des", des);

					bundle.putString("date", date);
					bundle.putString("time", time);

					bundle.putString("type", type);
					edit.putExtras(bundle);

					startActivityForResult(edit, 9);

				}
			});

		} catch (Exception e) {
			e.getMessage();
		}
		rr.addView(v);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == btn_addtask) {

			Share.task_edit = false;
			Share.cal_task = false;

			Intent intent = new Intent(Add_task_list.this, Task_add.class);
			startActivityForResult(intent, 9);

		} else if (v == btn_quicktask) {

			AlertDialog.Builder builder = new AlertDialog.Builder(

			Add_task_list.this);

			Share.task_reminder.add("Restaurant");
			Share.task_reminder.add("Mall");
			Share.task_reminder.add("Hospital");
			Share.task_reminder.add("ATM");
			Share.task_reminder.add("Bank");
			Share.task_reminder.add("Restaurant");
			Share.task_reminder.add("Mall");
			Share.task_reminder.add("Hotel");
			Share.task_reminder.add("Hostel");
			Share.task_reminder.add("Garden");
			Share.task_reminder.add("Beach");

			Share.quick_task_home.clear();
			Share.quick_task_home_type.clear();
			//
			// // builder.setTitle("Vessel list");
			builder.setTitle("Choose your quick task");
			builder.setPositiveButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			builder.setAdapter(VesselListAdaptertask,
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog,
								final int item) {

							String money_type = Share.quick_task.get(item);

							// Share.quick_task_home_type.add(Share.task_reminder
							// .get(item));

//							Log.e("", "msg  Type===" + money_type);
//							Log.e("",
//									"msg  Type==="
//											+ Share.task_reminder.get(item));

							Share.quick_task_home_type.add(Share.task_reminder
									.get(item));

							// Log.e("", )
							// txt_task_name.setText(Share.quick_task.get(item));

							Share.quick_task_home.add(Share.quick_task
									.get(item));

							SharedPreferences.Editor editor = Todo_Prefs.edit();
							editor.putString("name", Share.quick_task.get(item));
							editor.putString("type",
									Share.task_reminder.get(item));

							editor.commit();
							dialog.cancel();

							Intent intent = new Intent(Add_task_list.this,
									Task_map.class);

							Bundle bundle = new Bundle();
							bundle.putString("id", "1");
							bundle.putString("name", Share.quick_task.get(item));
							bundle.putString("des", "");
							bundle.putString("type",
									Share.task_reminder.get(item));
							intent.putExtras(bundle);
							startActivityForResult(intent, 9);

							// // Share.moneytpye = money_type;
							//
						}
					});

			final AlertDialog dialog = builder.create();
			dialog.show();

		}
	}

	public void remove_task(final String id) {
		// rr.removeAllViews();

		dba.open();

		// Log.e("", "Database Open for delete ");
		Cursor crsrGetMatches = dba.delete_task(id);

		if (crsrGetMatches.getCount() > 0) {

			// Log.e("", "COUNT:::" + crsrGetMatches.getCount());

			try {
				crsrGetMatches.moveToFirst();
				do {

				} while (crsrGetMatches.moveToNext());

			} catch (Exception e) {

			}

		}

		else {
			// Toast.makeText(getApplicationContext(), "Database error",
			// Toast.LENGTH_SHORT).show();
		}
		crsrGetMatches.close();

		// Log.e("", "Database Open for delete entry ");

		// onResume();
		dba.close();
		get_task();
		add_task_list();

	}

}
