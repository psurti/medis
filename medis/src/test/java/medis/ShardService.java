package medis;

import java.util.Map;
import java.util.Properties;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.spi.ManagedService;
import com.hazelcast.spi.MigrationAwareService;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.spi.Operation;
import com.hazelcast.spi.PartitionMigrationEvent;
import com.hazelcast.spi.PartitionReplicationEvent;
import com.hazelcast.spi.RemoteService;
import com.hazelcast.spi.partition.MigrationEndpoint;


public class ShardService implements ManagedService, RemoteService, MigrationAwareService {

	static final String NAME = "ShardService";
    Container[] containers;
	private NodeEngine nodeEngine;

	@Override
    public void init(NodeEngine nodeEngine, Properties properties) {
        System.out.println("ShardService.init()" + Thread.currentThread() + " " + nodeEngine.getLocalMember());
        this.nodeEngine = nodeEngine;
        containers = new Container[nodeEngine.getPartitionService().getPartitionCount()];
        for (int i = 0; i < containers.length; i++) {
            containers[i] = new Container();
        }
    }

    @Override
    public void shutdown(boolean terminate) {
        System.out.println("ShardService.shutdown()" + Thread.currentThread() + " " + nodeEngine.getLocalMember());
    }

    @Override
    public void reset() {

    }

    public NodeEngine getNodeEngine() {
        return nodeEngine;
    }

	@Override
	public DistributedObject createDistributedObject(String objectName) {
	    int partitionId = nodeEngine.getPartitionService().getPartitionId(objectName);
        Container container = containers[partitionId];
        container.init(objectName,0);
        return new ShardProxy(objectName, nodeEngine, this);	}

	@Override
	public void destroyDistributedObject(String objectName) {
		int partitionId = nodeEngine.getPartitionService().getPartitionId(objectName);
		Container container = containers[partitionId];
		container.destroy(objectName);	
	}

	@Override
	public Operation prepareReplicationOperation(PartitionReplicationEvent event) {
		if (event.getReplicaIndex() > 1) {
			return null;
		}
		Container container = containers[event.getPartitionId()];
		
		Map<String, ShardImpl> data = container.toMigrationData();
		
		return data.isEmpty() ? null : new ShardMigrationOperation(data);
	}

	@Override
	public void beforeMigration(PartitionMigrationEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commitMigration(PartitionMigrationEvent event) {
		if (event.getMigrationEndpoint() == MigrationEndpoint.SOURCE) {

            int newReplicaIndex = event.getNewReplicaIndex();

            if (newReplicaIndex == -1 || newReplicaIndex > 1) {

                clearPartitionReplica(event.getPartitionId());

            }

        }		
	}

	@Override
	public void rollbackMigration(PartitionMigrationEvent event) {
		if (event.getMigrationEndpoint() == MigrationEndpoint.DESTINATION) {

            int currentReplicaIndex = event.getCurrentReplicaIndex();

            if (currentReplicaIndex == -1 || currentReplicaIndex > 1) {

                clearPartitionReplica(event.getPartitionId());

            }

        }		
	}

	public void clearPartitionReplica(int partitionId) {

        Container container = containers[partitionId];

        container.clear();

    }


		
}
