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

import com.google.common.collect.Maps;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.Persist;
import org.syncany.config.ConfigException;
import org.syncany.config.to.converters.PasswordTypedPropertyElementConverter;
import org.syncany.config.to.converters.TypedPropertyElementConverter;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The typed property list is a helper data structure that allows storing an
 * object of a certain type with its properties .
 *
 * <p>It is used in the {@link RepoTO} for chunker, multichunker and transformer,
 * and in the {@link ConfigTO} for the connection settings.
 *
 * <p>It uses the Simple framework for XML serialization, and its corresponding
 * annotation-based configuration.
 *
 * @see <a href="http://simple.sourceforge.net/">Simple framework</a> at simple.sourceforge.net
 *
 * @author Philipp C. Heckel <philipp.heckel@gmail.com>
 * @author Christian Roth <christian.roth@port17.de>
 */
public abstract class TypedPropertyListTO {
	private static final Logger logger = Logger.getLogger(TypedPropertyListTO.class.getSimpleName());
	private static final Map<String, Class<? extends TypedPropertyElementConverter>> CONVERTERS = Maps.newHashMap();

	static {
		CONVERTERS.put(PasswordTypedPropertyElementConverter.PROPERTY_NAME.toLowerCase(), PasswordTypedPropertyElementConverter.class);
	}

	@Attribute(required = true)
	private String type;

	@ElementMap(entry = "property", key = "name", required = false, attribute = true, inline = true)
	protected Map<String, String> settings;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}

	@Commit
	public void commit() throws ConfigException {
		if (settings == null) {
			return;
		}

		for (String setting : settings.keySet()) {
			String settingLC = setting.toLowerCase();

			if (CONVERTERS.containsKey(settingLC)) {
				try {
					settings.put(setting, CONVERTERS.get(settingLC).newInstance().from(settings.get(setting)));
				}
				catch (Exception e) {
					logger.log(Level.SEVERE, "Unable to convert " + setting + " in xml property list before restoring", e);
					throw new ConfigException("Unable to convert " + setting + " in xml property list before restoring", e);
				}
			}
		}
	}

	@Persist
	public void prepare() throws ConfigException {
		if (settings == null) {
			return;
		}

		for (String setting : settings.keySet()) {
			String settingLC = setting.toLowerCase();

			if (CONVERTERS.containsKey(settingLC)) {
				try {
					settings.put(setting, CONVERTERS.get(settingLC).newInstance().to(settings.get(setting)));
				}
				catch (Exception e) {
					logger.log(Level.SEVERE, "Unable to convert " + setting + " in xml property list before saving", e);
					throw new ConfigException("Unable to convert " + setting + " in xml property list before saving", e);
				}
			}
		}
	}
}
