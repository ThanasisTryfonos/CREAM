package org.cloudface.tosca.loader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.namespace.QName;

import org.cloudface.tosca.model.properties.amazonec2.AmazonEC2Properties;
import org.cloudface.tosca.model.properties.apache.ApacheWebServerProperties;
import org.cloudface.tosca.model.properties.db.MoodleDatabasePropertiesDefinition;
import org.cloudface.tosca.model.properties.human.HumanResourcePropertiesDefinition;
import org.cloudface.tosca.model.properties.moodle.MoodleWebApplicationProperties;
import org.cloudface.tosca.model.properties.mysql.MySQLProperties;

public class JAXBMetaDataExtractor {

	public static Class[] getPropertiesClasses() {
		Class[] classes = new Class[6];
		classes[0] = HumanResourcePropertiesDefinition.class;
		classes[1] = MoodleDatabasePropertiesDefinition.class;
		classes[2] = MoodleWebApplicationProperties.class;
		classes[3] = ApacheWebServerProperties.class;
		classes[4] = MySQLProperties.class;
		classes[5] = AmazonEC2Properties.class;
		return classes;
	}

	public static Class getClassNameByQName(QName qName) throws JAXBException, InstantiationException, IllegalAccessException {
		Class[] classes = getPropertiesClasses();

		JAXBContext jc = JAXBContext.newInstance(classes);

		JAXBIntrospector ji = jc.createJAXBIntrospector();

		Map<QName, Class> classByQName = new HashMap<QName, Class>(classes.length);
		for (Class clazz : classes) {
			QName qName2 = ji.getElementName(clazz.newInstance());
			if (null != qName2) {
				classByQName.put(qName2, clazz);
			}
		}

		return classByQName.get(qName);
	}

	public static ArrayList<String> getXmlElementsName(Class classObject) {
		ArrayList<String> result = new ArrayList<String>();
		Field[] fields = classObject.getSuperclass().getDeclaredFields();
		for (Field field : fields) {
			Annotation anot = field.getAnnotation(XmlElement.class);
			if (anot != null) {
				XmlElement xe = (XmlElement) anot;
				result.add(xe.name());
			}
		}
		return result;
	}
}
