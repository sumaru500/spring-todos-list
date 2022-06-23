package todos.eesolutions.com.todos.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import todos.eesolutions.com.todos.model.Todo;
import todos.eesolutions.com.todos.model.dto.TodoDto;
import todos.eesolutions.com.todos.repository.TodoRepository;
import todos.eesolutions.com.websocket.WSClient;
import todos.eesolutions.com.websocket.model.WSMessage;

@RestController
@CrossOrigin
@RequestMapping("/todos")
public class TodoController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TodoRepository repository;
	@Autowired
	private WSClient wsClient;
	
	@GetMapping()
	public ResponseEntity<List<Todo>> getAllTodos() {
		List<Todo> todos = repository.findAll();
		logger.info(todos.stream().map(Todo::toString).collect(Collectors.joining(", ")));
		return new ResponseEntity<>(todos, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Todo> getTodoById(@PathVariable("id") String id) {
		Optional<Todo> todoOpt = repository.findById(id);
		return new ResponseEntity<>(todoOpt.get(), HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<?> addTodo(@RequestBody Todo todo) {
		logger.info("addTodo : {}", todo.toString());
		try {
			Todo newTodo = repository.save(todo);
			wsClient.send(new WSMessage(WSMessage.INSERT, newTodo.getId()));
			return new ResponseEntity<Todo>(newTodo, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateTodo(@PathVariable("id") String id, @RequestBody TodoDto todoDto) {
		logger.info("updateTodo : {}", todoDto.toString());
		Optional<Todo> todoOpt = repository.findById(id);
		if (todoOpt.isEmpty()) {
			return new ResponseEntity<String>("Not found Todo with id = " + id, HttpStatus.NOT_FOUND);
		}
		
		if (todoDto.getTitle() != null && !todoDto.getTitle().isEmpty()) {
			todoOpt.get().setTitle(todoDto.getTitle());
		}
		
		if (todoDto.getCompleted() != null) {
			todoOpt.get().setCompleted(todoDto.getCompleted());
		}
		
		Todo savedTodo = repository.save(todoOpt.get());
		wsClient.send(new WSMessage(WSMessage.UPDATE, id));
		return new ResponseEntity<>(savedTodo, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTodo(@PathVariable("id") String id) {
		logger.info("deleteTodo : {}", id);
		Optional<Todo> todoOpt = repository.findById(id);
		if (todoOpt.isEmpty()) {
			return new ResponseEntity<String>("Not found Todo with id = " + id, HttpStatus.NOT_FOUND);
		}

		repository.delete(todoOpt.get());
		wsClient.send(new WSMessage(WSMessage.DELETE, id));
		return new ResponseEntity<>(todoOpt.get(), HttpStatus.OK);
		
	}
}
