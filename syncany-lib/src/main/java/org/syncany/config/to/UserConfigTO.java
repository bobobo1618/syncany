/*
 * Syncany, www.syncany.org
 * Copyright (C) 2011-2014 Philipp C. Heckel <philipp.heckel@gmail.com> 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.syncany.config.to;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.syncany.config.ConfigException;
import org.syncany.util.ByteArray;
import org.syncany.util.StringUtil;

/**
 * The user config transfer object is a helper data structure that allows storing
 * a user's global system settings such as system properties.
 * 
 * <p>It uses the Simple framework for XML serialization, and its corresponding
 * annotation-based configuration.  
 *  
 * @see <a href="http://simple.sourceforge.net/">Simple framework</a> at simple.sourceforge.net
 * @author Philipp C. Heckel <philipp.heckel@gmail.com>
 */
@Root(name="userConfig", strict=false)
@Namespace(reference="http://syncany.org/userconfig/1")
public class UserConfigTO {
	private static final Serializer serializer;
	
	static {
		try {
			Registry registry = new Registry();

			registry.bind(ByteArray.class, new ByteArrayConverter());

			serializer = new Persister(new RegistryStrategy(registry));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Element(name = "sensitiveValueEncryptionKey", required = false)
	private ByteArray sensitiveValueEncryptionKey;
	
	@Element(name = "sensitiveValueEncryptionIV", required = false)
	private ByteArray sensitiveValueEncryptionIV;
	
	@ElementMap(name="systemProperties", entry="property", key="name", required=false, attribute=true)
	private TreeMap<String, String> systemProperties;
	
	public UserConfigTO() {
		this.systemProperties = new TreeMap<String, String>();
	}
	
	public Map<String, String> getSystemProperties() {
		return systemProperties;
	}		

	public ByteArray getSensitiveValueEncryptionKey() {
		return sensitiveValueEncryptionKey;
	}

	public void setSensitiveValueEncryptionKey(ByteArray sensitiveValueEncryptionKey) {
		this.sensitiveValueEncryptionKey = sensitiveValueEncryptionKey;
	}

	public ByteArray getSensitiveValueEncryptionIV() {
		return sensitiveValueEncryptionIV;
	}

	public void setSensitiveValueEncryptionIV(ByteArray sensitiveValueEncryptionIV) {
		this.sensitiveValueEncryptionIV = sensitiveValueEncryptionIV;
	}

	public static UserConfigTO load(File file) throws ConfigException {
		try {
			return serializer.read(UserConfigTO.class, file);
		}
		catch (Exception e) {
			throw new ConfigException("User config file cannot be read or is invalid: " + file, e);
		}
	}
	
	public static void save(UserConfigTO userConfigTO, File file) throws ConfigException {
		try {
			serializer.write(userConfigTO, file);
		}
		catch (Exception e) {
			throw new ConfigException("Cannot write user config to file " + file, e);
		}
	}
	
	private static class ByteArrayConverter implements Converter<ByteArray> {
		@Override
		public ByteArray read(InputNode node) throws Exception {
			return new ByteArray(StringUtil.fromHex(node.getValue()));
		}

		@Override
		public void write(OutputNode node, ByteArray value) throws Exception {
			node.setValue(StringUtil.toHex(value.getArray()));
		}
	}
}
