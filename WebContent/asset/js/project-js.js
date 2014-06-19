jsPlumb.bind("ready", function() {

	// there's the gallery and the trash
	var $gallery = $("#projectResourceToolbox"), $trash = $("#main");

	// let the gallery items be draggable
	$("div.projectResource", $gallery).draggable({
		cancel : "a.ui-icon",
		revert : "invalid",
		containment : "document",
		helper : "clone",
		cursor : "move"
	});

	$trash.droppable({
		accept : "#projectResourceToolbox div.projectResource",
		activeClass : "ui-state-highlight",
		drop : function(event, ui) {
			// @todo validate node type
			var dropped_item = ui.draggable;
			var newNodeID = "";
			var counter = 0;
			do {
				newNodeID = 'node-' + (counter++) + '-' + $(dropped_item).attr('id');
			} while ($('#' + newNodeID).length);

			var pos = ui.draggable.offset();

			$("#main").append(
					'<div rel="' + $(dropped_item).attr('rel') + '" style="top:' + pos.top + 'px ; left:' + pos.left + 'px ;" id="' + newNodeID
							+ '" class="w">' + $(dropped_item).html()
							+ '<div class="properties-handler expand-properties"></div><div class="properties"></div><div class="ep"></div></div>');

			CloudFace.behaviors.CloudAppDesigner.createPropertiesForm(newNodeID, $(dropped_item).attr('rel'));

			jsPlumb.draggable($("#" + newNodeID), {
				containment : "parent"
			});

			jsPlumb.makeSource($("#" + newNodeID), {
				filter : ".ep", // only supported by jquery
				anchor : "Continuous",
				connector : [ "StateMachine", {
					curviness : 20
				} ],
				connectorStyle : {
					strokeStyle : "#5c96bc",
					lineWidth : 2,
					outlineColor : "transparent",
					outlineWidth : 4
				},
				maxConnections : 5,
				onMaxConnections : function(info, e) {
					alert("Maximum connections (" + info.maxConnections + ") reached");
				}
			});

			jsPlumb.makeTarget($("#" + newNodeID), {
				dropOptions : {
					hoverClass : "dragHover"
				},
				anchor : "Continuous"
			});

		}
	});

});