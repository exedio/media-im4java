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

import static com.exedio.cope.im4java.ThumbnailMagickItem.TYPE;
import static com.exedio.cope.im4java.ThumbnailMagickItem.file;
import static com.exedio.cope.im4java.ThumbnailMagickItem.identity;
import static com.exedio.cope.im4java.ThumbnailMagickItem.noLocator;
import static com.exedio.cope.im4java.ThumbnailMagickItem.thumb;
import static com.exedio.cope.im4java.ThumbnailMagickItem.thumbFull;
import static com.exedio.cope.im4java.ThumbnailMagickItem.thumbRound;
import static com.exedio.cope.im4java.ThumbnailMagickItem.thumbSame;
import static com.exedio.cope.junit.Assert.assertFails;
import static com.exedio.cope.junit.CopeAssert.reserialize;
import static com.exedio.cope.pattern.MediaType.GIF;
import static com.exedio.cope.pattern.MediaType.JPEG;
import static com.exedio.cope.pattern.MediaType.PDF;
import static com.exedio.cope.pattern.MediaType.PNG;
import static com.exedio.cope.pattern.MediaType.SVG;
import static com.exedio.cope.pattern.MediaType.TIFF;
import static com.exedio.cope.pattern.MediaType.WEBP;
import static com.exedio.cope.pattern.MediaType.ZIP;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.exedio.cope.Feature;
import com.exedio.cope.Model;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import org.im4java.core.IMOperation;
import org.junit.jupiter.api.Test;

public final class ThumbnailMagickModelTest
{
	static final Model MODEL = new Model(TYPE);

	static
	{
		MODEL.enableSerialization(ThumbnailMagickModelTest.class, "MODEL");
	}

	@Test
	void testThumbs() throws IOException
	{
		assertEquals(asList(new Feature[]{
				TYPE.getThis(),
				file,
				file.getBody(),
				file.getContentType(),
				file.getLastModified(),
				file.getUnison(),
				thumb,
				thumbFull,
				thumbSame,
				thumbRound,
				identity,
				noLocator
			}), TYPE.getFeatures());
		assertEquals(TYPE, thumb.getType());
		assertEquals("thumb", thumb.getName());
		assertSame(file, thumb.getSource());
		assertEquals(
				new HashSet<>(asList(JPEG, "image/pjpeg", PNG, "image/x-png", GIF, WEBP, TIFF, PDF, "text/pdf", SVG)),
				thumb.getSupportedSourceContentTypes());
		assertEquals(
				new HashSet<>(asList(JPEG, "image/pjpeg", PNG, "image/x-png", GIF, WEBP, TIFF, PDF, "text/pdf", SVG, "text/plain")),
				identity.getSupportedSourceContentTypes());

		assertEquals(JPEG, thumb.getOutputContentType());
		assertEquals(PNG, thumbFull.getOutputContentType());
		assertEquals(null, thumbSame.getOutputContentType());
		assertEquals(null, thumbRound.getOutputContentType());

		assertEquals(JPEG, thumb.getOutputContentType(JPEG));
		assertEquals(JPEG, thumb.getOutputContentType(PNG));
		assertEquals(JPEG, thumb.getOutputContentType(GIF));
		assertEquals(JPEG, thumb.getOutputContentType("image/pjpeg"));
		assertEquals(JPEG, thumb.getOutputContentType("image/x-png"));
		assertEquals(null, thumb.getOutputContentType("text/plain"));
		assertEquals(null, thumb.getOutputContentType(null));

		assertEquals(PNG,  thumbFull.getOutputContentType(JPEG));
		assertEquals(PNG,  thumbFull.getOutputContentType(PNG));
		assertEquals(PNG,  thumbFull.getOutputContentType(GIF));
		assertEquals(PNG,  thumbFull.getOutputContentType("image/pjpeg"));
		assertEquals(PNG,  thumbFull.getOutputContentType("image/x-png"));
		assertEquals(null, thumbFull.getOutputContentType("text/plain"));
		assertEquals(null, thumbFull.getOutputContentType(null));

		assertEquals(JPEG, thumbSame.getOutputContentType(JPEG));
		assertEquals(PNG,  thumbSame.getOutputContentType(PNG));
		assertEquals(GIF,  thumbSame.getOutputContentType(GIF));
		assertEquals(JPEG, thumbSame.getOutputContentType("image/pjpeg"));
		assertEquals(PNG,  thumbSame.getOutputContentType("image/x-png"));
		assertEquals(null, thumbSame.getOutputContentType("text/plain"));
		assertEquals(null, thumbSame.getOutputContentType(null));

		assertEquals(PNG,  thumbRound.getOutputContentType(JPEG));
		assertEquals(GIF,  thumbRound.getOutputContentType(PNG));
		assertEquals(JPEG, thumbRound.getOutputContentType(GIF));
		assertEquals(PNG,  thumbRound.getOutputContentType("image/pjpeg"));
		assertEquals(GIF,  thumbRound.getOutputContentType("image/x-png"));
		assertEquals(null, thumbRound.getOutputContentType("text/plain"));
		assertEquals(null, thumbRound.getOutputContentType(null));

		assertEquals(JPEG, identity.getOutputContentType(JPEG));
		assertEquals(PNG,  identity.getOutputContentType(PNG));
		assertEquals(JPEG, identity.getOutputContentType(GIF));
		assertEquals(JPEG, identity.getOutputContentType("image/pjpeg"));
		assertEquals("image/x-png", identity.getOutputContentType("image/x-png"));
		assertEquals("text/plain", identity.getOutputContentType("text/plain"));
		assertEquals(null, identity.getOutputContentType(null));

		assertEquals(file.isNull(), thumb.isNull());
		assertEquals(file.isNotNull(), thumb.isNotNull());

		assertSerializedSame(thumb, 398);

		assertEquals(asList("-limit", "thread", "1", "-resize", "20x30>", "?img?", "?img?"), thumb     .getCmdArgs(JPEG));
		assertEquals(asList("-limit", "thread", "1", "-resize", "20x30>", "?img?", "?img?"), thumbSame .getCmdArgs(JPEG));
		assertEquals(asList("-limit", "thread", "1", "-resize", "20x30>", "?img?", "?img?"), thumbRound.getCmdArgs(ZIP));
		assertEquals(asList("-limit", "thread", "1", "-resize", "30x40>", "?img?", "?img?"), thumbRound.getCmdArgs(JPEG));
		assertEquals(asList("-limit", "thread", "1", "-resize", "20x30>",
				"-density", "300", "-units", "PixelsPerInch",
				"-flatten", "-background", "white",
				"?img?", "?img?"),
				thumbFull.getCmdArgs(JPEG));
		assertEquals(asList("-limit", "thread", "1", "-resize", "20x30>", "?img?", "?img?"), identity.getCmdArgs(JPEG));
		assertEquals(null, identity.getCmdArgs(PNG));
		assertEquals(null, identity.getCmdArgs("text/plain"));

		assertNotNull(thumb.getScript(JPEG));
		assertNotNull(thumbFull.getScript(JPEG));
		assertNotNull(thumbSame.getScript(JPEG));
		assertNotNull(thumbRound.getScript(JPEG));
		assertNotNull(identity.getScript(JPEG));
		assertNull(identity.getScript(PNG));
		assertNull(identity.getScript("text/plain"));

		thumb.test();
		thumbFull.test();
		thumbSame.test();
		thumbRound.test();
		identity.test();
		noLocator.test();
	}

	@Test
	void testCreate()
	{
		final IMOperation operation = new IMOperation();
		assertFails(
				() -> new MediaImageMagickFilter(null, operation),
				NullPointerException.class,
				"source");
		assertFails(
				() -> new MediaImageMagickFilter(file, null),
				NullPointerException.class,
				"operation");
		assertFails(
				() -> new MediaImageMagickFilter(file, operation, ZIP),
				IllegalArgumentException.class,
				"unsupported outputContentType >application/zip<");
		assertFails(
				() -> new MediaImageMagickFilter(file, operation, "non/sense"),
				IllegalArgumentException.class,
				"unsupported outputContentType >non/sense<");
		final MediaImageMagickFilter template = new MediaImageMagickFilter(file, operation, JPEG);
		assertFails(
				() -> template.forType(null, null, ZIP),
				NullPointerException.class,
				"inputContentType");
		assertFails(
				() -> template.forType(JPEG, null, ZIP),
				NullPointerException.class,
				"operation");
		assertFails(
				() -> template.forType(ZIP, operation, JPEG),
				IllegalArgumentException.class,
				"unsupported inputContentType >application/zip<");
		assertFails(
				() -> template.forType("non/sense", operation, JPEG),
				IllegalArgumentException.class,
				"unsupported inputContentType >non/sense<");
		assertFails(
				() -> template.forType(JPEG, operation, ZIP),
				IllegalArgumentException.class,
				"unsupported outputContentType >application/zip<");
		assertFails(
				() -> template.forType(JPEG, operation, "non/sense"),
				IllegalArgumentException.class,
				"unsupported outputContentType >non/sense<");
		final MediaImageMagickFilter template2 = template.forType(JPEG, operation, JPEG);
		assertFails(
				() -> template2.forType(JPEG, operation, JPEG),
				IllegalArgumentException.class,
				"duplicate inputContentType image/jpeg");
		assertFails(
				() -> template2.forTypeIdentity(JPEG),
				IllegalArgumentException.class,
				"duplicate inputContentType image/jpeg");
		assertFails(
				() -> template2.forTypeIdentity("image/pjpeg"),
				IllegalArgumentException.class,
				"duplicate inputContentType image/pjpeg");
		final MediaImageMagickFilter identityPng = template.forTypeIdentity(PNG);
		assertFails(
				() -> identityPng.forTypeIdentity(PNG),
				IllegalArgumentException.class,
				"duplicate inputContentType image/png");
		assertFails(
				() -> identityPng.forTypeIdentity("image/x-png"),
				IllegalArgumentException.class,
				"duplicate inputContentType image/x-png");
		assertFails(
				() -> identityPng.forType(PNG, operation, PNG),
				IllegalArgumentException.class,
				"duplicate inputContentType image/png");
		final MediaImageMagickFilter identityPngX = template.forTypeIdentity("image/x-png");
		assertFails(
				() -> identityPngX.forTypeIdentity(PNG),
				IllegalArgumentException.class,
				"duplicate inputContentType image/x-png");
		assertFails(
				() -> identityPngX.forTypeIdentity("image/x-png"),
				IllegalArgumentException.class,
				"duplicate inputContentType image/x-png");
		final MediaImageMagickFilter identityTxt = template.forTypeIdentity("text/plain");
		assertFails(
				() -> identityTxt.forTypeIdentity("text/plain"),
				IllegalArgumentException.class,
				"duplicate inputContentType text/plain");
	}

	private static void assertSerializedSame(final Serializable value, final int expectedSize)
	{
		assertSame(value, reserialize(value, expectedSize));
	}
}
