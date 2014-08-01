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
package org.syncany.config.to.converters;

/**
 * Interface defining methods which every {@link TypedPropertyElementConverter} has to support.
 *
 * @author Christian Roth <christian.roth@port17.de>
 */

public interface TypedPropertyElementConverter {

	/**
	 * Method invoked when {@link org.simpleframework.xml.Serializer} wants to restore a property list from a xml-file.
	 *
	 * @param in The string representation in the xml file
	 * @return The converted string representation
	 * @throws Exception Thrown if conversion failed
	 */
	public String from(String in) throws Exception;

  /**
   * Method invoked when {@link org.simpleframework.xml.Serializer} wants to store a property list to a xml-file.
   *
   * @param out The string representation while deserialized
   * @return The converted string representation to be stored in the xml file
   * @throws Exception Thrown if conversion failed
   */
	public String to(String out) throws Exception;

}
