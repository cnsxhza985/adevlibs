package adevlibs.net.postmachine.formdatapost;

import android.text.TextUtils;

public class ImgUploadParams {
	String mName = null; 			//name				eg.img
    String mFilename = null;			//filename			eg.222.png
    String mContenttype = null;		//contenttype		eg. PNG		
    byte[] mData = null;				//dispositon_data	图片对应的byte数组
    
    public ImgUploadParams(String name,String fileName,String contenttype,byte[] img) throws Exception{
    		this.mName = name;
    		this.mFilename = fileName;
    		this.mContenttype = contenttype;
    		this.mData = img;
    		
    		boolean checkResult = checkParams();
    		if(!checkResult){
    			throw new Exception("file upload params should not be null");  
    		}
    }
    
    public String getmName() {
		return mName;
	}

	public String getmFilename() {
		return mFilename;
	}

	public String getmContenttype() {
		return mContenttype;
	}

	public byte[] getmData() {
		return mData;
	}    
	
    /**
     * got suffix type from img name
     * @param imgName
     * @return
     */
    public String parseContentType(String imgName) {
        String contentType = "image/jpg";
        String name = imgName.toLowerCase();
        if (name.endsWith(".jpg"))
            contentType = "image/jpg";
        else if (name.endsWith(".png"))
            contentType = "image/png";
        else if (name.endsWith(".jpeg"))
            contentType = "image/jpeg";
        else if (name.endsWith(".gif"))
            contentType = "image/gif";
        else if (name.endsWith(".bmp"))
            contentType = "image/bmp";
        else
            throw new RuntimeException("Unsupport File type : " + name );
        return contentType;
    }	
	
	
	/**
	 * check nessary params for fileupload
	 * @return
	 */
	public boolean checkParams(){
		boolean checkResult = true;
		if(TextUtils.isEmpty(mName)){
			checkResult = false;
		}
		
		if(TextUtils.isEmpty(mFilename)){
			checkResult = false;
		}
		
		if(TextUtils.isEmpty(mContenttype)){
			checkResult = false;
		}
		
		if(mData==null || mData.length<=0){
			checkResult = false;
		}
		
		return checkResult;
	}	
	
	/**
	 * 释放内部持有的资源
	 */
	public void recycle(){
		 mName = null; 				//name
	     mFilename = null;			//filename
	     mContenttype = null;		//contenttype		
	     mData = null;				//dispositon_data			
	}

}
