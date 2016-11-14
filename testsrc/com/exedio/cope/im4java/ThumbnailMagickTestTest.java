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

import com.exedio.cope.junit.CopeAssert;
import com.exedio.cope.pattern.Media;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.util.regex.Pattern;
import org.im4java.core.CommandException;
import org.im4java.core.IMOperation;
import org.im4java.core.IMOps;

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

		assertFails("-kaputt", new MediaImageMagickFilter(file, (IMOperation)new IMOperation().addRawArgs("-kaputt")));
		assertFails("-kaputt", new MediaImageMagickFilter(file, (IMOperation)new IMOperation().addRawArgs("-kaputt"), JPEG));
		assertFails("-kaputt", new MediaImageMagickFilter(file, (IMOperation)new IMOperation().addRawArgs("-kaputt"), PNG));
		assertFails("-kaputt", new MediaImageMagickFilter(file, (IMOperation)new IMOperation().addRawArgs("-kaputt"), GIF));
		final MediaImageMagickFilter f1 = new MediaImageMagickFilter(file, op);
		assertFails("-broken", f1.forType(PNG, (IMOperation)new IMOperation().addRawArgs("-broken"), null));
		assertFails("-broken", f1.forType(PNG, (IMOperation)new IMOperation().addRawArgs("-broken"), JPEG));
		assertFails("-broken", f1.forType(PNG, (IMOperation)new IMOperation().addRawArgs("-broken"), PNG ));
		assertFails("-broken", f1.forType(PNG, (IMOperation)new IMOperation().addRawArgs("-broken"), GIF ));
	}

	@SuppressWarnings({"static-method","boxing"})
	public void testUnsupportedBySource() throws IOException
	{
		final Media source = new Media().optional().lengthMax(10000).contentType(JPEG);
		final MediaImageMagickFilter filter =
				new MediaImageMagickFilter(source, op).
				forType(PNG, new IMOperation().resize(30, 20, '#'), PNG);
		filter.test();
	}

	@SuppressFBWarnings("BC_UNCONFIRMED_CAST_OF_RETURN_VALUE")
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
			final String start = "org.im4java.core.CommandException: org.im4java.core.CommandException: convert";
			final String message = ": unrecognized option `" + errorMessage + "' @ ";
			assertExceptionMessage(runtimeException, start, message);

			final CommandException commandException = (CommandException)runtimeException.getCause();
			assertExceptionMessage(commandException, "org.im4java.core.CommandException: convert", message);

			assertEquals(-1, commandException.getReturnCode());
			assertEquals(list(), commandException.getErrorText());
			final CommandException commandException2 = (CommandException)commandException.getCause();

			assertExceptionMessage(commandException2, "convert", message);
			assertEquals(1, commandException2.getReturnCode());
			assertEquals(null, commandException2.getCause());
		}
	}

	private static void assertExceptionMessage( final Exception runtimeException, final String start, final String message)
	{
		final String pattern = Pattern.quote(start)+ ".*?"+Pattern.quote(message);
		assertTrue("The exception message >"+runtimeException.getMessage()+"< did not match the expected patterns >"+start+
			"< >"+message+"<.", Pattern.compile(pattern).matcher(runtimeException.getMessage()).find());
	}
}
