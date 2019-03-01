package adevlibs.net.business;

/**
 * 网络请求的数据通过本接口回传给上层
 * 
 * @author wangfan 网络请求对应的回调接口
 */
public interface IAsimoListener {
	/**
	 * 业务逻辑中定义的数据状态 【意味着 server 已经获取了上行的请求参数，但是认为我们的请求参数又误】
	 */
	public static final int API_FAIL_TYPE_STATUS_0 = 0;

	/**
	 * 业务逻辑中定义的数据状态 【意味着 server 已经获取了上行的请求参数，且正常的给出了客户端所需要的数据】
	 */
	public static final int API_FAIL_TYPE_STATUS_1 = 1;

	/**
	 * 业务逻辑中定义的数据状态 【意味着 server 认为用户身份不合发】
	 */
	public static final int API_FAIL_TYPE_STATUS_10000 = 10000;

	/**
	 * 业务逻辑中定义的数据状态 【意味着Server返回的数据为NULL】
	 */
	public static final int API_FAIL_TYPE_NULL = -1;

	/**
	 * 业务逻辑中定义的数据状态 【意味着未知的联网错误】
	 */
	public static final int API_FAIL_TYPE_UNKNOW = -2;

	/**
	 * 网络数据成功获取之后的回调方法
	 * 
	 * @param data
	 *            获取的数据
	 * @param apiNo
	 *            api对应的编号
	 */
	public void onDataArrival(Object data, int apiNo);

	/**
	 * 网络请求失败之后的回调方法
	 * 
	 * @param data
	 *            获得的数据
	 * @param apiNo
	 *            api对于的编号
	 * @param type
	 *            失败类型
	 */
	public void onDataFailed(Object data, int apiNo, int type);
}
