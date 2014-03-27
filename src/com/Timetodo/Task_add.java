package com.Timetodo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.database.DBAdapter;
import com.share.Share;

public class Task_add extends Activity implements OnClickListener {

	ArrayAdapter<String> VesselListAdapter, VesselListAdaptertask;
	static LayoutInflater linf;
	final DBAdapter dba = new DBAdapter(this);

	EditText edt_task_name, edt_task_type, edt_task_des, edt_task_time;
	TextView txt_task_type, txt_task_date, txt_task_name;
	Button btn_ok;
	private int pYear;
	private int pMonth;
	private int pDay;
	String formattedDate;
	LinearLayout lnr_add_task, lnr_qucik_task, lnr_task_des, lnr_task_date;
	static final int DATE_DIALOG_ID = 0;
	String task_id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.addtask);

		ImageView img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();

			}
		});

		lnr_add_task = (LinearLayout) findViewById(R.id.lnr_add_task);
		lnr_qucik_task = (LinearLayout) findViewById(R.id.lnr_qucik_task);
		lnr_task_des = (LinearLayout) findViewById(R.id.lnr_task_des);
		lnr_task_date = (LinearLayout) findViewById(R.id.lnr_task_date);
		txt_task_date = (TextView) findViewById(R.id.txt_task_date);

		if (!Share.qucktask) {

			lnr_qucik_task.setVisibility(View.GONE);
			lnr_add_task.setVisibility(View.VISIBLE);
			lnr_task_des.setVisibility(View.VISIBLE);
			lnr_task_date.setVisibility(View.VISIBLE);
			edt_task_name = (EditText) findViewById(R.id.edt_task_name);

			edt_task_des = (EditText) findViewById(R.id.edt_task_des);
			if (Share.cal_task) {

				Bundle b = getIntent().getExtras();

				if (b != null) {

					int date = b.getInt("date");
					int month = b.getInt("month");
					month = month + 1;
					int year = b.getInt("year");

					String today = date + "-" + month + "-" + year;

					Date d1 = null;
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");

					try {
						d1 = sdf.parse(today);

						formattedDate = output.format(d1);

						txt_task_date.setText(formattedDate);
						// Log.e("", "msg date======" + formattedDate);
					} catch (Exception e) {
						e.getMessage();
					}

					Log.e("", "msg current date" + formattedDate);

				}

			} else {

				txt_task_date.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showDialog(DATE_DIALOG_ID);
					}
				});

			}
		}
		edt_task_time = (EditText) findViewById(R.id.edt_task_time);

		txt_task_type = (TextView) findViewById(R.id.txt_task_type1);
		txt_task_type.setOnClickListener(this);

		btn_ok = (Button) findViewById(R.id.btn_ok);
		try {
			btn_ok.setOnClickListener(this);
		} catch (Exception e) {
			e.getMessage();
		}

		linf = (LayoutInflater) getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		linf = LayoutInflater.from(Task_add.this);
		Share.task_reminder.clear();

		Share.task_reminder.add("Hospital");
		Share.task_reminder.add("ATM");
		Share.task_reminder.add("Bank");
		Share.task_reminder.add("Restaurant");
		Share.task_reminder.add("Mall");
		Share.task_reminder.add("Hotel");
		Share.task_reminder.add("Hostel");
		Share.task_reminder.add("Garden");
		Share.task_reminder.add("Beach");

		VesselListAdapter = new ArrayAdapter<String>(Task_add.this,
				android.R.layout.select_dialog_singlechoice,
				Share.task_reminder);

		Log.e("", "msg  edit" + Share.task_edit);
		if (Share.task_edit) {

			Bundle b = getIntent().getExtras();

			if (b != null) {

				task_id = b.getString("id");
				String task_name = b.getString("name");
				String task_des = b.getString("des");
				String task_date = b.getString("date");
				String task_time = b.getString("time");
				String task_type = b.getString("type");

				edt_task_name.setText(task_name, BufferType.EDITABLE);
				edt_task_name.setSelection(edt_task_name.getText().length());
				edt_task_des.setText(task_des, BufferType.EDITABLE);
				edt_task_des.setSelection(edt_task_des.getText().length());
				edt_task_time.setText(task_time, BufferType.EDITABLE);
				edt_task_time.setSelection(edt_task_time.getText().length());

				txt_task_date.setText(task_date);

				txt_task_type.setText(task_type);
				// Log.e("", " msg time======" + task_time);
				// Log.e("", "msg text date===" + task_date);
				btn_ok.setText("Update");

			}

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == txt_task_date) {

			showDialog(DATE_DIALOG_ID);
		} else if (v == txt_task_type) {

			AlertDialog.Builder builder = new AlertDialog.Builder(Task_add.this);
			//
			// // builder.setTitle("Vessel list");
			builder.setTitle("Choose your Type");
			builder.setPositiveButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			builder.setAdapter(VesselListAdapter,
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog,
								final int item) {

							String money_type = Share.task_reminder.get(item);

							// Log.e("", )
							txt_task_type.setText(Share.task_reminder.get(item));
							dialog.cancel();
							// // Share.moneytpye = money_type;
							//
						}
					});

			final AlertDialog dialog = builder.create();
			dialog.show();

		} else if (v == txt_task_name) {

			AlertDialog.Builder builder = new AlertDialog.Builder(Task_add.this);
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

							// Log.e("", )
							txt_task_name.setText(Share.quick_task.get(item));
							dialog.cancel();
							// // Share.moneytpye = money_type;
							//
						}
					});

			final AlertDialog dialog = builder.create();
			dialog.show();

		} else if (v == btn_ok) {

			if (Share.task_edit) {

				dba.open();
				// String formattedDate = "";
				Date d1 = null;

				// Calendar c = Calendar.getInstance();

				Log.e("", "msg date====" + txt_task_date.getText().toString());

				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");

				try {
					d1 = sdf.parse(txt_task_date.getText().toString());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				formattedDate = output.format(d1);
				Log.e("", "msg date======" + formattedDate);

				dba.update_task(task_id, edt_task_name.getText().toString(),
						edt_task_des.getText().toString(), formattedDate,
						edt_task_time.getText().toString(), txt_task_type
								.getText().toString());

				Cursor crsrGetMatches = dba.select_id(edt_task_name.getText()
						.toString());

				String id = "";

				if (crsrGetMatches.getCount() > 0) {

					Log.e("", "COUNT:::" + crsrGetMatches.getCount());
					try {
						crsrGetMatches.moveToFirst();
						do {

							id = crsrGetMatches.getString(0);
							// Log.e("", "msg id====" + id);

						} while (crsrGetMatches.moveToNext());

					} catch (Exception e) {
					}
				}

				else {
				}
				crsrGetMatches.close();

				dba.close();
				finish();

			} else {

				if (!Share.qucktask) {
					if (edt_task_name.getText().toString().equalsIgnoreCase("")) {
						ok_dialog("Please insert task name.");
					} else if (edt_task_des.getText().toString()
							.equalsIgnoreCase("")) {
						ok_dialog("Please insert task descripation.");
					} else if (edt_task_time.getText().toString()
							.equalsIgnoreCase("")) {
						ok_dialog("Please insert task time.");
					} else if (txt_task_type.getText().toString()
							.equalsIgnoreCase("")) {
						ok_dialog("Please insert task type.");
					} else if (txt_task_date.getText().toString()
							.equalsIgnoreCase("")) {

						ok_dialog("Please insert task date.");
					} else {
						try {
							add_task();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					try {
						add_task();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

	}

	private void add_task() throws ParseException {
		// TODO Auto-generated method stub
		dba.open();

		if (!Share.qucktask) {
			//

			// Calendar c = Calendar.getInstance();

			if (!Share.cal_task) {

				txt_task_date.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showDialog(DATE_DIALOG_ID);
					}
				});

				// String formattedDate = "";
				Date d1 = null;
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");

				Log.e("", "msg formatdate" + txt_task_date.getText().toString());
				d1 = sdf.parse(txt_task_date.getText().toString());

				formattedDate = output.format(d1);

				// Log.e("", "msg formatdate" +
				// txt_task_date.getText().toString());

				// dba.insert_account_enrty("timetodo", edt_task_name.getText()
				// .toString(), edt_task_des.getText().toString(),
				// formattedDate, edt_task_time.getText().toString(),
				// txt_task_type.getText().toString());

				// Cursor crsrGetMatches = dba.select_id(edt_task_name.getText()
				// .toString());
				//
				// String id = "";
				//
				// if (crsrGetMatches.getCount() > 0) {
				//
				// Log.e("", "COUNT:::" + crsrGetMatches.getCount());
				// try {
				// crsrGetMatches.moveToFirst();
				// do {
				//
				// id = crsrGetMatches.getString(0);
				// // Log.e("", "msg id====" + id);
				//
				// } while (crsrGetMatches.moveToNext());
				//
				// } catch (Exception e) {
				// }
				// }

			}

			Log.e("", "formattedDate===" + formattedDate);
			dba.insert_account_enrty("timetodo", edt_task_name.getText()
					.toString(), edt_task_des.getText().toString(),
					formattedDate, edt_task_time.getText().toString(),
					txt_task_type.getText().toString());

			// Select ID
			Cursor crsrGetMatches = dba.select_id(edt_task_name.getText()
					.toString());

			String id = "";

			if (crsrGetMatches.getCount() > 0) {

				Log.e("", "COUNT:::" + crsrGetMatches.getCount());
				try {
					crsrGetMatches.moveToFirst();
					do {

						id = crsrGetMatches.getString(0);
						// Log.e("", "msg id====" + id);

					} while (crsrGetMatches.moveToNext());

				} catch (Exception e) {
				}
			}

			else {
			}
			crsrGetMatches.close();
		} else {

			Date d1 = null;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");

			d1 = sdf.parse(txt_task_date.getText().toString());

			Log.e("", "msg date======" + txt_task_date.getText().toString());

			formattedDate = output.format(d1);
			Log.e("", "msg current date" + formattedDate);

			dba.insert_account_enrty("timetodo", txt_task_name.getText()
					.toString(), "", formattedDate, edt_task_time.getText()
					.toString(), txt_task_type.getText().toString());

			Cursor crsrGetMatches = dba.select_id(txt_task_name.getText()
					.toString());

			String id = "";

			if (crsrGetMatches.getCount() > 0) {

				Log.e("", "COUNT:::" + crsrGetMatches.getCount());
				try {
					crsrGetMatches.moveToFirst();
					do {

						id = crsrGetMatches.getString(0);
						// Log.e("", "msg id====" + id);

					} while (crsrGetMatches.moveToNext());

				} catch (Exception e) {
				}
			}

			else {
			}
			crsrGetMatches.close();

		}

		dba.close();
		finish();

	}

	public void ok_dialog(String dialog_string) {
		View layout = linf.inflate(R.layout.alert_dialog, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(Task_add.this);

		final AlertDialog dialog = builder.create();
		dialog.setView(layout, 0, 0, 0, 0);
		dialog.show();

		Button action_ok = (Button) layout
				.findViewById(R.id.alert_callconfirmation_btnOk);
		// Button action_cancle = (Button) layout
		// .findViewById(R.id.btn_dialog_cancle);
		TextView action_txt = (TextView) layout.findViewById(R.id.txt_desc);
		action_txt.setText(dialog_string);

		action_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				dialog.dismiss();

			}
		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		Calendar calendar = Calendar.getInstance();
		pYear = calendar.get(Calendar.YEAR);
		pMonth = calendar.get(Calendar.MONTH);
		pDay = calendar.get(Calendar.DAY_OF_MONTH);

		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, pDateSetListener, pYear, pMonth,
					pDay);

		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			pYear = year;
			pMonth = monthOfYear;
			pDay = dayOfMonth;
			txt_task_date.setText(new StringBuilder()
					// Month is 0 based so add 1
					.append(pDay).append("-").append(pMonth + 1).append("-")
					.append(pYear));
		}
	};

}
