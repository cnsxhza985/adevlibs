package adevlibs.net.apitasks.taskcallback;

/**
 * 没有Context而引发的异常
 * 
 * @author wangfan
 * 
 */
public class NoContextException extends Exception {
	public NoContextException() {
		super();
	}

	public NoContextException(String msg) {
		super(msg);
	}

	public NoContextException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public NoContextException(Throwable throwable) {
		super(throwable);
	}

}
