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
import static com.exedio.cope.im4java.ThumbnailMagickItem.thumb;
import static com.exedio.cope.im4java.ThumbnailMagickItem.thumbFull;
import static com.exedio.cope.im4java.ThumbnailMagickItem.thumbRound;
import static com.exedio.cope.im4java.ThumbnailMagickItem.thumbSame;
import static com.exedio.cope.junit.Assert.assertFails;
import static com.exedio.cope.junit.CopeAssert.assertContains;
import static com.exedio.cope.pattern.MediaType.GIF;
import static com.exedio.cope.pattern.MediaType.JPEG;
import static com.exedio.cope.pattern.MediaType.PNG;
import static com.exedio.cope.pattern.MediaType.TIFF;
import static com.exedio.cope.pattern.MediaType.WEBP;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.exedio.cope.Model;
import com.exedio.cope.junit.CopeModelTest;
import com.exedio.cope.pattern.MediaPath.NotFound;
import com.exedio.cope.pattern.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class ThumbnailMagickTest extends CopeModelTest
{
	static final Model MODEL = ThumbnailMagickModelTest.MODEL;

	public ThumbnailMagickTest()
	{
		super(MODEL);
	}

	private String mediaRootUrl = null;
	private ThumbnailMagickItem jpg, jpgX, png, pngX, gif, wep, tif, txt, emp;
	private final byte[] data  = {-86,122,-8,23};

	// Ok, because Media#set(Item,InputStream,String) closes the stream.

	@BeforeEach
	private void setUp() throws Exception
	{
		mediaRootUrl = model.getConnectProperties().getMediaRootUrl();
		jpg = new ThumbnailMagickItem();
		jpgX= new ThumbnailMagickItem();
		png = new ThumbnailMagickItem();
		pngX= new ThumbnailMagickItem();
		gif = new ThumbnailMagickItem();
		wep = new ThumbnailMagickItem();
		tif = new ThumbnailMagickItem();
		txt = new ThumbnailMagickItem();
		emp = new ThumbnailMagickItem();
		jpg.setFile(resource("thumbnail-test.jpg"), JPEG);
		png.setFile(resource("thumbnail-test.png"), PNG);
		gif.setFile(resource("thumbnail-test.gif"), GIF);
		wep.setFile(resource("thumbnail-test.webp"), WEBP);
		tif.setFile(resource("thumbnail-test.tif"), TIFF);
		jpgX.setFile(resource("thumbnail-test.jpg"), "image/pjpeg");
		pngX.setFile(resource("thumbnail-test.png"), "image/x-png");
		txt.setFile(data, "text/plain");
	}

	private static InputStream resource(final String name)
	{
		return ThumbnailMagickTest.class.getResourceAsStream(name);
	}

	private static byte[] resourceBytes(final String name) throws IOException
	{
		try(InputStream source = resource(name))
		{
			final ByteArrayOutputStream sink = new ByteArrayOutputStream();
			long nread = 0L;
			final byte[] buf = new byte[1024];
			int n;
			//noinspection NestedAssignment
			while((n = source.read(buf)) > 0)
			{
				sink.write(buf, 0, n);
				nread += n;
			}
			return sink.toByteArray();
		}
	}

	@Test void testThumbs() throws IOException, NotFound
	{
		// content type
		assertEquals(JPEG, jpg.getThumbContentType());
		assertEquals(JPEG, png.getThumbContentType());
		assertEquals(JPEG, gif.getThumbContentType());
		assertEquals(JPEG, wep.getThumbContentType());
		assertEquals(JPEG, tif.getThumbContentType());
		assertEquals(JPEG, jpgX.getThumbContentType());
		assertEquals(JPEG, pngX.getThumbContentType());
		assertEquals(null, txt.getThumbContentType());
		assertEquals(null, emp.getThumbContentType());

		assertEquals(PNG, jpg.getThumbFullContentType());
		assertEquals(PNG, png.getThumbFullContentType());
		assertEquals(PNG, gif.getThumbFullContentType());
		assertEquals(PNG, wep.getThumbFullContentType());
		assertEquals(PNG, tif.getThumbFullContentType());
		assertEquals(PNG, jpgX.getThumbFullContentType());
		assertEquals(PNG, pngX.getThumbFullContentType());
		assertEquals(null, txt.getThumbFullContentType());
		assertEquals(null, emp.getThumbFullContentType());

		assertEquals(JPEG, jpg.getThumbSameContentType());
		assertEquals(PNG,  png.getThumbSameContentType());
		assertEquals(GIF,  gif.getThumbSameContentType());
		assertEquals(WEBP, wep.getThumbSameContentType());
		assertEquals(TIFF, tif.getThumbSameContentType());
		assertEquals(JPEG, jpgX.getThumbSameContentType());
		assertEquals(PNG,  pngX.getThumbSameContentType());
		assertEquals(null, txt.getThumbSameContentType());
		assertEquals(null, emp.getThumbSameContentType());

		assertEquals(PNG,  jpg.getThumbRoundContentType());
		assertEquals(GIF,  png.getThumbRoundContentType());
		assertEquals(JPEG, gif.getThumbRoundContentType());
		assertEquals(WEBP, wep.getThumbRoundContentType());
		assertEquals(TIFF, tif.getThumbRoundContentType());
		assertEquals(PNG,  jpgX.getThumbRoundContentType());
		assertEquals(GIF,  pngX.getThumbRoundContentType());
		assertEquals(null, txt.getThumbRoundContentType());
		assertEquals(null, emp.getThumbRoundContentType());

		assertEquals(JPEG, jpg.getIdentityContentType());
		assertEquals(PNG,  png.getIdentityContentType());
		assertEquals(JPEG, gif.getIdentityContentType());
		assertEquals(JPEG, wep.getIdentityContentType());
		assertEquals(JPEG, tif.getIdentityContentType());
		assertEquals(JPEG, jpgX.getIdentityContentType());
		assertEquals("image/x-png", pngX.getIdentityContentType());
		assertEquals("text/plain", txt.getIdentityContentType());
		assertEquals(null, emp.getIdentityContentType());

		// get
		assertType(JPEG, jpg.getThumb());
		assertType(JPEG, png.getThumb());
		assertType(JPEG, gif.getThumb());
		assertType(JPEG, wep.getThumb());
		assertType(JPEG, tif.getThumb());
		assertType(JPEG, jpgX.getThumb());
		assertType(JPEG, pngX.getThumb());
		assertNull(txt.getThumb());
		assertNull(emp.getThumb());

		assertType(PNG, jpg.getThumbFull());
		assertType(PNG, png.getThumbFull());
		assertType(PNG, gif.getThumbFull());
		assertType(PNG, wep.getThumbFull());
		assertType(PNG, tif.getThumbFull());
		assertType(PNG, jpgX.getThumbFull());
		assertType(PNG, pngX.getThumbFull());
		assertNull(txt.getThumbFull());
		assertNull(emp.getThumbFull());

		assertType(JPEG, jpg.getThumbSame());
		assertType(PNG,  png.getThumbSame());
		assertType(GIF,  gif.getThumbSame());
		assertType(WEBP, wep.getThumbSame());
		assertType(TIFF, tif.getThumbSame());
		assertType(JPEG, jpgX.getThumbSame());
		assertType(PNG,  pngX.getThumbSame());
		assertNull(txt.getThumbSame());
		assertNull(emp.getThumbSame());

		assertType(PNG,  jpg.getThumbRound());
		assertType(GIF,  png.getThumbRound());
		assertType(JPEG, gif.getThumbRound());
		assertType(WEBP, wep.getThumbRound());
		assertType(TIFF, tif.getThumbRound());
		assertType(PNG,  jpgX.getThumbRound());
		assertType(GIF,  pngX.getThumbRound());
		assertNull(txt.getThumbRound());
		assertNull(emp.getThumbRound());

		assertType(JPEG, jpg.getIdentity());
		assertArrayEquals(resourceBytes("thumbnail-test.png"), png.getIdentity());
		assertType(JPEG, gif.getIdentity());
		assertType(JPEG, wep.getIdentity());
		assertType(JPEG, tif.getIdentity());
		assertType(JPEG, jpgX.getIdentity());
		assertArrayEquals(resourceBytes("thumbnail-test.png"), pngX.getIdentity());
		assertArrayEquals(data, txt.getIdentity());
		assertNull(emp.getIdentity());

		// doGet
		assertDoGet(JPEG, thumb, jpg);
		assertDoGet(JPEG, thumb, png);
		assertDoGet(JPEG, thumb, gif);
		assertDoGet(JPEG, thumb, wep);
		assertDoGet(JPEG, thumb, tif);
		assertDoGet(JPEG, thumb, jpgX);
		assertDoGet(JPEG, thumb, pngX);
		assertDoGet404("not computable", thumb, txt);
		assertDoGet404("is null",        thumb, emp);

		assertDoGet(PNG, thumbFull, jpg);
		assertDoGet(PNG, thumbFull, png);
		assertDoGet(PNG, thumbFull, gif);
		assertDoGet(PNG, thumbFull, wep);
		assertDoGet(PNG, thumbFull, tif);
		assertDoGet(PNG, thumbFull, jpgX);
		assertDoGet(PNG, thumbFull, pngX);
		assertDoGet404("not computable", thumbFull, txt);
		assertDoGet404("is null",        thumbFull, emp);

		assertDoGet(JPEG, thumbSame, jpg);
		assertDoGet(PNG,  thumbSame, png);
		assertDoGet(GIF,  thumbSame, gif);
		assertDoGet(WEBP, thumbSame, wep);
		assertDoGet(TIFF, thumbSame, tif);
		assertDoGet(JPEG, thumbSame, jpgX);
		assertDoGet(PNG,  thumbSame, pngX);
		assertDoGet404("not computable", thumbSame, txt);
		assertDoGet404("is null",        thumbSame, emp);

		assertDoGet(PNG,  thumbRound, jpg);
		assertDoGet(GIF,  thumbRound, png);
		assertDoGet(JPEG, thumbRound, gif);
		assertDoGet(WEBP, thumbRound, wep);
		assertDoGet(TIFF, thumbRound, tif);
		assertDoGet(PNG,  thumbRound, jpgX);
		assertDoGet(GIF,  thumbRound, pngX);
		assertDoGet404("not computable", thumbRound, txt);
		assertDoGet404("is null",        thumbRound, emp);

		assertDoGet(JPEG, identity, jpg);
		assertDoGet(PNG,  resourceBytes("thumbnail-test.png"), identity, png);
		assertDoGet(JPEG, identity, gif);
		assertDoGet(JPEG, identity, wep);
		assertDoGet(JPEG, identity, tif);
		assertDoGet(JPEG, identity, jpgX);
		assertDoGet("image/x-png", resourceBytes("thumbnail-test.png"), identity, pngX);
		assertDoGet("text/plain", data, identity, txt);
		assertDoGet404("is null",       identity, emp);

		// url
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumb/" + jpg.getCopeID() + ".jpg", jpg.getThumbURL());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumb/" + png.getCopeID() + ".jpg", png.getThumbURL());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumb/" + gif.getCopeID() + ".jpg", gif.getThumbURL());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumb/" + wep.getCopeID() + ".jpg", wep.getThumbURL());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumb/" + tif.getCopeID() + ".jpg", tif.getThumbURL());
		assertEquals(null, txt.getThumbURL());
		assertEquals(null, emp.getThumbURL());

		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumbFull/" + jpg.getCopeID() + ".png", jpg.getThumbFullURL());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumbFull/" + png.getCopeID() + ".png", png.getThumbFullURL());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumbFull/" + gif.getCopeID() + ".png", gif.getThumbFullURL());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumbFull/" + wep.getCopeID() + ".png", wep.getThumbFullURL());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumbFull/" + tif.getCopeID() + ".png", tif.getThumbFullURL());
		assertEquals(null, txt.getThumbFullURL());
		assertEquals(null, emp.getThumbFullURL());

		// url fallback
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumb/" + jpg.getCopeID() + ".jpg", jpg.getThumbURLWithFallbackToSource());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumb/" + png.getCopeID() + ".jpg", png.getThumbURLWithFallbackToSource());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumb/" + gif.getCopeID() + ".jpg", gif.getThumbURLWithFallbackToSource());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumb/" + wep.getCopeID() + ".jpg", wep.getThumbURLWithFallbackToSource());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumb/" + tif.getCopeID() + ".jpg", tif.getThumbURLWithFallbackToSource());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/file/"  + txt.getCopeID() + ".txt", txt.getThumbURLWithFallbackToSource());
		assertEquals(null, emp.getThumbURLWithFallbackToSource());

		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumbFull/" + jpg.getCopeID() + ".png", jpg.getThumbFullURLWithFallbackToSource());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumbFull/" + png.getCopeID() + ".png", png.getThumbFullURLWithFallbackToSource());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumbFull/" + gif.getCopeID() + ".png", gif.getThumbFullURLWithFallbackToSource());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumbFull/" + wep.getCopeID() + ".png", wep.getThumbFullURLWithFallbackToSource());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/thumbFull/" + tif.getCopeID() + ".png", tif.getThumbFullURLWithFallbackToSource());
		assertEquals(mediaRootUrl + "ThumbnailMagickItem/file/"  + txt.getCopeID() + ".txt", txt.getThumbFullURLWithFallbackToSource());
		assertEquals(null, emp.getThumbFullURLWithFallbackToSource());

		// isNull
		assertContains(emp, TYPE.search(file.isNull()));
		assertContains(jpg, jpgX, png, pngX, gif, wep, tif, txt, TYPE.search(file.isNotNull()));
		assertContains(emp , TYPE.search(thumb.isNull())); // TODO check for getSupportedSourceContentTypes, add text
		assertContains(jpg, jpgX, png, pngX, gif, wep, tif, txt, TYPE.search(thumb.isNotNull())); // TODO check for getSupportedSourceContentTypes, remove text
		assertContains(emp , TYPE.search(identity.isNull())); // TODO check for getSupportedSourceContentTypes, add text
		assertContains(jpg, jpgX, png, pngX, gif, wep, tif, txt, TYPE.search(identity.isNotNull())); // TODO check for getSupportedSourceContentTypes, remove text
	}

	private static void assertType(final String expectedContentType, final byte[] actualBody)
	{
		assertNotNull(expectedContentType);
		assertNotNull(actualBody);
		assertEquals(
				Collections.singleton(MediaType.forName(expectedContentType)),
				MediaType.forMagics(actualBody));
	}

	private void assertDoGet(
			final String expectedContentType,
			final MediaImageMagickFilter feature,
			final ThumbnailMagickItem item) throws IOException, NotFound
	{
		assertNotNull(expectedContentType);
		assertNotNull(feature);
		assertNotNull(item);

		final Response response = new Response();
		feature.doGetAndCommit(null, response, item);
		assertFalse(model.hasCurrentTransaction());
		response.assertIt(expectedContentType);
		model.startTransaction(ThumbnailMagickTest.class.getName());
	}

	private void assertDoGet(
			final String expectedContentType,
			final byte[] expectedBody,
			final MediaImageMagickFilter feature,
			final ThumbnailMagickItem item) throws IOException, NotFound
	{
		assertNotNull(expectedContentType);
		assertNotNull(feature);
		assertNotNull(item);

		final Response response = new Response();
		feature.doGetAndCommit(null, response, item);
		assertFalse(model.hasCurrentTransaction());
		response.assertIt(expectedContentType, expectedBody);
		model.startTransaction(ThumbnailMagickTest.class.getName());
	}

	private static final class Response extends DummyResponse
	{
		Response()
		{
		}

		private int contentLength = Integer.MIN_VALUE;
		private String contentType = null;
		private ByteArrayOutputStream body = null;

		void assertIt(final String expectedContentType)
		{
			assertTrue(contentLength>0);
			assertEquals(expectedContentType, contentType);
			assertNotNull(body);
			assertEquals(
					Collections.singleton(MediaType.forName(contentType)),
					MediaType.forMagics(body.toByteArray()));
			assertEquals(contentLength, body.size());
		}

		void assertIt(final String expectedContentType, final byte[] expectedBody)
		{
			assertTrue(contentLength>0);
			assertEquals(expectedContentType, contentType);
			assertNotNull(body);
			assertArrayEquals(expectedBody, body.toByteArray());
			assertEquals(contentLength, expectedBody.length);
		}

		@Override
		public void setHeader(final String name, final String value)
		{
			if("Content-Length".equals(name))
				setContentLengthInternal(Integer.parseInt(value));
			else
				super.setHeader(name, value);
		}
		@Override
		public void setContentLength(final int len)
		{
			setContentLengthInternal(len);
		}
		private void setContentLengthInternal(final int len)
		{
			assertTrue(len>0);
			assertEquals(Integer.MIN_VALUE, contentLength);
			assertNull(body);
			contentLength = len;
		}

		@Override
		public void setContentType(final String type)
		{
			assertNotNull(type);
			assertNull(contentType);
			assertNull(body);
			contentType = type;
		}

		@Override
		public ServletOutputStream getOutputStream()
		{
			assertNull(body);
			@SuppressWarnings("NestedAssignment")
			final ByteArrayOutputStream body = this.body = new ByteArrayOutputStream();

			return new ServletOutputStream()
			{
				@Override
				public void write(final int b)
				{
					body.write(b);
				}

				@Override
				public boolean isReady()
				{
					throw new RuntimeException();
				}

				@Override
				public void setWriteListener(final WriteListener writeListener)
				{
					throw new RuntimeException();
				}
			};
		}
	}

	private void assertDoGet404(
			final String expectedResult,
			final MediaImageMagickFilter feature,
			final ThumbnailMagickItem item)
	{
		assertNotNull(feature);
		assertNotNull(item);

		final DummyResponse response = new DummyResponse();
		assertFails(
				() -> feature.doGetAndCommit(null, response, item),
				NotFound.class,
				expectedResult);
		assertTrue(model.hasCurrentTransaction());
	}
}
