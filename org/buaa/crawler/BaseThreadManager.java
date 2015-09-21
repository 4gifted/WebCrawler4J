package org.buaa.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.buaa.javaStudy.synchronizedTest;


/*
 * 管理thread的运行，负责启动和管理各thread的开启，关闭
 * 
 * 知道CrawlerThread，并且知道Thread和class的对应关系
 * 
 * 知道 Task 但不关心其中细节，只是负责将Task 内 Thread 关系的字段传递给Thread，
 * 启动Thread之后，其他操作由Thread自己处理
 * 
 */

public class BaseThreadManager <T extends CrawlerThread>{

	Map<String, Thread> threadQueue = new Hashtable<String, Thread>();
	Map<String, T> crawlerQueue = new Hashtable<String, T>();
	
	/** 
	 * 代理执行 线程，并将加入到管理队列中
	 * @param target 指定爬虫实例
	 * @return id    唯一id
	 */
	public synchronized String runCrawler(T target){
		Thread t = new Thread(target);
		String id = UUID.randomUUID().toString();
		t.start();
		threadQueue.put(id, t);
		crawlerQueue.put(id, target);
		return id;
	}

	/**
	 * 
	 * 获得指定线程状态
	 * @param threadId 需要查询的id
	 * @return Map<String, String> 状态信息
	 */
	public  Map<String, String> getCrawlerStatus(String threadId){
		Map<String, String> status = new HashMap<String, String>();
		Thread t = threadQueue.get(threadId);
		T crawler = crawlerQueue.get(threadId);
		status.put("name", t.getName());
		status.put("status", t.getState().toString());
		status.put("id", t.getId()+"");
		status.put("isAlive", t.isAlive()+"");
		// more info come from crawler
		//TODO
		return status;
	}
	
	/**
	 * 停止指定 threadId 的线程
	 * @param threadId
	 */
	public synchronized void stopCrawler(String threadId){
		Thread t = threadQueue.get(threadId);
		T crawler = crawlerQueue.get(threadId);
		if(t == null){
			return;
		}
		if(t.isAlive()){
			crawler.cleanUp();//清理工作
			t.interrupt();
			t.stop();
		}
	}
	
	/**
	 * 得到指定的thread 引用
	 * @param id
	 * @return
	 */
	protected Thread getThread(String id){
		return threadQueue.get(id);
	}
	
	

	/**
	 * 获取正在运行中的线程数
	 * @return 运行线程数
	 */
	public int getRunningThread(){
		clean();
		return threadQueue.size();
	}
	
	
	/**
	 * 清理线程相关资源，方便jvm回收死亡进程的资源
	 */
	public synchronized void clean(){
		List<String> removeList = new ArrayList<String>();
		for(String key : threadQueue.keySet()){
			Thread t = threadQueue.get(key);
			if(!t.isAlive()){
				removeList.add(key);
			}
		}
		for(String key: removeList){
			threadQueue.remove(key);
			crawlerQueue.remove(key);
		}
	}

	
	public static void main(String[] args){
		
		System.out.println("main start");
		BaseThreadManager<XXXThread> crawler = new BaseThreadManager<>();
		XXXThread xx = new XXXThread();
		String id = crawler.runCrawler(xx);	
		System.out.println(crawler.getCrawlerStatus(id));
		try {
			
			crawler.getThread(id).join(3000);
			System.out.println("now I want to stop the thread!");
			crawler.stopCrawler(id);	
			System.out.println(crawler.getCrawlerStatus(id));
			
			crawler.getThread(id).join();
			System.out.println(crawler.getCrawlerStatus(id));
			System.out.println("main end");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
}

class XXXThread extends CrawlerThread{

	@Override
	public void run() {
		for(int i = 0; i < 5; i++){
			try {
				Thread.sleep(1000);
				System.out.println( "counting ..." +i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void cleanUp() {
		// TODO Auto-generated method stub
		
	}
}


