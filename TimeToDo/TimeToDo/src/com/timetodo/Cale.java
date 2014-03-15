package com.timetodo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.calendar.CalendarDayMarker;
import com.calendar.CalendarView;
import com.calendar.CalendarView.OnMonthChangedListener;
import com.database.DBAdapter;

public class Cale extends Activity implements Runnable, OnClickListener {

	public static LinearLayout lnrLayoutTransactions;
	public static LinearLayout lnrLayoutTransactions_g;

	public static LinearLayout lnrLayoutCalendar;
	public static CalendarView _calendar;
	public static LinearLayout lay_main;

	// --
	public static LinearLayout lay_detail, lay_ib;
	public static TextView txt_ttl, txt_duration, txt_phone, txt_link,
			txt_desc, txt_time;
	public static ImageButton ib_back;
	public static ImageView img1, img2, img3;

	public static Drawable drawable;
	public static LinearLayout lay_phone, lay_link;
	public static ImageView img_view;
	public static ScrollView parentScroll;

	// --

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cale);

		lay_main = (LinearLayout) findViewById(R.id.cal_main);

		lnrLayoutTransactions = (LinearLayout) findViewById(R.id.forecast_lnrLayoutTransactions);
		lnrLayoutTransactions_g = (LinearLayout) findViewById(R.id.forecast_lnrLayoutTransactions_g);

		lnrLayoutCalendar = (LinearLayout) findViewById(R.id.forecast_lnrLayoutCalendar);
		_calendar = (CalendarView) findViewById(R.id.forecast_calendarView);

		CalendarView.flgMonthChanged = true;
		CalendarView.flgSetMonthData = true;

		_calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
			public void onMonthChanged(CalendarView view) {

				markDays();

			}
		});

		arr_event_id = new ArrayList<String>();
		// Share.task_id.clear();
		arr_event_name = new ArrayList<String>();
		arr_event_date = new ArrayList<String>();
		// arr_event_image = new ArrayList<String>();
		arr_event_type = new ArrayList<String>();

		arr_event_name.clear();
		arr_event_date.clear();
		arr_event_type.clear();

		Thread thread = new Thread(Cale.this);
		thread.start();

	}

	public static void markDays() {

		_calendar
				.setDaysWithEvents(new CalendarDayMarker[] { new CalendarDayMarker(
						Calendar.getInstance(), Color.BLUE) });

	}

	public static boolean check_date_arratlist(int year, int month, int day) {
		boolean found = false;

		// if (Cale.event_date != null) {
		for (int i = 0; i < Cale.arr_event_date.size(); i++) {

			String row[] = Cale.arr_event_date.get(i).toString().split("-");

			if (Integer.parseInt(row[0]) == year
					&& Integer.parseInt(row[1]) == month
					&& Integer.parseInt(row[2]) == day) {

				return true;
			}

		}
		// }

		return false;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {

			DBAdapter dba = new DBAdapter(this);
			dba.open();
			Cursor c = dba.gettask_list();
			//
			for (int i = 0; i < c.getCount(); i++) {
				//
				// if (Share.german) {
				Set_Arraylist(c.getString(0), c.getString(1), c.getString(3),
						c.getString(4), c.getString(5));

				Log.e("", "msg name" + c.getString(5));

				c.moveToNext();

			}
			c.close();
			dba.close();

			handler.sendEmptyMessage(0);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			handler.sendEmptyMessage(1);
		}

	}

	// --------------
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == 0) {

				try {

					markDays();

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} else {

			}

		}
	};

	public static ArrayList<String> arr_event_id;

	public static ArrayList<String> arr_event_name;
	public static ArrayList<String> arr_event_date;
	public static ArrayList<String> arr_event_type;

	// public static ArrayList<String> arr_event_image;

	public void Set_Arraylist(String event_id, String event_title,
			String from_date, String image, String type) {

		Calendar cal1 = Calendar.getInstance();
		// Calendar cal2 = Calendar.getInstance();
		//

		// Log.e("", "msg  from_date=======" + from_date);
		// String row[] = from_date.split("-");
		// String row1[] = row[2].split(" ");

		int date1 = Integer.parseInt(from_date.split("-")[0]);
		int month1 = Integer.parseInt(from_date.split("-")[1]);
		int year1 = Integer.parseInt(from_date.split("-")[2]);

		Log.e("", "year=" + year1 + "month=" + month1 + "date=" + date1);
		cal1.set(Calendar.DAY_OF_MONTH, date1);
		cal1.set(Calendar.MONTH, month1 - 1);
		cal1.set(Calendar.YEAR, year1);

		// String r[] = untill_date.split("-");
		// String r1[] = r[2].split(" ");

		// int year2 = Integer.parseInt(untill_date.split("-")[0]);
		// int month2 = Integer.parseInt(untill_date.split("-")[1]);
		// int date2 = Integer.parseInt(untill_date.split("-")[2]);
		//
		// // Log.e("", "year2=" + year2 + "month2=" + month2 + "date2=" +
		// date2);
		// cal2.set(Calendar.DAY_OF_MONTH, date2);
		// cal2.set(Calendar.MONTH, month2 - 1);
		// cal2.set(Calendar.YEAR, year2);

		// while (cal1.getTimeInMillis() <= cal2.getTimeInMillis()) {

		System.out.println("data has been added to arraylist");
		SimpleDateFormat month_date = new SimpleDateFormat("yyyy-MM-dd");

		String result = month_date.format(cal1.getTime());
		arr_event_date.add(result);
		arr_event_id.add(event_id);
		// Share.event_id.add(event_id);
		arr_event_name.add(event_title);
		arr_event_type.add(type);

		Log.e("", " msg Cale.arr_event_name=====" + type);
		// arr_event_image.add(image);

		cal1.add(Calendar.DATE, 1);

		// }

		// Log.e("", "imge==" + arr_event_image);

	}

	// ----------
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}