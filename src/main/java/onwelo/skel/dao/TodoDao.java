package onwelo.skel.dao;

import java.util.List;

import onwelo.skel.exceptions.InternalErrorException;
import onwelo.skel.pojo.TodoItem;

public interface TodoDao {

	TodoItem createTodoItem(TodoItem todoItem) throws InternalErrorException;

	TodoItem readTodoItem(String id);

	List<TodoItem> readTodoItems();

	TodoItem updateTodoItem(String id, boolean isComplete);

	boolean deleteTodoItem(String id);
	
	

}
