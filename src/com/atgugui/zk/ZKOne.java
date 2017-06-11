package com.atgugui.zk;

import org.apache.zookeeper.ZooKeeper;

public class ZKOne extends BaseZK {
	public static  ZKOne noWatch = new ZKOne();
	
	public static void main(String[] args) throws Exception {
		String path = "/atguigu";
		ZooKeeper zooKeeper = noWatch.getZK();
		if (zooKeeper.exists(path, false)==null) { //zookeeper 不允许设置相同路径
			noWatch.createZNode(path, "12345上山打老虎");
		}
		//多次watcher
		noWatch.getZNodeDataOnWatcher2(path);
		//一次 watcher
		//noWatch.getZNodeData(path);
		Thread.sleep(Long.MAX_VALUE);
		noWatch.closeZK();
	}

}
