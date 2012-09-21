package com.googlecode.cchlib.test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import com.googlecode.cchlib.io.FileHelper;
import com.googlecode.cchlib.util.ArrayCollection;
import com.googlecode.cchlib.util.properties.Populator;
import com.googlecode.cchlib.util.properties.PropertiesHelper;
import com.googlecode.cchlib.util.properties.PropertiesPopulator;

/**
 * Allow test case to use specific configuration according
 * to current computer
 * 
 * @since 4.1.7
 */
public class TestLocal
{
    //private final static Logger logger = Logger.getLogger( TestLocal.class );
    private PropertiesPopulator<TestLocal.Config> pp = new PropertiesPopulator<TestLocal.Config>( TestLocal.Config.class );
    private Config config;
    
    /**
     * 
     */
    public class Config
    {
        @Populator private String[] existingMACAddr;
        
        /**
         * Returns an unmodifiable collection of existing MAC Address
         * accessible by current computer.
         * @return an unmodifiable collection of existing MAC Address
         */
        public Collection<String> getExistingMACAddressCollection()
        {
            if( existingMACAddr == null ) {
                return Collections.emptyList();
                }
            else {
                return new ArrayCollection<String>( existingMACAddr );
                }
        }
        /**
         * Set a collection of existing MAC Address
         * accessible by current computer.
         */
        public void setExistingMACAddressCollection( Collection<String> c )
        {
            this.existingMACAddr = new String[ c.size() ];
            
            int i = 0;
            
            for( String e : c ) {
                this.existingMACAddr[ i++ ] = e;
                }
        }
    }

    /**
     * Create a TestLocalConfig
     */
    public TestLocal()
    {
        this.config = new TestLocal.Config();
    }
    
    private static File getConfigFile()
    {
        return FileHelper.getUserHomeDirFile( TestLocal.class.getName() + ".properties" );
    }
    
    /**
     * Load configuration.
     * 
     * @throws IOException if configuration can not be loaded.
     */
    public void load() throws IOException
    {
        Properties properties = PropertiesHelper.loadProperties( getConfigFile() );

        pp.populateBean( properties , this.config );
    }
    
    /**
     * Save configuration.
     * 
     * @throws IOException if configuration can not be saved.
     */
    public void save() throws IOException
    {
        Properties properties = new Properties();
        
        pp.populateProperties( this.config, properties );
        
        PropertiesHelper.saveProperties( getConfigFile(), properties );
    }

    /**
     * TODOC
     * @return TODOC
     */
    public Config getConfig() throws IllegalStateException
    {
        return this.config;
    }

}
