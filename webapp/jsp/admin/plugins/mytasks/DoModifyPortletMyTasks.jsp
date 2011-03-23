<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="myTasksPortlet" scope="session" class="fr.paris.lutece.plugins.mytasks.web.portlet.MyTasksPortletJspBean" />

<%
	myTasksPortlet.init( request, myTasksPortlet.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( myTasksPortlet.doModify( request ) );
%>
