package com.googlecode.cchlib.sql.debug;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.EnumSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * {@link Connection} wrapper to add some debug informations
 */
public final class ConnectionDebug implements Connection {

    /**
     * Configuration options for {@link ConnectionDebug}
     */
    @SuppressWarnings("squid:S00115")
    public enum Config {
        /**
         * Throws to IllegalStateException when {@link Connection#close()}
         * is invoke
         */
        Close_ThrowsIllegalStateException,

        /**
         * Log to {@link org.apache.log4j.Logger} each time
         * {@link Connection#close()} is invoke
         */
        Close_Log4j_Logger,
    }

    private static final org.apache.log4j.Logger LOGGER
        = org.apache.log4j.Logger.getLogger( ConnectionDebug.class );

    private final Connection  delegate;
    private final Set<Config> config;

    /**
     * Create {@link ConnectionDebug} which is a {@link Connection}
     * delegator
     *
     * @param connection Connection to use
     * @param config EnumSet of options
     */
    public ConnectionDebug( final Connection connection, final Set<Config> config ) {
        this.delegate = connection;
        this.config   = config == null ? EnumSet.allOf(ConnectionDebug.Config.class) : config;
    }

    @Override
    public void close() throws SQLException {
        if( this.config.contains( Config.Close_Log4j_Logger ) ) {
            LOGGER.info( "Connection:" + this + ".close()" );
        }

        if( this.config.contains( Config.Close_ThrowsIllegalStateException ) ) {
            throw new IllegalStateException();
        }
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        return this.delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return this.delegate.isWrapperFor(iface);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return this.delegate.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(final String sql) throws SQLException {
        return this.delegate.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        return this.delegate.prepareCall(sql);
    }

    @Override
    public String nativeSQL(final String sql) throws SQLException {
        return this.delegate.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        this.delegate.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return this.delegate.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        this.delegate.commit();
    }

    @Override
    public void rollback() throws SQLException {
        this.delegate.rollback();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.delegate.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return this.delegate.getMetaData();
    }

    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
        this.delegate.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return this.delegate.isReadOnly();
    }

    @Override
    public void setCatalog(final String catalog) throws SQLException {
        this.delegate.setCatalog( catalog );
    }

    @Override
    public String getCatalog() throws SQLException {
        return this.delegate.getCatalog();
    }

    @Override
    public void setTransactionIsolation(final int level) throws SQLException {
        this.delegate.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return this.delegate.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.delegate.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.delegate.clearWarnings();
    }

    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
        return this.delegate.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency)
            throws SQLException {
        return this.delegate.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency)
            throws SQLException {
        return this.delegate.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.delegate.getTypeMap();
    }

    @Override
    public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
        this.delegate.setTypeMap(map);
    }

    @Override
    public void setHoldability(final int holdability) throws SQLException {
        this.delegate.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return this.delegate.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return this.delegate.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(final String name) throws SQLException {
        return this.delegate.setSavepoint(name);
    }

    @Override
    public void rollback(final Savepoint savepoint) throws SQLException {
        this.delegate.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
        this.delegate.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency,
            final int resultSetHoldability) throws SQLException {
        return this.delegate.createStatement(resultSetType,resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency,
            final int resultSetHoldability) throws SQLException {
        return this.delegate.prepareStatement(sql, resultSetType, resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency,
            final int resultSetHoldability) throws SQLException {
        return this.delegate.prepareCall(sql, resultSetType, resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
        return this.delegate.prepareStatement(sql,autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
        return this.delegate.prepareStatement(sql,columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
        return this.delegate.prepareStatement(sql,columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return this.delegate.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {

        return this.delegate.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {

        return this.delegate.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {

        return this.delegate.createSQLXML();
    }

    @Override
    public boolean isValid(final int timeout) throws SQLException {
        return this.delegate.isValid( timeout );
    }

    @Override
    public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
        this.delegate.setClientInfo(name,value);
    }

    @Override
    public void setClientInfo(final Properties properties) throws SQLClientInfoException {
        this.delegate.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(final String name) throws SQLException {
        return this.delegate.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return this.delegate.getClientInfo();
    }

    @Override
    public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
        return this.delegate.createArrayOf(typeName,elements);
    }

    @Override
    public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
        return this.delegate.createStruct(typeName,attributes);
    }

    @Override
    public void setSchema(final String schema) throws SQLException {
        this.delegate.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return this.delegate.getSchema();
    }

    @Override
    public void abort(final Executor executor) throws SQLException {
        this.delegate.abort(executor);
    }

    @Override
    public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
        this.delegate.setNetworkTimeout(executor,milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return this.delegate.getNetworkTimeout();
    }
}
