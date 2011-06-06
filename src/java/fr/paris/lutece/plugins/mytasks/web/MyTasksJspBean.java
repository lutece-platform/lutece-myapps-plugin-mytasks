/*
 * Copyright (c) 2002-2011, Mairie de Paris
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
import fr.paris.lutece.plugins.mytasks.service.MyTasksResourceIdService;
import fr.paris.lutece.plugins.mytasks.service.parameter.MyTasksParameterService;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * MyTasksJspBean
 *
 */
public class MyTasksJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";

    // CONSTANTS
    private static final String ZERO = "0";

    // PARAMETERS
    private static final String PARAMETER_NB_MYTASKS_MAX = "nb_mytasks_max";

    // JSP
    private static final String JSP_ADMIN_HOME = "jsp/admin/AdminMenu.jsp";

    /**
     * Modify the parameter default values
     * @param request {@link HttpServletRequest}
     * @return the url return
     * @throws AccessDeniedException access denied if the user does not have the permission
     */
    public String doModifyMyTasksParameterDefaultValues( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( MyTask.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    MyTasksResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }

        ReferenceList listParams = MyTasksParameterService.getInstance(  ).getParamDefaultValues(  );

        for ( ReferenceItem param : listParams )
        {
            String strParamValue = request.getParameter( param.getCode(  ) );

            if ( StringUtils.isBlank( strParamValue ) && !PARAMETER_NB_MYTASKS_MAX.equals( param.getCode(  ) ) )
            {
                strParamValue = ZERO;
            }

            param.setName( strParamValue );
            MyTasksParameterService.getInstance(  ).update( param );
        }

        return AppPathService.getBaseUrl( request ) + JSP_ADMIN_HOME;
    }
}
