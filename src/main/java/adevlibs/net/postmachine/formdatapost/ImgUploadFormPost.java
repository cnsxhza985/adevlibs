package adevlibs.net.postmachine.formdatapost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import adevlibs.net.postmachine.IPostMachine;

/**
 * 
 * @author wangfan
 *  this class is used to upload img from form fomat.
 *  1:we create a ImgUploadParams Object 
 *  2:we must put ImgUploadParams Object into Map use the UploadImgFormPost.IMGUPLOADPARAMS
 *  3:then we use this post machine as others
 *
 */
public class ImgUploadFormPost implements IPostMachine {
	
	/**
	 * the special key for ImgUploadParams Object in map
	 */
	public static final String IMGUPLOADPARAMS = "ImgUploadParams";
	
	@Override
	public Object postParamsAndGetData(String apiAddress, Map parameters, int apiNo) {
		String responseContent = null;
		
		ImgUploadParams imgUploadParams = (ImgUploadParams) parameters.get(IMGUPLOADPARAMS);
		if(imgUploadParams==null){
			return null;
		}
		
		HttpURLConnection urlConn = null;
		
        try {
			URL url = new URL(apiAddress);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setConnectTimeout(5000);
			urlConn.setReadTimeout(5000);
			urlConn.setUseCaches(false);
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.addRequestProperty("Accept-Encoding", "gzip, deflate");
			//urlConn.setRequestProperty("connection", "keep-alive");
            //urlConn.setRequestProperty("Cookie", "");
			
            String boundary = getBoundary();
            urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            boundary = "--" + boundary;	
            
            //create common api part
            String commonApiParams = creatCommonApiFormStr(parameters, boundary);     
            
            //create img disposition head
            String imgDisposition = creatImgDispositon(imgUploadParams, boundary);
            
            byte[] ps = commonApiParams.getBytes();
            byte[] fileDiv = imgDisposition.getBytes();
            byte[] endData = ("\r\n" + boundary + "--\r\n").getBytes();
            
            //Form
//            StringBuffer formal = new StringBuffer();
//            formal.append(commonApiParams);
//            formal.append(imgDisposition);
//            formal.append(imgUploadParams.getmData());
//            formal.append(endData);   
            
            //write bytes step by step
            OutputStream os = urlConn.getOutputStream();
            os.write(ps);
            os.write(fileDiv);
            os.write(imgUploadParams.getmData());
            os.write(endData);
            os.flush();
            //release resources
            os.close();   
            ps = null;
            fileDiv = null;
            endData = null;
            imgUploadParams.recycle();
            
            //get response from connection
            responseContent = readResult(urlConn);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(urlConn!=null){
				urlConn.disconnect();
			}
		} 
		
        return responseContent;
	}

	/**
	 * creat seperate string for the entire form
	 * @return
	 */
	protected String getBoundary() {
		//String boundary = "-----------------------------114975832116442893661388290519";
		return String.valueOf(System.currentTimeMillis());
	}
	
	/**
	 * create common api params in form
	 * @param parameters
	 * @param boundary
	 * @return common api params in form
	 */
	protected String creatCommonApiFormStr(Map parameters, String boundary) {
		//common API params
		StringBuffer params = new StringBuffer();
		if (parameters != null) {
		    for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext();) {
		        String name = iter.next();
		        String value = (String) parameters.get(name);
		        // String value = parameters.getString(name);
		        params.append(boundary + "\r\n");
		        params.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
		        // params.append(URLEncoder.encode(value, "UTF-8"));
		        params.append(value);
		        params.append("\r\n");
		    }
		}
		return params.toString();
	}	

	/**
	 * creat img disposition head used in form
	 * @param imgUploadParams
	 * @param boundary
	 * @return img disposition head used in form
	 */
	protected String creatImgDispositon(ImgUploadParams imgUploadParams,
			String boundary) {
		StringBuilder sb = new StringBuilder();
		sb.append(boundary);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"" + imgUploadParams.getmName() + "\"; filename=\"" + imgUploadParams.getmFilename()
		        + "\"\r\n");
		sb.append("Content-Type: " + imgUploadParams.getmContenttype() + "\r\n\r\n");
		return sb.toString();
	}

	/**
	 * read result from connection
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
    private String readResult(HttpURLConnection connection) throws IOException {
        StringBuilder inStreamBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()), 1000);
        for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
            inStreamBuilder.append(line);
        }
        bufferedReader.close();
        return inStreamBuilder.toString();
    }	
    
}
