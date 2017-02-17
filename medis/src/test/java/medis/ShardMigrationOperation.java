package medis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.Operation;

public class ShardMigrationOperation extends Operation {
	 private Map<String, ShardImpl> migrationData;

	    public ShardMigrationOperation() {
	    }

	    ShardMigrationOperation(Map<String, ShardImpl> migrationData) {
	        this.migrationData = migrationData;
	    }

	    @Override
	    protected void writeInternal(ObjectDataOutput out) throws IOException {
	        out.writeInt(migrationData.size());
	        for (Entry<String, ShardImpl> entry : migrationData.entrySet()) {
	            out.writeUTF(entry.getKey());
	            out.writeObject(entry.getValue());
	        }
	    }

	    @Override
	    protected void readInternal(ObjectDataInput in) throws IOException {
	        int size = in.readInt();
	        migrationData = new HashMap<String, ShardImpl>();
	        for (int i = 0; i < size; i++) {
	            migrationData.put(in.readUTF(), in.readObject());
	        }
	    }

	    @Override
	    public void run() throws Exception {
	       ShardService service = getService();
	       Container container = service.containers[getPartitionId()];
	       container.applyMigrationData(migrationData);
	    }
}
