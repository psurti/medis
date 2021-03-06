package medis;

import java.io.IOException;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.BackupOperation;
import com.hazelcast.spi.Operation;

public class ShardBackupOperation extends Operation implements BackupOperation 
{
    private String objectId;
    private int amount;

    public ShardBackupOperation() {
    }

    ShardBackupOperation(String objectId, int amount) {
        this.amount = amount;
        this.objectId = objectId;
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
    public void run() throws Exception {
        ShardService service = getService();
        System.out.println("Executing backup " + objectId + ".inc() on: " + getNodeEngine().getThisAddress());
        Container container = service.containers[getPartitionId()];
        container.init(objectId, amount);
        
    }
}
