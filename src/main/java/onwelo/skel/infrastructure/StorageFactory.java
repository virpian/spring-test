package onwelo.skel.infrastructure;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

public class StorageFactory {

	public static final String storageConnectionString = "DefaultEndpointsProtocol=http;"
			+ "AccountName=storagevirpian;"
			+ "AccountKey=o0gM4CsA15uMT8icVVgpvvUEsFhqKV11sc+IoChtctRmkfQ+VZ8A9fn2SCcLxbs6cPZz5esm2iglq5wLbBCeKw==";

	public static final String blobContainerString = "azure-test-container";
	
	public static CloudBlobContainer getStorageContainer ()
	{
		CloudBlobContainer blobContainer = null;
		try{
			
			CloudBlobClient blobClient;
			CloudStorageAccount acc = CloudStorageAccount.parse(storageConnectionString);
			blobClient  = acc.createCloudBlobClient();
			blobContainer = blobClient.getContainerReference(blobContainerString);
			blobContainer.createIfNotExists();
					
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return blobContainer;
		
	
	}

}
