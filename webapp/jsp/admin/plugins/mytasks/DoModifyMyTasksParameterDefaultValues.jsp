<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="myTasks" scope="session" class="fr.paris.lutece.plugins.mytasks.web.MyTasksJspBean" />
<% 
	myTasks.init( request, myTasks.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( myTasks.doModifyMyTasksParameterDefaultValues( request ));
%>
