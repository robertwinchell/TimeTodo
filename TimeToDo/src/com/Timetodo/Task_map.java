package com.Timetodo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.share.Share;
import com.webservices.Webservice;

public class Task_map extends FragmentActivity {
	LayoutInflater linf;

	boolean gps_data = false;
	Timer timer;
	Geocoder geocoder;
	GoogleMap googleMap;

	MarkerOptions markerOptions;
	ProgressDialog hdialog;
	Marker marker;

	LocationManager lm;

	JSONObject jObj, jObj_data;
	JSONArray item;

	final LatLngBounds.Builder builder = new LatLngBounds.Builder();

	double curr_lat = Share.cur_lat, curr_lng = Share.cur_lng;

	protected LocationManager locationManager;
	private LocationListener locationListener = new MyLocationListener();
	int m = 0;

	Location location;

	String web_response = "";

	double meters = 0;

	Find_distance find = new Find_distance();
	private ArrayList<Marker> markers = new ArrayList<Marker>();

	HashMap<Integer, String> id_hash = new HashMap<Integer, String>();
	HashMap<Integer, String> name_hash = new HashMap<Integer, String>();
	HashMap<Integer, String> icon_hash = new HashMap<Integer, String>();
	HashMap<Integer, String> rating_hash = new HashMap<Integer, String>();
	HashMap<Integer, String> reference_hash = new HashMap<Integer, String>();
	HashMap<Integer, String> latitude_hash = new HashMap<Integer, String>();
	HashMap<Integer, String> longitude_hash = new HashMap<Integer, String>();
	HashMap<Integer, String> vicinity_hash = new HashMap<Integer, String>();
	HashMap<Integer, String> type_hash = new HashMap<Integer, String>();
	HashMap<Integer, String> distance_hash = new HashMap<Integer, String>();

	HashMap<Integer, Marker> marker_hash = new HashMap<Integer, Marker>();

	// LatLng latLng;
	boolean isGPSEnabled = false;

	String id = "", name = "", icon = "", types = "", rating = "",
			reference = "", vicinity = "", latitude = "", longitude = "";
	String task_id, task_name, task_des, task_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_map);

		getLocation();

		Bundle b = getIntent().getExtras();
		if (b != null) {

			task_id = b.getString("id");
			task_name = b.getString("name");
			task_des = b.getString("des");
			task_type = b.getString("type");

			if (task_type.equalsIgnoreCase("mall")) {
				task_type = "shopping_mall";
			}

			task_type = task_type.toLowerCase();

			Log.e("tyep", "msg type===" + task_type);

		}

		marker_hash.clear();
		id_hash.clear();
		name_hash.clear();
		icon_hash.clear();
		rating_hash.clear();
		reference_hash.clear();
		latitude_hash.clear();
		longitude_hash.clear();
		vicinity_hash.clear();
		type_hash.clear();
		//

		ImageView img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();

			}
		});
		linf = (LayoutInflater) getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		linf = LayoutInflater.from(this);

		setUpMapIfNeeded();

		// geocoder = new Geocoder(getBaseContext());

		if (isGPSEnabled) {

			Log.e("", "inside");

			new get_ManageFavourite().execute("");
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (gps_data) {

			gps_data = false;

			hdialog = ProgressDialog.show(Task_map.this, "",
					"Waiting for GPS location ...", true, false);

			if (timer != null) {

				timer.cancel();
				timer = null;

			}

			timer = new Timer();

			timer.schedule(new TimerTask() {
				public void run() {

					Log.e("START:::", "TIMER");

					// here you can start
					// your Activity B.
					handler1.sendEmptyMessage(0);
				}

			}, 1000, 1000);

		}

		Log.e("", "Lat : " + Share.cur_lat + ",,,,Lng : " + Share.cur_lng);

	}

	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == 0) {

				if (String.valueOf(Share.cur_lat).equalsIgnoreCase("0.0")
						&& String.valueOf(Share.cur_lng)
								.equalsIgnoreCase("0.0")) {

					if (timer != null) {

						timer.cancel();
						timer = null;

					}

					timer = new Timer();

					timer.schedule(new TimerTask() {
						public void run() {

							Log.e("START:::", "TIMER");

							// here you can start
							// your Activity B.
							handler1.sendEmptyMessage(0);
						}

					}, 1000, 1000);

				} else {

					hdialog.cancel();

					if (timer != null) {

						timer.cancel();
						timer = null;

					}

					new get_ManageFavourite().execute("");
				}

			}

		}
	};

	public Location getLocation() {
		try {
			locationManager = (LocationManager) this
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// if (!isGPSEnabled && !isNetworkEnabled) {
			if (!isGPSEnabled) {
				// no network provider is enabled

				showSettingsAlert();

			} else {

				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER, 0, 0,
								locationListener);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								curr_lat = location.getLatitude();
								curr_lng = location.getLongitude();

								Share.lat = curr_lat;
								Share.lng = curr_lng;

							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	private final class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location locFromGps) {
			// called when the listener is notified with a location update from
			// the GPS

			// if GPS Enabled get lat/long using GPS Services
			if (isGPSEnabled) {
				if (location == null) {
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, 0, 0,
							locationListener);
					Log.d("GPS Enabled", "GPS Enabled");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {
							curr_lat = location.getLatitude();
							curr_lng = location.getLongitude();

							Share.cur_lat = curr_lat;
							Share.cur_lng = curr_lng;
						}
					}
				}
			}

		}

		@Override
		public void onProviderDisabled(String provider) {
			// called when the GPS provider is turned off (user turning off the
			// GPS on the phone)
		}

		@Override
		public void onProviderEnabled(String provider) {
			// called when the GPS provider is turned on (user turning on the
			// GPS on the phone)
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// called when the status of the GPS provider changes
		}
	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(Task_map.this);

		// Setting Dialog Title
		alertDialog.setTitle("GPS Alert");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled.Please set the GPS Providers");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();

						gps_data = true;

						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivityForResult(intent, 10);

					}
				});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();

					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (googleMap == null) {

			Log.e("", "Into null map");
			// Try to obtain the map from the SupportMapFragment.
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map_medicalMap)).getMap();

			// googleMap=((SupportMapFragment)getSu)

			googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(
					Task_map.this));
			// googleMap.setMyLocationEnabled(true);
			// googleMap.setOnInfoWindowClickListener(this);
			// googleMap.setOnMarkerDragListener(this);
			if (googleMap != null) {
				Log.e("", "Into full map");
				googleMap.setMapType(googleMap.MAP_TYPE_NORMAL);
				googleMap.getUiSettings().setZoomControlsEnabled(false);
			}
		}
	}

	private class get_ManageFavourite extends AsyncTask<String, Void, Boolean> {

		ProgressDialog dialog;

		@Override
		protected Boolean doInBackground(String... params) {
			Boolean success = false;

			web_response = null;

			try {

				Log.e("", "Lat Lng1111 ::" + Share.cur_lat + ".........."
						+ Share.cur_lng);

				if (!String.valueOf(Share.cur_lat).equalsIgnoreCase("0.0")
						&& !String.valueOf(Share.cur_lng).equalsIgnoreCase(
								"0.0")) {

					if (Share.meters == 0) {

						Log.e("", "msg meter" + Share.meters);

						web_response = Webservice
								.webServiceCall("https://maps.googleapis.com/maps/api/place/search/json?&location="
										+ String.valueOf(Share.cur_lat)
										+ ","
										+ String.valueOf(Share.cur_lng)
										+ "&radius=2000&types="
										+ task_type
										+ "&sensor=false&key="
										+ Share.google_place_key);

						Log.e("",
								""
										+ "https://maps.googleapis.com/maps/api/place/search/json?&location="
										+ String.valueOf(Share.cur_lat) + ","
										+ String.valueOf(Share.cur_lng)
										+ "&radius=2000&types=" + task_type
										+ "&sensor=false&key="
										+ Share.google_place_key);

					} else {

						// Log.e("", "msg meter" + Share.meters);

						Share.meters = Share.meters * 25;
						web_response = Webservice
								.webServiceCall("https://maps.googleapis.com/maps/api/place/search/json?&location="
										+ String.valueOf(Share.cur_lat)
										+ ","
										+ String.valueOf(Share.cur_lng)
										+ "&radius="
										+ Share.meters
										+ "&types="
										+ task_type
										+ "&sensor=false&key="
										+ Share.google_place_key);

					}

				}

			} catch (Exception e) {

				e.printStackTrace();
			}

			return success;
		}

		@Override
		protected void onPostExecute(Boolean success) {

			super.onPostExecute(success);

			try {

				// tv_title_medicalMap.setText(Share.search_name);

				if (web_response != null) {

					jObj = new JSONObject(web_response);

					if (jObj.has("status")
							&& jObj.getString("status").equals("OK")) {

						item = jObj.getJSONArray("results");

						Log.e("", "count : " + item.length());

						for (int i = 0; i < item.length(); i++) {

							jObj_data = (JSONObject) item.get(i);

							JSONObject jObj_geometry = jObj_data
									.getJSONObject("geometry");

							JSONObject jObj_location = jObj_geometry
									.getJSONObject("location");

							if (!jObj_data.has("id")) {
								id = "";
							} else {
								id_hash.put(i, jObj_data.getString("id"));
							}

							if (!jObj_data.has("icon")) {
								icon = "";
							} else {
								icon_hash.put(i, jObj_data.getString("icon"));
							}

							if (!jObj_data.has("name")) {
								name = "";
							} else {
								name_hash.put(i, jObj_data.getString("name"));
							}

							if (!jObj_data.has("rating")) {
								rating = "";
							} else {
								rating_hash.put(i,
										jObj_data.getString("rating"));
							}

							if (!jObj_data.has("reference")) {
								reference = "";
							} else {
								reference_hash.put(i,
										jObj_data.getString("reference"));
							}

							if (!jObj_data.has("vicinity")) {
								vicinity = "";
							} else {
								vicinity_hash.put(i,
										jObj_data.getString("vicinity"));
							}

							if (!jObj_data.has("types")) {
								types = "";
							} else {
								JSONArray jValuesArray = jObj_data
										.getJSONArray("types");

								types = jValuesArray.getString(0);

								type_hash.put(i, types);

							}

							if (!jObj_location.has("lat")) {
								latitude = "";
							} else {
								latitude_hash.put(i,
										jObj_location.getString("lat"));
							}

							if (!jObj_location.has("lng")) {
								longitude = "";
							} else {
								longitude_hash.put(i,
										jObj_location.getString("lng"));
							}

							if (jObj_location.getString("lat")
									.equalsIgnoreCase("")
									|| jObj_location.getString("lng")
											.equalsIgnoreCase("")) {

							} else {

								Double distance = 0.0;
								if (!jObj_location.getString("lat").equals("")
										|| !jObj_location.getString("lng")
												.equals("")) {
									LatLng surat = new LatLng(
											Double.valueOf(jObj_location
													.getString("lat")),
											Double.valueOf(jObj_location
													.getString("lng")));

									distance = find.CalculationByDistance(
											surat, "K");
								}

								Log.e("", "Distance:::" + distance);
								distance = Math.round(distance * 100.0) / 100.0;
								final String dis = String.valueOf(distance);

								distance_hash.put(i, dis);

								setUpMap(types,
										jObj_data.getString("reference"),
										jObj_data.getString("vicinity"),
										jObj_location.getString("lat"),
										jObj_location.getString("lng"),
										jObj_data.getString("name"));

								// dba.fillMap(
								// jObj_location.getString("lat"),
								// jObj_location.getString("lng"),
								// jObj_data.getString("name").replace(
								// "'", "^"), types, distance);

								// horizontal_row(jObj_location.getString("lat"),
								// jObj_location.getString("lng"),
								// jObj_data.getString("name"), types);

							}

							// LatLngBounds.Builder builder = new
							// LatLngBounds.Builder();
							// for (Marker m : markers) {
							// builder.include(m.getPosition());
							// }
							// LatLngBounds bounds = builder.build();
							// int padding = 30;
							// CameraUpdate cu = CameraUpdateFactory
							// .newLatLngBounds(bounds, padding);
							//
							// googleMap.moveCamera(cu);

						}

						googleMap.setMyLocationEnabled(true);

						// Cursor c = dba.getMapdata();

						// Log.e("", "c count : " + c.getCount());

						// if (c.getCount() > 0) {
						//
						// try {
						// c.moveToFirst();
						//
						// do {
						//
						// // horizontal_row(c.getString(0),
						// // c.getString(1), c.getString(2)
						// // .replace("^", "'"),
						// // c.getString(3), c.getString(4));
						//
						// } while (c.moveToNext());

						// } catch (Exception e) {
						// e.printStackTrace();
						//
						// }
						// }
					}

					else {

						AlertDialog.Builder builder = new AlertDialog.Builder(
								Task_map.this);
						builder.setMessage("No data found!")
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
											}
										});

						AlertDialog alert = builder.create();
						alert.show();
					}

				} else {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							Task_map.this);
					builder.setMessage("No data found!")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});

					AlertDialog alert = builder.create();
					alert.show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(Task_map.this, "", "Loading ...",
					true, false);

		}
	}

	private void setUpMap(final String type, final String reference,
			final String vicinity, final String latitude, String longitude,
			final String name) {

		if (googleMap != null) {

			// CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(
			// builder.build(), 10);
			// googleMap.animateCamera(pos);

			final LatLng pos = new LatLng(Double.parseDouble(latitude),
					Double.parseDouble(longitude));
			builder.include(pos);

			// googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
			// builder.build(), 50));

			marker = googleMap.addMarker(new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble(latitude), Double
									.parseDouble(longitude))).title(name)
					.snippet(vicinity));

			markers.add(marker);

			marker_hash.put(m, marker);
			m++;

			Double distance = 0.0;
			if (!latitude.equals("") || !longitude.equals("")) {
				LatLng surat = new LatLng(Double.valueOf(latitude),
						Double.valueOf(longitude));

				distance = find.CalculationByDistance(surat, "K");
			}

			// Log.e("", "Distance:::" + distance);
			distance = Math.round(distance * 100.0) / 100.0;
			final String dis = String.valueOf(distance);

			// googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
			// new LatLng(Double.parseDouble(latitude), Double
			// .parseDouble(longitude)), 13));

			googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

				public void onCameraChange(CameraPosition arg0) {
					googleMap.animateCamera(CameraUpdateFactory
							.newLatLngBounds(builder.build(), 100));
					googleMap.setOnCameraChangeListener(null);
				}
			});

			googleMap
					.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

						@Override
						public void onInfoWindowClick(Marker marker) {

							for (int i = 0; i < id_hash.size(); i++) {
								if (name_hash.get(i).equalsIgnoreCase(
										marker.getTitle())) {

									Bundle b = new Bundle();

									b.putString("Name", name_hash.get(i));
									b.putString("Reference",
											reference_hash.get(i));
									b.putString("Type", type_hash.get(i));
									b.putString("latitude",
											latitude_hash.get(i));
									b.putString("longitude",
											longitude_hash.get(i));
									b.putString("distance",
											distance_hash.get(i));

								}
							}
						}
					});
		}
	}

	private void GetCurrentLocation() {

		// googleMap.addMarker(
		// new MarkerOptions()
		// .position(new LatLng(Share.lat, Share.lng))
		// .title("You are here")
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.dot_blue)))

		try {
			// .showInfoWindow();

			googleMap.getMyLocation();
			Log.e("", "Cuerrrrrrrrrrrrrrrr :: " + curr_lat + "," + curr_lng);
			googleMap.addMarker(new MarkerOptions().position(
					new LatLng(Share.cur_lat, Share.cur_lng)).title(
					"You are here"));

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	// @Override
	// public void onBackPressed() {
	// // TODO Auto-generated method stub
	// super.onBackPressed();
	// }
}
