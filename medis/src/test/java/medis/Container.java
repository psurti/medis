package medis;

import java.util.HashMap;
import java.util.Map;

public class Container {
	
	private final  Map<String, ShardImpl> values = new HashMap<>();

	
	public int update(String objectId, int amount) {
		ShardImpl shard = values.get(objectId);
		shard.numOfUpdates += amount;
		values.put(objectId, shard);
		return shard.numOfUpdates;
	}
	
    void clear() {
        values.clear();
    }

    void applyMigrationData(Map<String, ShardImpl> migrationData) {
        values.putAll(migrationData);
    }

    Map<String, ShardImpl> toMigrationData() {
        return new HashMap<String, ShardImpl>(values);
    }
    
    void init( String objectName, int amt ) {
    	ShardImpl s = new ShardImpl();
    	s.numOfUpdates = amt;
    	values.put(objectName,s);
    }
    
    void destroy(String objectName) {
        values.remove(objectName);
    }
}