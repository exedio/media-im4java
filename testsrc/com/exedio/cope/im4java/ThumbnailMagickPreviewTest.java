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

import static com.exedio.cope.junit.Assert.assertFails;
import static com.exedio.cope.pattern.MediaType.GIF;
import static com.exedio.cope.pattern.MediaType.JPEG;
import static com.exedio.cope.pattern.MediaType.PNG;
import static com.exedio.cope.pattern.MediaType.TIFF;
import static com.exedio.cope.pattern.MediaType.forMagics;
import static com.exedio.cope.pattern.MediaType.forName;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertThrows;

import com.exedio.cope.Item;
import com.exedio.cope.instrument.WrapperIgnore;
import com.exedio.cope.instrument.WrapperType;
import com.exedio.cope.pattern.Media;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import junit.framework.TestCase;
import org.im4java.core.IMOperation;

public final class ThumbnailMagickPreviewTest extends TestCase
{
	public void testDefaultType() throws IOException
	{
		load("thumbnail-test.png");
		assertEquals(PNG, AnItem.f.preview(sourceBody, "image/png", target));
		assertTrue(Files.exists(target));
		assertEquals(singleton(forName(PNG)), forMagics(target));
	}
	public void testForType() throws IOException
	{
		load("thumbnail-test.gif");
		assertEquals(JPEG, AnItem.f.preview(sourceBody, "image/gif", target));
		assertTrue(Files.exists(target));
		assertEquals(singleton(forName(JPEG)), forMagics(target));
	}
	public void testContentTypeMismatch() throws IOException
	{
		load("thumbnail-test.png");
		final RuntimeException e = assertThrows(
				"filter",
				RuntimeException.class,
				() -> AnItem.f.preview(sourceBody, "image/gif", target));
		final String message = e.getCause().getMessage();
		assertTrue(message, message.startsWith(
				"org.im4java.core.CommandException: convert-im6.q16: improper image header "));
		assertFalse(Files.exists(target));
	}
	public void testTruncated() throws IOException
	{
		load("thumbnail-test-truncated.png");
		final RuntimeException e = assertThrows(
				"filter",
				RuntimeException.class,
				() -> AnItem.f.preview(sourceBody, "image/png", target));
		final String message = e.getCause().getMessage();
		assertTrue(message, message.startsWith(
				"org.im4java.core.CommandException: convert-im6.q16: Expected 5166 bytes; found 2029 bytes " +
				"`" + sourceBody + "' @ warning/png.c/MagickPNGWarningHandler/"));
		assertFalse(Files.exists(target));
	}
	public void testUnsupportedContentType() throws IOException
	{
		load("thumbnail-test.png");
		assertFails(
				() -> AnItem.f.preview(sourceBody, "text/html", target),
				IllegalArgumentException.class,
				"unsupported content type text/html");
		assertFalse(Files.exists(target));
	}
	public void testOverwriteTarget() throws IOException
	{
		Files.copy(ThumbnailMagickExplicitFormatTest.class.getResourceAsStream("thumbnail-test.tif"), target);
		assertTrue(Files.exists(target));
		assertEquals(singleton(forName(TIFF)), forMagics(target));

		load("thumbnail-test.gif");
		assertEquals(JPEG, AnItem.f.preview(sourceBody, "image/gif", target));
		assertTrue(Files.exists(target));
		assertEquals(singleton(forName(JPEG)), forMagics(target));
	}
	public void testSourceBodyNull()
	{
		assertFails(
				() -> AnItem.f.preview(null, "image/png", target),
				NullPointerException.class, null);
	}
	public void testSourceBodyNotExists()
	{
		assertFalse(Files.exists(sourceBody));
		final RuntimeException e = assertThrows(
				"filter",
				RuntimeException.class,
				() -> AnItem.f.preview(sourceBody, "image/png", target));
		final String message = e.getCause().getMessage();
		assertTrue(message, message.startsWith(
				"org.im4java.core.CommandException: convert-im6.q16: unable to open image "));
		assertFalse(Files.exists(target));
	}
	public void testSourceContentTypeNull() throws IOException
	{
		load("thumbnail-test.png");
		assertFails(
				() -> AnItem.f.preview(sourceBody, null, target),
				NullPointerException.class, "name");
	}
	public void testTargetNull() throws IOException
	{
		load("thumbnail-test.png");
		assertFails(
				() -> AnItem.f.preview(sourceBody, "image/png", null),
				NullPointerException.class, null);
	}


	private Path sourceBody;
	private Path target;
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		sourceBody = Files.createTempFile("ThumbnailMagickFilterTest-sourceBody", ".dat");
		target = Files.createTempFile("ThumbnailMagickFilterTest-target", ".dat");
		Files.delete(sourceBody);
		Files.delete(target);
	}
	@Override
	protected void tearDown() throws Exception
	{
		Files.deleteIfExists(sourceBody);
		Files.deleteIfExists(target);
		super.tearDown();
	}
	private void load(final String name) throws IOException
	{
		Files.copy(ThumbnailMagickExplicitFormatTest.class.getResourceAsStream(name), sourceBody);
	}


	@WrapperType(indent=2, comments=false)
	static final class AnItem extends Item
	{
		@WrapperIgnore static final Media file = new Media();
		@WrapperIgnore static final MediaImageMagickFilter f =
				new MediaImageMagickFilter(file,
						new IMOperation().resize(20, 30, '>')).
				forType(GIF, new IMOperation().resize(20, 30, '>'), JPEG);


		@com.exedio.cope.instrument.Generated
		AnItem()
		{
			this(new com.exedio.cope.SetValue<?>[]{
			});
		}

		@com.exedio.cope.instrument.Generated
		private AnItem(final com.exedio.cope.SetValue<?>... setValues){super(setValues);}

		@com.exedio.cope.instrument.Generated
		private static final long serialVersionUID = 1l;

		@com.exedio.cope.instrument.Generated
		static final com.exedio.cope.Type<AnItem> TYPE = com.exedio.cope.TypesBound.newType(AnItem.class);

		@com.exedio.cope.instrument.Generated
		private AnItem(final com.exedio.cope.ActivationParameters ap){super(ap);}
	}
}
