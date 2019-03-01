package adevlibs.net.apitasks.taskcallback;


public interface ITaskCallBack {
	/**
	 * 分发Server返回的数据
	 */
	public void disPatchServerData(Object data);
	
	/**
	 * 展示当前APITask 对应的UI
	 */
	public void showCurrTaskApiUi();
	
	/**
	 * 隐藏当前APITask对应的UI
	 */
	public void hideCurrTaskApiUi();
}
