package com.googlecode.cchlib.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 *
 */
public class PropertierPopulator<E>
{
    private final static Logger logger = Logger.getLogger( PropertierPopulator.class );
    private Set<Field> keyFieldSet;

    /**
     *
     * @param clazz
     */
    public PropertierPopulator( Class<? extends E> clazz )
    {
        this.keyFieldSet     = new HashSet<>();
        Field[] fields  = clazz.getDeclaredFields();

        for( Field f : fields ) {
            if( f.getAnnotation( Populator.class ) != null ) {
                this.keyFieldSet.add( f );
                }
            }

        if( logger.isTraceEnabled() ) {
            logger.trace( "Found " + this.keyFieldSet.size() + " fields." );
            }
    }

    /**
     *
     * @param bean
     * @param properties
     */
    public void populateProperties( E bean, Properties properties )
    {
        for( Field f : this.keyFieldSet ) {
            try {
                f.setAccessible( true );
                final Object o = f.get( bean );
                f.setAccessible( false );

                if( o != null ) {
                    properties.put( f.getName(), o.toString() );
                    }
                }
            catch( IllegalArgumentException | IllegalAccessException e ) {
                // ignore !
                logger .warn( "Cannot read field:" + f, e );
                }
            }
    }

    /**
     *
     * @param properties
     * @param bean
     */
    public void populateBean( Properties properties, E bean )
    {
        for( Field f : this.keyFieldSet ) {
            final String strValue = properties.getProperty( f.getName() );
             try {
                f.setAccessible( true );

                //logger .trace( "F:" + f.getName() + " * getType()=" + f.getType() );
                //logger .trace( "F:" + f.getName() + " * toGenericString()=" + f.toGenericString() );

                if( String.class.isAssignableFrom( f.getType() ) ) {
                    f.set( bean, strValue );
                    }
                else if( boolean.class.isAssignableFrom( f.getType() ) ) {
                    f.setBoolean( bean, Boolean.valueOf( strValue ).booleanValue() );
                    }
                else if( int.class.isAssignableFrom( f.getType() ) ) {
                    f.setInt( bean, Integer.valueOf( strValue ).intValue() );
                    }
                else {
                    logger .error( "Bad type for field:" + f + " * class=" + f.getType() );
                    }

                f.setAccessible( false );
                }
            catch( IllegalArgumentException | IllegalAccessException e ) {
                // ignore !
                logger .error( "Cannot set field:" + f, e );
                }
            }
    }


}