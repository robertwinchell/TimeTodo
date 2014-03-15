package com.timetodo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.database.DBAdapter;
import com.database.importdatabase;
import com.share.Share;

public class Splash extends Activity {

	public static InputStream databaseInputStream1;
	int counter = 1;
	private Handler guiThread;
	private Runnable updateTask;
	private SharedPreferences MoneyBookPrefs;
	final DBAdapter dba = new DBAdapter(this);
	String response = "";
	JSONArray jArray;
	JSONObject jObject, JObject1;
	AlertDialog my_list;
	boolean top = false;

	// boolean agree ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		MoneyBookPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		my_list = new AlertDialog.Builder(Splash.this).create();
		my_list.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				my_list.dismiss();
			}
		});

		// Share.font = Typeface.createFromAsset(getAssets(), "my_font.TTF");

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (MoneyBookPrefs.getBoolean("agree", true)) {

			SharedPreferences.Editor editor = MoneyBookPrefs.edit();
			editor.putBoolean("agree", false);
			editor.putString("txt_currncy", "र");
			editor.putString("time", "9.00");
			editor.putString("notification", "true");

			editor.commit();
			new InsertTask().execute("");
		} else {
			new InsertTask().execute("");
		}
	}

	private class InsertTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				File f = new File(
						"/data/data/com.timetodo/databases/timetodo.sql");
				if (f.exists()) {
					//
				} else {
					try {

						dba.open();
						dba.close();

						System.out.println("copying database .... ");
						databaseInputStream1 = getAssets()
								.open("timetodo.sql");

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

			// if (isConnectingToInternet()) {
			initThreading();
			guiThread.postDelayed(updateTask, 1000);

			// } else {
			// my_list.setMessage("Please check your internet connection");
			// my_list.show();
			// }

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

}
