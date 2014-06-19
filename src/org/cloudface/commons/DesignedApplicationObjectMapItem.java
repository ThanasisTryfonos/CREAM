package org.cloudface.commons;

import java.util.List;

public class DesignedApplicationObjectMapItem {

	public static class EndPoint {

		public static class Properties {
			public String data;
			public String name;
		}

		public String id;
		public String pathID;
		public List<Properties> properties;
	}

	public String id;
	public String name;
	public EndPoint source;
	public EndPoint target;

}