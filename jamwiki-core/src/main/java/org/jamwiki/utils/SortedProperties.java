/*
 * Copyright (C) 2005-2017 Christian P. Lerch <christian.p.lerch[at]gmail.com>
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
package org.jamwiki.utils;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Christian P. Lerch
 */
public class SortedProperties extends Properties {

    private static final long serialVersionUID = 1L;

	/**
	 * Standard constructor for creating a sorted properties.
	 */
	public SortedProperties() {
		super();
	}

	/**
	 * Copy constructor used to create a sorted properties.
     * @param properties
	 */
	public SortedProperties(Properties properties) {
		super();
		putAll(properties);
	}

	/**
	 * Overridden Properties.keys() method so that the keyset returned is sorted.
     * @return sorted keys set
	 */
    @Override
	public Enumeration keys() {
		Enumeration keyEnum = super.keys();
		Set<String> keys = new TreeSet<>();
		while (keyEnum.hasMoreElements()) {
			keys.add((String)keyEnum.nextElement());
		}
		return new IteratorEnumeration<>(keys.iterator());
	}
    
    public Map<String,String> asMap() {
        return (Map<String,String>)(Map<?,?>)this;
    }
}
