package org.cloudface.wso2.greg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;

import javax.net.ssl.SSLException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.cloudface.tosca.model.TDefinitions;
import org.cloudface.tosca.model.TExtensibleElements;
import org.cloudface.tosca.model.TNodeType;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

/**
 * Servlet implementation class Sebto
 */
public class Sebto extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public Sebto() {
		// TODO Auto-generated constructor stub

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String storeKey = "/WEB-INF/lib/resources/security/wso2carbon.jks";

		String keyStore = getServletContext().getRealPath(storeKey);

		File keyStoreFile = new File(keyStore);

		if (keyStoreFile.exists()) {

			System.setProperty("javax.net.ssl.trustStore", keyStore);

			System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

			System.setProperty("javax.net.ssl.trustStoreType", "JKS");

			System.setProperty("carbon.repo.write.mode", "true");

			try {

				String res = "/ToscaTemplates/RootNodeType.tosca";

				RemoteRegistry remote_registry = new RemoteRegistry(new URL("https://192.168.100.143:9443/registry"), "admin", "123456");
				// boolean value = remote_registry.resourceExists(res);
				// Resource resource = remote_registry.get(res);
				// response.getOutputStream().print(resource.getMediaType());

				// Registry rootRegistry = new RemoteRegistry(new URL(
				// "https://192.168.100.143:9443/registry"), "admin", "123456");
				// Registry governanceRegistry =
				// GovernanceUtils.getGovernanceUserRegistry(rootRegistry,
				// "admin");

				Resource resource = remote_registry.get(res);
				// response.getOutputStream().print(resource.getMediaType());

				InputStream IO = resource.getContentStream();

				// Scanner sc = new Scanner(IO);
				// response.getOutputStream().print(sc.nextLine());

				JAXBContext jaxbContext = JAXBContext.newInstance(TDefinitions.class.getPackage().getName());
				TDefinitions template = null;

				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

				Object obj = unmarshaller.unmarshal(IO);

				template = (TDefinitions) obj;
				List<TExtensibleElements> arr = template.getServiceTemplateOrNodeTypeOrNodeTypeImplementation();

				for (TExtensibleElements tExtensibleElement : arr) {
					TNodeType nodeType = (TNodeType) tExtensibleElement;
					response.getOutputStream().print(nodeType.getName());
				}

				IO.close();

			} catch (RegistryException | SSLException | JAXBException e) {
				// response.getOutputStream().println("<br /><br /><br /><br />");
				// e.printStackTrace(new
				// PrintStream(response.getOutputStream()));
			} catch (Exception e) {
				response.getOutputStream().println("<br /><br /><br /><br />");
				e.printStackTrace(new PrintStream(response.getOutputStream()));
			}
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
