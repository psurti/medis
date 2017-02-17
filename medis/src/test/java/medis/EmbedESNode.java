package medis;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.node.internal.InternalSettingsPreparer;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.Netty4Plugin;

public class EmbedESNode {

	public EmbedESNode() {
		
	}
	private static class PluginConfigurableNode extends org.elasticsearch.node.Node {

        public PluginConfigurableNode(Settings settings, Collection<Class<? extends Plugin>> classpathPlugins) {

            super(InternalSettingsPreparer.prepareEnvironment(settings, null), classpathPlugins);
        }

    }
	public static void main(String[] args) throws NodeValidationException, IOException {
		Properties props = new Properties();
        String homePath =".";
        String clusterName = "star";
        String dataPath = ".";
        String httpRange = "9200-9210";
        String transportRange = "9300-9310";
        props.setProperty("path.home", homePath);
		props.setProperty("path.data", dataPath);
		props.setProperty("http.port", httpRange);
		props.setProperty("transport.tcp.port", transportRange);
        props.setProperty("transport.type", "local");
        //props.setProperty("discovery.type", "local");
        props.setProperty("http.type", "netty4");
        props.setProperty("script.inline", "true");
		props.setProperty("cluster.name", clusterName);
        props.setProperty("node.ingest", "true");



        Settings settings = Settings.builder().put(props).build();
        Collection plugins = Arrays.asList(Netty4Plugin.class);
		org.elasticsearch.node.Node node =  new PluginConfigurableNode(settings, plugins);
		node.start();
        String localNodeId = node.client().admin().cluster().prepareState().get().getState().getNodes().getLocalNodeId();
        String value = node.client().admin().cluster().prepareNodesInfo(localNodeId).get().getNodes().iterator().next().getHttp().address().publishAddress().toString();
        System.out.println( localNodeId );
        System.out.println( value );
        
        System.in.read();

	}

}
