package org.cloudface.tosca.loader;

import java.util.Scanner;

import org.cloudface.tosca.model.TDefinitions;
import org.cloudface.tosca.model.TNodeTemplate;
import org.cloudface.tosca.model.TServiceTemplate;
import org.cloudface.tosca.model.TTopologyTemplate;

/**
 *
 * @author drupalika
 */
public class TestOpenTosca {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    TOSCAFileHandler toc = new TOSCAFileHandler();
    TDefinitions st = toc.loadTServiceTemplate("sugar-test.xml");

    TNodeTemplate newNode = new TNodeTemplate();
    newNode.setId("Drupal");
    newNode.setId("Drupal");

    TServiceTemplate c = (TServiceTemplate) st.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().get(0);
    TTopologyTemplate temp = (TTopologyTemplate) c.getTopologyTemplate();
    temp.getNodeTemplateOrRelationshipTemplate().add(newNode);

    toc.saveTServiceTemplate(st, "new-save.xml");

    Scanner sc = new Scanner(System.in);
    sc.next();

  }
}
