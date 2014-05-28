package com.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;

import android.util.Log;

public class Webservice {

	public static String get_Response(String url_call) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;

		try {

			url = new URI(url_call.replace(" ", "%20"));

			Log.e("WEBCALL", "URL : " + url);

			HttpPost httppost = new HttpPost(url);
			HttpResponse httpResponse = null;

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						3);

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				httpResponse = httpclient.execute(httppost);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				// this is what we extended for the getting the response string
				// which we going to parese for out use in database //

				System.out.println("WEB_RESPONSE : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;
	}

	// public static String webServiceCall(String strUrl, String operation)
	// throws IOException {
	// URL url = null;
	// String response = null;
	//
	// try {
	// url = new URL(strUrl);
	//
	// System.out.println("-----------------start---------------"
	// + operation + " response------------------------------");
	// Log.e("", "URL : " + url);
	// InputSource doc = new InputSource(url.openStream());
	// response = convertStreamToString(doc.getByteStream());
	// Log.e("", "RESPONSE : " + response);
	// System.out.println("------------------end-----------------"
	// + operation + " response------------------------------");
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return response;
	// }

	public static String webServiceCall(String strUrl) throws IOException {
		URL url = null;
		String response = null;

		try {
			url = new URL(strUrl);

			System.out.println("-----------------start---------------"
					+ " response------------------------------");
			Log.e("", "URL : " + url);
			InputSource doc = new InputSource(url.openStream());
			response = convertStreamToString(doc.getByteStream());
//			Log.e("", "RESPONSE : " + response);
//			System.out.println("------------------end-----------------"
//					+ " response------------------------------");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public static String convertStreamToString(InputStream is)
			throws IOException {

		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}

	}

	// public static String Send_deviceinfo(String device_token,
	// String device_register_id) {
	//
	// HttpClient httpclient = new DefaultHttpClient();
	// String response = null;
	// URI url;
	// try {
	//
	// // String s = "http://lb-alert.nl/api/accounts/save-token?user_id="
	// // + Share.user_id + "&type=" + "2" + "&token=" + device_token
	// // + "&regid=" + device_register_id;
	//
	// // url = new URI(s.replace(" ", "%20"));
	//
	// Log.e("my webservice", "My webservice : " + url);
	//
	// HttpPost httppost = new HttpPost(url);
	// HttpResponse httpResponse = null;
	//
	// try {
	//
	// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
	// 3);
	//
	// httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	//
	// // Execute HTTP Post Request
	// httpResponse = httpclient.execute(httppost);
	//
	// response = EntityUtils.toString(httpResponse.getEntity(),
	// "UTF-8");
	//
	// // this is what we extended for the getting the response string
	// // which we going to parese for out use in database //
	//
	// System.out
	// .println("get my local centre response : " + response);
	//
	// } catch (ClientProtocolException e) {
	// // TODO Auto-generated catch block
	// Log.e("Error : ", "Client Protocol exception");
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// Log.e("Error : ", "IO Exception");
	//
	// }
	// } catch (URISyntaxException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// return response;
	// }
	
	public static String send_link(String link) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		String login1 = "";
		// String login2 = "";
		try {

			login1 = link;

			// login2= login1.replace("%2F", "/");
			//
			// login2 = login2.replace("%3D%0A", "=");

			url = new URI(login1.replace(" ", "%20"));

			Log.e("my webservice", "Login : " + url);
			System.out.println(url);
			HttpPost httppost = new HttpPost(url);
			HttpResponse httpResponse = null;

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						3);

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				httpResponse = httpclient.execute(httppost);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				// this is what we extended for the getting the response string
				// which we going to parese for out use in database //

				System.out.println("sendMail= : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;
	}

}
