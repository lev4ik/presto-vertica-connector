/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.plugin.vertica;

import static java.util.Locale.ENGLISH;

import java.sql.*;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;

import com.vertica.jdbc.Driver;

import org.apache.log4j.Logger;

import com.facebook.presto.plugin.jdbc.BaseJdbcClient;
import com.facebook.presto.plugin.jdbc.BaseJdbcConfig;
import com.facebook.presto.plugin.jdbc.ConnectionFactory;
import com.facebook.presto.plugin.jdbc.DriverConnectionFactory;
import com.facebook.presto.plugin.jdbc.JdbcConnectorId;
import com.facebook.presto.spi.SchemaTableName;
import com.facebook.presto.spi.type.Type;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import static com.facebook.presto.plugin.jdbc.DriverConnectionFactory.basicConnectionProperties;
/**
 * Implementation of VerticaClient. It describes table, schemas and columns behaviours.
 * It allows to change the QueryBuilder to a custom one as well.
 * 
 * @author Brailovskiy Lev
 *
 */
public class VerticaClient extends BaseJdbcClient {

	private static final Logger log = Logger.getLogger(VerticaClient.class);

	@Inject
	public VerticaClient(JdbcConnectorId connectorId, BaseJdbcConfig config, VerticaConfig verticaConfig)
			throws SQLException {
		super(connectorId, config, "", connectionFactory(config, verticaConfig));
		//connectionProperties.setProperty("SEARCH_PATH", verticaConfig.getSchema());
	}

	 private static ConnectionFactory connectionFactory(BaseJdbcConfig config, VerticaConfig verticaConfig)
	            throws SQLException
	    {
	        Properties connectionProperties = basicConnectionProperties(config);
	        connectionProperties.setProperty("useInformationSchema", "true");
	        connectionProperties.setProperty("nullCatalogMeansCurrent", "false");
	        connectionProperties.setProperty("useUnicode", "true");
	        connectionProperties.setProperty("characterEncoding", "utf8");
	        connectionProperties.setProperty("tinyInt1isBit", "false");
	        if (verticaConfig.isAutoReconnect()) {
	            connectionProperties.setProperty("autoReconnect", String.valueOf(verticaConfig.isAutoReconnect()));
	            connectionProperties.setProperty("maxReconnects", String.valueOf(verticaConfig.getMaxReconnects()));
	        }
	        if (verticaConfig.getConnectionTimeout() != null) {
	            connectionProperties.setProperty("connectTimeout", String.valueOf(verticaConfig.getConnectionTimeout().toMillis()));
	        }

	        return new DriverConnectionFactory(new Driver(), config.getConnectionUrl(), connectionProperties);
	    }

	
	/*@Override
	public Set<String> getSchemaNames() {
		try (Connection connection = driver.connect(connectionUrl, connectionProperties);
			 ResultSet resultSet = connection.getMetaData().getCatalogs()) {
			ImmutableSet.Builder<String> schemaNames = ImmutableSet.builder();
			while (resultSet.next()) {
				String schemaName = resultSet.getString("TABLE_CAT").toLowerCase(ENGLISH);
				// skip internal schemas
				if (!schemaName.equals("v_internal") && !schemaName.equals("v_catalog") && !schemaName.equals("v_monitor") && !schemaName.startsWith("v_dbd")) {
					schemaNames.add(schemaName);
				}
			}
			return schemaNames.build();
		}
		catch (SQLException e) {
			throw Throwables.propagate(e);
		}
	}*/

	protected ResultSet getTables(Connection connection, String schemaName, String tableName) throws SQLException {
		DatabaseMetaData metadata = connection.getMetaData();
		String escape = metadata.getSearchStringEscape();
		return metadata.getTables(
				connection.getCatalog(),
				escapeNamePattern(schemaName, escape),
				escapeNamePattern(tableName, escape),
				new String[] {"TABLE", "VIEW"});
	}

	@Override
	public PreparedStatement getPreparedStatement(Connection connection, String sql)
			throws SQLException
	{
		connection.setAutoCommit(false);
		PreparedStatement statement = connection.prepareStatement(sql);
		return statement;
	}


	@Override
	protected SchemaTableName getSchemaTableName(ResultSet resultSet)  throws SQLException {
		return new SchemaTableName(
				resultSet.getString("TABLE_CAT").toLowerCase(ENGLISH),
				resultSet.getString("TABLE_NAME").toLowerCase(ENGLISH));
	}

	@Override
	protected String toSqlType(Type type) {
		//just for debug
		String sqlType = super.toSqlType(type);
		return sqlType;
	}
}
