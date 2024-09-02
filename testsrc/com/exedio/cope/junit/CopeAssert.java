/*
 * Copyright (C) 2004-2015  exedio GmbH (www.exedio.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.exedio.cope.junit;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public final class CopeAssert
{
	public static <T> void assertContainsList(final List<T> expected, final Collection<T> actual)
	{
		if(expected==null && actual==null)
			return;

		assertNotNull(expected, "expected null, but was " + actual);
		assertNotNull(actual, "expected " + expected + ", but was null");

		if(expected.size()!=actual.size() ||
				!expected.containsAll(actual) ||
				!actual.containsAll(expected))
			fail("expected "+expected+", but was "+actual);
	}

	public static void assertContains(final Collection<?> actual)
	{
		assertContainsList(Collections.emptyList(), actual);
	}

	public static <T> void assertContains(final T o, final Collection<T> actual)
	{
		assertContainsList(Collections.singletonList(o), actual);
	}

	public static <T> void assertContains(final T o1, final T o2, final Collection<T> actual)
	{
		assertContainsList(Arrays.asList(o1, o2), actual);
	}

	public static <T> void assertContains(final T o1, final T o2, final T o3, final Collection<T> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3), actual);
	}

	public static <T> void assertContains(final T o1, final T o2, final T o3, final T o4, final Collection<T> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4), actual);
	}

	public static <T> void assertContains(final T o1, final T o2, final T o3, final T o4, final T o5, final Collection<T> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4, o5), actual);
	}

	public static <T> void assertContains(final T o1, final T o2, final T o3, final T o4, final T o5, final T o6, final Collection<T> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4, o5, o6), actual);
	}

	public static <T> void assertContains(final T o1, final T o2, final T o3, final T o4, final T o5, final T o6, final T o7, final Collection<T> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4, o5, o6, o7), actual);
	}

	public static <T> void assertContains(final T o1, final T o2, final T o3, final T o4, final T o5, final T o6, final T o7, final T o8, final Collection<T> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8), actual);
	}

	public static List<Object> list(final Object... o)
	{
		return List.of(o);
	}

	public static <S extends Serializable> S reserialize(final S value, final int expectedSize)
	{
		final byte[] bos = serialize(value);
		assertEquals(expectedSize, bos.length);
		@SuppressWarnings("unchecked")
		final S result = (S)deserialize(bos);
		return result;
	}

	public static byte[] serialize(final Serializable value)
	{
		requireNonNull(value);

		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try(ObjectOutputStream oos = new DeduplicateStringsObjectOutputStream(bos))
		{
			oos.writeObject(value);
		}
		catch(final IOException e)
		{
			throw new RuntimeException(e);
		}
		return bos.toByteArray();
	}

	public static Object deserialize(final byte[] bytes)
	{
		requireNonNull(bytes);

		try(ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes)))
		{
			return ois.readObject();
		}
		catch(final IOException | ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static final class DeduplicateStringsObjectOutputStream extends ObjectOutputStream
	{
		private HashMap<String, String> strings = null;

		DeduplicateStringsObjectOutputStream(final ByteArrayOutputStream out) throws IOException
		{
			super(out);
			enableReplaceObject(true);
		}

		@Override
		protected Object replaceObject(final Object obj)
		{
			if(obj instanceof final String string)
			{
				if(strings==null)
					strings = new HashMap<>();

				final String replacement = strings.get(string);
				if(replacement==null)
				{
					strings.put(string, string);
					return string;
				}
				else
				{
					return replacement;
				}
			}
			else
				return obj;
		}
	}

	private CopeAssert()
	{
		// prevent instantiation
	}
}
