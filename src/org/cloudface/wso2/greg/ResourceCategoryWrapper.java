package org.cloudface.wso2.greg;

import org.wso2.carbon.registry.core.Collection;

public class ResourceCategoryWrapper extends Wso2ResourceWrapper {

	public ResourceCategoryWrapper(Collection resource) {
		super(resource);
	}

	public Collection getCollection() {
		return (Collection) getResource();
	}
}
