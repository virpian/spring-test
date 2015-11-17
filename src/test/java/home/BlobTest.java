package home;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import onwelo.skel.infrastructure.StorageFactory;

public class BlobTest {

	@Test
	public void test() {
		CloudBlobContainer bContainer = StorageFactory.getStorageContainer();
		File f = new File ("c:\\temp\\laptopy.jpg");
		CloudBlockBlob blob;
		
		try {
			blob = bContainer.getBlockBlobReference(f.getName());
			
			blob.upload(new FileInputStream(f), f.length());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException fe) {
			// TODO Auto-generated catch block
			fe.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
