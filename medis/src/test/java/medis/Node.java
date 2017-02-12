/**
 * 
 */
package medis;

import com.hazelcast.config.Config;
import com.hazelcast.config.ServiceConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * @author psurti
 *
 */
public class Node {

	private static final int NUM_OF_NODES = 3;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		HazelcastInstance[] instances = new HazelcastInstance[NUM_OF_NODES];

		Config config = new Config();
		ServiceConfig service = new ServiceConfig();
		service.setName(ShardService.NAME).setEnabled(true).setClassName(ShardService.class.getName());
		config.getServicesConfig().addServiceConfig(service);
		for (int i = 0; i < instances.length; i++) {
			instances[i] = Hazelcast.newHazelcastInstance(config);
		}

		Shard[] shards = new Shard[4];
		for (int i = 0; i < shards.length; i++) {
			shards[i] = instances[0].getDistributedObject(ShardService.NAME, "shard-" + i);
		}

		for (Shard shard : shards) {
			shard.update(null);
		}

		System.out.println("Finished");

		for (int i = instances.length - 1; i > 1; i--) {
			instances[i].shutdown();
		}

		for (Shard shard : shards) {
			shard.update(null);
		}

		Thread.sleep(10000);
		System.out.println("Finished");
		Hazelcast.shutdownAll();
	}

}
