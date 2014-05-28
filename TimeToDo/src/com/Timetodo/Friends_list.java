package com.Timetodo;

import java.util.List;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.share.Share;
import com.webservices.Webservice;

public class Friends_list extends FragmentActivity implements
		OnInfoWindowClickListener, OnClickListener {

	SupportMapFragment sp;
	GoogleMap gMap;
	JSONObject JObject1;
	JSONArray jArray;
	String response = "";
	String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_list);

		ImageView img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();

			}
		});

		sp = (SupportMapFragment) getSupportFragmentManager().findFragmentById(
				R.id.simple_map);
		gMap = sp.getMap();
		new get_friends_list().execute("");
	}

	private class get_friends_list extends AsyncTask<String, Void, Boolean> {
		ProgressDialog pd;
		List<Object[]> l;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = ProgressDialog.show(Friends_list.this, "", "Loading...", true,
					false);
			pd.setCancelable(true);
			pd.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {

			Boolean success = false;
			String url_friend_list = "";

			try {

				if (Share.meters == 0) {

					url_friend_list = "http://api.vasundharavision.com/timetodo/?op=friend_list&lat="
							+ String.valueOf(Share.cur_lat)
							+ "&long="
							+ String.valueOf(Share.cur_lng) + "&distance=8000";

					// String url_food_truck_list = Urls.main +
					// "get_event_lat_long&lat="
					// + "38.749437" + "&long=" + "-121.28402";

					Log.e("", "msg link============" + url_friend_list);
				} else {

					Share.meters = Share.meters * 25;

					float meters = Share.meters / 1000;

					url_friend_list = "http://api.vasundharavision.com/timetodo/?op=friend_list&lat="
							+ String.valueOf(Share.cur_lat)
							+ "&long="
							+ String.valueOf(Share.cur_lng)
							+ "&distance="
							+ String.valueOf(meters);

					Log.e("", "msg link============" + url_friend_list);
				}

				response = Webservice.send_link(url_friend_list);

				System.out.println("response" + response);
			} catch (Exception e) {
				System.out.println("Error : " + e.toString());
			}

			return success;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			super.onPostExecute(success);

			try {
				JObject1 = new JSONObject(response);

				String success1 = JObject1.getString("Success");

				Share.user_id.clear();
				Share.user_name.clear();
				Share.user_latitude.clear();
				Share.user_longitude.clear();
				Share.user_distance.clear();
				Share.user_email.clear();

				if (success1.equals("1")) {
					String dictionary = JObject1.getString("friend_list");

					JSONArray current = new JSONArray(dictionary);

					if (current.length() != 0) {

						Log.e("", "msg" + current.length());
						for (int i = 0; i < current.length(); i++) {

							JSONObject e = current.getJSONObject(i);

							String user_id = e.getString("user_id");
							String user_name = e.getString("user_name");
							String user_email = e.getString("user_email");
							String user_latitude = e.getString("user_latitude");
							String user_longitude = e
									.getString("user_longitude");
							String distance = e.getString("distance");

							Share.user_id.add(user_id);
							Share.user_name.add(user_name);
							Share.user_email.add(user_email);
							Share.user_latitude.add(user_latitude);
							Share.user_longitude.add(user_longitude);
							Share.user_distance.add(distance);

							Log.e("", "msg ID====" + user_id);

							Log.e("", "msg" + distance);

						}

					} else {

						AlertDialog.Builder builder = new AlertDialog.Builder(
								Friends_list.this);
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
					setUpMapIfNeeded();

				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();

				Log.e("Error", e.toString());
			}
			pd.dismiss();
			pd.setCancelable(true);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub

	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the

		gMap.clear();
		gMap.setMyLocationEnabled(true);
		gMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Friends_list.this));

		Log.e("", "msg size===========" + Share.user_id.size());
		for (int i = 0; i < Share.user_id.size(); i++) {

			id = Share.user_id.get(i);

			// String distance = String.format("%.2f",
			// Double.parseDouble(Share.event_map_distance.get(i)));

			addMarkerToMap(Double.parseDouble(Share.user_latitude.get(i)),
					Double.parseDouble(Share.user_longitude.get(i)),
					Share.user_name.get(i), Share.user_id.get(i),
					Share.user_distance.get(i));

		}

		LatLng latLng = new LatLng(Share.cur_lat, Share.cur_lng);

		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,
				15);
		if (Share.cur_lat != 0.0 || Share.cur_lng != 0.0) {
			gMap.animateCamera(cameraUpdate);
		}

	}

	private void addMarkerToMap(double lat, double lng,
			final String event_mapTitle, final String map_id,
			final String event_mapDiscribtion) {

		// final LatLng pos = new LatLng(lat, lng);
		// builder.include(pos);
		Log.e("", "msg addmaker method call");

		MarkerOptions markerOptions = new MarkerOptions()
				.position(new LatLng(lat, lng)).title(event_mapTitle)
				.snippet(map_id).snippet(event_mapDiscribtion);

		// markerOptions = markerOptions.icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.ic_launcher));

		gMap.addMarker(markerOptions);

		gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				// TODO Auto-generated method stub
				// gMap.animateCamera(cameraUpdate);
				Log.e("", "msg map click");
				String snippet = marker.getSnippet();

				id = map_id;
				String[] s = null;
				String[] s1 = null;
				if (snippet != null) {
					StringTokenizer strtoken = new StringTokenizer(snippet, "~");

					System.out.println("NEXT  :  " + snippet);

					s = snippet.split("~");

					for (int i = 0; i < s.length; i++)
						System.out.println("NEXT " + String.valueOf(i) + " :  "
								+ s[i]);

				}

				Log.e("", "msg distance" + s[0]);

				// // Share.current_loc_id = s[5];
				// Intent comment_view = new Intent(Friends_list.this,
				// Home.class);
				// // Log.e("tag", "msg" + map_id);
				// // Share.event_food_id = s1[0];
				//
				// startActivityForResult(comment_view, 9);

			}
		});
	}

}
