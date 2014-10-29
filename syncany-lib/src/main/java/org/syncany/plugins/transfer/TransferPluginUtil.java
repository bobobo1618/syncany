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
package org.syncany.plugins.transfer;

import org.syncany.plugins.Plugins;

/**
 * Helper class for {@link TransferPlugin}s, using to retrieve
 * the required transfer plugin classes -- namely {@link TransferSettings},
 * {@link TransferManager} and {@link TransferPlugin}. 
 * 
 * @author Christian Roth <christian.roth@port17.de>
 */
public abstract class TransferPluginUtil {
	/**
	 * Determines the {@link TransferPlugin} class for a given 
	 * {@link TransferSettings} class.
	 */
	public static Class<? extends TransferPlugin> getTransferPluginClass(Class<? extends TransferSettings> transferSettingsClass) {
		for (TransferPlugin plugin : Plugins.list(TransferPlugin.class)) {
			if (TransferPluginUtil.getTransferSettingsClass(plugin.getClass()).equals(transferSettingsClass)) {
				return plugin.getClass();
			}
		}

		throw new RuntimeException("There is no transfer plugin for settings (" + transferSettingsClass.getName() + ")");
	}
}
