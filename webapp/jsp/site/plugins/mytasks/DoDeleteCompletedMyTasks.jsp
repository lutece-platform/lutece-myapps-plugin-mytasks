
<jsp:useBean id="myTasksApp" scope="request" class="fr.paris.lutece.plugins.mytasks.web.MyTasksApp" />

<%
    response.sendRedirect( myTasksApp.doDeleteCompletedMyTasks( request ));
%>
