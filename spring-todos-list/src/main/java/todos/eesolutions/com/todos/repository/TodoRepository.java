package todos.eesolutions.com.todos.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import todos.eesolutions.com.todos.model.Todo;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {

}
