package com.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class Dialog extends Activity implements OnClickListener {

	static LayoutInflater linf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		linf = (LayoutInflater) getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		linf = LayoutInflater.from(Dialog.this);
	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
