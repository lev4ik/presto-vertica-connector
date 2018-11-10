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

import java.util.concurrent.TimeUnit;

import javax.validation.constraints.Min;

import io.airlift.configuration.Config;
import io.airlift.units.Duration;

/**
 * To get the custom properties to connect to the database. User, password and
 * URL is provided by de BaseJdbcClient is not required. If there is another
 * custom configuration it should be put in here.
 * 
 * @author Brailovskiy Lev
 * @author jithin ravi
 */
public class VerticaConfig {

	private String user;
	private String password;
	private String url;
	private String schema;
	 private boolean autoReconnect = true;
	    private int maxReconnects = 3;
	    private Duration connectionTimeout = new Duration(10, TimeUnit.SECONDS);

	    public boolean isAutoReconnect()
	    {
	        return autoReconnect;
	    }

	    @Config("vertica.auto-reconnect")
	    public VerticaConfig setAutoReconnect(boolean autoReconnect)
	    {
	        this.autoReconnect = autoReconnect;
	        return this;
	    }

	    @Min(1)
	    public int getMaxReconnects()
	    {
	        return maxReconnects;
	    }

	    @Config("vertica.max-reconnects")
	    public VerticaConfig setMaxReconnects(int maxReconnects)
	    {
	        this.maxReconnects = maxReconnects;
	        return this;
	    }

	    public Duration getConnectionTimeout()
	    {
	        return connectionTimeout;
	    }

	    @Config("vertica.connection-timeout")
	    public VerticaConfig setConnectionTimeout(Duration connectionTimeout)
	    {
	        this.connectionTimeout = connectionTimeout;
	        return this;
	    }
	
	
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	@Config("vertica.user")
	public VerticaConfig setUser(String user) {
		this.user = user;
		return this;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	@Config("vertica.password")
	public VerticaConfig setPassword(String password) {
		this.password = password;
		return this;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	@Config("vertica.password")
	public VerticaConfig setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema
	 *            the url to set
	 */
	@Config("vertica.schema")
	public VerticaConfig setSchema(String schema) {
		this.schema = schema;
		return this;
	}
}
