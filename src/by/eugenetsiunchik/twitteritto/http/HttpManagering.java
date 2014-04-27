package by.eugenetsiunchik.twitteritto.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpManagering {

	private static HttpManagering instance;
	
	private DefaultHttpClient client;
	
	public static final int SO_TIMEOUT = 20000;

	/* Constant encoding for http client. */
	private static final String UTF_8 = "UTF-8";
	
	private HttpManagering() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, UTF_8);
		params.setBooleanParameter("http.protocol.expect-continue", false);
		HttpConnectionParams.setConnectionTimeout(params, SO_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);
		

		// REGISTERS SCHEMES FOR BOTH HTTP AND HTTPS
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		final SSLSocketFactory sslSocketFactory = SSLSocketFactory
				.getSocketFactory();
		sslSocketFactory
				.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		registry.register(new Scheme("https", sslSocketFactory, 443));
		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(
				params, registry);
		client = new DefaultHttpClient(manager, params);
	}
	
	public static HttpManagering getInstance() {
		if (instance == null) {
			instance = new HttpManagering();
		}
		return instance;
	}
	
	public String loadAsString(String url) throws IOException {
		return loadAsString(new HttpGet(url));
	}
	
	public JSONObject loadAsJSONObject(String url) throws IOException, JSONException {
		return new JSONObject(loadAsString(url));
	}
	
	public JSONArray loadAsJSONArray(String url) throws IOException, JSONException {
		return new JSONArray(loadAsString(url));
	}
	
	public String loadAsString(HttpRequestBase request) throws IOException {
		InputStream content = null;
		BufferedReader reader = null;
		try {
			HttpResponse response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				String entityValue = null;
					entityValue = EntityUtils.toString(response.getEntity());
				throw new IOException(response.getStatusLine().getReasonPhrase()+" "+entityValue + " " + statusCode);
			}
			content = response.getEntity().getContent();
			Header contentEncoding = response.getFirstHeader("Content-Encoding");
			if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(content)), 8192);
			} else {
				reader = new BufferedReader(
						new InputStreamReader(content), 8192);
			}
			StringBuilder sb = new StringBuilder();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line)
					.append(System.getProperty("line.separator"));
				}
			} catch (IOException e) {
				throw e;
			}
			String value = sb.toString();
			content.close();
			return value;
		} catch (IOException e) {
			throw e;
		} finally {
			if (content != null) {
				try {
					content.close();
				} catch (IOException e) {
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO: handle exception
				}
			}
		}
	}

	public HttpClient getClient() {
		// TODO Auto-generated method stub
		return client;
	}
	
}
