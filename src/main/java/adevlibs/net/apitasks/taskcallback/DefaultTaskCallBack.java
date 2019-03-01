package adevlibs.net.apitasks.taskcallback;

import java.util.HashMap;
import java.util.Map;

import adevlibs.net.JSONParser;
import adevlibs.net.apitasks.apicounter.ApiCounter;
import adevlibs.net.apitasks.apicounter.IApiCounter;
import adevlibs.net.business.IAsimoListener;
import adevlibs.net.business.apiview.DialogApiView;
import adevlibs.net.business.apiview.IApiView;
import adevlibs.net.business.clazz.BaseInfo;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

/**
 * 1：主要的作用是按照业务需求以及约定解析返回的数据 2：将从网络层回来数据的分发到业务层
 * 
 * @author wangfan
 * 
 */
public class DefaultTaskCallBack<T> implements ITaskCallBack {
	/**
	 * 持有的当前上上下文，主要用来进行联网UI展示
	 */
	private Context mContext = null;
	private static Map<Context, IApiView> sApiViewMap = new HashMap<Context, IApiView>();
	private static Map<Context, IApiCounter> sApiCounterMap = new HashMap<Context, IApiCounter>();

	/**
	 * api对应的编号
	 */
	private int mApiNo = -1;

	/**
	 * 业务层的API数据回调接口
	 */
	private IAsimoListener mAsimoListener = null;

	/**
	 * 业务层期望的数据结构【解析模板】
	 */
	private Class<T> mClazz = null;

	/**
	 * 从Serer获取的JSON数据，被解析出来后对应的Object
	 */
	private T classResult = null;

	public DefaultTaskCallBack(Context context,Class<T> template, int apiNo,
			IAsimoListener asimoListener) throws Exception {
		this.mApiNo = apiNo;
		this.mAsimoListener = asimoListener;
		this.mClazz = template;
		this.mContext = context;
		
		if(context == null){
			throw new NoContextException("Context used in LongForTask must not be null");
		}
	}

	@Override
	public void disPatchServerData(Object data) {
		// 应该判断 mAsimoListener 是否还在Listener池中。
		if (mAsimoListener != null) {
			if (data != null) {
				String jStr = (String) data;

				// 数据符合JSON格式规范
				if (!TextUtils.isEmpty(jStr) && jStr.startsWith("{")
						&& jStr.endsWith("}")) {
					// 根据解析模板将server返回的数据解析出来
					// classResult = RubbishCodes.parseJStr2Object(mClazz,
					// jStr);
					classResult = JSONParser.getInstance().parser(mClazz, jStr);

					BaseInfo baseInfo = (BaseInfo) classResult;
					if (null != baseInfo) {
						if (baseInfo.status == IAsimoListener.API_FAIL_TYPE_STATUS_1) {
							mAsimoListener.onDataArrival(classResult, mApiNo);
						} else if (baseInfo.status == IAsimoListener.API_FAIL_TYPE_STATUS_0) {
							mAsimoListener.onDataFailed(data, mApiNo,
									IAsimoListener.API_FAIL_TYPE_STATUS_0);
						} else if (baseInfo.status == IAsimoListener.API_FAIL_TYPE_STATUS_10000) {
							// 通知上层获取数据失败
							mAsimoListener.onDataFailed(data, mApiNo,
									IAsimoListener.API_FAIL_TYPE_STATUS_10000);
						} else {
							// 通知上层获取数据失败
							mAsimoListener.onDataFailed(data, mApiNo,
									IAsimoListener.API_FAIL_TYPE_UNKNOW);
						}
					} else {
						// 通知上层获取数据失败
						mAsimoListener.onDataFailed(data, mApiNo,
								IAsimoListener.API_FAIL_TYPE_UNKNOW);
					}
				}
			} else {
				// 告知上层，获取数据失败
				mAsimoListener.onDataFailed(data, mApiNo,
						IAsimoListener.API_FAIL_TYPE_NULL);
			}
		}
	}

	@Override
	public void showCurrTaskApiUi() {
		// mAsimoListener.onNetShow();
		if (mContext instanceof Activity
				&& !((Activity) mContext).isFinishing()) {
			if (!sApiViewMap.containsKey(mContext)) {
				sApiViewMap.put(mContext, new DialogApiView(mContext));
			}
			IApiView apiview = sApiViewMap.get(mContext);
			if(apiview!=null){
				apiview.showApiView();
			}
		}
	}

	@Override
	public void hideCurrTaskApiUi() {
		// mAsimoListener.onNetDismiss();
		if (sApiViewMap.containsKey(mContext)) {
			IApiView apiview = sApiViewMap.get(mContext);
			if(apiview!=null){
				apiview.closeApiView();
			}
			sApiViewMap.remove(mContext);
		}
		
		//清除APICounter
		removeApiCounter(mContext);
	}
	
	public static IApiCounter getApiCounter(Context context){
		if(!sApiCounterMap.containsKey(context)){
			IApiCounter apiCounter = new ApiCounter();
			sApiCounterMap.put(context, apiCounter);
		}
		
		return sApiCounterMap.get(context);
	}
	
	private static void removeApiCounter(Context context){
		if(sApiCounterMap.containsKey(context)){
			IApiCounter apiCounter = new ApiCounter();
			sApiCounterMap.remove(context);
		}
	}
}
