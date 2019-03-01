package adevlibs.net.apitasks.apicounter;

public interface IApiCounter {

	/**
	 * num+1
	 */
	public void increase();
	
	/**
	 *num-1
	 */
	public void decrease();
	
	/**
	 * @return num<=0 means all api returned,we should dismiss api dialog
	 */
	public boolean allApiReturn();
	
	/**
	 * reset apiCounter to 0
	 */
	public void reset();
}
