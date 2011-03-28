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
package fr.paris.lutece.plugins.mytasks.business.portlet;

import fr.paris.lutece.plugins.mytasks.service.MyTasksPlugin;
import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;


/**
 *
 * MyTasksPortletHome
 *
 */
public class MyTasksPortletHome extends PortletHome
{
    // Static variable pointed at the DAO instance
    private static final String BEAN_MYTASKS_MYTASKSPORTLETDAO = "mytasks.myTasksPortletDAO";
    private static IMyTasksPortletDAO _dao = (IMyTasksPortletDAO) SpringContextService.getPluginBean( MyTasksPlugin.PLUGIN_NAME,
            BEAN_MYTASKS_MYTASKSPORTLETDAO );

    /** This class implements the Singleton design pattern. */
    private static MyTasksPortletHome _singleton;

    /**
     * Constructor
    */
    public MyTasksPortletHome(  )
    {
    }

    /**
     * Returns the instance of the MyTasksPortletHome singleton
     * @return the MyTasksPortletHome instance
     */
    public static MyTasksPortletHome getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new MyTasksPortletHome(  );
        }

        return _singleton;
    }

    /**
     * Returns the type of the portlet
     * @return The type of the portlet
     */
    public String getPortletTypeId(  )
    {
        String strCurrentClassName = this.getClass(  ).getName(  );
        String strPortletTypeId = PortletTypeHome.getPortletTypeId( strCurrentClassName );

        return strPortletTypeId;
    }

    /**
     * Returns the instance of the MyTasksPortletDAO singleton
     * @return the instance of the MyTasksPortletDAO
     */
    public IPortletInterfaceDAO getDAO(  )
    {
        return _dao;
    }
}
