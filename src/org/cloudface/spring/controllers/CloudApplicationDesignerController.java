package org.cloudface.spring.controllers;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cloudface.commons.ApplicationConfiguration;
import org.cloudface.commons.DesignedApplicationObjectMapItem;
import org.cloudface.commons.ResourceObjectMap;
import org.cloudface.commons.ServletContextHelper;
import org.cloudface.tosca.loader.ToscaBuilder;
import org.cloudface.tosca.loader.ToscaFileReader;
import org.cloudface.wso2.greg.GovernanceRegistryConnector;
import org.cloudface.wso2.greg.GovernanceRegistryReader;
import org.cloudface.wso2.greg.ResourceCategoryWrapper;
import org.cloudface.wso2.greg.ResourceWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

@Controller
@RequestMapping("/user/cloud-app")
public class CloudApplicationDesignerController {

	private GovernanceRegistryConnector gregConnector;

	private ApplicationConfiguration applicationConfiguration;

	private void setup(HttpServletRequest request) throws Exception {
		ServletContextHelper servletContextHelper = ServletContextHelper.initializeInstance(request.getServletContext());
		applicationConfiguration = ApplicationConfiguration.getInstance();

		String URL = applicationConfiguration.getConfiguration("GovernanceRegistryURL");
		String user = applicationConfiguration.getConfiguration("GovernanceRegistryUser");
		String password = applicationConfiguration.getConfiguration("GovernanceRegistryPassword");
		String StoreKey = servletContextHelper.getResourceRealPath(applicationConfiguration.getConfiguration("StorekeyFile"));
		String StorePassword = applicationConfiguration.getConfiguration("StorePassword");
		String StoreType = applicationConfiguration.getConfiguration("StoreType");

		gregConnector = new GovernanceRegistryConnector(URL, user, password, StoreKey, StorePassword, StoreType);

	}

	@RequestMapping(value = "/designer", method = RequestMethod.GET)
	public ModelAndView userCloudApplicationDesigner(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// initialize
		this.setup(request);

		ModelAndView model = new ModelAndView("CloudApplicationDesignerPage");

		String rootResourceCollectionPath = applicationConfiguration.getConfiguration("GovernanceRegistryRootCollectionPath");
		GovernanceRegistryReader gregReader = new GovernanceRegistryReader(this.gregConnector, rootResourceCollectionPath);

		// get categories
		ArrayList<ResourceCategoryWrapper> categories;
		try {
			categories = gregReader.getResourceCategories();
			Map<String, String> resourceCategories = new HashMap<String, String>();

			for (ResourceCategoryWrapper resourceCategory : categories) {
				resourceCategories.put(resourceCategory.getPath(), resourceCategory.getTitle());
			}
			model.addObject("resourceCategories", resourceCategories);

		} catch (RegistryException | MalformedURLException e1) {
		}

		return model;
	}

	@RequestMapping(value = "/ajax/category-resource", method = RequestMethod.POST)
	public @ResponseBody
	List<ResourceObjectMap> userCloudApplicationAjaxCategoryResource(HttpServletRequest request, HttpServletResponse response, @RequestParam("category") String category) throws Exception {

		List<ResourceObjectMap> result = new ArrayList<ResourceObjectMap>();

		// initialize
		this.setup(request);

		String rootResourceCollectionPath = applicationConfiguration.getConfiguration("GovernanceRegistryRootCollectionPath");
		GovernanceRegistryReader gregReader = new GovernanceRegistryReader(this.gregConnector, rootResourceCollectionPath);

		ArrayList<ResourceWrapper> resources = gregReader.getResources(category);

		for (ResourceWrapper resourceWrapper : resources) {
			ArrayList<String> properties = new ArrayList<>();
			try {
				ToscaFileReader toscaFileReader = new ToscaFileReader(resourceWrapper.getContentStream());
				properties = toscaFileReader.getPropertiesDefinitionNameOfSingleNodeType();
			} catch (Exception e) {
			}
			result.add(new ResourceObjectMap(resourceWrapper.getPath(), resourceWrapper.getTitle(), resourceWrapper.getName(), resourceWrapper.getUUID(), properties));
		}

		return result;
	}

	@RequestMapping(value = "/ajax/deploy", method = RequestMethod.POST)
	public @ResponseBody
	String userCloudApplicationAjaxDeploy(HttpServletRequest request, HttpServletResponse response, @RequestBody List<DesignedApplicationObjectMapItem> data) throws Exception {

		// List<String> result = new ArrayList<String>();

		// initialize
		this.setup(request);

		String rootResourceCollectionPath = applicationConfiguration.getConfiguration("GovernanceRegistryRootCollectionPath");
		GovernanceRegistryReader gregReader = new GovernanceRegistryReader(this.gregConnector, rootResourceCollectionPath);

		ToscaBuilder toscaBuilder = new ToscaBuilder(gregReader);
		// create final tosca files and store in Greg
		return toscaBuilder.build(data);

		// return result;
	}

}