package concurrent;

public class FineRunnable implements Runnable {

	private Thread t;
	
	public FineRunnable() {
		t = new Thread(this);
	}
	
	@Override
	public void run() {
		System.out.println("I'm running");
	}

	public void start(){
		t.start();
	}
	
	/**
	 * 
	 * 原先的写法
	 * FineRunnable r = new FineRunnable();
	 * Thread t = new Thread(r);
	 * t.start();
	 */
	public static void main(String[] args) {
		FineRunnable r = new FineRunnable();
		r.start();
	}
}
