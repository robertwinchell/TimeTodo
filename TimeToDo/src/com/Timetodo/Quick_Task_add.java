package com.Timetodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.share.Share;

public class Quick_Task_add extends Activity implements OnClickListener {

	Button btn_quick_task;
	TextView txt_task_name;

	ArrayAdapter<String> VesselListAdapter, VesselListAdaptertask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.quick_task);
		btn_quick_task = (Button) findViewById(R.id.btn_quick_task);
		btn_quick_task.setOnClickListener(this);

		txt_task_name = (TextView) findViewById(R.id.txt_task_name);

		txt_task_name.setOnClickListener(this);

		Share.quick_task.clear();

		Share.quick_task.add("I am Hungry");
		Share.quick_task.add("I Want to Shop");
		Share.quick_task.add("I Want to Hospital ");
		Share.quick_task.add("I Want to ATM ");
		Share.quick_task.add("I Want to Bank ");
		Share.quick_task.add("I Want to Restaurant ");
		Share.quick_task.add("I Want to Mall ");
		Share.quick_task.add("I Want to Hotel ");
		Share.quick_task.add("I Want to Hostel ");
		Share.quick_task.add("I Want to Garden ");
		Share.quick_task.add("I Want to Beach ");

		VesselListAdaptertask = new ArrayAdapter<String>(Quick_Task_add.this,
				android.R.layout.select_dialog_singlechoice, Share.quick_task);

		Share.task_reminder.clear();
		Share.task_reminder.add("Restaurant");
		Share.task_reminder.add("Mall");
		Share.task_reminder.add("Hospital");
		Share.task_reminder.add("ATM");
		Share.task_reminder.add("Bank");
		Share.task_reminder.add("Restaurant");
		Share.task_reminder.add("Mall");
		Share.task_reminder.add("Hotel");
		Share.task_reminder.add("Hostel");
		Share.task_reminder.add("Garden");
		Share.task_reminder.add("Beach");

		VesselListAdapter = new ArrayAdapter<String>(Quick_Task_add.this,
				android.R.layout.select_dialog_singlechoice,
				Share.task_reminder);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == btn_quick_task)

		{
			Intent intent = new Intent(Quick_Task_add.this, Home.class);
			startActivity(intent);

		} else if (v == txt_task_name) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					Quick_Task_add.this);
			//
			// // builder.setTitle("Vessel list");
			builder.setTitle("Choose your quick task");
			builder.setPositiveButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			builder.setAdapter(VesselListAdaptertask,
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog,
								final int item) {

							String money_type = Share.quick_task.get(item);

							Share.quick_task_home.clear();
							Share.quick_task_home_type.clear();
							Share.quick_task_home_type.add(Share.task_reminder
									.get(item));

							Log.e("",
									"msg  Type==="
											+ Share.task_reminder.get(item));

							// Log.e("", )
							txt_task_name.setText(Share.quick_task.get(item));

							Share.quick_task_home.add(Share.quick_task
									.get(item));
							dialog.cancel();
							// // Share.moneytpye = money_type;
							//
						}
					});

			final AlertDialog dialog = builder.create();
			dialog.show();

		}

	}

}
