/*
 * Copyright (C) 2017 Christian P. Lerch
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
import java.util.Iterator;

/**
 *
 * @author Christian P. Lerch
 */
class IteratorEnumeration<E> implements Enumeration<E>
{
    private final Iterator<E> iterator;

    public IteratorEnumeration(Iterator<E> iterator)
    {
        this.iterator = iterator;
    }

    @Override
    public E nextElement() {
        return iterator.next();
    }

    @Override
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

}
