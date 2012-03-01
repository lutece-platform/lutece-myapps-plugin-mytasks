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
package fr.paris.lutece.plugins.mytasks.service;

import fr.paris.lutece.plugins.mytasks.business.MyTask;
import fr.paris.lutece.plugins.mytasks.business.MyTaskHome;
import fr.paris.lutece.plugins.mytasks.business.portlet.MyTasksPortletHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.commons.lang.StringUtils;

import java.util.List;


/**
 *
 * MyTasksService
 *
 */
public final class MyTasksService extends AbstractCacheableService
{
    private static final String SERVICE_NAME = "MyTasks Service";

    // CONSTANTS
    private static final String TRUE = "true";

    // PROPERTIES
    private static final String PROPERTY_CACHE_MYTASKSSERVICE_ENABLE = "mytasks.cache.myTasksService.enable";

    // CACHES
    private static final String CACHE_MYTASKS_LIST = "[mytasks list]";
    private static final String CACHE_USER = "[user:";
    private static final String CACHE_END = "]";
    private static MyTasksService _singleton;

    /**
     * Private constructor
     */
    private MyTasksService(  )
    {
    }

    /**
     * {@inheritDoc }
     */
    public String getName(  )
    {
        return SERVICE_NAME;
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
     * Init service
     */
    public void init(  )
    {
        String strCacheEnable = AppPropertiesService.getProperty( PROPERTY_CACHE_MYTASKSSERVICE_ENABLE, TRUE );
        boolean bCacheEnable = TRUE.equalsIgnoreCase( strCacheEnable );

        if ( bCacheEnable )
        {
            initCache( getName(  ) );
        }
        else
        {
            CacheService.registerCacheableService( this );
        }
    }

    /**
     * Get the list of MyTasks from a given LuteceUser
     * @param user the {@link LuteceUser}
     * @return a list of {@link MyTask}
     */
    public List<MyTask> getMyTasksList( LuteceUser user )
    {
        String strKey = getMyTasksListKey( user.getName(  ) );
        List<MyTask> listMyTasks = (List<MyTask>) getFromCache( strKey );

        if ( listMyTasks == null )
        {
            listMyTasks = MyTaskHome.selectMyTasksListFromUser( user.getName(  ) );
            putInCache( strKey, listMyTasks );
        }

        return listMyTasks;
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
        resetCacheFromUser( user );
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
        resetCacheFromUser( user );
        MyTaskHome.update( myTask );
    }

    /**
     * Do remove a {@link MyTask}
     * @param nIdMyTask the id my task
     * @param user a {@link LuteceUser}
     */
    public void doRemoveMyTask( int nIdMyTask, LuteceUser user )
    {
        resetCacheFromUser( user );
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
        resetCacheFromUser( user );
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

    /**
     * Do update the mytasks status
     * @param user a {@link LuteceUser}
     */
    public void doDeleteCompletedMyTasks( LuteceUser user )
    {
        List<MyTask> listMyTasks = getMyTasksList( user );

        for ( MyTask myTask : listMyTasks )
        {
            if ( myTask.isDone(  ) )
            {
                doRemoveMyTask( myTask.getIdMyTask(  ), user );
            }
        }
    }

    /**
     * Get the nb of tasks from a given user
     * @param user the {@link LuteceUser}
     * @return the nb of tasks
     */
    public int getNbMyTasks( LuteceUser user )
    {
        return MyTaskHome.getNbMyTasks( user.getName(  ) );
    }

    /**
     * Reset cache
     * @param user the {@link LuteceUser}
     */
    private void resetCacheFromUser( LuteceUser user )
    {
        try
        {
            if ( isCacheEnable(  ) && ( getCache(  ) != null ) )
            {
                List<Portlet> listPortlets = PortletHome.findByType( MyTasksPortletHome.getInstance(  )
                                                                                       .getPortletTypeId(  ) );

                for ( Portlet portlet : listPortlets )
                {
                    PortletHome.invalidate( portlet );
                }

                String strKey = getMyTasksListKey( user.getName(  ) );
                getCache(  ).remove( strKey );
            }
        }
        catch ( IllegalStateException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
    }

    /**
     * Build the cache key for mytasks list from a given user guid
     * @param strUserGuid the user guid
     * @return the cache key
     */
    private String getMyTasksListKey( String strUserGuid )
    {
        StringBuilder sbKey = new StringBuilder(  );
        sbKey.append( CACHE_MYTASKS_LIST );
        sbKey.append( CACHE_USER + strUserGuid + CACHE_END );

        return sbKey.toString(  );
    }
}
