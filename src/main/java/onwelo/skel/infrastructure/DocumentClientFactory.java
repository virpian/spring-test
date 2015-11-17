package onwelo.skel.infrastructure;

import org.apache.log4j.Logger;

import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;

public class DocumentClientFactory {

	private static final String HOST = "https://virpian.documents.azure.com:443/";
	private static final String MASTER_KEY = "k6F1o6qvsuWX/MRSC9E/fGPyuqt4rUvHie2npJhjqGX1nANET02v9SNtSwEfRN1BiBhEtEFe4qSwbb1HDBpO+g==";

	private static Logger log =  Logger.getLogger(DocumentClientFactory.class);
	
	private static DocumentClient documentClient;

	public static DocumentClient getDocumentClient() {
		
		//log.info("Wejscie do tworzenia documentDB");
		if (documentClient == null) {
			//log.info("tworzenie połączenia");	
			
			documentClient = new DocumentClient(HOST, MASTER_KEY, ConnectionPolicy.GetDefault(),
					ConsistencyLevel.Session);
		}

		return documentClient;
	}

}