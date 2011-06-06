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
package fr.paris.lutece.plugins.mytasks.service.parameter;

import fr.paris.lutece.plugins.mytasks.business.parameter.MyTasksParameterHome;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;


/**
 *
 * MyTasksParameterService
 *
 */
public final class MyTasksParameterService
{
    private static MyTasksParameterService _singleton;

    /**
     * Private constructor
     */
    private MyTasksParameterService(  )
    {
    }

    /**
     * Return the MyTasksParameterService singleton
     *
     * @return the MyTasksParameterService singleton
     */
    public static synchronized MyTasksParameterService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new MyTasksParameterService(  );
        }

        return _singleton;
    }

    /**
     * Init
     */
    public void init(  )
    {
    }

    /**
     * Load all the parameter default values
     *
     * @return a list of {@link ReferenceItem}
     */
    public ReferenceList getParamDefaultValues(  )
    {
        return MyTasksParameterHome.findAll(  );
    }

    /**
     * Get the parameter default value given a parameter key
     *
     * @param strParameterKey the parameter key
     * @return a {@link ReferenceItem}
     */
    public ReferenceItem getParamDefaultValue( String strParameterKey )
    {
        return MyTasksParameterHome.findByKey( strParameterKey );
    }

    /**
     * Update the parameter
     *
     * @param param The parameter
     */
    public void update( ReferenceItem param )
    {
        MyTasksParameterHome.update( param );
    }
}
