package todos.eesolutions.com.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import todos.eesolutions.com.websocket.model.WSMessage;

@Service
public class WSServer {
	private static final String TODOS_TOPIC = "todos";

	private static final Logger logger = LoggerFactory.getLogger(WSServer.class);
	
	private final SocketIONamespace namespace;
	
	@Autowired
	public WSServer(SocketIOServer server) {
		// should start IO server
		server.start();
		
		// then configure server
		this.namespace = server.addNamespace("");
		this.namespace.addConnectListener(handleConnection());
		this.namespace.addDisconnectListener(handleDisconnect());
		// add topic
		logger.info("[WSServer] Web socket listen on topic '{}'", TODOS_TOPIC);
		this.namespace.addEventListener(TODOS_TOPIC, WSMessage.class, handleMessage());
	}

	private DataListener handleMessage() {
		return (client, data, ackSender) -> {
			logger.info("[WSServer] Client[{}] - Received message '{}'", client.getSessionId().toString(), data);
			namespace.getBroadcastOperations().sendEvent(TODOS_TOPIC, data);
		};
	}

	private ConnectListener handleConnection() {
		return client -> {
			HandshakeData handshakeData = client.getHandshakeData();
			logger.info("[WSServer] Client[{}] - Connected to socket server through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
		};
	}

	private DisconnectListener handleDisconnect() {
		return client -> {
			logger.info("[WSServer] Client[{}] - Disconnect from socket server", client.getSessionId().toString());
		};
	}
	
}
