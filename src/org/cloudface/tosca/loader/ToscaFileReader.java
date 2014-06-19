package org.cloudface.tosca.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.cloudface.tosca.model.TDefinitions;
import org.cloudface.tosca.model.TEntityType;
import org.cloudface.tosca.model.TExtensibleElements;

public class ToscaFileReader {

	private InputStream stream;
	private TDefinitions tDfinitions;

	public ToscaFileReader(InputStream stream) throws JAXBException, IOException {
		if (stream.available() > 0) {
			this.stream = stream;
			tDfinitions = getTDefinitions();
		}
		else {
			throw new IOException();
		}
	}

	public TDefinitions getTDfinitions() {
		return tDfinitions;
	}

	private TDefinitions getTDefinitions() throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(TDefinitions.class.getPackage().getName());
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Object obj = unmarshaller.unmarshal(stream);
		TDefinitions template = (TDefinitions) obj;
		stream.close();
		return template;
	}

	public ArrayList<String> getPropertiesDefinitionNameOfSingleNodeType() throws InstantiationException, IllegalAccessException, JAXBException {
		ArrayList<String> propertiesDefinitionName = new ArrayList<String>();
		List<TExtensibleElements> childTypes = tDfinitions.getServiceTemplateOrNodeTypeOrNodeTypeImplementation();
		for (TExtensibleElements tExtensibleElement : childTypes) {
			if (tExtensibleElement instanceof TEntityType) {
				propertiesDefinitionName = this.getPropertiesDefinitionName((TEntityType) tExtensibleElement);
			}
		}
		return propertiesDefinitionName;
	}

	public ArrayList<String> getPropertiesDefinitionName(TEntityType tEntityType) throws InstantiationException, IllegalAccessException, JAXBException {
		TEntityType.PropertiesDefinition propertiesDefinition = tEntityType.getPropertiesDefinition();

		if (propertiesDefinition != null) {
			QName qName = propertiesDefinition.getType();

			Class classObj = JAXBMetaDataExtractor.getClassNameByQName(qName);
			ArrayList<String> properiesDefinitions = JAXBMetaDataExtractor.getXmlElementsName(classObj);

			return properiesDefinitions;
		}

		return null;
	}
	
	public TEntityType getSingleEntityType() {
		List<TExtensibleElements> childTypes = tDfinitions.getServiceTemplateOrNodeTypeOrNodeTypeImplementation();
		for (TExtensibleElements tExtensibleElement : childTypes) {
			if (tExtensibleElement instanceof TEntityType) {
				return (TEntityType) tExtensibleElement;
			}
		}
		return null;
	}
}
