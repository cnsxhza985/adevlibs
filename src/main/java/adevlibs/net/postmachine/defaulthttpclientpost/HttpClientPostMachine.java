package adevlibs.net.postmachine.defaulthttpclientpost;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import adevlibs.net.postmachine.IPostMachine;

/**
 * IPostMachine的一个具体实现，主要作用是通过DefaultHttpClient以POST的方式向Server发送数据,
 * @author wangfan
 *
 */
public class HttpClientPostMachine implements IPostMachine {
	private static final String XNAME = "HttpClientPostRequest";
	
	public static final boolean DBG = true;		//启用Log
	public static boolean USE_COOKIE = false;	//启用Cookie
	public static boolean USE_USER_AGENT = false; //启用UserAgent
	
	public static String sSET_COOKIE = null;	
	public static String VALUE_USER_AGENT = "ANDROID"+ "#"+ Build.MANUFACTURER + "#" + Build.MODEL+ "#" + Build.VERSION.SDK+ "#" + Build.VERSION.RELEASE;
	
	
	/**
	 * 编码格式	【和server交互数据，sever给出的数据通过 ENCODING 指定的类型进行解码】
	 */
	public static final String ENCODING_UTF8 = "UTF-8";
	
	/**
	 * 初始化Cookie的API编号
	 */
	public static int sCookie_API_NO = -1;
	
	/**
	 * 建立链接超时时间
	 */
	private int mConnectionTimeOut = 8*000;
	
	/**
	 * Socket超时时间
	 */
	private int mSocketTimeOut = 10*000;
	
	public HttpClientPostMachine() {
	}

	/**
	 * 发送请求参数并且获返回结果
	 * @param apiAddress 完整的api接口路径
	 * @param params	上行请求的参数
	 * @param apiNo     API编号
	 */
	public Object postParamsAndGetData(String apiAddress, Map params, int apiNo) {
		// wanted result String
		String jStrResult = null;

		// create httpclient this way , it works better,especially first time connect to server,it cost less time.
		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
		final SSLSocketFactory socketFactory = SSLSocketFactory
				.getSocketFactory();
		socketFactory
				.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		registry.register(new Scheme("https", socketFactory, 443));
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
		HttpParams clientParams = new BasicHttpParams();
		HttpProtocolParams.setVersion(clientParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(clientParams, HTTP.UTF_8);

        // set connection and socket timeout
        HttpConnectionParams.setConnectionTimeout(clientParams, mConnectionTimeOut);
        HttpConnectionParams.setSoTimeout(clientParams, mSocketTimeOut);
		DefaultHttpClient httpClient = new DefaultHttpClient(clientParams);

		// create httpPost
		HttpPost httpPost = new HttpPost(apiAddress);

		//set USER_AGENT
		if (USE_USER_AGENT) {
			httpPost.addHeader("User-Agent",VALUE_USER_AGENT);
		}

		if(USE_COOKIE && !TextUtils.isEmpty(sSET_COOKIE)){
			// set Cookie
			httpPost.addHeader("Cookie", sSET_COOKIE);
		}

		//set other specified params into HttpHeader
		httpPost.setEntity(getHttpEntityFromMap(params, apiNo));

		try {
			HttpResponse response = httpClient.execute(httpPost);
			
			if (response.getStatusLine().getStatusCode() == 200) {
                if (DBG) {
                    Log.i(XNAME, "---GOT 200---");
                }
                
				// update Cookie
				if (USE_COOKIE) {
					//get cookie from login response
					sSET_COOKIE = getCookieFromHttpResponse(response);
				} 

				// server returned response
				HttpEntity resultEntity = response.getEntity();

				// server returned str Data
				jStrResult = EntityUtils.toString(resultEntity,ENCODING_UTF8);
			}
		} catch (Exception e) {
			if (DBG) {
				Log.e("HttpClientPostRequest", apiNo + " exc= " + e.toString());
			}
		} finally {
			// close current HttpClient
			httpClient.getConnectionManager().shutdown();

			httpPost = null;
			httpClient = null;
		}

		return jStrResult;
	}
	
	/**
	 * 从HttpResponse中获取Cookie
	 * 
	 * @param response
	 * @return
	 */
	public static String getCookieFromHttpResponse(HttpResponse response) {
		Header[] heads = response.getAllHeaders();
		String cookievalue = null;
		if (heads != null && heads.length > 0) {
			for (int i = 0; i < heads.length; i++) {
				String name = heads[i].getName();
				String value = heads[i].getValue();
				if(!TextUtils.isEmpty(name)){
					name = name.trim();
					// 从server 回来的Cookie 是被放置在Set-Cookie中的
					if (TextUtils.equals(name, "Set-Cookie")) {
						if (!TextUtils.isEmpty(value)) {
							cookievalue = value;
						}
					}
				}
			}
		}
		return cookievalue;
	}
	
    /**
     * 将HashMap中的key value放入HttpEntity
     * @param params
     * @param apiNo
     * @return
     */
	public static HttpEntity getHttpEntityFromMap(Map<String, String> params,
			int apiNo) {
		if (params != null && !params.isEmpty()) {
			List<BasicNameValuePair> pair = new ArrayList<BasicNameValuePair>();
			Set<Entry<String, String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				pair.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}

			try {
				return new UrlEncodedFormEntity(pair, ENCODING_UTF8);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}

	}
}
