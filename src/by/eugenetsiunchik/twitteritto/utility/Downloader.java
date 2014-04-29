package by.eugenetsiunchik.twitteritto.utility;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import by.eugenetsiunchik.twitteritto.ContextHolder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Downloader {

	private static final String TAG = Downloader.class.getSimpleName();
	private static byte[] response;

	public static byte[] imageToByteArray(String urlString) {

		URL url;
		try {
			url = new URL(urlString);

			InputStream in = new BufferedInputStream(url.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			response = out.toByteArray();
			out.close();
			in.close();

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return response;

	}

	public static void saveImageOnFileSystem(String url) throws IOException {

		FileOutputStream fos = new FileOutputStream(ContextHolder.getInstance().getContext().getCacheDir());
		fos.write(imageToByteArray(url));
		fos.close();

	}

	public static Bitmap getBitmap(String name) throws Exception {
		FileInputStream fis = null;
		try {
			File file = new File(ContextHolder.getInstance().getContext().getCacheDir(), name);
			fis = new FileInputStream(file);
			Bitmap bitmap = BitmapFactory.decodeStream(fis);
			return bitmap;
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Bitmap's file not found in file cashe");
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					throw new Exception("Error while closing stream: " + e.getMessage());
				}
			}
		}
	}

}
