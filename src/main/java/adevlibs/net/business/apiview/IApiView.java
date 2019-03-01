package adevlibs.net.business.apiview;

/**
 * android联网时展示的UI的抽象
 * 
 * @author wangfan
 * 
 */
public interface IApiView {
	
	/**
	 * 展示联网UI
	 */
	public void showApiView();
	
	/**
	 * 隐藏联网UI
	 */
	public void closeApiView();
}

