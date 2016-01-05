package org.linphone.newphone.tools;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * http网络连接请求
 * @author ty
 * @time 2015/5/12
 */
public class HttpRequest {

	private static final String TAG = "HttpApi";
	private static final Boolean logFlag = false;
	public static HttpClient httpClient;

	/**
	 * 获取全局HttpClient实例
	 * @return HttpClient实例
	 */
	public static synchronized HttpClient getHttpClient() {
		if (httpClient == null) {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params,
					HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);

			HttpProtocolParams
					.setUserAgent(
							params,
							"Mozilla/5.0 (Linux; U; Android 2.2.1; en-us; Nexus One Build/FRG83) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
			ConnManagerParams.setTimeout(params, 1000);
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);

			httpClient = new DefaultHttpClient(conMgr, params);
		}
		return httpClient;
	}

	/***
	 * 获取HttpPost
	 * @param url
	 * @param params
	 * @return HttpPost
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpPost createHttpPost(String url, List<NameValuePair> params)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = null;
		if (params == null) {
			httpPost = new HttpPost(url);
		} else {
			httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		}
		if (logFlag) {
			System.out.println("urlpost : " + httpPost.getURI().toString());
		}
		return httpPost;
	}

	/***
	 * 获取HttpGet
	 * @param url
	 * @param params
	 * @return HttpGet
	 */
	public static HttpGet createHttpGet(String url, List<NameValuePair> params) {
		System.out.println(params);
		String query = null;
		HttpGet httpGet = null;
		if (params == null) {
			httpGet = new HttpGet(url);
		} else {
			query = URLEncodedUtils.format(params, HTTP.UTF_8);
			httpGet = new HttpGet(url + "?" + query);
		}
		if (logFlag) {
			System.out.println("protocol : " + httpGet.getURI().toString());
			System.out.println(url + "?" + query);
		}
		return httpGet;
	}

	/***
	 * 连接服务端，并返回数据
	 * 如果返回失败，有6次连接机会
	 * @param httpRequest
	 * @return String
	 * @throws Exception
	 */
	public static String executeHttpRequestWithRetry(HttpRequestBase httpRequest)
			throws Exception {
		int retry = 1;
		int count = 0;
		while (count < retry) {
			count += 1;
			try {
				HttpResponse response = getHttpClient().execute(httpRequest);
				String src = EntityUtils.toString(response.getEntity(), "UTF-8");
				return src;
			} catch (Exception e) {
				if (count < retry) {
					Log.w(TAG, "Retry http : " + e.toString());
				} else {
					Log.w(TAG, "All retries failed");
				}
			}
		}
		return "errer";
	}

}
