package manager_workers;

import worker.AbstractWorker;
import worker.PictureWorker;
import worker.SoundWorker;
import worker.VideoWorker;

public class FactoryMethods {
	public static AbstractWorker getWorker(ManagerVO vo, ContentType type) {
		AbstractWorker worker = null;
				
		if (type == ContentType.PICTURE) {
			worker = new PictureWorker(vo);
		} else if (type == ContentType.SOUND) {
			worker = new SoundWorker(vo);
		} else {
			worker = new VideoWorker(vo);
		}
		
		return worker;
	}
}
