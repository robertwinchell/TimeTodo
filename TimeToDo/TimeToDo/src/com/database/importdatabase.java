package com.database;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.timetodo.Splash;

public class importdatabase {

	@SuppressWarnings("unused")
	public static void copyDataBase() {
		try {

			OutputStream databaseOutputStream = new FileOutputStream(
					"/data/data/com.timetodo/databases/timetodo.sql");

			InputStream databaseInputStream;

			byte[] buffer = new byte[1024];
			int length;

			databaseInputStream = Splash.databaseInputStream1;

			while ((length = databaseInputStream.read(buffer)) > 0) {
				databaseOutputStream.write(buffer);
			}
			databaseInputStream.close();
			databaseOutputStream.flush();
			databaseOutputStream.close();

		} catch (Exception e) {
			// TODO: handle exception
			// Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
	}
}
