package manager_workers;

import java.util.Random;

import com.owlike.genson.Genson;

import redis.clients.jedis.Jedis;

public class Main {
	public static Jedis jedis;

	public static void main(String[] args) {
		jedis = new Jedis("localhost");
		createRedisQueue();
		Manager.getInstance().startReading();
	}
	
	private static void createRedisQueue() {
		Genson genson = new Genson();
		jedis.flushAll();
		int rand;
		
		for ( int i = 0; i < 50; i++ ) {
			ManagerVO preVo = new ManagerVO();
			preVo.addContentType(ContentType.PICTURE);
			
			rand = randInt(1, 3);
			
			if (rand == 1) {
				preVo.addContentType(ContentType.PICTURE);
			}
			
			rand = randInt(1, 3);
			
			if ( rand == 2) {
				preVo.addContentType(ContentType.SOUND);
			}
			
			rand = randInt(1, 3);
			
			if ( rand == 3)	{
				preVo.addContentType(ContentType.VIDEO);
			}
						
			String json = genson.serialize(preVo);
			jedis.lpush(Manager.REDIS_LIST, json);
		}
	}
	
	private static int randInt(int min, int max) {
	    Random rand = new Random();

	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
}
