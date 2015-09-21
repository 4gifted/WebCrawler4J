package org.buaa.crawler;


public abstract class CrawlerThread implements Runnable{

	//thread info 
	String status;//给上层标识用，不是有这个实例就一定是运行状态的，也可能是在某个队列里
	
	String ThreadId;//线程的唯一标示，用来查找和定位thread
	
	String describeText;//描述性字段，可以写也可以不写
	
	String name;        //爬虫名字
	
	//task info 用来标明所属的任务，对相同任务保留去重能力
	protected String sDate;
	
	protected String eDate;
	
	protected String keyword;
	
	public void Setting(String sDate, String eDate, String keyword){
		this.sDate = sDate;
		this.eDate = eDate;
		this.keyword = keyword;
	}
	
	/**
	 * 用于中断线程时，需要进行的一些清理工作, 在管理器终止该线程前，会调用该方法， 如果没有需要清理的内容，可以留空
	 */
	protected abstract void cleanUp();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	};
}
