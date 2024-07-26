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

import static com.exedio.cope.im4java.OSHelper.assumeNotGithub;
import static com.exedio.cope.im4java.OSHelper.getProgramName;
import static com.exedio.cope.im4java.OSHelper.isWindows;
import static com.exedio.cope.junit.Assert.assertFails;
import static com.exedio.cope.pattern.MediaType.GIF;
import static com.exedio.cope.pattern.MediaType.JPEG;
import static com.exedio.cope.pattern.MediaType.PNG;
import static com.exedio.cope.pattern.MediaType.TIFF;
import static com.exedio.cope.pattern.MediaType.forMagics;
import static com.exedio.cope.pattern.MediaType.forName;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.exedio.cope.Item;
import com.exedio.cope.instrument.WrapperIgnore;
import com.exedio.cope.instrument.WrapperType;
import com.exedio.cope.pattern.Media;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.im4java.core.IMOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class ThumbnailMagickPreviewTest
{
	@Test void testDefaultType() throws IOException
	{
		load("thumbnail-test.png");
		assertEquals(PNG, AnItem.f.preview(sourceBody, "image/png", target));
		assertTrue(Files.exists(target));
		assertEquals(singleton(forName(PNG)), forMagics(target));
	}
	@Test void testForType() throws IOException
	{
		load("thumbnail-test.gif");
		assertEquals(JPEG, AnItem.f.preview(sourceBody, "image/gif", target));
		assertTrue(Files.exists(target));
		assertEquals(singleton(forName(JPEG)), forMagics(target));
	}
	@Test void testForTypeIdentity() throws IOException
	{
		final byte[] bytes = "some text".getBytes(UTF_8);
		Files.write(sourceBody, bytes);
		assertEquals("text/plain", AnItem.f.preview(sourceBody, "text/plain", target));
		assertTrue(Files.exists(target));
		assertArrayEquals(bytes, Files.readAllBytes(target));
	}
	@Test void testContentTypeMismatch() throws IOException
	{
		load("thumbnail-test.png");
		final RuntimeException e = assertThrows(
				RuntimeException.class,
				() -> AnItem.f.preview(sourceBody, "image/gif", target));
		final String message = e.getCause().getMessage();
		assertTrue(message.startsWith(
				"org.im4java.core.CommandException: " + getProgramName() + ": improper image header "), message);
		assertFalse(Files.exists(target));
	}
	@Test void testTruncated() throws IOException
	{
		load("thumbnail-test-truncated.png");
		final RuntimeException e = assertThrows(
				RuntimeException.class,
				() -> AnItem.f.preview(sourceBody, "image/png", target));
		final String message = e.getCause().getMessage();
		assumeNotGithub();
		final String expectedMessageDetails = isWindows() ?
				("Expected 5166 bytes; found 2029 bytes `" + sourceBody + "' ") :
				("no images defined `png:" + target + "' @ error/convert.c/ConvertImageCommand/");
		assertTrue(
				message.startsWith("org.im4java.core.CommandException: " + getProgramName() + ": " + expectedMessageDetails),
				message
		);
		assertFalse(Files.exists(target));
	}
	@Test void testUnsupportedContentType() throws IOException
	{
		load("thumbnail-test.png");
		assertFails(
				() -> AnItem.f.preview(sourceBody, "text/html", target),
				IllegalArgumentException.class,
				"unsupported content type text/html");
		assertFalse(Files.exists(target));
	}
	@Test void testOverwriteTarget() throws IOException
	{
		Files.copy(requireNonNull(ThumbnailMagickExplicitFormatTest.class.getResourceAsStream("thumbnail-test.tif")), target);
		assertTrue(Files.exists(target));
		assertEquals(singleton(forName(TIFF)), forMagics(target));

		load("thumbnail-test.gif");
		assertEquals(JPEG, AnItem.f.preview(sourceBody, "image/gif", target));
		assertTrue(Files.exists(target));
		assertEquals(singleton(forName(JPEG)), forMagics(target));
	}
	@Test void testSourceBodyNull()
	{
		assertFails(
				() -> AnItem.f.preview(null, "image/png", target),
				NullPointerException.class, null);
	}
	@Test void testSourceBodyNotExists()
	{
		assertFalse(Files.exists(sourceBody));
		final RuntimeException e = assertThrows(
				RuntimeException.class,
				() -> AnItem.f.preview(sourceBody, "image/png", target));
		final String message = e.getCause().getMessage();
		assertTrue(message.startsWith(
				"org.im4java.core.CommandException: " + getProgramName() + ": unable to open image "), message);
		assertFalse(Files.exists(target));
	}
	@Test void testSourceContentTypeNull() throws IOException
	{
		load("thumbnail-test.png");
		assertFails(
				() -> AnItem.f.preview(sourceBody, null, target),
				NullPointerException.class, "name");
	}
	@Test void testTargetNull() throws IOException
	{
		load("thumbnail-test.png");
		assertFails(
				() -> AnItem.f.preview(sourceBody, "image/png", null),
				NullPointerException.class, null);
	}


	private Path sourceBody;
	private Path target;
	@BeforeEach
	private void setUp() throws Exception
	{
		sourceBody = Files.createTempFile("ThumbnailMagickFilterTest-sourceBody", ".dat");
		target = Files.createTempFile("ThumbnailMagickFilterTest-target", ".dat");
		Files.delete(sourceBody);
		Files.delete(target);
	}
	@AfterEach
	private void tearDown() throws Exception
	{
		Files.deleteIfExists(sourceBody);
		Files.deleteIfExists(target);
	}
	private void load(final String name) throws IOException
	{
		Files.copy(requireNonNull(ThumbnailMagickExplicitFormatTest.class.getResourceAsStream(name)), sourceBody);
	}


	@WrapperType(indent=2, comments=false)
	static final class AnItem extends Item
	{
		@WrapperIgnore static final Media file = new Media();
		@WrapperIgnore static final MediaImageMagickFilter f =
				new MediaImageMagickFilter(file,
						new IMOperation().resize(20, 30, '>')).
				forTypeIdentity("text/plain").
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
