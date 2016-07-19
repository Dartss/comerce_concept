package worker;

import manager_workers.ContentType;
import manager_workers.ManagerVO;

public class PictureWorker extends AbstractWorker {
	private ContentType type = ContentType.PICTURE;
	
	public PictureWorker(ManagerVO vo) {
		super(vo);
	}
	
	public void run() {
		this.vo.workOnType(type);
	}
}
