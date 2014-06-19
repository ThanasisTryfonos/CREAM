package org.cloudface.wso2.greg;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.cloudface.commons.SSLStoreKeyNotFoundException;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

public class GovernanceRegistryConnector {

	private String URL;
	private String user;
	private String password;
	private String StoreKey;
	private String StorePassword;
	private String StoreType;

	private RemoteRegistry remoteRegistry;

	public GovernanceRegistryConnector(String URL, String user, String password, String StoreKey, String StorePassword, String StoreType) throws SSLStoreKeyNotFoundException {
		this.URL = URL;
		this.user = user;
		this.password = password;
		this.StoreKey = StoreKey;
		this.StorePassword = StorePassword;
		this.StoreType = StoreType;

		File keyStoreFile = new File(StoreKey);

		if (!keyStoreFile.exists()) {
			throw new SSLStoreKeyNotFoundException();
		}
	}

	public RemoteRegistry getRemoteRegistry() throws RegistryException, MalformedURLException {
		System.setProperty("javax.net.ssl.trustStore", StoreKey);
		System.setProperty("javax.net.ssl.trustStorePassword", StorePassword);
		System.setProperty("javax.net.ssl.trustStoreType", StoreType);
		System.setProperty("carbon.repo.write.mode", "true");
		remoteRegistry = new RemoteRegistry(new URL(URL), user, password);
		return remoteRegistry;
	}

}
