package adevlibs.net.apitasks;

import java.util.HashSet;

public interface ITask {
	/**
	 * 记录当前运行的ITask实例的容器
	 */
	public static HashSet<ITask> sTaskPool = new HashSet<ITask>();
	
	/**
	 * 获取task的编号
	 * @return
	 */
	public int getTaskNo();
	
	/**
	 * 启动task
	 */
	public void startTask();
	
	/**
	 * 丢弃task
	 */
	public void dropTask();
}
