package nmd.controllers;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.ListBlobItem;

import nmd.infrastructure.StorageFactory;

@RestController
@RequestMapping("/blob")
public class BlobController {

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<String> getList() throws StorageException, URISyntaxException {

		CloudBlobContainer blobContainer = StorageFactory.getStorageContainer();
		ArrayList<String> al = new ArrayList<>();
		for (ListBlobItem lbi : blobContainer.listBlobs(null, true)) {
			if (lbi instanceof CloudBlob)
				al.add(lbi.getUri().getPath());
			/*
			 * else { for (ListBlobItem llbi :
			 * blobContainer.getDirectoryReference("test").listBlobs())
			 * al.add(llbi.getUri().getPath()); }
			 */

		}
		return al;

	}

}
