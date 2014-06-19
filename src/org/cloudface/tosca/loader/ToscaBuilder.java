package org.cloudface.tosca.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.namespace.QName;

import org.cloudface.commons.DesignedApplicationObjectMapItem;
import org.cloudface.commons.DesignedApplicationObjectMapItem.EndPoint;
import org.cloudface.tosca.model.Definitions;
import org.cloudface.tosca.model.ObjectFactory;
import org.cloudface.tosca.model.TDefinitions;
import org.cloudface.tosca.model.TEntityTemplate;
import org.cloudface.tosca.model.TEntityType;
import org.cloudface.tosca.model.TImport;
import org.cloudface.tosca.model.TNodeTemplate;
import org.cloudface.tosca.model.TRelationshipTemplate;
import org.cloudface.tosca.model.TServiceTemplate;
import org.cloudface.tosca.model.TTopologyTemplate;
import org.cloudface.tosca.model.properties.human.THumanResourcePropertiesDefinition;
import org.cloudface.wso2.greg.GovernanceRegistryReader;
import org.cloudface.wso2.greg.ResourceWrapper;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

public class ToscaBuilder {

	private GovernanceRegistryReader gregReader;

	public ToscaBuilder(GovernanceRegistryReader gregReader) {
		this.gregReader = gregReader;
	}

	public String build(List<DesignedApplicationObjectMapItem> designedApplicationObjectMapItems) throws RegistryException, JAXBException, IOException, InstantiationException, IllegalAccessException {

		ObjectFactory toscaFactory = new ObjectFactory();
		Definitions definitions = toscaFactory.createDefinitions();
		TServiceTemplate serviceTemplate = toscaFactory.createTServiceTemplate();
		TTopologyTemplate topolgy = toscaFactory.createTTopologyTemplate();

		Map<String, TNodeTemplate> nodeTemplates = new HashMap<String, TNodeTemplate>();

		Map<String, TEntityType> entityTypes = new HashMap<String, TEntityType>();

		for (DesignedApplicationObjectMapItem designedApplicationObjectMapItem : designedApplicationObjectMapItems) {

			/************ Read required node type or policy type from governance ******************/
			if (!entityTypes.containsKey(designedApplicationObjectMapItem.source.pathID)) {

				TEntityType thisEntityType = getEntityType(designedApplicationObjectMapItem.source.pathID);
				entityTypes.put(designedApplicationObjectMapItem.source.pathID, thisEntityType);

				TImport tImport = new TImport();
				// tImport.setLocation("Definitions/" + thisEntityType.getName()
				// + ".tosca");
				tImport.setLocation(designedApplicationObjectMapItem.source.pathID);
				tImport.setImportType("http://docs.oasis-open.org/tosca/ns/2011/12");
				definitions.getImport().add(tImport);
			}

			if (!entityTypes.containsKey(designedApplicationObjectMapItem.target.pathID)) {

				TEntityType thisEntityType = getEntityType(designedApplicationObjectMapItem.target.pathID);
				entityTypes.put(designedApplicationObjectMapItem.target.pathID, thisEntityType);

				TImport tImport = new TImport();
				// tImport.setLocation("Definitions/" + thisEntityType.getName()
				// + ".tosca");
				tImport.setLocation(designedApplicationObjectMapItem.target.pathID);
				tImport.setImportType("http://docs.oasis-open.org/tosca/ns/2011/12");
				definitions.getImport().add(tImport);
			}

			/********** create node templates *****************/
			if (!nodeTemplates.containsKey(designedApplicationObjectMapItem.source.id)) {
				TNodeTemplate sourceNodeTempalte = buildNodeTemplate(designedApplicationObjectMapItem.source, entityTypes.get(designedApplicationObjectMapItem.source.pathID));
				nodeTemplates.put(designedApplicationObjectMapItem.source.id, sourceNodeTempalte);
			}

			if (!nodeTemplates.containsKey(designedApplicationObjectMapItem.target.id)) {
				TNodeTemplate sourceNodeTempalte = buildNodeTemplate(designedApplicationObjectMapItem.target, entityTypes.get(designedApplicationObjectMapItem.target.pathID));
				nodeTemplates.put(designedApplicationObjectMapItem.target.id, sourceNodeTempalte);
			}

			/********** create relationship templates *****************/
			TRelationshipTemplate relationshipTemplate = new TRelationshipTemplate();
			relationshipTemplate.setId(designedApplicationObjectMapItem.id);
			relationshipTemplate.setName(designedApplicationObjectMapItem.name);

			TRelationshipTemplate.SourceElement source = new TRelationshipTemplate.SourceElement();
			source.setRef(nodeTemplates.get(designedApplicationObjectMapItem.source.id));
			TRelationshipTemplate.TargetElement target = new TRelationshipTemplate.TargetElement();
			target.setRef(nodeTemplates.get(designedApplicationObjectMapItem.target.id));

			relationshipTemplate.setSourceElement(source);
			relationshipTemplate.setTargetElement(target);

			/************ add relationship template to topolgy tempalte *****************/
			topolgy.getNodeTemplateOrRelationshipTemplate().add(relationshipTemplate);
		}

		/************ add node template to topolgy tempalte *****************/
		topolgy.getNodeTemplateOrRelationshipTemplate().addAll(nodeTemplates.values());

		serviceTemplate.setTopologyTemplate(topolgy);

		definitions.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().add(serviceTemplate);

		/**** create appropertiate jaxb marshaller ********/
		Class[] propertiesClasses = JAXBMetaDataExtractor.getPropertiesClasses();
		Class[] contextClasses = new Class[propertiesClasses.length + 1];
		contextClasses[0] = TDefinitions.class;
		System.arraycopy(propertiesClasses, 0, contextClasses, 1, propertiesClasses.length);

		JAXBContext jaxbContext = JAXBContext.newInstance(contextClasses);
		Marshaller marshaller = jaxbContext.createMarshaller();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		marshaller.marshal(definitions, baos);

		/**************************/
		String ss = new String(baos.toByteArray());
		gregReader.writeResourceToRegistry(ss, "/cream/csar/" + UUID.randomUUID().toString(), "application/vnd.oasis.tosca.definitions");
		/*************************/

		return ss;
	}

	private TEntityType getEntityType(String pathID) throws RegistryException, JAXBException, IOException {
		ResourceWrapper resource = gregReader.getResource(pathID);
		ToscaFileReader toscaFileReader = new ToscaFileReader(resource.getContentStream());
		return toscaFileReader.getSingleEntityType();
	}

	private TNodeTemplate buildNodeTemplate(EndPoint source, TEntityType tEntityType) throws InstantiationException, IllegalAccessException, JAXBException {
		TNodeTemplate node = new TNodeTemplate();
		node.setId(source.id);

		node.setType(new QName(tEntityType.getName()));

		TEntityType.PropertiesDefinition propertiesDefinition = tEntityType.getPropertiesDefinition();

		if (propertiesDefinition != null) {
			QName qName = propertiesDefinition.getType();

			Class classObject = JAXBMetaDataExtractor.getClassNameByQName(qName);
			Field[] fields = classObject.getSuperclass().getDeclaredFields();

			Object propertiesObject = classObject.newInstance();

			for (DesignedApplicationObjectMapItem.EndPoint.Properties property : source.properties) {
				if (property.data != null && property.data.length() > 0) {
					for (Field field : fields) {
						Annotation anot = field.getAnnotation(XmlElement.class);
						if (anot != null) {
							XmlElement xe = (XmlElement) anot;
							if (xe.name().equals(property.name)) {
								field.setAccessible(true);
								field.set(propertiesObject, property.data);
							}
						}
					}
				}
			}

			TEntityTemplate.Properties tEntityTemplateProperties = new TEntityTemplate.Properties();
			tEntityTemplateProperties.setAny(propertiesObject);

			node.setProperties(tEntityTemplateProperties);
		}

		return node;
	}
}
