;
(function() {

	// helper method to generate a color from a cycle of colors.
	/*
	 * var curColourIndex = 1, maxColourIndex = 24, nextColour = function() {
	 * var R,G,B; R = parseInt(128+Math.sin((curColourIndex*3+0)*1.3)*128); G =
	 * parseInt(128+Math.sin((curColourIndex*3+1)*1.3)*128); B =
	 * parseInt(128+Math.sin((curColourIndex*3+2)*1.3)*128); curColourIndex =
	 * curColourIndex + 1; if (curColourIndex > maxColourIndex) curColourIndex =
	 * 1; return "rgb(" + R + "," + G + "," + B + ")"; };
	 */

	window.jsPlumbDemo = {

		init : function() {

			// setup some defaults for jsPlumb.
			jsPlumb.importDefaults({
				Endpoint : [ "Dot", {
					radius : 2
				} ],
				HoverPaintStyle : {
					strokeStyle : "#1e8151",
					lineWidth : 2
				},
				ConnectionOverlays : [ [ "Arrow", {
					location : 1,
					id : "arrow",
					length : 14,
					foldback : 0.8
				} ], [ "Label", {
					label : "Relationship",
					id : "label",
					cssClass : "aLabel"
				} ] ]
			});

			var windows = $(".w");

			// initialise draggable elements.
			jsPlumb.draggable(windows, {
				containment : "parent"
			});

			// bind a click listener to each connection; the connection is
			// deleted. you could of course
			// just do this: jsPlumb.bind("click", jsPlumb.detach), but I wanted
			// to make it clear what was
			// happening.
			jsPlumb.bind("click", function(c) {
				// @todo event on click for connectors
				// jsPlumb.detach(c);

			});

			// make each ".ep" div a source and give it some parameters to work
			// with. here we tell it
			// to use a Continuous anchor and the StateMachine connectors, and
			// also we give it the
			// connector's paint style. note that in this demo the strokeStyle
			// is dynamically generated,
			// which prevents us from just setting a
			// jsPlumb.Defaults.PaintStyle. but that is what i
			// would recommend you do. Note also here that we use the 'filter'
			// option to tell jsPlumb
			// which parts of the element should actually respond to a drag
			// start.
			jsPlumb.makeSource(windows, {
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
				maxConnections : 10,
				onMaxConnections : function(info, e) {
					// alert("Maximum connections (" + info.maxConnections + ")
					// reached");
				}
			});

			// bind a connection listener. note that the parameter passed to
			// this function contains more than
			// just the new connection - see the documentation for a full list
			// of what is included in 'info'.
			// this listener sets the connection's internal
			// id as the label overlay's text.
			jsPlumb.bind("connection", function(info) {
				// @todo validate relationship

				// _jsPlumb_overlay aLabel
				

				$(info.connection.canvas.nextElementSibling).editable({
					type : 'text',
					pk : 1,
					title : 'Rename the relationship'
				});

//				$(info.connection.canvas.nextElementSibling).on('save', function(e, params) {
//					alert('Saved value: ' + params.newValue);
//
//					console.log(info);
//				});

				info.connection.getOverlay("label").setLabel(info.connection.id);
			});

			// initialise all '.w' elements as connection targets.
			jsPlumb.makeTarget(windows, {
				dropOptions : {
					hoverClass : "dragHover"
				},
				anchor : "Continuous"
			});

		}
	};
})();