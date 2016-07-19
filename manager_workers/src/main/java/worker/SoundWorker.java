package worker;

import manager_workers.ContentType;
import manager_workers.ManagerVO;

public class SoundWorker extends AbstractWorker {
	private ContentType type = ContentType.SOUND;
	
	public SoundWorker(ManagerVO vo) {
		super(vo);
	}
	
	public void run() {
		this.vo.workOnType(type);
	}

	


}
