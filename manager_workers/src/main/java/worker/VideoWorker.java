package worker;

import manager_workers.ContentType;
import manager_workers.ManagerVO;

public class VideoWorker extends AbstractWorker {
	private ContentType type = ContentType.VIDEO;

	public VideoWorker(ManagerVO vo) {
		super(vo);
	}

	public void run() {
		this.vo.workOnType(type);
	}
}
