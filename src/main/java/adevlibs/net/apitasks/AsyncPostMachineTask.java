package adevlibs.net.apitasks;

import java.util.HashMap;

import adevlibs.net.apitasks.apicounter.IApiCounter;
import adevlibs.net.apitasks.taskcallback.ITaskCallBack;
import adevlibs.net.postmachine.IPostMachine;
import android.os.AsyncTask;

/**
 * AsyncTask的子类，采用短连接的形式和Server进行String类型的数据交互。
 * 
 * @author wangfan
 *
 * @param <T>
 */
public abstract class AsyncPostMachineTask<T> extends AsyncTask<Void, Void, Object> implements
		ITask {

	private final String XNAME = "AsyncStringTask";
	
	/**
	 * 将要请求的API对于的全路径地址
	 */
	private String mApiAddress = null;
	
	/**
	 * 将要请求的API对于的编号
	 */
	private int mApiNo = -1;
	
	/**
	 * 上行参数
	 */
	private HashMap mParams = null;
	
	/**
	 * API计数器
	 */
	private IApiCounter mApiCounter = null;
	
	/**
	 * 上层的回调接口
	 */
	private ITaskCallBack mTaskCallBack = null;
	
	/**
	 * AsyncStringTask 的构造函数
	 * @param apiAdd		api对应的完整地址
	 * @param apiNumber		api对应的编号
	 * @param param			上行参数
	 * @param apiCounter	api计数器
	 * @param apiTaskSet	当前正在运行的所有的API的集合
	 * @param listener		业务层的回调接口
	 * @param clazz			解析server返回数据的解析模板
	 */
	public AsyncPostMachineTask(String apiAdd, int apiNumber,HashMap param,
			IApiCounter apiCounter,
			ITaskCallBack taskCallBack) {
		this.mApiAddress = apiAdd;
		this.mApiNo = apiNumber;
		this.mParams = param;
		this.mApiCounter = apiCounter;
		this.mTaskCallBack = taskCallBack;
		
		//将本实例加入统计容器
		sTaskPool.add(this);
	}

	protected void onPreExecute() {
		super.onPreExecute();

		/**
		 * API计数器增加一
		 */
		mApiCounter.increase();

		/**
		 *	通知上层展示联网UI
		 */
		mTaskCallBack.showCurrTaskApiUi();
	}

	/**
	 * 继承自AsyncTask的方法，用来执行任务
	 */
	protected Object doInBackground(Void... params) {
		/**
		 * 生成一个IPostMachine的实例
		 */
		IPostMachine postmachine = getPostMachine();
//		IPostMachine postmachine = new HttpClientPostMachine();
//		IPostMachine postmachine = new HttpURLConnectionPostRequest();
//		IPostMachine postmachine = new ImgUploadFormPost();
		
		/**
		 * 通过IPostMachine发送上行参数，获取返回的结果
		 */
		return postmachine.postParamsAndGetData(mApiAddress,mParams, mApiNo);
	}

	protected abstract IPostMachine getPostMachine();

	/**
	 * 处理doinBackground返回的结果
	 */
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		
		//如果Task还在Task记录中，说明Task没有被丢弃
		if(sTaskPool.contains(this)){
			//将获取的数据传递给上层
			mTaskCallBack.disPatchServerData(result);
		}
		
		//api计数器减一
		mApiCounter.decrease();
		//如果所有的API都已经运行完毕则通知上层隐藏联网UI
		if (mApiCounter.allApiReturn()) {
			mTaskCallBack.hideCurrTaskApiUi();
		}
		
		//从记录容器中移除当前对象
		sTaskPool.remove(this);
		
		releaseResource();
	}
	
	/**
	 * 获取当前API的编号
	 */
	public int getTaskNo() {
		return this.mApiNo;
	}	

	/**
	 * 启动当前Task
	 */
	public void startTask() {
		this.execute((Void)null);
	}

	/**
	 * 丢弃当前API
	 */
	public void dropTask() {
		//将当前Task从Task容器之中移除
		sTaskPool.remove(this);

		//one api in a group drop,all will drop. just hide net ui 
		mApiCounter.reset();
		if(mTaskCallBack != null){
			mTaskCallBack.hideCurrTaskApiUi();
		}
		
		releaseResource();

		// 尝试将当前的AsyncTask结束【按照Android文档说明，只是尝试结束，并不能保证一定能结束掉】
		this.cancel(true);
	}

	/**
	 * 释放对上层资源的引用
	 */
	protected void releaseResource() {
		//不要持有上层引用
		if(mApiAddress!=null){
			mApiAddress = null;
		}
		if(mParams!=null){
			mParams = null;
		}		
		if(mApiCounter!=null){
			mApiCounter = null;
		}
		if(mTaskCallBack!=null){
			mTaskCallBack = null;
		}
	}
}
