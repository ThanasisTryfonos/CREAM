package org.cloudface.commons;

import java.util.ArrayList;

public class ResourceObjectMap {

	public String pathID;
	public String title;
	public String name;
	public String id;
	public ArrayList<String> properties;

	public ResourceObjectMap(String pathID, String title, String name, String id, ArrayList<String> properties) {
		this.pathID = pathID;
		this.title = title;
		this.name = name;
		this.id = id;
		this.properties = properties;
	}

}
