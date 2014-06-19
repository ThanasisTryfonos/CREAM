package org.cloudface.wso2.greg;

import java.io.File;
import java.io.InputStream;

import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

public abstract class Wso2ResourceWrapper {

	private Resource resource;

	public Wso2ResourceWrapper(Resource resource) {
		this.resource = resource;
	}

	public Resource getResource() {
		return resource;
	}

	public String getTitle() {
		String title = resource.getProperty("title");
		if(title == null || title.length() < 1)
			title = this.getName();
		return title;

	}
	
	public String getName() {
		String path = resource.getPath();
		File file = new File(path);
		return file.getName();
	}

	public String getPath() {
		return resource.getPath();
	}

	public String getUUID() {
		return resource.getUUID();
	}
	
	public InputStream getContentStream() throws RegistryException {
		return resource.getContentStream();
	}
	
	
}
