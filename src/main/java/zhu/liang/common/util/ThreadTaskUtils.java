package zhu.liang.common.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/**
 * 通过线程池管理线程数量
 * @author zhengmingzhi
 *
 */
public class ThreadTaskUtils {
	/**
	 * 创建多个线程池
	 */
	private static ConcurrentHashMap<String, ThreadPoolTaskExecutor> executors = new ConcurrentHashMap<String, ThreadPoolTaskExecutor>();
	private static ThreadPoolTaskExecutor taskExecutor = null;
	private static Integer corePoolSize = 10;
	private static Integer maxPoolSize = 20;
	private static Integer queueCapacity = 1000;
    static {
        taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数，队列未满之前可执行线程数是按照 core的size
        taskExecutor.setCorePoolSize(corePoolSize);
        // 最大线程数,原来50 8.10号改为20，队列满的时候执行线程数为max的size
        taskExecutor.setMaxPoolSize(maxPoolSize);
        // 队列最大长度
        taskExecutor.setQueueCapacity(queueCapacity);
        // 线程池维护线程所允许的空闲时间(单位秒)
        taskExecutor.setKeepAliveSeconds(120);
        // 线程池对拒绝任务(无线程可用)的处理策略 ThreadPoolExecutor.CallerRunsPolicy策略 ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃.
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        taskExecutor.initialize();
    }

    public static void run(Runnable runnable) {
        taskExecutor.execute(runnable);
    }
    
    public static ThreadPoolTaskExecutor getExecutor(String key) {
    	if(executors.containsKey(key))
    		return executors.get(key);
    	else {
    		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    		executor.setCorePoolSize(corePoolSize);
    		executor.setMaxPoolSize(maxPoolSize);
    		executor.setQueueCapacity(queueCapacity);
    		executor.setKeepAliveSeconds(120);
    		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    		executor.initialize();
    		executors.put(key, executor);
    		return executor;
    	}
    }
}
