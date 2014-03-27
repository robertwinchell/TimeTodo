package com.service;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class LocationUtil
{
	Activity activity;
	Location location;

	public LocationUtil(Activity activity)
	{
		this.activity = activity;
	}

	public int getLogitudeE6(Location location)
	{
		LocationManager lm = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);
		int lg=0;
		if (location == null)
		{
			location = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		lg = (int) (((double) location.getLongitude()) * 1E6);
		return lg;
	}

	public int getLatitudeE6(Location location)
	{
		LocationManager lm = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);
		int lt=0;
		if (location == null)
		{
			location = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location == null)
			{
				location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		}
		lt = (int) (((double) location.getLatitude()) * 1E6);
		System.out.println("Latitude :: " + lt);
		return lt;
	}

	public double getLogitude(Location location)
	{
		if (location == null)
		{
			LocationManager lm = (LocationManager) activity
					.getSystemService(Context.LOCATION_SERVICE);

			location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location == null)
			{
				location = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
			this.location = location;
		}

		return location.getLongitude();
	}

	public double getLatitude(Location location)
	{
		if (location == null)
		{
			LocationManager lm = (LocationManager) activity
					.getSystemService(Context.LOCATION_SERVICE);
			location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location == null)
			{
				location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
			this.location = location;
		}
		return location.getLatitude();
	}

	public double getLogitude()
	{
		if (location == null)
		{
			LocationManager lm = (LocationManager) activity
					.getSystemService(Context.LOCATION_SERVICE);
			Location location = lm
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			this.location = location;
		}

		return location.getLongitude();
	}

	public double getLatitude()
	{
		if (location == null)
		{
			LocationManager lm = (LocationManager) activity
					.getSystemService(Context.LOCATION_SERVICE);
			Location location = lm
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			this.location = location;
		}
		return location.getLatitude();
	}
}
