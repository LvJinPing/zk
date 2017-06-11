package com.atgugui.zk;


 class TestHaha implements Runnable{
	private boolean flag = false;
	
	@Override
	public void run() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		flag = true;
		System.out.println("flag ===="+flag);
	}
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	
}
public class haha{
	public static void main(String[] args) throws InterruptedException {
		TestHaha tv = new TestHaha();
		new Thread(tv).start();
		while(true){
			Thread.sleep(2);
			if(tv.isFlag()){
				break;
			}
		}
	}
}
