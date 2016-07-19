package manager_workers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManagerVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger("ConceptVO");
	
	// Define what API can handle this object
	private HashMap<ContentType, Boolean> contentTypes = new HashMap<ContentType, Boolean>();
	
	public ManagerVO() {}
	
	public void addContentType(ContentType type) {		
		// Not allowed to duplicate content types
		
		if (!this.contentTypes.containsKey(type)) {
			this.contentTypes.put(type, false);
		} else {
			logger.log(Level.ALL, "This object already has such content type");
		}
	}
	
	public HashMap<ContentType, Boolean> getTypes() {
		return this.contentTypes;
	}
	
	// Simulates jobs for the workers
	public synchronized void workOnType(ContentType type) {
		if (this.contentTypes.containsKey(type)) {
			this.contentTypes.put(type, true);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
 			logger.log(Level.ALL, "Wrong content type. Worker should handle: ");
 			for (Map.Entry<ContentType, Boolean> entry : contentTypes.entrySet() ) {
 				logger.log(Level.ALL, "---> " + entry.getKey().toString());
 			}
 		}
	}
	
	public HashMap<ContentType, Boolean> getContentTypes() {
		return contentTypes;
	}

	public void setContentTypes(HashMap<ContentType, Boolean> contentTypes) {
		this.contentTypes = contentTypes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<ContentType, Boolean> entry : getTypes().entrySet()) {
			builder.append(entry.getKey() + " : " + entry.getValue() + "\n");
		}
		return builder.toString();
	}	
}
