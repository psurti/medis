/**
 * 
 */
package medis;
import java.util.List;
import java.util.Map;

import com.hazelcast.core.DistributedObject;
/**
 * @author psurti
 */
public interface Shard extends DistributedObject {

	int update(List<Map<String,Object>> dataList, int amount);
}