package adevlibs.net.business.apiview;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * DialogApiView是对IApiView的一个实现，采用一个对话框来展示联网ui
 * 
 * @author wangfan
 * 
 */
public class DialogApiView implements IApiView {
	Context mContext = null;

	/* api progress ui */
	private ProgressDialog mPd = null;

	public DialogApiView(Context context) {
		this.mContext = context;

		mPd = new ProgressDialog(context);
	}

	@Override
	public void showApiView() {
		mPd.show();
	}

	@Override
	public void closeApiView() {
		mPd.dismiss();
	}
}
