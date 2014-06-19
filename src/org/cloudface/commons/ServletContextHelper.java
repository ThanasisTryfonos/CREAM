package org.cloudface.commons;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class ServletContextHelper {

	private static ServletContextHelper instance = null;

	public static ServletContextHelper initializeInstance(ServletContext servletContext) {
		if (instance == null) {
			instance = new ServletContextHelper(servletContext);
		}
		return instance;
	}

	public static ServletContextHelper getInstance() throws ServletContextHelperNullException {
		if (instance == null) {
			throw new ServletContextHelperNullException();
		}
		return instance;
	}

	private ServletContext servletContext;

	private ServletContextHelper(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public String getResourceRealPath(String resource) {
		return servletContext.getRealPath(resource);
	}

}
