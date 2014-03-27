package com.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnAlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
	    WakeIntentService.acquireStaticLock(context);
	    
	    String name = intent.getStringExtra("name");
	    String days = intent.getStringExtra("days");
		
	    Intent i = new Intent(context, AlarmService.class);
	    i.putExtra("name", name);
	    i.putExtra("days", days);
	    context.startService(i);
	    
	    
	}}
