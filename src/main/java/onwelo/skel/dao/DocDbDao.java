package onwelo.skel.dao;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.stereotype.Component;

import com.microsoft.azure.documentdb.Database;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.DocumentCollection;
import com.microsoft.azure.documentdb.RequestOptions;

import onwelo.skel.exceptions.InternalErrorException;
import onwelo.skel.infrastructure.DocumentClientFactory;
import onwelo.skel.pojo.TodoItem;

@Component
public class DocDbDao implements TodoDao {
    // The name of our database.
    private static final String DATABASE_ID = "TodoDB";

    // The name of our collection.
    private static final String COLLECTION_ID = "TodoCollection";

    // The DocumentDB Client
    private static DocumentClient documentClient = DocumentClientFactory
            .getDocumentClient();

    // Cache for the database object, so we don't have to query for it to
    // retrieve self links.
    private static Database databaseCache;

    // Cache for the collection object, so we don't have to query for it to
    // retrieve self links.
    private static DocumentCollection collectionCache;

    private Database getTodoDatabase() {
        if (databaseCache == null) {
            // Get the database if it exists
            List<Database> databaseList = documentClient
                    .queryDatabases(
                            "SELECT * FROM root r WHERE r.id='" + DATABASE_ID
                                    + "'", null).getQueryIterable().toList();

            if (databaseList.size() > 0) {
                // Cache the database object so we won't have to query for it
                // later to retrieve the selfLink.
                databaseCache = databaseList.get(0);
            } else {
                // Create the database if it doesn't exist.
                try {
                    Database databaseDefinition = new Database();
                    databaseDefinition.setId(DATABASE_ID);

                    databaseCache = documentClient.createDatabase(
                            databaseDefinition, null).getResource();
                } catch (DocumentClientException e) {
                    // TODO: Something has gone terribly wrong - the app wasn't
                    // able to query or create the collection.
                    // Verify your connection, endpoint, and key.
                    e.printStackTrace();
                }
            }
        }

        return databaseCache;
    }

    private DocumentCollection getTodoCollection() {
        if (collectionCache == null) {
            // Get the collection if it exists.
            List<DocumentCollection> collectionList = documentClient
                    .queryCollections(
                            getTodoDatabase().getSelfLink(),
                            "SELECT * FROM root r WHERE r.id='" + COLLECTION_ID
                                    + "'", null).getQueryIterable().toList();

            if (collectionList.size() > 0) {
                // Cache the collection object so we won't have to query for it
                // later to retrieve the selfLink.
                collectionCache = collectionList.get(0);
            } else {
                // Create the collection if it doesn't exist.
                try {
                    DocumentCollection collectionDefinition = new DocumentCollection();
                    collectionDefinition.setId(COLLECTION_ID);

                    // Configure the new collection performance tier to S1.
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.setOfferType("S1");

                    collectionCache = documentClient.createCollection(
                            getTodoDatabase().getSelfLink(),
                            collectionDefinition, requestOptions).getResource();
                } catch (DocumentClientException e) {
                    // TODO: Something has gone terribly wrong - the app wasn't
                    // able to query or create the collection.
                    // Verify your connection, endpoint, and key.
                    e.printStackTrace();
                }
            }
        }

        return collectionCache;
    }
    
    
 // We'll use Gson for POJO <=> JSON serialization for this example.
    //private static Gson gson = new Gson();

    
    private static ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private static ObjectReader or = new ObjectMapper().reader().withType( TodoItem.class);
    
    @Override
    public TodoItem createTodoItem(TodoItem todoItem) throws InternalErrorException {
        // Serialize the TodoItem as a JSON Document.
    	
        Document todoItemDocument = null;//specjalnie na razie
		try {
			todoItemDocument = new Document( ow.writeValueAsString(todoItem));
		} catch (JsonGenerationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        // Annotate the document as a TodoItem for retrieval (so that we can
        // store multiple entity types in the collection).
        todoItemDocument.set("entityType", "todoItem");

        try {
            // Persist the document using the DocumentClient.
            todoItemDocument = documentClient.createDocument(
                    getTodoCollection().getSelfLink(), todoItemDocument, null,
                    false).getResource();
        } catch (DocumentClientException e) {
            throw new InternalErrorException("Saving in DB failed");
        }
        
        TodoItem ti = null;;
		try {
			ti = or.readValue(todoItemDocument.toString());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return ti; 
    }
    
    
    private Document getDocumentById(String id) {
        // Retrieve the document using the DocumentClient.
        List<Document> documentList = documentClient
                .queryDocuments(getTodoCollection().getSelfLink(),
                        "SELECT * FROM root r WHERE r.id='" + id + "'", null)
                .getQueryIterable().toList();

        if (documentList.size() > 0) {
            return documentList.get(0);
        } else {
            return null;
        }
    }
    @Override
    public TodoItem readTodoItem(String id) {
        // Retrieve the document by id using our helper method.
        Document todoItemDocument = getDocumentById(id);
        
        if (todoItemDocument != null) {
            // De-serialize the document in to a TodoItem.
            try {
				return or.readValue(todoItemDocument.toString());
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
           return null;
        }
        return null;
        
    }
    
    
    @Override
    public List<TodoItem> readTodoItems() {
        List<TodoItem> todoItems = new ArrayList<TodoItem>();

        // Retrieve the TodoItem documents
        
		LocalTime d1= LocalTime.now();
		
		List<Document> documentList = documentClient
                .queryDocuments(getTodoCollection().getSelfLink(),
                        "SELECT * FROM root r WHERE r.entityType = 'todoItem'",
                        null).getQueryIterable().toList();

		LocalTime d2 = LocalTime.now();
		Duration d = Duration.between(d1, d2);
	//	Object p = pjp.getThis();
		
		System.out.println("Database query executed in  "+d.getNano()/1000000 + " ms" );
        
        // De-serialize the documents in to TodoItems.
        for (Document todoItemDocument : documentList) {
            try {
				todoItems.add(or.readValue(todoItemDocument.toString()));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return todoItems;
    }
    
    
    @Override
    public TodoItem updateTodoItem(String id, boolean isComplete) {
        // Retrieve the document from the database
        Document todoItemDocument = getDocumentById(id);

        // You can update the document as a JSON document directly.
        // For more complex operations - you could de-serialize the document in
        // to a POJO, update the POJO, and then re-serialize the POJO back in to
        // a document.
        todoItemDocument.set("complete", isComplete);

        try {
            // Persist/replace the updated document.
            todoItemDocument = documentClient.replaceDocument(todoItemDocument,
                    null).getResource();
        } catch (DocumentClientException e) {
            e.printStackTrace();
            return null;
        }

        try {
			return or.readValue(todoItemDocument.toString());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
        
    }
    
    
    @Override
    public boolean deleteTodoItem(String id) {
        // DocumentDB refers to documents by self link rather than id.

        // Query for the document to retrieve the self link.
        Document todoItemDocument = getDocumentById(id);

        try {
            // Delete the document by self link.
            documentClient.deleteDocument(todoItemDocument.getSelfLink(), null);
        } catch (DocumentClientException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    
    
    
}