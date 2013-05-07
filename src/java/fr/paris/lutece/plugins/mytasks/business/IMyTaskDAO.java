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
package fr.paris.lutece.plugins.mytasks.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 *
 * IMyTaskDAO
 *
 */
public interface IMyTaskDAO
{
    /**
    * Generates a new primary key
    * @param plugin The Plugin
    * @return The new primary key
    */
    int newPrimaryKey( Plugin plugin );

    /**
     * Insert a new record in the table.
     * @param myTask instance of the MyTask object to insert
     * @param plugin the Plugin
     */
    void insert( MyTask myTask, Plugin plugin );

    /**
    * Update the record in the table
    * @param myTask the reference of the MyTask
    * @param plugin the Plugin
    */
    void store( MyTask myTask, Plugin plugin );

    /**
     * Delete a record from the table
     * @param nIdMyTask int identifier of the MyTask to delete
     * @param plugin the Plugin
     */
    void delete( int nIdMyTask, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * @param nIdMyTask The identifier of the mytask
     * @param plugin the Plugin
     * @return The instance of the widget
     */
    MyTask load( int nIdMyTask, Plugin plugin );

    /**
     * Load the list of MyTask from a given user guid
     * @param strUserGuid the user guid
     * @param plugin {@link Plugin}
     * @return a list of {@link MyTask}
     */
    List<MyTask> selectMyTasksListFromUser( String strUserGuid, Plugin plugin );

    /**
     * Insert a new record in the table
     * @param strUserGuid the user guid
     * @param nIdMyTask the Id of the MyTask
     * @param plugin {@link Plugin}
     */
    void insertUserMyTask( String strUserGuid, int nIdMyTask, Plugin plugin );

    /**
     * Delete a record from the table
     * @param nIdMyTask the Id of the task
     * @param plugin {@link Plugin}
     */
    void deleteUserMyTask( int nIdMyTask, Plugin plugin );

    /**
     * Change every tasks to undone from the given user guid
     * @param strUserGuid the user guid
     * @param plugin {@link Plugin}
     */
    void undoneMyTasks( String strUserGuid, Plugin plugin );

    /**
     * Get the nb of tasks from a given user guid
     * @param strUserGuid the user guid
     * @param plugin {@link Plugin}
     * @return the nb of tasks
     */
    int getNbMyTasks( String strUserGuid, Plugin plugin );
}
