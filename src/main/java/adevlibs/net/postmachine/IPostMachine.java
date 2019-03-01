package adevlibs.net.postmachine;

import java.util.Map;

/**
 * 向Server发起请求，并返回Server给出的数据的基类
 * @author wangfan
 *
 */
public interface IPostMachine{
	/**
	 * 向Server发起请求，并返回Server给的结果
	 * @param apiAddress
	 * @param params
	 * @param apiNo
	 * @return
	 */
	public Object postParamsAndGetData(String apiAddress,Map params,int apiNo);
}
