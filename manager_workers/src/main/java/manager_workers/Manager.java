package manager_workers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.owlike.genson.Genson;

import redis.clients.jedis.Jedis;

public class Manager implements AdaptorListener {
	public final static int THREAD_LIMIT = 10;
	public final static String REDIS_LIST = "queue_of_jsons";
	
	private static Manager instance = null;
	
	private Logger logger = Logger.getLogger("Manager");
	
	private Jedis jedis = Main.jedis;
	
	// Stores amount of available threads for each API type
	private HashMap<ContentType, Integer> availableThreadsMap = new HashMap<ContentType, Integer>();
	
	private Manager() {
		this.availableThreadsMap = new HashMap<ContentType, Integer>();
		
		availableThreadsMap.put(ContentType.VIDEO, THREAD_LIMIT);
		availableThreadsMap.put(ContentType.PICTURE, THREAD_LIMIT);
		availableThreadsMap.put(ContentType.SOUND, THREAD_LIMIT);
	}
	
	public static Manager getInstance() {
		if (instance == null) {
			instance = new Manager();
		}
		return instance;
	}
	
	private ManagerVO parseJSONToVO(String json) {
		ManagerVO vo;
		Genson gson = new Genson();
		
		vo = gson.deserialize(json, ManagerVO.class);
		return vo;
	}
	
	// Checks if there are available threads
	private boolean threadsAreAvailable(ManagerVO vo) {
		for (Map.Entry<ContentType, Boolean> entry : vo.getTypes().entrySet()) {
			if (availableThreadsMap.get(entry.getKey()) == 0 ) {
				return false;
			}
		}
		return true;
	}
	
	private void decrementAvailableThreadsCounter(ManagerVO vo) {
		for (Map.Entry<ContentType, Boolean> entry : vo.getTypes().entrySet()) {
			int curentAvailable = availableThreadsMap.get(entry.getKey());
			availableThreadsMap.put(entry.getKey(), curentAvailable - 1);
		}
	}
	
	private void incrementAvailableThreadsCounter(ManagerVO vo) {
		for (Map.Entry<ContentType, Boolean> entry : vo.getTypes().entrySet()) {
			int curentAvailable = availableThreadsMap.get(entry.getKey());
			availableThreadsMap.put(entry.getKey(), curentAvailable + 1);
		}
	}
	
	public void startReading() {
		//Get objects from redis queue
		String jsonObj = jedis.lpop(REDIS_LIST);
		
		while ( jsonObj != null) {						
			ManagerVO vo = parseJSONToVO(jsonObj);
			
			logger.log(Level.INFO, "VO created: " + vo);
			
			if (threadsAreAvailable(vo)) {
				decrementAvailableThreadsCounter(vo);
				
				//Send object on processing in adaptor
				Thread adaptor = new Thread(new Adaptor(vo));
				adaptor.start();				
			} else {

				// return VO to the queue if there 
				// are no available threads now
				jedis.rpush(REDIS_LIST, jsonObj);
			}
			
			jsonObj = jedis.lpop(REDIS_LIST);
		}
	}
	
	//Calls when adaptor finishes processing on the VO
	public void onWorkersJobDone(ManagerVO handeledVO) {
		incrementAvailableThreadsCounter(handeledVO);
		System.out.println(handeledVO.toString());
	}
}