package adevlibs.net.apitasks.apicounter;


/**
 * ApiCounter used to control api progress ui.
 * 
 * @author wangfan
 *
 */
public class ApiCounter implements IApiCounter{
	
	//num
	private int mNum = 0;

	/**
	 * num+1
	 */
	public void increase(){
		mNum++;
	}
	
	/**
	 *num-1
	 */
	public void decrease(){
		mNum--;
	}
	
	/**
	 * @return num<=0 means all api returned,we should dismiss api dialog
	 */
	public boolean allApiReturn(){
		return mNum<=0;
	}
	
	/**
	 * reset apiCounter to 0
	 */
	public void reset(){
		mNum = 0;
	}

}
