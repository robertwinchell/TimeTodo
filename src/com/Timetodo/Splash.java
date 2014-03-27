package com.Timetodo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.database.DBAdapter;
import com.database.importdatabase;
import com.service.TimetodoService;
import com.share.Share;

public class Splash extends Activity {

	public static InputStream databaseInputStream1;
	int counter = 1;
	private Handler guiThread;
	private Runnable updateTask;
	// private SharedPreferences MoneyBookPrefs;
	final DBAdapter dba = new DBAdapter(this);
	String response = "";
	JSONArray jArray;
	JSONObject jObject, JObject1;
	AlertDialog my_list;
	boolean top = false;

	// boolean agree ;

	LocationManager lm;
	public static final LocationListener locListener = new MyLocationListener();

	private boolean gps_setted = false;

	private boolean gps_enabled = false;
	private boolean network_enabled = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		// MoneyBookPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		my_list = new AlertDialog.Builder(Splash.this).create();
		my_list.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				my_list.dismiss();
			}
		});

		startService(new Intent(Splash.this, TimetodoService.class));

		// Share.font = Typeface.createFromAsset(getAssets(), "my_font.TTF");

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		Boolean isSDPresent = android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);

		if (isSDPresent) {
			new InsertTask().execute("");
		} else {
			my_list.setTitle("Sdcard is not installed");
			my_list.show();
		}

	}

	private class InsertTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				File f = new File(
						"/data/data/com.Timetodo/databases/timetodo.sql");
				if (f.exists()) {
					//
				} else {
					try {

						dba.open();
						dba.close();

						System.out.println("copying database .... ");
						databaseInputStream1 = getAssets().open("timetodo.sql");

						importdatabase.copyDataBase();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				// Toast.makeText(Splash.this, e.toString(), Toast.LENGTH_SHORT)
				// .show();
			}

		}

		@Override
		protected Boolean doInBackground(String... params) {

			Boolean success = false;

			try {

				success = true;
			} catch (Exception e) {
				if (e.getMessage() != null)
					e.printStackTrace();
			}
			return success;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			super.onPostExecute(success);

			if (isConnectingToInternet()) {

				// if (!gps_enabled || !network_enabled) {
				if (!gps_enabled) {
					Log.e("", "gps==******************************************"
							+ gps_enabled);

					AlertDialog.Builder builder = new Builder(Splash.this);
					builder.setTitle("Attention!");
					builder.setMessage("Sorry, location is not determined. Do you want to enable location providers ?");
					builder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();

									gps_setted = true;
									startActivity(new Intent(
											android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
								}
							});
					builder.setNeutralButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();

									double[] d = getlocation();
									Share.cur_lat = d[0];
									Share.cur_lng = d[1];

									initThreading();
									guiThread.postDelayed(updateTask, 1000);

								}
							});
					builder.create().show();

				} else {

					//
					// if (gps_enabled)
					// lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					// 0, 0, locListener);
					// if (network_enabled)
					// lm.requestLocationUpdates(
					// LocationManager.NETWORK_PROVIDER, 0, 0,
					// locListener);

					double[] d = getlocation();
					Share.cur_lat = d[0];
					Share.cur_lng = d[1];

					initThreading();
					guiThread.postDelayed(updateTask, 1000);

				}

			} else {
				my_list.setMessage("Please check your internet connection");
				my_list.show();
			}

		}

	}

	private void initThreading() {
		guiThread = new Handler();
		updateTask = new Runnable() {
			public void run() {
				changeImage();
			}
		};
	}

	private void changeImage() {
		counter = counter + 1;
		if (counter == 2) {

			Intent menu = new Intent(Splash.this, Home.class);
			startActivity(menu);
			finish();

			setActivityAnimation(Splash.this, R.anim.fade_in, R.anim.fade_out);
		}
	}

	static public void setActivityAnimation(Activity activity, int in, int out) {
		try {
			Method method = Activity.class.getMethod(
					"overridePendingTransition", new Class[] { int.class,
							int.class });
			method.invoke(activity, in, out);
		} catch (Exception e) {
			// Can't change animation, so do nothing
		}
	}

	private double[] getlocation() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);

		Location l = null;
		for (int i = 0; i < providers.size(); i++) {
			l = lm.getLastKnownLocation(providers.get(i));
			if (l != null)
				break;
		}
		double[] gps = new double[2];

		if (l != null) {
			gps[0] = l.getLatitude();
			gps[1] = l.getLongitude();
		}
		return gps;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
						return true;

		}
		return false;
	}

	public static class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				// This needs to stop getting the location data and save the
				// battery power.
				// lm.removeUpdates(locListener);

				Share.lat = location.getLatitude();
				Share.lng = location.getLongitude();
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

}
