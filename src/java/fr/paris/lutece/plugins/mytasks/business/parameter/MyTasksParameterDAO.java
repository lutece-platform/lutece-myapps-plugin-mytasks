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
package fr.paris.lutece.plugins.mytasks.business.parameter;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 *
 * MyTasksParameterDAO
 *
 */
public class MyTasksParameterDAO implements IMyTasksParameterDAO
{
    private static final String TRUE = "1";
    private static final String SQL_QUERY_SELECT = " SELECT parameter_value FROM mytasks_mytasks_parameter WHERE parameter_key = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE mytasks_mytasks_parameter SET parameter_value = ? WHERE parameter_key = ? ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT parameter_key, parameter_value FROM mytasks_mytasks_parameter ORDER BY parameter_key ASC ";

    /**
     * Load all the default values
     * @param plugin Plugin
     * @return a list of ReferenceItem
     */
    public ReferenceList selectAll( Plugin plugin )
    {
        ReferenceList listParams = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            ReferenceItem param = new ReferenceItem(  );
            param.setCode( daoUtil.getString( nIndex++ ) );
            param.setName( daoUtil.getString( nIndex++ ) );
            param.setChecked( param.getName(  ).equals( TRUE ) ? true : false );
            listParams.add( param );
        }

        daoUtil.free(  );

        return listParams;
    }

    /**
     * Load the parameter value
     * @param strParameterKey the parameter key
     * @param plugin {@link Plugin}
     * @return The parameter value
     */
    public ReferenceItem load( String strParameterKey, Plugin plugin )
    {
        ReferenceItem param = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setString( 1, strParameterKey );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            param = new ReferenceItem(  );
            param.setCode( strParameterKey );
            param.setName( daoUtil.getString( 1 ) );
            param.setChecked( param.getName(  ).equals( TRUE ) ? true : false );
        }

        daoUtil.free(  );

        return param;
    }

    /**
     * Update the parameter
     * @param param the parameter
     * @param plugin {@link Plugin}
     */
    public void store( ReferenceItem param, Plugin plugin )
    {
        int nIndex = 1;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setString( nIndex++, param.getName(  ) );
        daoUtil.setString( nIndex++, param.getCode(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
