package todos.eesolutions.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Import;

import todos.eesolutions.com.config.cors.CorsConfiguration;
import todos.eesolutions.com.websocket.WebSocketConfiguration;

@SpringBootApplication
@Import({ CorsConfiguration.class, WebSocketConfiguration.class })
public class SpringTodosListApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTodosListApplication.class, args);
	}

}
