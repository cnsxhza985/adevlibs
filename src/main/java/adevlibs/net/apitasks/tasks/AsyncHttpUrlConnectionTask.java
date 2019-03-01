package adevlibs.net.apitasks.tasks;

import java.util.HashMap;

import adevlibs.net.apitasks.AsyncPostMachineTask;
import adevlibs.net.apitasks.apicounter.IApiCounter;
import adevlibs.net.apitasks.taskcallback.ITaskCallBack;
import adevlibs.net.postmachine.IPostMachine;
import adevlibs.net.postmachine.httpurlconnectionpost.HttpURLConnectionPostRequest;

public class AsyncHttpUrlConnectionTask<T> extends AsyncPostMachineTask<T> {

	public AsyncHttpUrlConnectionTask(String apiAdd, int apiNumber,
			HashMap param, IApiCounter apiCounter, ITaskCallBack taskCallBack) {
		super(apiAdd, apiNumber, param, apiCounter, taskCallBack);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IPostMachine getPostMachine() {
		return new HttpURLConnectionPostRequest();
	}

}
