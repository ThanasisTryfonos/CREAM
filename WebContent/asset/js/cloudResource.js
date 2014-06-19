var CloudFace = CloudFace || {
	'settings' : {},
	'behaviors' : {}
};

(function($) {

	$(document).ready(function() {
		CloudFace.behaviors.CloudAppDesigner.attach();
	});

	CloudFace.behaviors.CloudAppDesigner = {
		attach : function(context, settings) {
			$('#resource-categories-list').accordion({
				clearStyle : true,
				autoHeight : false
			});

			$('ul#resource-categories-list  li.resource-category h3').click(this.openCategory);

			// make ajax call to get first category
			this.firstOpenCategoryRequest();

			$('section#operationsMenu a').click(this.operationMenuItemClicked);
		},

		operationMenuItemClicked : function() {
			var command = $(this).attr('rel');
			command = command.replace("command:", "");
			var designedApp = CloudFace.behaviors.CloudAppDesigner.getDesignedApplication();
			$.ajax({
				url : CloudFace.settings[command],
				type : "POST",
				contentType : "application/json; charset=utf-8",
				dataType : 'json',
				data : JSON.stringify(designedApp),
				success : function(data) {
					// CloudFace.behaviors.CloudAppDesigner.ajaxRenderResourceCategory(data,
					// categoryID);
					console.log(data);
				}
			});
		},

		getDesignedApplication : function() {
			var jsPlumbConnections = jsPlumb.getAllConnections();
			var connections = [];
			if (jsPlumbConnections != null) {
				$.each(jsPlumbConnections, function(index, value) {
					var relatioshShipName = $(value.canvas.nextElementSibling.firstChild).text();

					var propertiesSource = [];
					$(value.source).find('.properties form input').each(function(index, value) {
						if ($(value).val() != null)
							propertiesSource.push({
								name : $(value).attr('name'),
								data : $(value).val()
							});
					});

					var propertiesTarget = [];
					$(value.target).find('.properties form input').each(function(index, value) {
						if ($(value).val() != null)
							propertiesTarget.push({
								name : $(value).attr('name'),
								data : $(value).val()
							});
					});

					var connectionObject = {
						source : {
							id : $(value.source).attr('id'),
							pathID : $(value.source).attr('rel').replace("resource:", ""),
							properties : propertiesSource,
						},
						target : {
							id : $(value.target).attr('id'),
							pathID : $(value.target).attr('rel').replace("resource:", ""),
							properties : propertiesTarget,
						},
						id : value.id,
						name : relatioshShipName
					};
					connections.push(connectionObject);
				});
			}
			return connections;
		},

		firstOpenCategoryRequest : function() {
			$('ul#resource-categories-list li.resource-category h3').first().trigger('click');
		},

		openCategory : function() {
			if ($(this).next().children().length < 1 || $(this).next().find('.ajaxLoad').length != 0) {
				$(this).next().empty();
				$(this).next().append('<div class="ajaxLoad"></div>');
				var firstResourceCategoryID = $(this).parent().attr('rel');
				firstResourceCategoryID = firstResourceCategoryID.replace("resource-category:", "");
				CloudFace.behaviors.CloudAppDesigner.getCategoryResourcePath(firstResourceCategoryID);
			}
		},

		getCategoryResourcePath : function(categoryID) {
			$.ajax({
				url : CloudFace.settings.getCategoryResourcePath,
				type : "POST",
				data : {
					category : categoryID
				},
				success : function(data) {
					CloudFace.behaviors.CloudAppDesigner.ajaxRenderResourceCategory(data, categoryID);

				}
			});
		},

		ajaxRenderResourceCategory : function(data, categoryID) {
			var listItemRel = "resource-category:" + categoryID;
			$('ul#resource-categories-list  li.resource-category[rel="' + listItemRel + '"] div.resource-children').empty();
			if (data != null) {

				// store data in DOM
				if (CloudFace.settings.CategoryResourceData == null)
					CloudFace.settings.CategoryResourceData = [];

				$.each(data, function(index, value) {

					/*
					 * if(CloudFace.settings.CategoryResourceData[categoryID] ==
					 * null) CloudFace.settings.CategoryResourceData[categoryID] =
					 * [];
					 * 
					 * CloudFace.settings.CategoryResourceData[categoryID][value.pathID] =
					 * value;
					 */
					CloudFace.settings.CategoryResourceData[value.pathID] = value;

					$('ul#resource-categories-list  li.resource-category[rel="' + listItemRel + '"] div.resource-children').append(
							'<div id="' + value.id + '" class="projectResource ' + value.name + '" rel="resource:' + value.pathID + '">' + value.title
									+ '</div>');

					CloudFace.behaviors.CloudAppDesigner.makeProjectResourceDivDraggable(value.pathID);
				});
			}

		},

		makeProjectResourceDivDraggable : function(id) {
			$('div.projectResource[rel="resource:' + id + '"]', $("#projectResourceToolbox")).draggable({
				cancel : "a.ui-icon",
				revert : "invalid",
				containment : "document",
				helper : "clone",
				cursor : "move"
			});
		},

		createPropertiesForm : function(canvasResourceID, relAttrWithPathID) {
			relAttrWithPathID = relAttrWithPathID.replace("resource:", "");
			var propertiesData = CloudFace.settings.CategoryResourceData[relAttrWithPathID].properties;

			var html = [];

			if (propertiesData != null) {

				$.each(propertiesData, function(index, value) {
					var formItem = {
						"name" : value,
						"caption" : value,
						"type" : "text",
					};
					html.push(formItem);
				});

				$("#" + canvasResourceID + ' .properties').dform({
					"html" : html
				});

				$("#" + canvasResourceID + ' .properties-handler.expand-properties').click(function() {
					$("#" + canvasResourceID + ' .properties').slideToggle();
				});

			}
		},
	};

})(jQuery);
