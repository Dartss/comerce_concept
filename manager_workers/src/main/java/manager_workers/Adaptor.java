package manager_workers;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import worker.AbstractWorker;

public class Adaptor implements Runnable {
	private ManagerVO vo;
	private AdaptorListener listener = (AdaptorListener)Manager.getInstance();
	
	public Adaptor(ManagerVO vo) {
		this.vo = vo;
	}
	
	/**
	 * Creates needed number of threads with workers
	 * and waits while they finish processing.
	 * 
	 * Listener will notify the Manager when job is done  
	 */
	
	public void run() {
		ExecutorService executor = Executors.newFixedThreadPool(vo.getTypes().size());
		for (Map.Entry<ContentType, Boolean> entry : vo.getTypes().entrySet()) {
			AbstractWorker worker = FactoryMethods.getWorker(vo, entry.getKey());
			executor.submit(worker);
		}
		executor.shutdown();
		
		while (!executor.isTerminated()) {}
		
		listener.onWorkersJobDone(vo);
	}
}
