package adevlibs.net.apitasks.tasks;

import java.util.HashMap;

import adevlibs.net.apitasks.AsyncPostMachineTask;
import adevlibs.net.apitasks.apicounter.IApiCounter;
import adevlibs.net.apitasks.taskcallback.ITaskCallBack;
import adevlibs.net.postmachine.IPostMachine;
import adevlibs.net.postmachine.formdatapost.ImgUploadFormPost;

public class AsyncImgUploadTask<T> extends AsyncPostMachineTask<T> {

	public AsyncImgUploadTask(String apiAdd, int apiNumber, HashMap param,
			IApiCounter apiCounter, ITaskCallBack taskCallBack) {
		super(apiAdd, apiNumber, param, apiCounter, taskCallBack);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IPostMachine getPostMachine() {
		return new ImgUploadFormPost();
	}

}
