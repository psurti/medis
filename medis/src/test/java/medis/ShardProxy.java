package medis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.hazelcast.spi.AbstractDistributedObject;
import com.hazelcast.spi.InvocationBuilder;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.util.ExceptionUtil;

public class ShardProxy extends AbstractDistributedObject<ShardService> implements Shard {

	private final String name;

	ShardProxy(String name, NodeEngine nodeEngine, ShardService shardService) {
		super(nodeEngine, shardService);
		this.name = name;
	}


	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getServiceName() {
		return ShardService.NAME;
	}

	@Override
	public int update(List<Map<String, Object>> dataList, int amount) {
		System.out.println( "UpdateOp " + Thread.currentThread() + " " + getNodeEngine().getLocalMember());
		NodeEngine nodeEngine = getNodeEngine();
        ShardOperation operation = new ShardOperation(name, amount);
        int partitionId = nodeEngine.getPartitionService().getPartitionId(name);
        InvocationBuilder builder = nodeEngine.getOperationService()
                .createInvocationBuilder(ShardService.NAME, operation, partitionId);
        try {
            Future<Integer> future = builder.invoke();
            return future.get();
        } catch (Exception e) {
            throw ExceptionUtil.rethrow(e);
        }

	}


}
