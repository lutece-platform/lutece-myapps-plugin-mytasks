<%@ page import="fr.paris.lutece.portal.service.security.UserNotSignedException" %>
<%@ page import="fr.paris.lutece.portal.service.security.SecurityService" %>
<%@page import="fr.paris.lutece.portal.service.page.PageNotFoundException"%>
<%@page import="fr.paris.lutece.portal.service.message.SiteMessageException"%>
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>
<%@page import="fr.paris.lutece.portal.web.PortalJspBean"%>
<jsp:useBean id="myTasksApp" scope="request" class="fr.paris.lutece.plugins.mytasks.web.MyTasksApp" />

<%
	try
	{
		response.sendRedirect( myTasksApp.doActionMyTask( request ));	
	}
	catch ( PageNotFoundException pnfe )
	{
		response.sendError( response.SC_NOT_FOUND );
	}
	catch( SiteMessageException lme )
	{
		response.sendRedirect( AppPathService.getBaseUrl( request ) );
	}
    catch ( UserNotSignedException e )
	{
%>
		<center>
			<br />
			<br />
			<h3>
				Error : This page requires an authenticated user identified
			</h3>
		</center>
<%
	}
%>
