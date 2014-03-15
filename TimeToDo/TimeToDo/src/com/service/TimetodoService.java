package com.service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.share.Share;

public class TimetodoService extends Service implements LocationListener {

	String bestProvider;
	LocationManager lm;

	LocationUtil locationUtil;
	Location location;

	String lat, lng;

	public class LocalBinder extends Binder {
		TimetodoService getService() {
			return TimetodoService.this;
		}
	}

	@Override
	public void onCreate() {

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		bestProvider = lm.getBestProvider(criteria, false);

		Log.e("", "msg service class");

		startListening();

		TimerTask task = new MyTimer();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, new Date(), 30000);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		stopListening();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final IBinder mBinder = new LocalBinder();

	public class MyTimer extends TimerTask {

		@Override
		public void run() {
			try {
				lat = String.valueOf(getLatitude());
				lng = String.valueOf(getLogitude());

				if (lat != null && !lat.equals("0.0") && !lng.equals("0.0")) {
					
					Share.cur_lat = Double.parseDouble(lat);
					Share.cur_lng = Double.parseDouble(lng);
					
				}
				Log.e("", "" + "Service Latitude : " + Share.cur_lat
						+ ", Longitude : " + Share.cur_lng);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public double getLogitude() {
		if (location == null) {
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location location = lm
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			this.location = location;
		}
		return location.getLongitude();
	}

	public double getLatitude() {
		if (location == null) {
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location location = lm
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			this.location = location;
		}
		return location.getLatitude();
	}

	private void stopListening() {
		if (lm != null)
			lm.removeUpdates(this);
	}

	private void startListening() {
		lm.requestLocationUpdates(bestProvider, 20000, 0, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			this.location = location;

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
