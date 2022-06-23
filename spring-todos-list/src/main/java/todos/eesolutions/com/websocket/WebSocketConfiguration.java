package todos.eesolutions.com.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.SocketIOServer;

//@Configuration
public class WebSocketConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(WebSocketConfiguration.class);
	
	@Value("${web.socket.host}")
	private String host;
	
	@Value("${web.socket.port}")
	private Integer port;
	
	@Bean
	public SocketIOServer socketIOServer() {
		com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
		config.setHostname(host);
		config.setPort(port);
		logger.info("Web socket listen on port {}", port);
		return new SocketIOServer(config);
	}
}
