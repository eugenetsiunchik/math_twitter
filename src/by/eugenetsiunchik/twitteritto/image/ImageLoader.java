package by.eugenetsiunchik.twitteritto.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import by.eugenetsiunchik.twitteritto.ContextHolder;
import by.eugenetsiunchik.twitteritto.http.HttpManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.util.SparseArray;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageLoader {

	private static final int POOL_SIZE = 5;
	private static final int SIZE_CACHE = 8 * 1024 * 1024;
	protected static final String TAG = ImageLoader.class.getSimpleName();
	private static ImageLoader instance;

	private LruCache<String, Bitmap> mLruCashe;
	private List<ImageLoadCallback> mQueue;
	private ExecutorService mExecutorService;
	private Handler mImageHandler;
	private SparseArray<String> mImageViewUrls;

	public interface ImageLoadCallback {

		String getUrl();

		void onSuccess(Bitmap bm);

		void onError(Exception e);
	}

	public ImageLoader() {
		mImageHandler = new Handler();
		mExecutorService = Executors.newFixedThreadPool(POOL_SIZE);
		mQueue = Collections
				.synchronizedList(new ArrayList<ImageLoadCallback>());

		mImageViewUrls = new SparseArray<String>();
		mLruCashe = new LruCache<String, Bitmap>(SIZE_CACHE) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				int size = value.getRowBytes() * value.getHeight();
				Log.d(TAG, value + " bitmap size");
				return size;
			}
		};
	}

	public static ImageLoader getInstance() {
		if (instance == null) {
			instance = new ImageLoader();
		}
		return instance;
	}

	public void bind(final String url, final ImageView imageView,
			final BaseAdapter adapter) {
		mImageViewUrls.put(imageView.hashCode(), url);
		// Log.d(TAG, "Thumbnails url " + url);
		Bitmap bitmap = mLruCashe.get(url);
		{
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
				return;
			}
		}
		for (ImageLoadCallback callback : mQueue) {
			if (callback.getUrl().equals(url)) {
				mQueue.remove(callback);
				mQueue.add(0, callback);
				proceed();
				return;
			}
		}
		mQueue.add(0, new ImageLoadCallback() {

			@Override
			public void onSuccess(Bitmap bm) {
				String currentUrl = mImageViewUrls.get(imageView.hashCode());
				if (currentUrl.equals(getUrl())) {
					imageView.setImageBitmap(bm);
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onError(Exception e) {
				Log.d(TAG, e + "");
			}

			@Override
			public String getUrl() {
				return url;
			}
		});

		proceed();
	}

	public void bind(final String url, final ImageView imageView) {
		mImageViewUrls.put(imageView.hashCode(), url);
		 Log.d(TAG, "Thumbnails url " + url);
		Bitmap bitmap = mLruCashe.get(url);
		{
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
				return;
			}
		}
		for (ImageLoadCallback callback : mQueue) {
			if (callback.getUrl().equals(url)) {
				mQueue.remove(callback);
				mQueue.add(0, callback);
				proceed();
				return;
			}
		}
		mQueue.add(0, new ImageLoadCallback() {

			@Override
			public void onSuccess(Bitmap bm) {
				String currentUrl = mImageViewUrls.get(imageView.hashCode());
				if (currentUrl.equals(getUrl())) {
					imageView.setImageBitmap(bm);
					
				}
			}

			@Override
			public void onError(Exception e) {
				Log.d(TAG, e + "");
			}

			@Override
			public String getUrl() {
				return url;
			}

		});

		proceed();
	}

	private void proceed() {
		if (mQueue.isEmpty()) {
			return;
		}
		final ImageLoadCallback callback = mQueue.remove(0);
		mExecutorService.execute(new Runnable() {

			public void run() {
				boolean cachedInCasheDir = false;
				String url = callback.getUrl();
				String name = md5(url) + ".jpeg";
				final Bitmap bitmap;
				try {
					if (cheakFileCache(name)) {
						Log.d(TAG, "bitmap in cache, getting");
						cachedInCasheDir = true;
						bitmap = getCachedBitmap(name);
					} else {
						Log.d(TAG, "bitmap not in cache, loading");
						bitmap = HttpManager.getInstance().loadAsBitmap(url);
					}
					if (bitmap != null) {
						Log.d(TAG, "bitmap is near, cacheing");
						addCasheBitmap(url, name, bitmap, cachedInCasheDir);
						mImageHandler.post(new Runnable() {

							@Override
							public void run() {
								callback.onSuccess(bitmap);
							}
						});
						return;
					} else {
						throw new Exception("Some errors = (");
					}
				} catch (final Exception e) {
					mImageHandler.post(new Runnable() {

						@Override
						public void run() {
							callback.onError(e);
						}
					});
				}
			}

			private void addCasheBitmap(String url, String name, Bitmap bitmap,
					boolean cachedInCasheDir) throws Exception {
				mLruCashe.put(url, bitmap);
				Log.d(TAG, mLruCashe.size() + " lru size");
				if (!cachedInCasheDir) {
					FileOutputStream os = null;
					try {
						File file = new File(ContextHolder.getInstance()
								.getContext().getCacheDir(), name);
						file.createNewFile();
						os = new FileOutputStream(file);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
					} catch (FileNotFoundException e) {
						throw e;
					} finally {
						if (os != null) {
							try {
								os.close();
							} catch (Exception e) {
								throw new Exception(
										"Error while closing stream: "
												+ e.getMessage());
							}
						}
					}
				}

			}

			protected Bitmap getCachedBitmap(String name) throws Exception {
				FileInputStream fis = null;
				try {
					File file = new File(ContextHolder.getInstance()
							.getContext().getCacheDir(), name);
					fis = new FileInputStream(file);
					Bitmap bitmap = BitmapFactory.decodeStream(fis);
					return bitmap;
				} catch (FileNotFoundException e) {
					throw new FileNotFoundException(
							"Bitmap's file not found in file cashe");
				} finally {
					if (fis != null) {
						try {
							fis.close();
						} catch (Exception e) {
							throw new Exception("Error while closing stream: "
									+ e.getMessage());
						}
					}
				}
			}

			protected boolean cheakFileCache(String name) {
				File file = new File(ContextHolder.getInstance().getContext()
						.getCacheDir(), name);
				if (file.exists() && file.length() != 0) {
					return true;
				}
				return false;
			}

			private String md5(String s) {
				try {
					// Create MD5 Hash
					MessageDigest digest = java.security.MessageDigest
							.getInstance("MD5");
					digest.update(s.getBytes());
					byte messageDigest[] = digest.digest();
					// Create Hex String
					StringBuffer hexString = new StringBuffer();
					for (int i = 0; i < messageDigest.length; i++)
						hexString.append(Integer
								.toHexString(0xFF & messageDigest[i]));
					return hexString.toString();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				return "";

			}
		});
	}
}
