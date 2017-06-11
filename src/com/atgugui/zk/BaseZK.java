package com.atgugui.zk;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class BaseZK {
	private static final Logger LOGGER = Logger.getLogger(BaseZK.class);
	private static final String CONNECT_STRING = "192.168.198.128:2181";
	private static final int SESSION_TIMEOUT = 50*1000;
	private ZooKeeper ZK = null;
	
	
	public ZooKeeper getZK() throws IOException {
		ZK = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT,null ); 
		return ZK;
	}
	public void createZNode(String path,String data) throws KeeperException, InterruptedException {
		ZK.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	public String getZNodeData(String path) throws KeeperException, InterruptedException {
		byte[] data = ZK.getData(path, false, new Stat());
		String result = new String(data);
		LOGGER.info("newData"+result);
		return result;
	}
	public String getZNodeDataOnWatcher(final String path) throws KeeperException, InterruptedException {
		byte[] data = ZK.getData(path, new Watcher() {
			@Override
			public void process(WatchedEvent arg0) {
				try {
					
					getZNodeData(path);
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			}}, new Stat());
		String result = new String(data);
		LOGGER.info("oldData"+result);
		return result;
	}
	public String getZNodeDataOnWatcher2(final String path) throws KeeperException, InterruptedException {
		byte[] data = ZK.getData(path, new Watcher() {
			
			@Override
			public void process(WatchedEvent arg0) {
				try {
					getZNodeDataOnWatcher2(path);
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			}}, new Stat());
		String result = new String(data);
		LOGGER.info("newData"+result);
		return result;
	}
	
	public void closeZK() {
		if (ZK != null) {
			try {
				ZK.close();
			} catch (InterruptedException e) {
				LOGGER.info(e);
				e.printStackTrace();
			}
		}
	}
}

