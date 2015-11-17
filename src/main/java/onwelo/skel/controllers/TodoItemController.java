package onwelo.skel.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import onwelo.skel.dao.TodoDao;
import onwelo.skel.exceptions.InternalErrorException;
import onwelo.skel.exceptions.ObjectNotFoundException;
import onwelo.skel.infrastructure.TodoDaoFactory;
import onwelo.skel.pojo.TodoItem;

@RestController
@RequestMapping("/docdb")
public class TodoItemController {
	
    public static TodoItemController getInstance() {
        if (todoItemController == null) {
            todoItemController = new TodoItemController(TodoDaoFactory.getDao());
        }
        return todoItemController;
    }

    

	private static TodoItemController todoItemController;

	
    private final TodoDao todoDao;

    @Autowired
    TodoItemController(TodoDao todoDao) {
        this.todoDao = todoDao;
    }

    
    @RequestMapping(value="/add",method = RequestMethod.POST)
    public TodoItem createTodoItem(@NonNull @RequestBody TodoItem ti) throws InternalErrorException {
        
        return todoDao.createTodoItem(ti);
    }

    public boolean deleteTodoItem(@NonNull String id) {
        return todoDao.deleteTodoItem(id);
    }

    @RequestMapping (value="/get/{id}", method =RequestMethod.GET)
    public TodoItem getTodoItemById(@NonNull @PathVariable("id") String id) throws ObjectNotFoundException {
        TodoItem ti = todoDao.readTodoItem(id);
        
        if (ti == null) 
        	throw new ObjectNotFoundException("Object not found");
        return ti;
    }

    
    @RequestMapping ("/get")
    public List<TodoItem> getTodoItems() throws Exception {
    	
        return todoDao.readTodoItems();
    }

    
    
    public TodoItem updateTodoItem(@NonNull String id, boolean isComplete) {
        return todoDao.updateTodoItem(id, isComplete);
    }
}