package com.atgugui.zk;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.ZKDatabase;
import org.jboss.netty.channel.ChildChannelStateEvent;


public class BaseZKChild {
	private static final Logger LOGGER = Logger.getLogger(BaseZKChild.class);
	private static final String CONNECT_STRING = "192.168.198.128:2181";
	private static final int SESSION_TIMEOUT = 50*1000;
	private ZooKeeper ZK = null;
	private List<String> children = null;
	public static final String PATH ="/atguigu";
	public ZooKeeper getZK() throws IOException {
		ZK = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT,new Watcher() {

			@Override
			public void process(WatchedEvent arg0) {
				if(arg0.getType()== EventType.NodeChildrenChanged&&PATH.equals(arg0.getPath())) {
					getChildern(PATH);
				}else {
					getChildern(PATH);
				}
			}} ); 
		return ZK;
	}
	
	public void getChildern(String path) {
		
		try {
			children = ZK.getChildren(path, true);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LOGGER.info(path +"下的子节点是" + children);
	}
	
	//若共有五个服务器
	private final int COUNT_SERVER = 5;
	private int currentServer = 0;//用来记录使用的是第几个服务
	private final String BASE_PATH = "div";
	private int requestPeople = 0;
	public void  gotoServer() throws Exception {
		//来一次服务请求，分配一个服务器
		currentServer = currentServer + 1;
		LOGGER.info(currentServer + "..........");
		for (int i = currentServer; i <= COUNT_SERVER; i++) {
			if (children.contains(BASE_PATH + currentServer)) {
				LOGGER.info("第***"+ ++requestPeople +"个顾客分配到***"+ new String(ZK.getData(PATH+"/"+BASE_PATH + currentServer, false, new Stat())));
				return;
			}else {
				currentServer = currentServer + 1;
			}
		}
		//请求大于服务时重置到初始服务开始轮询
		
		for (int i = 1; i <= COUNT_SERVER; i++) {
			if (children.contains(BASE_PATH + i)) {
				currentServer= i ;
				LOGGER.info("第***"+ ++requestPeople +"个顾客分配到***"+new String(ZK.getData(PATH+"/"+BASE_PATH + currentServer, false, new Stat())));
				return;
			}
		}
		
		
	}
	
	
	
	public static void main(String[] args) throws Exception {
		BaseZKChild child = new BaseZKChild();
		ZooKeeper zk2 = child.getZK();
		Thread.sleep(3000); 
		while(zk2 != null) {
			for (int i = 0; i < 15; i++) {
				Thread.sleep(3000);
				child.gotoServer();
			}
			break;
		}
		
	}
	
}
