/**
 * 
 */
package medis;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.BackupAwareOperation;
import com.hazelcast.spi.Operation;
import com.hazelcast.spi.PartitionAwareOperation;

import java.io.IOException;
import java.util.Map;
/**
 * @author psurti
 *        System.out.println("Executing " + objectId + ".inc() on: " + getNodeEngine().getThisAddress());
        CounterService service = getService();
        CounterService.Container container = service.containers[getPartitionId()];
        Map<String, Integer> valuesMap = container.values;

        Integer counter = valuesMap.get(objectId);
        counter += amount;
        valuesMap.put(objectId, counter);
        returnValue = counter;
 */
class ShardOperation extends Operation implements PartitionAwareOperation, BackupAwareOperation {

    private String objectId;
    private int amount;
    private int returnValue;

    // it is important to have a no-arg constructor for deserialization
    public ShardOperation() {
    }

    ShardOperation(String objectId, int amount) {
        this.amount = amount;
        this.objectId = objectId;
    }

    @Override
    public void run() throws Exception {
        System.out.println("Executing " + objectId + ".update() on: " + getNodeEngine().getThisAddress());
        ShardService service = getService();
        Container container = service.containers[getPartitionId()];
        returnValue = container.update(objectId, amount);
    }

    @Override
    public Object getResponse() {
        return returnValue;
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        out.writeUTF(objectId);
        out.writeInt(amount);
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        objectId = in.readUTF();
        amount = in.readInt();
    }

	@Override
	public boolean shouldBackup() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getSyncBackupCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getAsyncBackupCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Operation getBackupOperation() {
		return new ShardBackupOperation( this.objectId, this.amount);
	}
}
