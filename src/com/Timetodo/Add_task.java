package com.Timetodo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.database.DBAdapter;
import com.share.Share;

public class Add_task extends Activity implements OnClickListener {

	Button btn_addtask;
	LinearLayout rr;
	static LayoutInflater linf;
	ArrayAdapter<String> VesselListAdapter;

	final DBAdapter dba = new DBAdapter(this);
	String name, time, des, type;
	ScrollView scr_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_task);

		btn_addtask = (Button) findViewById(R.id.btn_addtask);
		btn_addtask.setOnClickListener(this);

		scr_title = (ScrollView) findViewById(R.id.scr_title);

		linf = (LayoutInflater) getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		linf = LayoutInflater.from(Add_task.this);
		rr = (LinearLayout) findViewById(R.id.lnr_task_list);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		get_task();
		task_list();

	}

	public void get_task() {

		dba.open();

		Cursor crsrGetMatches = dba.get_Alltask_list();

		Log.e("", "length" + crsrGetMatches.getCount());

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

				} while (crsrGetMatches.moveToNext());

			} catch (Exception e) {

			}
		}

		else {

		}
		crsrGetMatches.close();
		dba.close();

	}

	private void task_list() {
		// TODO Auto-generated method stub

		rr.removeAllViews();

		for (int i = 0; i < Share.task_id.size(); i++) {

			String id = Share.task_id.get(i);
			String name = Share.task_name.get(i);
			String des = Share.task_des.get(i);
			String date = Share.task_date.get(i);
			String time = Share.task_time.get(i);
			String type = Share.task_type.get(i);

			// Log.e("", "msg current date" + date);
			Calendar cal = Calendar.getInstance();
			cal.get(Calendar.DATE);
			cal.get(Calendar.MONTH);
			cal.get(Calendar.YEAR);

			String today = cal.get(Calendar.DATE) + "-"
					+ (cal.get(Calendar.MONTH) + 1) + "-"
					+ cal.get(Calendar.YEAR);

			String formattedDate = "";
			Date d1 = null;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");

			try {
				d1 = sdf.parse(today);

				formattedDate = output.format(d1);
				// Log.e("", "msg date======" + formattedDate);
			} catch (Exception e) {
				e.getMessage();
			}

			Log.e("", "msg current date" + formattedDate);

			if (date.equalsIgnoreCase(formattedDate)) {
				row(id, name, des, date, time, type);
			}

		}

	};

	private void row(final String id, final String name, final String des,
			final String date, final String time, final String type) {
		// TODO Auto-generated method stub

		final View v = linf.inflate(R.layout.row_task, null);

		LinearLayout lnr_task_row = (LinearLayout) v
				.findViewById(R.id.lnr_task_row);

		TextView txt_task = (TextView) v.findViewById(R.id.txt_task);
		ImageView img_task_delete = (ImageView) v
				.findViewById(R.id.img_task_delete);
		LinearLayout lnr_task_delete = (LinearLayout) v
				.findViewById(R.id.lnr_task_delete);
		LinearLayout lnr_task_edit = (LinearLayout) v
				.findViewById(R.id.lnr_task_edit);

		try {
			txt_task.setText(name);

			lnr_task_row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					scr_title.requestDisallowInterceptTouchEvent(true);

					Intent intent = new Intent(Add_task.this, Task_map.class);

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
					scr_title.requestDisallowInterceptTouchEvent(true);
					AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
							Add_task.this);

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

					Intent edit = new Intent(Add_task.this, Task_add.class);

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

			Intent intent = new Intent(Add_task.this, Task_add.class);
			startActivityForResult(intent, 9);

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

		onResume();

		// onResume();
		dba.close();

	}

}
