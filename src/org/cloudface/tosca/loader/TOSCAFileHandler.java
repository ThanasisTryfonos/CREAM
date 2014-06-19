package org.cloudface.tosca.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.cloudface.tosca.model.TDefinitions;
import org.cloudface.tosca.model.TServiceTemplate;

/**
 * Class for loading and saving Service Templates
 *
 *
 */
public class TOSCAFileHandler {

  private JAXBContext jaxbContext;

  public TOSCAFileHandler() {
    try {
      jaxbContext = JAXBContext.newInstance(TServiceTemplate.class.getPackage().getName());
    } catch (JAXBException e) {
    }
  }

  /**
   * Method for loading and unmarshalling of Service Templates
   *
   * @param filePath String that indicates the path to the Service Template to
   * be loaded
   * @return TServiceTemplate
   */
  public TDefinitions loadTServiceTemplate(String filePath) {

    File file = new File(filePath);
    TDefinitions template = null;
    try {
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

      Object obj = unmarshaller.unmarshal(file);

      template = (TDefinitions) obj;

    } catch (JAXBException e) {
      e.printStackTrace();
    }

    return template;

  }

  /**
   * Method for marshalling and saving of Service Templates
   *
   *
   * @param serviceTemplate Service Template to be saved
   * @param filePath String that indicates the path where the Service Template
   * should be saved
   */
  public void saveTServiceTemplate(TDefinitions serviceTemplate, String filePath) {

    Marshaller marshaller;
    try {
      marshaller = jaxbContext.createMarshaller();

      marshaller.marshal(serviceTemplate, new FileOutputStream(filePath));

    } catch (JAXBException e) {
    } catch (FileNotFoundException e) {
    }
  }
}
