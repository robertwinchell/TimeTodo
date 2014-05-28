package com.share;

import java.util.ArrayList;

public class Share {

	public static String google_place_key = "AIzaSyB1SnXrsBQtZqpZu5_XYHs2-j15wT6FlGQ";

	public static ArrayList<String> task_id = new ArrayList<String>();
	public static ArrayList<String> task_name = new ArrayList<String>();

	public static ArrayList<String> task_des = new ArrayList<String>();
	public static ArrayList<String> task_date = new ArrayList<String>();
	public static ArrayList<String> task_type = new ArrayList<String>();
	public static ArrayList<String> task_reminder = new ArrayList<String>();
	public static ArrayList<String> quick_task = new ArrayList<String>();
	public static ArrayList<String> task_reminder_time = new ArrayList<String>();
	public static ArrayList<String> task_time = new ArrayList<String>();

	public static ArrayList<String> check_id = new ArrayList<String>();
	public static ArrayList<String> quick_task_home = new ArrayList<String>();
	public static ArrayList<String> quick_task_home_type = new ArrayList<String>();
	public static ArrayList<String> user_id = new ArrayList<String>();
	public static ArrayList<String> user_name = new ArrayList<String>();
	public static ArrayList<String> user_email = new ArrayList<String>();
	public static ArrayList<String> user_latitude = new ArrayList<String>();
	public static ArrayList<String> user_longitude = new ArrayList<String>();
	public static ArrayList<String> user_distance = new ArrayList<String>();

	public static String time;
	public static Double cur_lat = 0.0, cur_lng = 0.0;
	public static Double lat = 0.0, lng = 0.0;

	public static float meters;
	public static int width = 0;

	public static boolean task_edit = false;

	public static String notification = "";
	public static boolean qucktask = false;
	public static boolean cal_task = false;

}