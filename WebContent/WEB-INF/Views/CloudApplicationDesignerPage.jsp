<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
<title>Cloud REA Model | Cloud Application Designer</title>


<link href="<c:url value='/asset/js-lib/ui-lightness/jquery-ui-1.9.2.custom.min.css'/>" rel="stylesheet" type="text/css">


<link href="<c:url value='/asset/css/demo-all.css'/>" rel="stylesheet" type="text/css">
<link href="<c:url value='/asset/css/demo.css'/>" rel="stylesheet" type="text/css">
<link href="<c:url value='/asset/css/project-js.css'/>" rel="stylesheet" type="text/css">

<!-- DEP -->
<script src="<c:url value='/asset/js-lib/jquery-1.9.0.js'/>" type="text/javascript"></script>
<script src="<c:url value='/asset/js-lib/jquery-ui-1.9.2.custom.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/asset/js-lib/jquery.ui.touch-punch.min.js'/>" type="text/javascript"></script>
<!-- /DEP -->
<!-- JS -->
<!-- support lib for bezier stuff -->
<script src="<c:url value='/asset/js-lib/jsBezier-0.6.js'/>" type="text/javascript"></script>
<!-- jsplumb geom functions -->
<script src="<c:url value='/asset/js-lib/jsplumb-geom-0.1.js'/>" type="text/javascript"></script>
<!-- jsplumb util -->
<script src="<c:url value='/asset/js-lib/util.js'/>" type="text/javascript"></script>
<!-- base DOM adapter -->
<script src="<c:url value='/asset/js-lib/dom-adapter.js'/>" type="text/javascript"></script>
<!-- main jsplumb engine -->
<script src="<c:url value='/asset/js-lib/jsPlumb.js'/>" type="text/javascript"></script>
<!-- endpoint -->
<script src="<c:url value='/asset/js-lib/endpoint.js'/>" type="text/javascript"></script>
<!-- connection -->
<script src="<c:url value='/asset/js-lib/connection.js'/>" type="text/javascript"></script>
<!-- anchors -->
<script src="<c:url value='/asset/js-lib/anchors.js'/>" type="text/javascript"></script>
<!-- connectors, endpoint and overlays  -->
<script src="<c:url value='/asset/js-lib/defaults.js'/>" type="text/javascript"></script>
<!-- bezier connectors -->
<script src="<c:url value='/asset/js-lib/connectors-bezier.js'/>" type="text/javascript"></script>
<!-- state machine connectors -->
<script src="<c:url value='/asset/js-lib/connectors-statemachine.js'/>" type="text/javascript"></script>
<!-- flowchart connectors -->
<script src="<c:url value='/asset/js-lib/connectors-flowchart.js'/>" type="text/javascript"></script>
<!-- SVG renderer -->
<script src="<c:url value='/asset/js-lib/renderers-svg.js'/>" type="text/javascript"></script>
<!-- canvas renderer -->
<script src="<c:url value='/asset/js-lib/renderers-canvas.js'/>" type="text/javascript"></script>
<!-- vml renderer -->
<script src="<c:url value='/asset/js-lib/renderers-vml.js'/>" type="text/javascript"></script>
<!-- jquery jsPlumb adapter -->
<script src="<c:url value='/asset/js-lib/jquery.jsPlumb.js'/>" type="text/javascript"></script>
<!-- jquery dForm -->
<script src="<c:url value='/asset/js-lib/jquery.dform-1.1.0.min.js'/>" type="text/javascript"></script>

<!-- /JS -->


<link href="<c:url value='/asset/js-lib/jqueryui-editable/css/jqueryui-editable.css'/>" rel="stylesheet">
<script src="<c:url value='/asset/js-lib/jqueryui-editable/js/jqueryui-editable.min.js'/>" type="text/javascript"></script>

</head>

<body>

	<div id="wrapper">

		<header id="header">
			<div class="header-inner">
				<hgroup>
					<h1>Cloud REA Model</h1>
				</hgroup>
				<section id="operationsMenu">
					<a id="deployOperator" rel="command:deploy" href="#">Generate CSAR & Register</a>
				</section>
				<div class="clear"></div>
			</div>
		</header>

		<div id="cloudAppDesignerInterface">
			<section id="projectResourceToolbox">
				<ul id="resource-categories-list">
					<c:forEach var="category" items="${resourceCategories}">
						<li class="resource-category" rel="resource-category:${category.key}">
							<h3 class="category-title">${category.value}</h3>
							<div class="resource-children"></div>
						</li>
					</c:forEach>
				</ul>

			</section>
			<section id="cloudAppDesignerArea">

				<div id="main">
					<div id="render"></div>



				</div>
			</section>
			<div class="clear"></div>
		</div>
	</div>

	<footer id="footer"> CREAM &copy; 2013 </footer>


	<script src="<c:url value='/asset/js/demo-jquery.js'/>" type="text/javascript"></script>
	<script src="<c:url value='/asset/js/demo-list.js'/>" type="text/javascript"></script>
	<script src="<c:url value='/asset/js/demo-helper-jquery.js'/>" type="text/javascript"></script>
	<script src="<c:url value='/asset/js/project-js.js'/>" type="text/javascript"></script>
	<script src="<c:url value='/asset/js/cloudResource.js'/>" type="text/javascript"></script>
	<script type="text/javascript">
	<!--//--><![CDATA[//><!--
		jQuery.extend(CloudFace.settings, {
			"getCategoryResourcePath" : "${pageContext.request.contextPath}/user/cloud-app/ajax/category-resource",
			"deploy" : "${pageContext.request.contextPath}/user/cloud-app/ajax/deploy",
		});
		//--><!]]>
	</script>


</body>

</html>
