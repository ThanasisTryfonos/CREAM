package org.cloudface.commons;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApplicationConfiguration {

	private static String configurationFile = "/WEB-INF/project-data/configuration.properties";

	private static ApplicationConfiguration instance = null;

	public static ApplicationConfiguration getInstance() throws IOException, ServletContextHelperNullException  {
		if (instance == null) {
			instance = new ApplicationConfiguration();
		}
		return instance;
	}

	private Properties configuration;

	private ApplicationConfiguration() throws IOException,
			ServletContextHelperNullException {
		configuration = new Properties();
		FileInputStream fis;
		try {
			String configurationFileRealPath = ServletContextHelper
					.getInstance().getResourceRealPath(configurationFile);
			fis = new FileInputStream(configurationFileRealPath);
			configuration.load(fis);
			fis.close();
		} catch (IOException | ServletContextHelperNullException e) {
			throw e;
		}
	}

	public String getConfiguration(String key) {
		return configuration.getProperty(key);
	}

}
