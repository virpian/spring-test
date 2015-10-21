package nmd.dao;

import java.util.List;

import nmd.pojo.TodoItem;

public interface TodoDao {

	TodoItem createTodoItem(TodoItem todoItem);

	TodoItem readTodoItem(String id);

	List<TodoItem> readTodoItems();

	TodoItem updateTodoItem(String id, boolean isComplete);

	boolean deleteTodoItem(String id);

}
