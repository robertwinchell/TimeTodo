package com.calendar;

import java.text.DecimalFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.Timetodo.Cale;
import com.Timetodo.R;
import com.Timetodo.Task_add;
import com.Timetodo.Task_map;
import com.calendar.CalendarWrapper.OnDateChangedListener;
import com.share.Share;

public class CalendarView extends LinearLayout implements OnClickListener {

	boolean first_load;

	Context c;
	ImageButton img_add_task;

	int date, month, year;

	/**********************************************************************************************************/

	public void SysDate() {

		Calendar c = Calendar.getInstance();
		sysYear = c.get(Calendar.YEAR);
		sysMonth = c.get(Calendar.MONTH);
		sysDay = c.get(Calendar.DAY_OF_MONTH);

	}

	/**********************************************************************************************************/

	public CalendarView(Context context) {
		super(context);
		c = context;
		init(context);
	}

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public interface OnMonthChangedListener {
		public void onMonthChanged(CalendarView view);
	}

	public void setOnMonthChangedListener(OnMonthChangedListener l) {
		_onMonthChangedListener = l;
	}

	public interface OnSelectedDayChangedListener {
		public void onSelectedDayChanged(CalendarView view);
	}

	public void setOnSelectedDayChangedListener(OnSelectedDayChangedListener l) {
		_onSelectedDayChangedListener = l;
	}

	public Calendar getVisibleStartDate() {
		return _calendar.getVisibleStartDate();
	}

	public Calendar getVisibleEndDate() {
		return _calendar.getVisibleEndDate();
	}

	public Calendar getSelectedDay() {
		return _calendar.getSelectedDay();
	}

	public void setDaysWithEvents(CalendarDayMarker[] markers) {

		int dayItemsInGrid = weekRows * 7;
		int abb = dayItemsInGrid;

		Boolean flag = true;
		Calendar new_tempCal = _calendar.getVisibleStartDate();

		int new_col = 0;
		int new_row = 1;

		for (int j = 0; j < abb; j++) {

			TableRow tr = (TableRow) _days.getChildAt(new_row);
			RelativeLayout rltvLayout = (RelativeLayout) tr.getChildAt(new_col);

			BitmapDrawable bd = (BitmapDrawable) this.getResources()
					.getDrawable(R.drawable.current_date_bg);
			int height = bd.getBitmap().getHeight();
			int width = bd.getBitmap().getWidth();

			rltvLayout.setMinimumWidth(width);
			rltvLayout.setMinimumHeight(height);

			LinearLayout lnrLayoutPayStatus = (LinearLayout) rltvLayout
					.getChildAt(3);
			ImageView ivGreen = (ImageView) lnrLayoutPayStatus.getChildAt(0);
			ImageView ivRed = (ImageView) lnrLayoutPayStatus.getChildAt(1);

			int[] tag = (int[]) rltvLayout.getTag();
			int day = tag[1];

			TextView tvDay = (TextView) rltvLayout.getChildAt(2);
			tvDay.setTextSize(getResources().getDimension(R.dimen.txt_1));

			if (tvDay.getTextColors() == ColorStateList.valueOf(getResources()
					.getColor(R.color.blue))) {

				rltvLayout.setBackgroundColor(getResources().getColor(
						R.color.cal_purple_light));

				// rltvLayout.setBackgroundColor(R.drawable.cal_light);

			} else {

				rltvLayout.setBackgroundColor(getResources().getColor(
						R.color.cal_purple_dark));

				// rltvLayout.setBackgroundColor(R.drawable.cal_dark);

			}

			if (flgMonthChanged) {

				Cale.lnrLayoutTransactions.removeAllViews();
				Cale.lnrLayoutTransactions_g.removeAllViews();

				boolean _found = false;

				for (int l = 0; l < markers.length; l++) {
					CalendarDayMarker n = markers[l];

					if (Cale.check_date_arratlist(
							new_tempCal.get(Calendar.YEAR),
							new_tempCal.get(Calendar.MONTH) + 1,
							new_tempCal.get(Calendar.DAY_OF_MONTH))) {

						ivGreen.setBackgroundResource(R.drawable.event_dot);
						ivGreen.setVisibility(View.VISIBLE);
					} else {
						ivGreen.setVisibility(View.GONE);
					}

					if (day == 1) {
						if (flag) {
							flag = false;
							rltvLayout.setEnabled(true);

						} else {
							flag = true;
							rltvLayout.setEnabled(false);

						}
					} else {
						if (flag) {
							rltvLayout.setEnabled(false);

						} else {
							rltvLayout.setEnabled(true);

						}
					}

					if (new_tempCal.get(Calendar.YEAR) == sysYear
							&& new_tempCal.get(Calendar.MONTH) == sysMonth
							&& day == sysDay) {

						// rltvLayout.setBackgroundColor(getResources().getColor(
						// R.color.cal_blue_dark));

						Log.e("", "1");

						rltvLayout
								.setBackgroundResource(R.drawable.current_date_bg);

						tvDay.setTextColor(Color.WHITE);

						ivGreen.setBackgroundResource(R.drawable.selected_dot);

						if (!initializing) {

						}
					}

					else if (new_tempCal.get(Calendar.YEAR) == n.getYear()
							&& new_tempCal.get(Calendar.MONTH) == n.getMonth()) {

						Log.e("", "2");

						if (day == n.getDay()) {

							ivGreen.setBackgroundResource(R.drawable.event_dot);

							if (tvDay.getTextColors() == ColorStateList
									.valueOf(getResources().getColor(
											R.color.blue))) {

								rltvLayout
										.setBackgroundResource(R.drawable.selected_date_bg);
								ivGreen.setBackgroundResource(R.drawable.selected_dot);

							}
						}
					}
				}

			} else {
				for (int k = 0; k < markers.length; k++) {
					CalendarDayMarker m = markers[k];

					if (Cale.check_date_arratlist(
							new_tempCal.get(Calendar.YEAR),
							new_tempCal.get(Calendar.MONTH) + 1,
							new_tempCal.get(Calendar.DAY_OF_MONTH))) {

						ivGreen.setBackgroundResource(R.drawable.event_dot);
						ivGreen.setVisibility(View.VISIBLE);
					} else {
						ivGreen.setVisibility(View.GONE);
					}

					if (new_tempCal.get(Calendar.YEAR) == sysYear
							&& new_tempCal.get(Calendar.MONTH) == sysMonth
							&& day == sysDay) {

						Log.e("", "3");

						rltvLayout
								.setBackgroundResource(R.drawable.current_date_bg);

						ivGreen.setBackgroundResource(R.drawable.selected_dot);

						new_setTransactionRow(m.getYear(), m.getMonth() + 1,
								m.getDay());
						new_setTransactionRow_g(m.getYear(), m.getMonth() + 1,
								m.getDay());

						// XXX

					} else if (new_tempCal.get(Calendar.YEAR) == m.getYear()
							&& new_tempCal.get(Calendar.MONTH) == m.getMonth()) {

						if (day == m.getDay()) {

							if (tvDay.getTextColors() == ColorStateList
									.valueOf(getResources().getColor(
											R.color.blue))) {

								rltvLayout
										.setBackgroundResource(R.drawable.selected_date_bg);

								ivGreen.setBackgroundResource(R.drawable.selected_dot);

								new_setTransactionRow(m.getYear(),
										m.getMonth() + 1, m.getDay());

								new_setTransactionRow_g(m.getYear(),
										m.getMonth() + 1, m.getDay());

							}

						}
					}
				}
			}

			new_col++;

			if (new_col == 7) {
				new_col = 0;
				new_row++;
			}

			new_tempCal.add(Calendar.DAY_OF_MONTH, 1);
		}

		if (first_load) {
			first_load = false;

			new_setTransactionRow(sysYear, sysMonth + 1, sysDay);
			new_setTransactionRow_g(sysYear, sysMonth + 1, sysDay);
		}
		flgMonthChanged = false;
	}

	public void setListViewItems(View[] views) {

		_events.removeAllViews();

		for (int i = 0; i < views.length; i++) {
			_events.addView(views[i]);
		}
	}

	private void init(Context context) {

		View v = LayoutInflater.from(context).inflate(R.layout.calendar, this,
				true);

		_calendar = new CalendarWrapper();
		_days = (TableLayout) v.findViewById(R.id.days);
		_up = (Button) v.findViewById(R.id.up);
		_prev = (ImageButton) v.findViewById(R.id.previous);
		_next = (ImageButton) v.findViewById(R.id.next);

		img_add_task = (ImageButton) v.findViewById(R.id.img_add_task);

		img_add_task.setOnClickListener(this);
		
		ImageView img_back = (ImageView)v. findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				((Activity) getContext()).finish();

			}
		});

		_events = (LinearLayout) v.findViewById(R.id.events);

		_prev.setBackgroundDrawable(null);
		_next.setBackgroundDrawable(null);
		_up.setBackgroundDrawable(null);

		first_load = true;

		/*********************************************************************************************************/
		SysDate();
		refreshCurrentDate();

		/**********************************************************************************************************/

		// Days Table
		for (int i = 0; i < 1; i++) { // Rows
			TableRow tr = (TableRow) _days.getChildAt(i);

			for (int j = 0; j < 7; j++) { // Columns
				Boolean header = i == 0; // First row is weekday headers
				RelativeLayout rltvLayout = (RelativeLayout) tr.getChildAt(j);

				if (!header) {

					rltvLayout.setClickable(true);
					rltvLayout.setOnClickListener(_dayClicked);
				}
			}
		}

		refreshDayCells();

		// Listeners
		_calendar.setOnDateChangedListener(_dateChanged);
		_prev.setOnClickListener(_incrementClicked);
		_next.setOnClickListener(_incrementClicked);

		setView(MONTH_VIEW);
	}

	private OnDateChangedListener _dateChanged = new OnDateChangedListener() {
		public void onDateChanged(CalendarWrapper sc) {

			Boolean monthChanged = _currentYear != sc.getYear()
					|| _currentMonth != sc.getMonth();

			Log.e("", "msg=====" + sc.getMonth());
			if (monthChanged) {
				refreshDayCells();
				invokeMonthChangedListener();
				// setOnMonthChangedListener();
			}

			refreshCurrentDate();
			refreshUpText();
		}
	};

	private OnClickListener _incrementClicked = new OnClickListener() {
		public void onClick(View v) {

			int inc = (v == _next ? 1 : -1);

			if (_currentView == MONTH_VIEW) {

				flgMonthChanged = true;
				flgSetMonthData = true;
				_calendar.addMonth(inc);

			} else if (_currentView == DAY_VIEW) {
				_calendar.addDay(inc);
				invokeSelectedDayChangedListener();
			} else if (_currentView == YEAR_VIEW) {
				_currentYear += inc;
				refreshUpText();
			}
		}
	};

	// For change the date select date
	private OnClickListener _dayClicked = new OnClickListener() {
		public void onClick(View v) {

			int[] tag = (int[]) v.getTag();
			_calendar.addMonthSetDay(tag[0], tag[1]);

			if (initializing)
				initializing = false;

			final Calendar c = Calendar.getInstance();
			c.set(_currentYear, _currentMonth, tag[1]);

			date = tag[1];
			month = _currentMonth;
			year = _currentYear;
			Log.e("", "msg cal====" + tag[1]);
			final CalendarView _calendarView = (CalendarView) findViewById(R.id.forecast_calendarView);

			// saves the clicked date when month is changed..
			_calendarView
					.setOnMonthChangedListener(new OnMonthChangedListener() {
						public void onMonthChanged(CalendarView view) {

							_calendarView
									.setDaysWithEvents(new CalendarDayMarker[] { new CalendarDayMarker(
											c, Color.BLUE) });
						}
					});

			_calendarView
					.setDaysWithEvents(new CalendarDayMarker[] { new CalendarDayMarker(
							c, Color.BLUE) });
		}
	};

	private void refreshDayCells() {

		int[] dayGrid = _calendar.get7x6DayArray();
		int monthAdd = -1;
		int row = 1; // Skip weekday header row
		int col = 0;
		Boolean flgDeleteRow = false;

		for (int i = 0; i < dayGrid.length; i++) {
			int day = dayGrid[i];

			if (day == 1)
				monthAdd++;

			TableRow tr = (TableRow) _days.getChildAt(row);
			RelativeLayout rltvLayout = (RelativeLayout) tr.getChildAt(col);
			rltvLayout.setMinimumHeight(rltvLayout.getWidth());

			ImageView ivRight = (ImageView) rltvLayout.getChildAt(1);
			// ivRight.setMinimumHeight(rltvLayout.getWidth());

			BitmapDrawable bd = (BitmapDrawable) this.getResources()
					.getDrawable(R.drawable.current_date_bg);
			int height = bd.getBitmap().getHeight();
			int width = bd.getBitmap().getWidth();

			ivRight.setMinimumHeight((Share.width / 7));

			if (!flgDeleteRow) {

				if (col == 0 && monthAdd > 0) {

					weekRows = 5;
					flgDeleteRow = true;

					for (int x = 0; x < rltvLayout.getChildCount(); x++)

						rltvLayout.setVisibility(View.GONE);
					col++;

				} else {

					weekRows = 6;
					rltvLayout.setVisibility(View.VISIBLE);

					/****************************** Date Text ************************************/
					TextView tv = (TextView) rltvLayout.getChildAt(2);
					tv.setText(String.valueOf(day));
					// tv.setTypeface(Splash.font);

					for (int k = 0; k < 6; k++)
						for (int j = 0; j < 7; j++) {

							_dayCell[k][j] = new DayCell();
							_dayCell[k][j].day = day;
						}

					if (monthAdd != 0) {

						// rltvLayout.setBackgroundColor(R.drawable.cal_dark);

						rltvLayout.setBackgroundColor(getResources().getColor(
								R.color.cal_purple_dark));

						tv.setTextColor(getResources().getColor(
								R.color.cal_inactiveText));

						rltvLayout.setClickable(false);

					} else {

						// rltvLayout.setBackgroundColor(R.drawable.cal_light);

						rltvLayout.setBackgroundColor(getResources().getColor(
								R.color.cal_purple_light));
						tv.setTextColor(getResources().getColor(R.color.blue));

						rltvLayout.setClickable(true);
						rltvLayout.setOnClickListener(_dayClicked);
					}

					rltvLayout.setTag(new int[] { monthAdd, dayGrid[i] });

					col++;

					if (col == 7) {
						col = 0;
						row++;
					}
				}
			} else {
				rltvLayout.setVisibility(View.GONE);
				col++;
			}
		}
	}

	private void setView(int view) {

		if (_currentView != view) {
			_currentView = view;
			_events.setVisibility(_currentView == DAY_VIEW ? View.VISIBLE
					: View.GONE);
			_days.setVisibility(_currentView == MONTH_VIEW ? View.VISIBLE
					: View.GONE);
			_up.setEnabled(_currentView != YEAR_VIEW);

			if (_up.isEnabled() == false)
				_up.setTextColor(Color.BLACK);

			refreshUpText();
		}
	}

	private void refreshUpText() {

		switch (_currentView) {
		case MONTH_VIEW:
			_up.setText(_calendar.toString("MMMM yyyy"));
			break;
		case YEAR_VIEW:
			_up.setText(_currentYear + "");
			break;
		case CENTURY_VIEW:
			_up.setText("CENTURY_VIEW");
			break;
		case DECADE_VIEW:
			_up.setText("DECADE_VIEW");
			break;
		case DAY_VIEW:
			_up.setText(_calendar.toString("EEEE, MMMM dd, yyyy"));
			break;
		case ITEM_VIEW:
			_up.setText("ITEM_VIEW");
			break;
		default:
			break;
		}
	}

	private void refreshCurrentDate() {

		_currentYear = _calendar.getYear();
		_currentMonth = _calendar.getMonth();
		_calendar.getDay();
	}

	private void invokeMonthChangedListener() {

		if (_onMonthChangedListener != null)
			_onMonthChangedListener.onMonthChanged(this);

	}

	private void invokeSelectedDayChangedListener() {

		if (_onSelectedDayChangedListener != null)
			_onSelectedDayChangedListener.onSelectedDayChanged(this);
	}

	private final int CENTURY_VIEW = 5;
	private final int DECADE_VIEW = 4;
	private final int YEAR_VIEW = 3;
	private final int MONTH_VIEW = 2;
	private final int DAY_VIEW = 1;
	private final int ITEM_VIEW = 0;

	/**********************************************************************************************************/
	int sysYear;
	int sysMonth;
	int sysDay;
	/**********************************************************************************************************/

	private CalendarWrapper _calendar;
	private TableLayout _days;
	private LinearLayout _events;
	private Button _up;
	private ImageButton _prev;
	private ImageButton _next;
	private OnMonthChangedListener _onMonthChangedListener;
	private OnSelectedDayChangedListener _onSelectedDayChangedListener;
	private int _currentView;
	private int _currentYear;
	private int _currentMonth;
	private Boolean initializing = true;
	public static Boolean flgMonthChanged = true;
	public static Boolean flgSetMonthData = true;
	private int weekRows;
	DayCell[][] _dayCell = new DayCell[6][7];

	DecimalFormat df = new DecimalFormat("##0.00");
	DecimalFormat df1 = new DecimalFormat("##0");

	public void new_setTransactionRow(int year, int month, int day) {

		System.out.println("New Set transaction row is called  1");

		LayoutInflater linf;
		linf = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		linf = LayoutInflater.from(getContext());

		Cale.lnrLayoutTransactions.removeAllViews();

		// if (Cale.event_date != null) {

		Log.e("", "msg" + Cale.arr_event_date.size());

		for (int i = 0; i < Cale.arr_event_date.size(); i++) {

			String row[] = Cale.arr_event_date.get(i).toString().split("-");

			Log.e("", "msg hello=======" + row[0]);

			if (Integer.parseInt(row[0]) == year
					&& Integer.parseInt(row[1]) == month
					&& Integer.parseInt(row[2]) == day) {

				System.out.println("available items::");

				final View v = linf.inflate(R.layout.row_event_cal, null);

				Log.e("",
						" msg Cale.arr_event_name====="
								+ Cale.arr_event_name.get(i));

				((TextView) v.findViewById(R.id.txt_event_name_cal))
						.setText(Cale.arr_event_name.get(i).toString());

				// final String eventid = Share.event_id.get(i).toString();

				// Log.e("", "inside==" + Cale.arr_event_image.get(i));
				//
				// imageLoader.DisplayImage(Cale.arr_event_image.get(i),
				// img_event_photo);

				final int j = i;

				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent Class_search_detail = new Intent(v.getContext(),
								Task_map.class);
						//
						Bundle b = new Bundle();
						b.putString("type", Cale.arr_event_type.get(j)
								.toString());
						Class_search_detail.putExtras(b);
						//
						v.getContext().startActivity(Class_search_detail);

					}
				});

				Cale.lnrLayoutTransactions.addView(v);
			}

		}
	}

	// }

	public void new_setTransactionRow_g(int year, int month, int day) {

		System.out.println("New Set transaction row is called 2");

		LayoutInflater linf;
		linf = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		linf = LayoutInflater.from(getContext());

		Cale.lnrLayoutTransactions_g.removeAllViews();

		// if (Cale.event_date != null) {
		Log.e("", "msg" + Cale.arr_event_date.size());

		for (int i = 0; i < Cale.arr_event_date.size(); i++) {

			String row[] = Cale.arr_event_date.get(i).toString().split("-");

			if (Integer.parseInt(row[0]) == year
					&& Integer.parseInt(row[1]) == month
					&& Integer.parseInt(row[2]) == day) {

				System.out.println("available items::");

				final View v = linf.inflate(R.layout.row_event_cal, null);

				Log.e("",
						" msg Cale.arr_event_name====="
								+ Cale.arr_event_name.get(i));

				((TextView) v.findViewById(R.id.txt_event_name_cal))
						.setText(Cale.arr_event_name.get(i).toString());

				// final String eventid = Share.event_id.get(i).toString();

				// imageLoader.DisplayImage(Cale.arr_event_image.get(i),
				// img_event_photo);

				final int j = i;
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent Class_search_detail = new Intent(v.getContext(),
								Task_map.class);
						//
						Bundle b = new Bundle();
						b.putString("type", Cale.arr_event_type.get(j)
								.toString());
						Class_search_detail.putExtras(b);
						//
						v.getContext().startActivity(Class_search_detail);
					}
				});

				Cale.lnrLayoutTransactions_g.addView(v);
			}

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == img_add_task) {

			Share.qucktask = false;
			Share.cal_task = true;
			Intent intent = new Intent(getContext(), Task_add.class);
			// startActionMode((Callback) intent);

			Bundle b = new Bundle();
			b.putInt("date", date);
			b.putInt("month", month);
			b.putInt("year", year);

			intent.putExtras(b);

			getContext().startActivity(intent);
		}

	}
}

class DayCell {

	int day;
	int saving;
	Boolean flgGreen;
	Boolean flgRed;

	DayCell() {
		day = 0;
		saving = 0;
		flgGreen = false;
		flgRed = false;
	}

	@SuppressWarnings("null")
	DayCell getDayCell() {

		DayCell temp = null;
		temp.day = this.day;
		temp.saving = this.saving;
		temp.flgGreen = this.flgGreen;
		temp.flgRed = this.flgRed;

		return temp;
	}

	void setDayCell(DayCell temp) {

		day = temp.day;
		saving = temp.saving;
		flgGreen = temp.flgGreen;
		flgRed = temp.flgRed;
	}

}
