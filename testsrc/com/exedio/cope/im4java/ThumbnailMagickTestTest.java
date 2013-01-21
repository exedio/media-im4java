/*
 * Copyright (C) 2004-2012  exedio GmbH (www.exedio.com)
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

package com.exedio.cope.im4java;

import static com.exedio.cope.pattern.MediaType.GIF;
import static com.exedio.cope.pattern.MediaType.JPEG;
import static com.exedio.cope.pattern.MediaType.PNG;

import java.io.IOException;

import org.im4java.core.CommandException;
import org.im4java.core.IMOperation;
import org.im4java.core.IMOps;

import com.exedio.cope.junit.CopeAssert;
import com.exedio.cope.pattern.Media;

public final class ThumbnailMagickTestTest extends CopeAssert
{
	static final Media file = new Media().optional().lengthMax(10000);
	@SuppressWarnings("boxing")
	static final IMOps op = new IMOperation().resize(20, 30, '>');

	@SuppressWarnings({"static-method","boxing"})
	public void testTest() throws IOException
	{
		new MediaImageMagickFilter(file, op).test();
		new MediaImageMagickFilter(file, op, JPEG).test();
		new MediaImageMagickFilter(file, op, PNG).test();
		new MediaImageMagickFilter(file, op, GIF).test();

		assertFails("30x10#", new MediaImageMagickFilter(file, new IMOperation().resize(30, 10, '#')));
		assertFails("30x10#", new MediaImageMagickFilter(file, new IMOperation().resize(30, 10, '#'), JPEG));
		assertFails("30x10#", new MediaImageMagickFilter(file, new IMOperation().resize(30, 10, '#'), PNG));
		assertFails("30x10#", new MediaImageMagickFilter(file, new IMOperation().resize(30, 10, '#'), GIF));
		final MediaImageMagickFilter f1 = new MediaImageMagickFilter(file, op);
		f1.forType(PNG, new IMOperation().resize(40, 10, '#'), null).test(); // TODO
		f1.forType(PNG, new IMOperation().resize(40, 10, '#'), JPEG).test(); // TODO
		f1.forType(PNG, new IMOperation().resize(40, 10, '#'), PNG ).test(); // TODO
		f1.forType(PNG, new IMOperation().resize(40, 10, '#'), GIF ).test(); // TODO
	}

	private static final void assertFails(
			final String errorMessage,
			final MediaImageMagickFilter filter)
		throws IOException
	{
		try
		{
			filter.test();
			fail();
		}
		catch(final RuntimeException runtimeException)
		{
			assertStartsWith(
					"org.im4java.core.CommandException: org.im4java.core.CommandException: convert: invalid argument for option `-resize': " + errorMessage + " @ ",
					runtimeException.getMessage());
			final CommandException commandException = (CommandException)runtimeException.getCause();
			assertStartsWith(
					"org.im4java.core.CommandException: convert: invalid argument for option `-resize': " + errorMessage + " @ ",
					commandException.getMessage());
			assertEquals(-1, commandException.getReturnCode());
			assertEquals(list(), commandException.getErrorText());
			final CommandException commandException2 = (CommandException)commandException.getCause();
			assertStartsWith(
					"convert: invalid argument for option `-resize': " + errorMessage + " @ ",
					commandException2.getMessage());
			assertEquals(1, commandException2.getReturnCode());
			assertEquals(null, commandException2.getCause());
		}
	}

	private static final void assertStartsWith(
			final String expected,
			final String actual)
	{
		assertTrue(actual, actual.startsWith(expected));
	}
}
