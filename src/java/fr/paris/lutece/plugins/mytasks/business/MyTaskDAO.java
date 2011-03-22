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
package fr.paris.lutece.plugins.mytasks.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * MyTaskDAO
 *
 */
public final class MyTaskDAO implements IMyTaskDAO
{
    // CONSTANTS
    private static final String COMMA = ",";

    // SQL
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_mytask ) FROM mytasks_mytask ";
    private static final String SQL_QUERY_SELECT = " SELECT id_mytask, name, date_mytask, is_done FROM mytasks_mytask WHERE id_mytask = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO mytasks_mytask ( id_mytask, name, date_mytask, is_done ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM mytasks_mytask WHERE id_mytask = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE mytasks_mytask SET name = ?, date_mytask = ?, is_done = ? WHERE id_mytask = ? ";
    private static final String SQL_QUERY_UNDONE_MYTASKS = " UPDATE mytasks_mytask SET is_done = 0 WHERE id_mytask IN " +
        " ( SELECT id_mytask FROM mytasks_user_mytask WHERE user_guid = ? ) ";
    private static final String SQL_ORDER_BY = " ORDER BY ";
    private static final String SQL_ASC = " ASC ";
    private static final String SQL_IS_DONE = " is_done ";
    private static final String SQL_DATE_MYTASK = " date_mytask ";
    private static final String SQL_QUERY_SELECT_MYTASKS_FROM_USER = " SELECT a.id_mytask, a.name, a.date_mytask, a.is_done " +
        " FROM mytasks_mytask AS a INNER JOIN mytasks_user_mytask b ON a.id_mytask = b.id_mytask WHERE b.user_guid = ? ";
    private static final String SQL_QUERY_INSERT_USER_MYTASK = " INSERT INTO mytasks_user_mytask ( user_guid, id_mytask ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE_MYTASK_FROM_USER = " DELETE FROM mytasks_user_mytask WHERE id_mytask = ? ";

    /**
     * {@inheritDoc}
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    public void insert( MyTask myTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        int nIndex = 1;
        myTask.setIdMyTask( newPrimaryKey( plugin ) );

        daoUtil.setInt( nIndex++, myTask.getIdMyTask(  ) );
        daoUtil.setString( nIndex++, myTask.getName(  ) );
        daoUtil.setDate( nIndex++, myTask.getDate(  ) );
        daoUtil.setBoolean( nIndex++, myTask.isDone(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public MyTask load( int nIdMyTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nIdMyTask );
        daoUtil.executeQuery(  );

        MyTask myTask = null;

        if ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            myTask = new MyTask(  );

            myTask.setIdMyTask( daoUtil.getInt( nIndex++ ) );
            myTask.setName( daoUtil.getString( nIndex++ ) );
            myTask.setDate( daoUtil.getDate( nIndex++ ) );
            myTask.setDone( daoUtil.getBoolean( nIndex++ ) );
        }

        daoUtil.free(  );

        return myTask;
    }

    /**
     * {@inheritDoc}
     */
    public void delete( int nIdMyTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdMyTask );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void store( MyTask myTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nIndex = 1;
        daoUtil.setString( nIndex++, myTask.getName(  ) );
        daoUtil.setDate( nIndex++, myTask.getDate(  ) );
        daoUtil.setBoolean( nIndex++, myTask.isDone(  ) );

        daoUtil.setInt( nIndex++, myTask.getIdMyTask(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public List<MyTask> selectMyTasksListFromUser( String strUserGuid, Plugin plugin )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_MYTASKS_FROM_USER );
        sbSQL.append( SQL_ORDER_BY );
        sbSQL.append( SQL_IS_DONE + SQL_ASC );
        sbSQL.append( COMMA + SQL_DATE_MYTASK + SQL_ASC );

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString(  ), plugin );
        daoUtil.setString( 1, strUserGuid );
        daoUtil.executeQuery(  );

        List<MyTask> myTasksList = new ArrayList<MyTask>(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            MyTask myTask = new MyTask(  );

            myTask.setIdMyTask( daoUtil.getInt( nIndex++ ) );
            myTask.setName( daoUtil.getString( nIndex++ ) );
            myTask.setDate( daoUtil.getDate( nIndex++ ) );
            myTask.setDone( daoUtil.getBoolean( nIndex++ ) );
            myTasksList.add( myTask );
        }

        daoUtil.free(  );

        return myTasksList;
    }

    /**
     * {@inheritDoc}
     */
    public void insertUserMyTask( String strUserGuid, int nIdMyTask, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_USER_MYTASK, plugin );
        daoUtil.setString( nIndex++, strUserGuid );
        daoUtil.setInt( nIndex++, nIdMyTask );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void deleteUserMyTask( int nIdMyTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_MYTASK_FROM_USER, plugin );
        daoUtil.setInt( 1, nIdMyTask );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void undoneMyTasks( String strUserGuid, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UNDONE_MYTASKS, plugin );
        daoUtil.setString( 1, strUserGuid );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
