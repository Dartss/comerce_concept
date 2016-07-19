package worker;

import manager_workers.ManagerVO;

public abstract class AbstractWorker implements Runnable {
	protected ManagerVO vo;
	
	public AbstractWorker(ManagerVO vo) { 
		this.vo = vo;
	}
}
