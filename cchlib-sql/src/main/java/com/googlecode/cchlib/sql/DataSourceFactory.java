package com.googlecode.cchlib.sql;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import com.googlecode.cchlib.NeedDoc;
import com.googlecode.cchlib.NeedTestCases;

/**
 * Create a simple {@link DataSource} based on a standard driver class object.
 */
@NeedDoc
@NeedTestCases
public class DataSourceFactory
{
    private static final Logger NO_PARENT_LOGGER = null;

    private DataSourceFactory()
    {// All static
    }

    /**
     * Create a simple {@link DataSource} based on a standard driver class object.
     *
     * @param driverClassName Driver class name
     * @param url Database URL according to driver specifications
     * @param username Default username for connection use by {@link DataSource#getConnection()}
     * @param password Default password for connection use by {@link DataSource#getConnection()}
     * @param logger Valid {@link PrintWriter} that will use as define by {@link DataSource#getLogWriter()}
     * @return a simple {@link DataSource}
     * @throws ClassNotFoundException if driver class not found
     * @throws NullPointerException if driverClassName or logger is null
     */
    public static DataSource buildDataSource(
            final String        driverClassName,
            final String        url,
            final String        username,
            final String        password,
            final PrintWriter   logger
            )
        throws ClassNotFoundException, NullPointerException // NOSONAR
    {
        return buildDataSource(
                driverClassName,
                url,
                username,
                password,
                30,
                logger
                );
    }

    /**
     * Create a simple {@link DataSource} based on a standard driver class object.
     *
     * @param driverClassName Driver class name
     * @param url Database URL according to driver specifications
     * @param username Default username for connection use by {@link DataSource#getConnection()}
     * @param password Default password for connection use by {@link DataSource#getConnection()}
     * @param timeout default login timeout in second for {@link DataSource}, see {@link DataSource#getLoginTimeout()}
     * @param logger Valid {@link Logger} that will use as define by {@link DataSource#getLogWriter()}
     * @return a simple {@link DataSource}
     * @throws ClassNotFoundException if driver class not found
     * @throws NullPointerException if driverClassName or logger is null
     */
    public static DataSource buildDataSource(
            final String        driverClassName,
            final String        url,
            final String        username,
            final String        password,
            final int           timeout,
            final Logger        logger
            )
        throws ClassNotFoundException, NullPointerException // NOSONAR
    {
        if( logger == null ) {
            throw new NullPointerException();
            }
        if( driverClassName == null ) {
            throw new NullPointerException();
            }

        Class.forName( driverClassName );

        final PrintWriter pwLogger = newPrintWriterFromLogger( logger );

        return new AbstractDataSource( username, password, logger, timeout, pwLogger )
        {
            @Override
            public Connection getConnection( final String username, final String password ) throws SQLException
            {
                Connection conn = null;

                do {
                    if((conn != null) && !conn.isClosed()) {
                        break;
                        }
                    conn = getDriverManagerConnection( url, username, password );

                    if( conn.isClosed() ) {
                        logger.log( Level.WARNING, "*** Connection is closed !" );
                        }
                    } while(true);

                return conn;
            }
        };
    }

    private static PrintWriter newPrintWriterFromLogger( final Logger logger )
    {
        final Writer wLogger = new Writer()
        {
            @Override
            public void close() throws IOException {} // NOSONAR - not required

            @Override
            public void flush() throws IOException {} // NOSONAR - not required

            @Override
            public void write(final char[] cbuf, final int off, final int len)
                    throws IOException
            {
                logger.log( Level.WARNING, new String( cbuf, off, len ) );
            }
        };
        return new PrintWriter( wLogger );
    }

    /**
     * Create a simple {@link DataSource} based on a standard driver class object.
     *
     * @param driverClassName Driver class name
     * @param url Database URL according to driver specifications
     * @param username Default username for connection use by {@link DataSource#getConnection()}
     * @param password Default password for connection use by {@link DataSource#getConnection()}
     * @param timeout default login timeout in second for {@link DataSource}, see {@link DataSource#getLoginTimeout()}
     * @param logger Valid {@link PrintWriter} that will use as define by {@link DataSource#getLogWriter()}
     * @return a simple {@link DataSource}
     * @throws ClassNotFoundException if driver class not found
     * @throws NullPointerException if driverClassName or logger is null
     */
    public static DataSource buildDataSource(
            final String        driverClassName,
            final String        url,
            final String        username,
            final String        password,
            final int           timeout,
            final PrintWriter   logger
            )
        throws ClassNotFoundException, NullPointerException // NOSONAR
    {
        if( logger == null ) {
            throw new NullPointerException();
            }
        if( driverClassName == null ) {
            throw new NullPointerException();
            }

        Class.forName( driverClassName );

        return new AbstractDataSource(username, password, NO_PARENT_LOGGER, timeout, logger)
        {
            @Override
            public Connection getConnection( final String username, final String password )
                throws SQLException
            {
                Connection conn = null;

                do {
                    if((conn != null) && !conn.isClosed()) {
                        break;
                        }
                    conn = getDriverManagerConnection( url, username, password );

                    if( conn.isClosed() ) {
                        final PrintWriter pw = getLogWriter();

                        if( pw != null ) {
                            pw.println( "*** Connection is closed !" );
                        }
                        }
                    } while(true);

                return conn;
            }
        };
    }

    private static Connection getDriverManagerConnection( //
        final String url, //
        final String username, //
        final String password ) throws SQLException
    {
        return DriverManager.getConnection( url, username, password );
    }
}
