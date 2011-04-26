/*
 * Copyright (c) 2002-2010, Mairie de Paris
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
package fr.paris.lutece.plugins.mytasks.web;

import fr.paris.lutece.plugins.mytasks.business.MyTask;
import fr.paris.lutece.plugins.mytasks.service.MyTasksService;
import fr.paris.lutece.plugins.mytasks.service.parameter.MyTasksParameterService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.page.PageNotFoundException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.sql.Date;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides a simple implementation of an XPage
 */
public class MyTasksApp implements XPageApplication
{
    // CONSTANTS
    private static final int ALL_INT = -1;

    // TEMPLATES
    private static final String TEMPLATE_MYTASKS_PAGE = "skin/plugins/mytasks/mytasks.html";
    private static final String TEMPLATE_ADD_MYTASK_PAGE = "skin/plugins/mytasks/add_mytask.html";
    private static final String TEMPLATE_EDIT_MYTASK_PAGE = "skin/plugins/mytasks/update_mytask.html";

    // PARAMETERS
    private static final String PARAMETER_ACTION = "action";
    private static final String PARAMETER_ID_MYTASK = "id_mytask";
    private static final String PARAMETER_MYTASK_NAME = "mytask_name";
    private static final String PARAMETER_MYTASK_DATE = "mytask_date";
    private static final String PARAMETER_MYTASK_URL_RETURN = "mytasks_url_return";
    private static final String PARAMETER_NB_MYTASKS_MAX = "nb_mytasks_max";

    // MARKS
    private static final String MARK_MYTASKS_LIST = "mytasks_list";
    private static final String MARK_MYTASK = "mytask";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_MYTASKS_URL_RETURN = "mytasks_url_return";

    // PROPERTIES
    private static final String PROPERTY_PAGE_PATH = "mytasks.mytasks.pagePathLabel";
    private static final String PROPERTY_PAGE_TITLE = "mytasks.mytasks.pageTitle";
    private static final String PROPERTY_ADD_MYTASK_PAGE_TITLE = "mytasks.add_mytask.pageTitle";
    private static final String PROPERTY_UPDATE_MYTASK_PAGE_TITLE = "mytasks.update_mytask.pageTitle";
    private static final String PROPERTY_MAX_LENGTH = "mytasks.maxLength";

    // ACTIONS
    private static final String ACTION_ADD_MYTASK = "add_mytask";
    private static final String ACTION_UPDATE_MYTASK = "update_mytask";
    private static final String ACTION_DO_ADD_MYTASK = "do_add_mytask";
    private static final String ACTION_DO_UPDATE_MYTASK = "do_update_mytask";
    private static final String ACTION_DO_REMOVE_MYTASK = "do_remove_mytask";
    private static final String ACTION_DO_CHANGE_MYTASKS_STATUS = "do_change_mytasks_status";
    private static final String ACTION_DO_DELETE_COMPLETED_MYTASKS = "do_delete_completed_mytasks";

    // MESSAGES
    private static final String MESSAGE_ERROR_DATEFORMAT = "mytasks.message.error.dateFormat";
    private static final String MESSAGE_NB_MYTASKS_MAX = "mytasks.message.nbMyTasksMax";
    private static final String MESSAGE_MAX_LENGTH = "mytasks.message.maxLength";

    // private fields
    private MyTasksService _myTasksService = MyTasksService.getInstance(  );

    /**
     * Returns the content of the page myportal.
     * @param request The http request
     * @param nMode The current mode
     * @param plugin The plugin object
     * @return the {@link XPage}
     * @throws SiteMessageException Message displayed if an exception occurs
     * @throws UserNotSignedException exception if user not connected
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
        throws SiteMessageException, UserNotSignedException
    {
        XPage page = null;

        String strAction = request.getParameter( PARAMETER_ACTION );

        if ( StringUtils.isNotBlank( strAction ) )
        {
            if ( ACTION_ADD_MYTASK.equals( strAction ) )
            {
                page = getAddMyTaskPage( request );
            }
            else if ( ACTION_UPDATE_MYTASK.equals( strAction ) )
            {
                page = getUpdateMyTaskPage( request );
            }
            else
            {
                doActionMyTask( request );
            }
        }

        if ( page == null )
        {
            page = getMyTasksPage( request );
        }

        return page;
    }

    /**
     * Get the MyTasks page
     * @param request {@link HttpServletRequest}
     * @return the {@link XPage}
     * @throws UserNotSignedException exception if user not connected
     */
    private XPage getMyTasksPage( HttpServletRequest request )
        throws UserNotSignedException
    {
        XPage page = new XPage(  );
        LuteceUser user = getUser( request );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_MYTASKS_LIST, _myTasksService.getMyTasksList( user ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MYTASKS_PAGE, request.getLocale(  ), model );

        page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, request.getLocale(  ) ) );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PAGE_PATH, request.getLocale(  ) ) );
        page.setContent( template.getHtml(  ) );

        return page;
    }

    /**
     * Get the AddMyTask Page
     * @param request {@link HttpServletRequest}
     * @return the {@link XPage}
     * @throws UserNotSignedException exception if user not connected
     */
    private XPage getAddMyTaskPage( HttpServletRequest request )
        throws UserNotSignedException
    {
        XPage page = new XPage(  );
        // Check if the user is indeed connected
        getUser( request );

        String strUrlReturn = request.getParameter( PARAMETER_MYTASK_URL_RETURN );

        if ( StringUtils.isBlank( strUrlReturn ) )
        {
            strUrlReturn = StringUtils.EMPTY;
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_LOCALE, request.getLocale(  ) );
        model.put( MARK_MYTASKS_URL_RETURN, strUrlReturn );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADD_MYTASK_PAGE, request.getLocale(  ), model );

        page.setTitle( I18nService.getLocalizedString( PROPERTY_ADD_MYTASK_PAGE_TITLE, request.getLocale(  ) ) );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PAGE_PATH, request.getLocale(  ) ) );
        page.setContent( template.getHtml(  ) );

        return page;
    }

    /**
     * Get the update MyTask page
     * @param request {@link HttpServletRequest}
     * @return the {@link XPage}
     * @throws UserNotSignedException exception if user not connected
     * @throws SiteMessageException Message displayed if an exception occurs
     */
    private XPage getUpdateMyTaskPage( HttpServletRequest request )
        throws SiteMessageException, UserNotSignedException
    {
        XPage page = new XPage(  );
        // Check if the user is indeed connected
        getUser( request );

        String strIdMyTask = request.getParameter( PARAMETER_ID_MYTASK );
        String strUrlReturn = request.getParameter( PARAMETER_MYTASK_URL_RETURN );

        if ( StringUtils.isBlank( strUrlReturn ) )
        {
            strUrlReturn = StringUtils.EMPTY;
        }

        if ( StringUtils.isNotBlank( strIdMyTask ) )
        {
            int nIdMyTask = Integer.parseInt( strIdMyTask );
            MyTask myTask = _myTasksService.getMyTask( nIdMyTask );

            Map<String, Object> model = new HashMap<String, Object>(  );
            model.put( MARK_MYTASK, myTask );
            model.put( MARK_LOCALE, request.getLocale(  ) );
            model.put( MARK_MYTASKS_URL_RETURN, strUrlReturn );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_EDIT_MYTASK_PAGE, request.getLocale(  ),
                    model );

            page.setTitle( I18nService.getLocalizedString( PROPERTY_UPDATE_MYTASK_PAGE_TITLE, request.getLocale(  ) ) );
            page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PAGE_PATH, request.getLocale(  ) ) );
            page.setContent( template.getHtml(  ) );
        }
        else
        {
            SiteMessageService.setMessage( request, Messages.MANDATORY_FIELDS, SiteMessage.TYPE_ERROR );
        }

        return page;
    }

    /**
     * Do action MyTask
     * @param request {@link HttpServletRequest
     * @return the url return from the parameter
     * @throws UserNotSignedException exception if user not connected
     * @throws SiteMessageException Message displayed if an exception occurs
     */
    public String doActionMyTask( HttpServletRequest request )
        throws SiteMessageException, UserNotSignedException
    {
        String strUrlReturn = request.getParameter( PARAMETER_MYTASK_URL_RETURN );

        if ( StringUtils.isBlank( strUrlReturn ) )
        {
            strUrlReturn = AppPathService.getPortalUrl(  );
        }

        String strAction = request.getParameter( PARAMETER_ACTION );

        if ( StringUtils.isNotBlank( strAction ) )
        {
            if ( ACTION_DO_ADD_MYTASK.equals( strAction ) )
            {
                doAddMyTask( request );
            }
            else if ( ACTION_DO_UPDATE_MYTASK.equals( strAction ) )
            {
                doUpdateMyTask( request );
            }
            else if ( ACTION_DO_REMOVE_MYTASK.equals( strAction ) )
            {
                doRemoveMyTask( request );
            }
            else if ( ACTION_DO_CHANGE_MYTASKS_STATUS.equals( strAction ) )
            {
                doChangeMyTaskStatus( request );
            }
            else if ( ACTION_DO_DELETE_COMPLETED_MYTASKS.equals( strAction ) )
            {
                doDeleteCompletedMyTasks( request );
            }
        }

        return strUrlReturn;
    }

    /**
     * Do add a my task
     * @param request {@link HttpServletRequest}
     * @throws UserNotSignedException exception if user not connected
     * @throws SiteMessageException Message displayed if an exception occurs
     */
    public void doAddMyTask( HttpServletRequest request )
        throws SiteMessageException, UserNotSignedException
    {
        LuteceUser user = getUser( request );
        String strName = request.getParameter( PARAMETER_MYTASK_NAME );
        String strDate = request.getParameter( PARAMETER_MYTASK_DATE );

        if ( StringUtils.isNotBlank( strName ) && StringUtils.isNotBlank( strDate ) )
        {
            int nMaxLength = AppPropertiesService.getPropertyInt( PROPERTY_MAX_LENGTH, 50 );

            if ( strName.length(  ) < nMaxLength )
            {
                ReferenceItem nbTasksMax = MyTasksParameterService.getInstance(  )
                                                                  .getParamDefaultValue( PARAMETER_NB_MYTASKS_MAX );
                int nNbMyTasksMax = ALL_INT;

                if ( ( nbTasksMax != null ) && StringUtils.isNotBlank( nbTasksMax.getName(  ) ) &&
                        StringUtils.isNumeric( nbTasksMax.getName(  ) ) )
                {
                    nNbMyTasksMax = Integer.parseInt( nbTasksMax.getName(  ) );
                }

                int nNbMyTasks = _myTasksService.getNbMyTasks( user );

                if ( ( nNbMyTasksMax == ALL_INT ) || ( nNbMyTasks < nNbMyTasksMax ) )
                {
                    Date date = DateUtil.formatDateSql( strDate, request.getLocale(  ) );

                    if ( date != null )
                    {
                        MyTask myTask = new MyTask(  );
                        myTask.setName( strName );
                        myTask.setDate( date );
                        _myTasksService.doAddMyTask( myTask, user );
                    }
                    else
                    {
                        SiteMessageService.setMessage( request, MESSAGE_ERROR_DATEFORMAT, SiteMessage.TYPE_ERROR );
                    }
                }
                else
                {
                    Object[] params = { nNbMyTasksMax };
                    SiteMessageService.setMessage( request, MESSAGE_NB_MYTASKS_MAX, params, SiteMessage.TYPE_ERROR );
                }
            }
            else
            {
                Object[] params = { nMaxLength };
                SiteMessageService.setMessage( request, MESSAGE_MAX_LENGTH, params, SiteMessage.TYPE_ERROR );
            }
        }
        else
        {
            SiteMessageService.setMessage( request, Messages.MANDATORY_FIELDS, SiteMessage.TYPE_ERROR );
        }
    }

    /**
     * Do update a MyTask
     * @param request {@link HttpServletRequest}
     * @throws UserNotSignedException exception if user not connected
     * @throws SiteMessageException Message displayed if an exception occurs
     */
    public void doUpdateMyTask( HttpServletRequest request )
        throws SiteMessageException, UserNotSignedException
    {
        LuteceUser user = getUser( request );
        String strIdMyTask = request.getParameter( PARAMETER_ID_MYTASK );
        String strName = request.getParameter( PARAMETER_MYTASK_NAME );
        String strDate = request.getParameter( PARAMETER_MYTASK_DATE );

        if ( StringUtils.isNotBlank( strIdMyTask ) && StringUtils.isNotBlank( strName ) &&
                StringUtils.isNotBlank( strDate ) )
        {
            int nIdMyTask = Integer.parseInt( strIdMyTask );
            MyTask myTask = _myTasksService.getMyTask( nIdMyTask );

            if ( myTask != null )
            {
                int nMaxLength = AppPropertiesService.getPropertyInt( PROPERTY_MAX_LENGTH, 50 );

                if ( strName.length(  ) < nMaxLength )
                {
                    Date date = DateUtil.formatDateSql( strDate, request.getLocale(  ) );

                    if ( date != null )
                    {
                        myTask.setIdMyTask( nIdMyTask );
                        myTask.setName( strName );
                        myTask.setDate( date );
                        _myTasksService.doUpdateMyTask( myTask, user );
                    }
                    else
                    {
                        SiteMessageService.setMessage( request, MESSAGE_ERROR_DATEFORMAT, SiteMessage.TYPE_ERROR );
                    }
                }
                else
                {
                    Object[] params = { nMaxLength };
                    SiteMessageService.setMessage( request, MESSAGE_MAX_LENGTH, params, SiteMessage.TYPE_ERROR );
                }
            }
        }
        else
        {
            SiteMessageService.setMessage( request, Messages.MANDATORY_FIELDS, SiteMessage.TYPE_ERROR );
        }
    }

    /**
     * Do remove a MyTask
     * @param request {@link HttpServletRequest}
     * @throws UserNotSignedException exception if user not connected
     * @throws SiteMessageException Message displayed if an exception occurs
     */
    public void doRemoveMyTask( HttpServletRequest request )
        throws SiteMessageException, UserNotSignedException
    {
        LuteceUser user = getUser( request );
        String strIdMyTask = request.getParameter( PARAMETER_ID_MYTASK );

        if ( StringUtils.isNotBlank( strIdMyTask ) )
        {
            int nIdMyTask = Integer.parseInt( strIdMyTask );
            _myTasksService.doRemoveMyTask( nIdMyTask, user );
        }
        else
        {
            SiteMessageService.setMessage( request, Messages.MANDATORY_FIELDS, SiteMessage.TYPE_ERROR );
        }
    }

    /**
     * Do change the mytask ids
     * @param request {@link HttpServletRequest}
     * @throws UserNotSignedException exception if user not connected
     */
    public void doChangeMyTaskStatus( HttpServletRequest request )
        throws UserNotSignedException
    {
        LuteceUser user = getUser( request );
        String[] strIdsMyTask = request.getParameterValues( PARAMETER_ID_MYTASK );
        _myTasksService.doUpdateMyTasksStatus( strIdsMyTask, user );
    }

    /**
     * Do delete the completed tasks
     * @param request {@link HttpServletRequest}
     * @throws UserNotSignedException exception if user not connected
     */
    public void doDeleteCompletedMyTasks( HttpServletRequest request )
        throws UserNotSignedException
    {
        LuteceUser user = getUser( request );
        _myTasksService.doDeleteCompletedMyTasks( user );
    }

    /**
     * Gets the user from the request
     * @param request The HTTP user
     * @return The Lutece User
     * @throws UserNotSignedException exception if user not connected
     */
    public LuteceUser getUser( HttpServletRequest request )
        throws UserNotSignedException
    {
        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRemoteUser( request );

            if ( user == null )
            {
                throw new UserNotSignedException(  );
            }

            return user;
        }
        else
        {
            throw new PageNotFoundException(  );
        }
    }
}
