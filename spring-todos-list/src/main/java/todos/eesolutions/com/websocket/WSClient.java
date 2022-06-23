package todos.eesolutions.com.websocket;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.socket.client.IO;
import io.socket.client.Socket;
import todos.eesolutions.com.websocket.model.WSMessage;

@Service
public class WSClient {
	private static final Logger logger = LoggerFactory.getLogger(WSClient.class);
	private static final String URL = "http://localhost:8081"; 
	private Socket client;
	public WSClient() {
		IO.Options options = new IO.Options();
        options.transports = new String[]{"websocket"};
        options.reconnectionAttempts = 3;
        // Time interval for failed reconnection
        options.reconnectionDelay = 1000;
        // Connection timeout (ms)
        options.timeout = 500;
        
        //Create socket client
        try {
			client = IO.socket(URL, options);
//			client.connect();
			// connect by interval
			final int interval = 5000;
			Timer connectTimer = new Timer();
			connectTimer.scheduleAtFixedRate(new TimerTask(){
			    @Override
			    public void run(){
			    	if (!client.connected()) {
			    		logger.info("Retry to connect to server each {} seconds", interval/1000);
			    		client.connect();
			    	}
			    }
			},0, interval);
			
			client.on(Socket.EVENT_CONNECT, (objects) -> {
				logger.info("[WSClient] Client socket connected to server '{}'", objects.toString());
				connectTimer.cancel();
			});

			client.on(Socket.EVENT_CONNECT_ERROR, (error) -> {
				logger.info("[WSClient] Client socket on error when connect to server '{}'", error.toString());
			});

			client.on(Socket.EVENT_DISCONNECT, (error) -> {
				logger.info("[WSClient] Client disconnected from server '{}'", error.toString());
			});
			
			client.on("todos", (message) -> {
				logger.info("[WSClient] Client socket receive message from server '{}'", message);
			});


			

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public void send(WSMessage message) {
		logger.info("[WSClient] Client send message '{}'", message);
		
 
        client.emit("todos", new JSONObject(message));

	}
}
