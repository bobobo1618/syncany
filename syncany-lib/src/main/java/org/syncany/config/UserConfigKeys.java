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
package org.syncany.config;

/**
 * Overview of supported and used keys in {@link org.syncany.config.UserConfig}.
 *
 * @author Christian Roth <christian.roth@port17.de>
 */

public final class UserConfigKeys {

	private UserConfigKeys() {
	}

	public enum System {
		// @formatter:off
		STORAGE_SECRET("storage.secret"), STORAGE_IV("storage.iv");
		// @formatter:on

		private final String key;

		private System(String key) {
			this.key = key;
		}

		@Override
		public String toString() {
			return key;
		}

		public static UserConfigKeys.System fromString(String key) {
			for (System k : values()) {
				if (k.toString().equalsIgnoreCase(key)) {
					return k;
				}
			}
			throw new IllegalArgumentException("No UserConfigKey for " + key);
		}
	}

}
