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
package fr.paris.lutece.plugins.mytasks.service;

import fr.paris.lutece.plugins.mytasks.business.MyTask;
import fr.paris.lutece.plugins.mytasks.business.MyTaskHome;
import fr.paris.lutece.portal.service.security.LuteceUser;

import org.apache.commons.lang.StringUtils;

import java.util.List;


/**
 *
 * MyTasksService
 *
 */
public final class MyTasksService
{
    private static MyTasksService _singleton;

    /**
     * Private constructor
     */
    private MyTasksService(  )
    {
    }

    /**
     * Get the instance of {@link MyTasksService}
     *
     * @return an instance of {@link MyTasksService}
     */
    public static synchronized MyTasksService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new MyTasksService(  );
        }

        return _singleton;
    }

    /**
     * Get the list of MyTasks from a given LuteceUser
     * @param user the {@link LuteceUser}
     * @return a list of {@link MyTask}
     */
    public List<MyTask> getMyTasksList( LuteceUser user )
    {
        return MyTaskHome.selectMyTasksListFromUser( user.getName(  ) );
    }

    /**
     * Get a {@link MyTask} from a given id mytask
     * @param nIdMyTask the id mytask
     * @return a {@link MyTask}
     */
    public MyTask getMyTask( int nIdMyTask )
    {
        return MyTaskHome.findByPrimaryKey( nIdMyTask );
    }

    /**
     * Do add a {@link MyTask}
     * @param myTask a {@link MyTask}
     * @param user a {@link LuteceUser}
     */
    public void doAddMyTask( MyTask myTask, LuteceUser user )
    {
        MyTaskHome.create( myTask );
        MyTaskHome.createUserMyTask( user.getName(  ), myTask.getIdMyTask(  ) );
    }

    /**
     * Do update a {@link MyTask}
     * @param myTask a {@link MyTask}
     * @param user a {@link LuteceUser}
     */
    public void doUpdateMyTask( MyTask myTask, LuteceUser user )
    {
        MyTaskHome.update( myTask );
    }

    /**
     * Do remove a {@link MyTask}
     * @param nIdMyTask the id my task
     * @param user a {@link LuteceUser}
     */
    public void doRemoveMyTask( int nIdMyTask, LuteceUser user )
    {
        MyTaskHome.remove( nIdMyTask );
        MyTaskHome.removeUserMyTask( nIdMyTask );
    }

    /**
     * Do update the mytasks status
     * @param listIdsMyTask the list of ids mytask
     * @param user a {@link LuteceUser}
     */
    public void doUpdateMyTasksStatus( String[] listIdsMyTask, LuteceUser user )
    {
        MyTaskHome.undoneMyTasks( user.getName(  ) );

        if ( listIdsMyTask != null )
        {
            for ( String strIdMyTask : listIdsMyTask )
            {
                if ( StringUtils.isNotBlank( strIdMyTask ) && StringUtils.isNumeric( strIdMyTask ) )
                {
                    int nIdMyTask = Integer.parseInt( strIdMyTask );
                    MyTask myTask = getMyTask( nIdMyTask );
                    myTask.setDone( true );
                    doUpdateMyTask( myTask, user );
                }
            }
        }
    }
}
