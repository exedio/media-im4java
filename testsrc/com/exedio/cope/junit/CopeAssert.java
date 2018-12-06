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
import junit.framework.TestCase;
import org.junit.Assert;

public abstract class CopeAssert extends TestCase
{
	public static final void assertContainsList(final List<?> expected, final Collection<?> actual)
	{
		if(expected==null && actual==null)
			return;

		Assert.assertNotNull("expected null, but was " + actual, expected);
		Assert.assertNotNull("expected " + expected + ", but was null", actual);

		if(expected.size()!=actual.size() ||
				!expected.containsAll(actual) ||
				!actual.containsAll(expected))
			Assert.fail("expected "+expected+", but was "+actual);
	}

	public static final void assertContains(final Collection<?> actual)
	{
		assertContainsList(Collections.emptyList(), actual);
	}

	public static final void assertContains(final Object o, final Collection<?> actual)
	{
		assertContainsList(Collections.singletonList(o), actual);
	}

	public static final void assertContains(final Object o1, final Object o2, final Collection<?> actual)
	{
		assertContainsList(Arrays.asList(o1, o2), actual);
	}

	public static final void assertContains(final Object o1, final Object o2, final Object o3, final Collection<?> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3), actual);
	}

	public static final void assertContains(final Object o1, final Object o2, final Object o3, final Object o4, final Collection<?> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4), actual);
	}

	public static final void assertContains(final Object o1, final Object o2, final Object o3, final Object o4, final Object o5, final Collection<?> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4, o5), actual);
	}

	public static final void assertContains(final Object o1, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Collection<?> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4, o5, o6), actual);
	}

	public static final void assertContains(final Object o1, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Collection<?> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4, o5, o6, o7), actual);
	}

	public static final void assertContains(final Object o1, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Collection<?> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8), actual);
	}

	public static final List<Object> list(final Object... o)
	{
		return Collections.unmodifiableList(Arrays.asList(o));
	}

	public static final <S extends Serializable> S reserialize(final S value, final int expectedSize)
	{
		final byte[] bos = serialize(value);
		Assert.assertEquals(expectedSize, bos.length);
		@SuppressWarnings("unchecked")
		final S result = (S)deserialize(bos);
		return result;
	}

	public static final byte[] serialize(final Serializable value)
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

	public static final Object deserialize(final byte[] bytes)
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
			if(obj instanceof String)
			{
				final String string = (String)obj;
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
}
