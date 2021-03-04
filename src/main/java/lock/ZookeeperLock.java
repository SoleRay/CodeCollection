package lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ZookeeperLock implements Lock {
	private static Logger logger = LoggerFactory.getLogger(ZookeeperLock.class);

	private static final String ZOOKEEPER_IP_PORT = "192.168.0.11:2181";
	private static final String LOCK_PATH = "/LOCK";

	private ZkClient client = new ZkClient(ZOOKEEPER_IP_PORT, 1000, 1000, new SerializableSerializer());

	private ThreadLocal<String> beforePath = new ThreadLocal<>();// 当前请求的节点前一个节点
	private ThreadLocal<String> currentPath = new ThreadLocal<>();// 当前请求的节点

	// 判断有没有LOCK目录，没有则创建
	public ZookeeperLock() {
		if (!this.client.exists(LOCK_PATH)) {
			this.client.createPersistent(LOCK_PATH);
		}
	}

	public boolean tryLock() {

		// 如果currentPath为空则为第一次尝试加锁，第一次加锁赋值currentPath
		if (currentPath.get() == null || currentPath.get().length() <= 0) {
			// 创建一个临时顺序节点
			String path = this.client.createEphemeralSequential(LOCK_PATH + '/', "lock");
			currentPath.set(path);
			System.out.println(Thread.currentThread().getName()+": create currentPath = " + currentPath.get());
		}else {
			System.out.println(Thread.currentThread().getName()+": exist currentPath = " + currentPath.get());
		}



		// 获取所有临时节点并排序，临时节点名称为自增长的字符串如：0000000400
		List<String> childrens = this.client.getChildren(LOCK_PATH);
		Collections.sort(childrens);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (currentPath.get().equals(LOCK_PATH + '/' + childrens.get(0))) {// 如果当前节点在所有节点中排名第一则获取锁成功
			System.out.println(Thread.currentThread().getName()+": get Lock!!!!");
			return true;
		} else {// 如果当前节点在所有节点中排名中不是排名第一，则获取前面的节点名称，并赋值给beforePath
			int wz = Collections.binarySearch(childrens, currentPath.get().substring(6));
			String path = LOCK_PATH + '/' + childrens.get(wz - 1);
			beforePath.set(path);
			System.out.println(Thread.currentThread().getName()+": doesn't get Lock, get beforePath = " + beforePath.get());
		}



		return false;
	}

	public void unlock() {
		// 删除当前临时节点
		client.delete(currentPath.get());
		currentPath.remove();
		beforePath.remove();

	}

	public void lock() {
		if (!tryLock()) {
			waitForLock();
			lock();
		} else {
			logger.info(Thread.currentThread().getName() + " 获得分布式锁！");
		}
	}

	private void waitForLock() {
		String currentThreadName = Thread.currentThread().getName();
		CountDownLatch cdl = new CountDownLatch(1);
		IZkDataListener listener = new IZkDataListener() {
			public void handleDataDeleted(String dataPath) throws Exception {
				logger.info(Thread.currentThread().getName() + ":捕获到DataDelete事件！---------------------------");
				if (cdl != null) {
					cdl.countDown();
					System.out.println(currentThreadName +": watched path has countDown...");
				}
			}

			public void handleDataChange(String dataPath, Object data) throws Exception {

			}
		};

		// 给排在前面的的节点增加数据删除的watcher
		this.client.subscribeDataChanges(beforePath.get(), listener);
		System.out.println(Thread.currentThread().getName()+": has subscribed beforePath = " + beforePath.get());


		// 有可能执行到这里的时候，获得锁的那个节点已经执行完毕，导致节点消失
		if (this.client.exists(beforePath.get())) {
			try {
				/**如果刚好在await之前，前一个节点业务执行完毕，率先触发了countDown呢?
				 * CountDownLatch 底层是用LockSupport来实现的，所以如果先countDown，后await，await是不会阻塞的。
				 * */
				System.out.println(Thread.currentThread().getName()+": subscribed beforePath  = " + beforePath.get() + " still exist, will await for countDown...");
				cdl.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println(Thread.currentThread().getName()+": beforePath = " + beforePath.get() + " is disappeared，maybe that business has done at this point");
		}
		this.client.unsubscribeDataChanges(beforePath.get(), listener);
		System.out.println(Thread.currentThread().getName()+": unsubscribe beforePath = " + beforePath.get());
	}

	// ==========================================
	public void lockInterruptibly() throws InterruptedException {

	}

	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	public Condition newCondition() {
		return null;
	}
}
