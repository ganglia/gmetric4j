package info.ganglia.gmetric4j;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class DefaultGScheduler implements GScheduler {
	
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
	
	boolean daemon = true;
	
	private ThreadFactory daemonThreadGroup = new ThreadFactory() {
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName("Ganglia Sampling Thread");
			t.setDaemon(daemon);
			return t;
		}
	};

	public DefaultGScheduler() {
	}

	public void scheduleAtFixedRate(Runnable command, long initialDelay,
			long period, TimeUnit unit) {
		executor.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	public void onStart() {
		executor.setThreadFactory(daemonThreadGroup);
	}

	public void onStop() {
		executor.shutdown();
	}
}
