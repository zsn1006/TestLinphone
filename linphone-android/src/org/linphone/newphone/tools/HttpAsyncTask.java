package org.linphone.newphone.tools;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/***
 * 用线程AsyncTask封装读取网络数据
 * 
 * @author ty
 * @time 2015/5/12
 */
public class HttpAsyncTask extends AsyncTask<String, Void, String> {

	private Context mContext = null;
	private String url = null;
	private List<NameValuePair> list = null;
	private HttpCallBack callback = null;
	private Boolean mIsGet = true;
	private Boolean mNetConnect = true;
	private Boolean mBackResult = false;

	/**
	 * @param mContext
	 * @param url
	 * @param list
	 */
	public HttpAsyncTask(Context mContext, String url, List<NameValuePair> list,
			HttpCallBack jsonCallback, Boolean isGet) {
		this.mContext = mContext;
		this.url = url;
		this.list = list;
		this.callback = jsonCallback;
		mIsGet = isGet;
	}
	
	public void onSetBackData(Boolean flag) {
		mBackResult = flag;
	}

	@Override
	protected String doInBackground(String... params) {
		String srcStr = "error";
		if (mNetConnect) {
			try {
				if (mIsGet) {
					srcStr = HttpRequest.executeHttpRequestWithRetry(HttpRequest.createHttpGet(url,
							list));
				} else {
					srcStr = HttpRequest.executeHttpRequestWithRetry(HttpRequest.createHttpPost(
							url, list));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return srcStr;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (!result.contentEquals("error")) {
			try {
				String aaa = "\",}";
				int index = result.indexOf(aaa);
				if (index > 0 && index < result.length()-2) {
					result = result.substring(0, index+1) + result.substring(index + 2);
				}
				JSONObject ret = new JSONObject(result);
				String State = ret.getString("State");
				if (State != null && !State.isEmpty() && State.equals("1")) {
					callback.setResult(result);
				} else {
					if (mBackResult) {
						callback.setResult(result);
					} else {
						callback.setResult("errorData");
						Toast.makeText(mContext, "获取数据失败！", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				callback.setResult("errorData");
				Toast.makeText(mContext, "获取数据失败！", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(mContext, "连接服务出错。请检查网络，重新登录。", Toast.LENGTH_SHORT).show();
			callback.setResult("errorNet");
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mNetConnect = true;
		if (mContext != null && !MethodNet.isNetworkConnected(mContext)) {
			mNetConnect = false;
			return;
		}
	}

	/***
	 * 回调函数,数据加载完毕后调用该函数
	 * 
	 * @author ty
	 * 
	 */
	public interface HttpCallBack {
		public void setResult(String result);
	}

}
