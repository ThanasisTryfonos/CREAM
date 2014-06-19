package org.cloudface.wso2.greg;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.wso2.carbon.registry.core.utils.RegistryClientUtils;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

public class GovernanceRegistryReader {

	private GovernanceRegistryConnector greg;

	private String rootResourceCollectionPath;

	public GovernanceRegistryReader(GovernanceRegistryConnector greg, String rootResourceCollectionPath) {
		this.greg = greg;
		this.rootResourceCollectionPath = rootResourceCollectionPath;
	}

	public ArrayList<ResourceCategoryWrapper> getResourceCategories() throws RegistryException, MalformedURLException {
		ArrayList<ResourceCategoryWrapper> result = new ArrayList<ResourceCategoryWrapper>();

		RemoteRegistry remoteRegistry = this.greg.getRemoteRegistry();
		Collection resource = (Collection) remoteRegistry.get(this.rootResourceCollectionPath);

		String[] childrenPath = resource.getChildren();

		for (String childPath : childrenPath) {
			Resource resourceChild = remoteRegistry.get(childPath);
			if ((resourceChild instanceof Collection) && resource.getChildCount() > 0) {
				ResourceCategoryWrapper category = new ResourceCategoryWrapper((Collection) resourceChild);
				result.add(category);
			}
		}

		return result;
	}

	public ArrayList<ResourceWrapper> getResources(String categoryPath) throws RegistryException, MalformedURLException {
		ArrayList<ResourceWrapper> result = new ArrayList<ResourceWrapper>();

		RemoteRegistry remoteRegistry = this.greg.getRemoteRegistry();
		Collection resources = (Collection) remoteRegistry.get(categoryPath);

		String[] childrenPath = resources.getChildren();

		for (String childPath : childrenPath) {
			result.add(this.getResource(childPath));
		}

		return result;
	}

	public ResourceWrapper getResource(String path) throws RegistryException, MalformedURLException {
		ResourceWrapper resourceWrapper = null;
		RemoteRegistry remoteRegistry = this.greg.getRemoteRegistry();
		Resource resource = remoteRegistry.get(path);
		if (resource != null) {
			resourceWrapper = new ResourceWrapper(resource);
		}
		return resourceWrapper;
	}

	public void writeResourceToRegistry(String content, String destPath, String mediaType) throws RegistryException, MalformedURLException {
		RemoteRegistry registry = this.greg.getRemoteRegistry();
		Resource res = registry.newResource();
		res.setContent(content.getBytes());
		res.setMediaType(mediaType);
		registry.put(destPath, res);
	}

}
