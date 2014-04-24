/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.mytasks.business.portlet;

import fr.paris.lutece.plugins.mytasks.business.MyTask;
import fr.paris.lutece.plugins.mytasks.service.MyTasksService;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.xml.XmlUtil;

import org.apache.commons.lang.StringUtils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * MyTasksPortlet
 *
 */
public class MyTasksPortlet extends Portlet
{
    // CONSTANTS
    private static final int TRUE = 1;
    private static final int FALSE = 0;

    // TAGS
    private static final String TAG_MYTASKS_LIST = "mytasks-list";
    private static final String TAG_MYTASK_URL_RETURN = "mytasks-url-return";
    private static final String TAG_MYTASK = "mytask";
    private static final String TAG_ID_MYTASK = "id-mytask";
    private static final String TAG_MYTASK_NAME = "mytask-name";
    private static final String TAG_MYTASK_DATE = "mytask-date";
    private static final String TAG_MYTASK_IS_DONE = "mytask-is-done";

    /**
     * Sets the identifier of the portlet type to the value specified in the MyTasksPortletHome class
     */
    public MyTasksPortlet(  )
    {
    }

    /**
     * Returns the XML content of the portlet without the XML header
     * @param request The HTTP Servlet request
     * @return The Xml content of this portlet
     */
    public String getXml( HttpServletRequest request )
    {
        String strUserName = null;
        LuteceUser user = null;

        if ( request != null )
        {
            try
            {
                user = SecurityService.getInstance(  ).getRemoteUser( request );

                if ( user != null )
                {
                    strUserName = user.getName(  );
                }
                else
                {
                    strUserName = StringUtils.EMPTY;
                }
            }
            catch ( UserNotSignedException e )
            {
                strUserName = StringUtils.EMPTY;
            }
        }
        else
        {
            strUserName = StringUtils.EMPTY;
        }

        StringBuffer sbXml = new StringBuffer(  );

        if ( StringUtils.isNotBlank( strUserName ) )
        {
            List<MyTask> listMyTasks = MyTasksService.getInstance(  ).getMyTasksList( user );
            XmlUtil.beginElement( sbXml, TAG_MYTASKS_LIST );
            XmlUtil.addElement( sbXml, TAG_MYTASK_URL_RETURN, buildUrl( request ) );

            for ( MyTask myTask : listMyTasks )
            {
                String strLocalizedDate = DateUtil.getDateString( myTask.getDate(  ), request.getLocale(  ) );
                int nIsDone = myTask.isDone(  ) ? TRUE : FALSE;

                XmlUtil.beginElement( sbXml, TAG_MYTASK );

                XmlUtil.addElement( sbXml, TAG_ID_MYTASK, myTask.getIdMyTask(  ) );
                XmlUtil.addElement( sbXml, TAG_MYTASK_NAME, myTask.getName(  ) );
                XmlUtil.addElement( sbXml, TAG_MYTASK_DATE, strLocalizedDate );
                XmlUtil.addElement( sbXml, TAG_MYTASK_IS_DONE, nIsDone );

                XmlUtil.endElement( sbXml, TAG_MYTASK );
            }

            XmlUtil.endElement( sbXml, TAG_MYTASKS_LIST );
        }

        return addPortletTags( sbXml );
    }

    /**
     * Updates the current instance of the portlet object
     */
    public void update(  )
    {
    }

    /**
     * Removes the current instance of the  the portlet  object
     */
    public void remove(  )
    {
    }

    /**
     * Returns the XML content of the portlet with the XML header
     * @param request The HTTP Servlet request
     * @return The XML content of this portlet
     */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Build the url return
     * @param request {@link HttpServletRequest}
     * @return the url return
     */
    private String buildUrl( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( request.getRequestURI(  ) );

        String strPageId = request.getParameter( Parameters.PAGE_ID );

        if ( StringUtils.isNotBlank( strPageId ) )
        {
            url.addParameter( Parameters.PAGE_ID, strPageId );
        }

        return url.getUrl(  );
    }
}
