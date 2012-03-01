/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.mytasks.business;

import fr.paris.lutece.plugins.mytasks.service.MyTasksPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 * MyTasksHome
 *
 */
public final class MyTaskHome
{
    // Static variable pointed at the DAO instance
    private static final String BEAN_MYTASKS_MYTASKDAO = "mytasks.myTaskDAO";
    private static Plugin _plugin = PluginService.getPlugin( MyTasksPlugin.PLUGIN_NAME );
    private static IMyTaskDAO _dao = (IMyTaskDAO) SpringContextService.getPluginBean( MyTasksPlugin.PLUGIN_NAME,
            BEAN_MYTASKS_MYTASKDAO );

    /**
     * Private constructor - this class need not be instantiated
     */
    private MyTaskHome(  )
    {
    }

    /**
     * Generates a new primary key
     * @return The new primary key
     */
    public static int newPrimaryKey(  )
    {
        return _dao.newPrimaryKey( _plugin );
    }

    /**
     * Create an instance of the MyTask class
     * @param myTask The instance of the MyTask which contains the informations to store
     * @return The instance of MyTask which has been created with its primary key.
     */
    public static MyTask create( MyTask myTask )
    {
        _dao.insert( myTask, _plugin );

        return myTask;
    }

    /**
     * Update of the widget which is specified in parameter
     * @param myTask The instance of the MyTask which contains the data to store
     * @return The instance of the MyTask which has been updated
     */
    public static MyTask update( MyTask myTask )
    {
        _dao.store( myTask, _plugin );

        return myTask;
    }

    /**
     * Remove the widget whose identifier is specified in parameter
     * @param nIdMyTask The Id of the task
     */
    public static void remove( int nIdMyTask )
    {
        _dao.delete( nIdMyTask, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a MyTask whose identifier is specified in parameter
     * @param nIdMyTask The MyTask primary key
     * @return an instance of MyTask
     */
    public static MyTask findByPrimaryKey( int nIdMyTask )
    {
        return _dao.load( nIdMyTask, _plugin );
    }

    /**
     * Load the list of MyTask from a given user guid
     * @param strUserGuid the user guid
     * @return a list of {@link MyTask}
     */
    public static List<MyTask> selectMyTasksListFromUser( String strUserGuid )
    {
        return _dao.selectMyTasksListFromUser( strUserGuid, _plugin );
    }

    /**
     * Insert a new record in the table
     * @param strUserGuid the user guid
     * @param nIdMyTask the Id of the MyTask
     */
    public static void createUserMyTask( String strUserGuid, int nIdMyTask )
    {
        _dao.insertUserMyTask( strUserGuid, nIdMyTask, _plugin );
    }

    /**
     * Delete a record from the table
     * @param nIdMyTask the Id of the task
     */
    public static void removeUserMyTask( int nIdMyTask )
    {
        _dao.deleteUserMyTask( nIdMyTask, _plugin );
    }

    /**
     * Change every tasks to undone from the given user guid
     * @param strUserGuid the user guid
     */
    public static void undoneMyTasks( String strUserGuid )
    {
        _dao.undoneMyTasks( strUserGuid, _plugin );
    }

    /**
     * Get the nb of tasks from a given user guid
     * @param strUserGuid the user guid
     * @return the nb of tasks
     */
    public static int getNbMyTasks( String strUserGuid )
    {
        return _dao.getNbMyTasks( strUserGuid, _plugin );
    }
}
