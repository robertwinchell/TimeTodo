package com.Timetodo;

import java.util.StringTokenizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements InfoWindowAdapter {

	private final View mWindow;
	private final View mContents;
	Context c;
	private LayoutInflater inflater;

	private SharedPreferences MuscatPrefs;

	public CustomInfoWindowAdapter(Context c) {
		this.c = c;

		MuscatPrefs = PreferenceManager.getDefaultSharedPreferences(c);

		inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mWindow = inflater.inflate(R.layout.custom_info_window, null);
		mContents = inflater.inflate(R.layout.custom_info_contents, null);

	}

	@Override
	public View getInfoWindow(Marker marker) {

		render(marker, mWindow);
		return mWindow;
	}

	@Override
	public View getInfoContents(Marker marker) {

		render(marker, mContents);
		return mContents;
	}

	private void render(Marker marker, View view) {

		Log.e("", "hello testing in 1");

		// ((ImageView) view.findViewById(R.id.badge)).setVisibility(View.GONE);

		String title = marker.getTitle();
		TextView titleUi = ((TextView) view
				.findViewById(R.id.balloon_item_title));
		if (title != null) {
			// Spannable string allows us to edit the formatting of the
			// text.
			// SpannableString titleText = new SpannableString(title);
			// titleText.setSpan(new ForegroundColorSpan(Color.WHITE), 0,
			// titleText.length(), 0);
			titleUi.setText(title);
			Log.e("", "title==" + title);
			// titleUi.setTextColor(Color.WHITE);

		} else {
			titleUi.setText("");
			// titleUi.setTextColor(Color.WHITE);
		}

		String snippet = marker.getSnippet();
		String[] s = null;
		if (snippet != null) {
			StringTokenizer strtoken = new StringTokenizer(snippet, "~");
			System.out.println("NEXT  :  " + snippet);

			s = snippet.split("~");

			for (int i = 0; i < s.length; i++) {
				System.out.println("NEXT " + String.valueOf(i) + " :  " + s[i]);
			}

		}

		TextView snippetUi = ((TextView) view
				.findViewById(R.id.balloon_item_snippet));

		if (snippet != null) {
			// snippetText = new SpannableString(snippet);
			// // snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0,
			// // 10, 0);
			// // snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12,
			// // 21, 0);
			snippetUi.setText(s[0]);
			// snippetUi.setTextColor(Color.WHITE);
			// } else {
			// snippetUi.setText("");
			// snippetUi.setTextColor(Color.WHITE);
		}

//		ImageView img_dir = ((ImageView) view.findViewById(R.id.badge));
//		img_dir.setBackgroundResource(R.drawable.direction);

		mWindow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.e("", "hello testing in 23");

			}
		});

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("", "hello testing in 23");
			}
		});

	}

}
