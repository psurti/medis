package medis;

import java.util.Properties;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.spi.ManagedService;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.spi.RemoteService;


public class ShardService implements ManagedService, RemoteService {

	static final String NAME = "ShardService";
	
	private NodeEngine nodeEngine;

	@Override
    public void init(NodeEngine nodeEngine, Properties properties) {
        System.out.println("ShardService.init()" + Thread.currentThread() + " " + nodeEngine.getLocalMember());
        this.nodeEngine = nodeEngine;
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
		return new ShardProxy(objectName,nodeEngine,this);
	}

	@Override
	public void destroyDistributedObject(String objectName) {
		// TODO Auto-generated method stub
	}

}
