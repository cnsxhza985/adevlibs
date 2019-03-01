package adevlibs.net.postmachine.httpurlconnectionpost;

import java.io.IOException;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import adevlibs.net.postmachine.IPostMachine;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.os.Build;

/**
 * IPostMachine的一个具体实现，主要是使用HttpURLConnection发送POST请求
 * @author wangfan
 *
 */
public class HttpURLConnectionPostRequest implements IPostMachine {
	/**
	 * 链接超时数值
	 */
    private int mConnTimeout = 30000;
    
    /**
     * 读取数据超时
     */
    private int mReadDataTimeout = 30000;    
	
    /**
     * 返回的数据
     */
    private String mstrResult = null;
	
	/**
	 * 默认构造
	 */
	public HttpURLConnectionPostRequest(){}

	/**
	 * <p>HttpURLConnection uses the GET method by default. It will use POST if setDoOutput(true) has been called.
	 *	  Other HTTP methods (OPTIONS, HEAD, PUT, DELETE and TRACE) can be used with setRequestMethod(String).
	 * <p>Proxies
	 * By default, this class will connect directly to the origin server. It can also connect via an HTTP or SOCKS proxy. 
	 * 
	 * To use a proxy, use URL.openConnection(Proxy) when creating the connection.
	 * @param apiAddress	api的完整路径 
	 * @param params		上行参数
	 * @param apiNo			api编号
	 */
	public Object postParamsAndGetData(String apiAddress, Map params, int apiNo) {
		//依照当前SDK，决定是否启用连接池
		disableConnectionReuseIfNecessary();
		
		try {
			URL apiUrl = new URL(apiAddress);
			
			try {
				HttpURLConnection httpUrlConnection = (HttpURLConnection) apiUrl.openConnection();
				
				try{
					httpUrlConnection.setConnectTimeout(mConnTimeout);
					httpUrlConnection.setReadTimeout(mReadDataTimeout);
					httpUrlConnection.setUseCaches(false);
					httpUrlConnection.addRequestProperty("Accept-Encoding", "gzip, deflate");
					httpUrlConnection.setRequestMethod("POST");
					httpUrlConnection.setDoOutput(true);
					httpUrlConnection.setDoInput(true);
					httpUrlConnection.addRequestProperty("Content-Type", URLEncodedUtils.CONTENT_TYPE);
					//we can do auth from cookie or uid
					//httpUrlConnection.setRequestProperty("Cookie", "hello we should add cookie here");
					
		            if (params != null) {
		                List<BasicNameValuePair> pair = new ArrayList<BasicNameValuePair>();
		                Set<Entry<String, String>> entrySet = params.entrySet();
		                for (Entry<String, String> entry : entrySet) {
		                    pair.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		                }
		                OutputStreamWriter writer = new OutputStreamWriter(httpUrlConnection.getOutputStream());
		                String data = URLEncodedUtils.format(pair, "UTF-8");
		                // write parameters
		                writer.write(data);
		                writer.flush();
		                writer.close();
		                writer = null;
		                
		                pair.clear();
		                pair=null;
		                entrySet.clear();
		                entrySet=null;
		            }
					
					int responseCode = httpUrlConnection.getResponseCode();
					if(responseCode == 200){
						mstrResult = EntityUtils.toString(httpUrlConnection);
					}
				}finally{
					httpUrlConnection.disconnect();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return mstrResult;
	}
	
	/**
	 * 依照当前android SDK，决定是否启用连接池
     * <p>from blog http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     * Prior to Froyo, HttpURLConnection had some frustrating bugs. 
     * In particular, calling close() on a readable InputStream could poison the connection pool. 
     * Work around this by disabling connection pooling:
	 */
	private void disableConnectionReuseIfNecessary() {
	    // HTTP connection reuse which was buggy pre-froyo
	    if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
	        System.setProperty("http.keepAlive", "false");
	    }
	}
}
