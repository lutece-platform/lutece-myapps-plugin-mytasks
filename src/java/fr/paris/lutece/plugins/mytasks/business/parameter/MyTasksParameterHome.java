/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
package fr.paris.lutece.plugins.mytasks.business.parameter;

import fr.paris.lutece.plugins.mytasks.service.MyTasksPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;


/**
 *
 * MyTasksParameterHome
 *
 */
public final class MyTasksParameterHome
{
    // Static variable pointed at the DAO instance
    private static final String BEAN_MYTASKS_MYTASKSPARAMETERDAO = "mytasks.myTasksParameterDAO";
    private static Plugin _plugin = PluginService.getPlugin( MyTasksPlugin.PLUGIN_NAME );
    private static IMyTasksParameterDAO _dao = (IMyTasksParameterDAO) SpringContextService.getPluginBean( MyTasksPlugin.PLUGIN_NAME,
            BEAN_MYTASKS_MYTASKSPARAMETERDAO );

    /**
     * Constructor
     */
    private MyTasksParameterHome(  )
    {
    }

    /**
     * Load all the parameter default values
     * @return a list of ReferenceItem
     */
    public static ReferenceList findAll(  )
    {
        return _dao.selectAll( _plugin );
    }

    /**
    * Load the parameter value
    * @param strParameterKey the parameter key
    * @return The parameter value
    */
    public static ReferenceItem findByKey( String strParameterKey )
    {
        return _dao.load( strParameterKey, _plugin );
    }

    /**
     * Update the parameter
     * @param param The parameter
     */
    public static void update( ReferenceItem param )
    {
        _dao.store( param, _plugin );
    }
}
