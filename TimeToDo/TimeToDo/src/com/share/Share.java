package com.share;

import java.util.ArrayList;

public class Share {

	public static String google_place_key = "AIzaSyCCDmXDcCgEylduuFCyH7kjwTMqqr_vtLk";

	public static ArrayList<String> task_id = new ArrayList<String>();
	public static ArrayList<String> task_name = new ArrayList<String>();

	public static ArrayList<String> task_des = new ArrayList<String>();
	public static ArrayList<String> task_date = new ArrayList<String>();
	public static ArrayList<String> task_type = new ArrayList<String>();
	public static ArrayList<String> task_reminder = new ArrayList<String>();

	public static ArrayList<String> task_reminder_time = new ArrayList<String>();
	public static ArrayList<String> task_time = new ArrayList<String>();

	public static String time;
	public static Double cur_lat = 0.0, cur_lng = 0.0;
	public static Double lat = 0.0, lng = 0.0;

	public static int width = 0;

	public static boolean task_edit = false;

}