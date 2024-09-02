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
import static com.exedio.cope.im4java.ThumbnailMagickModelTest.MODEL;
import static com.exedio.cope.junit.CopeAssert.list;
import static com.exedio.cope.pattern.MediaType.GIF;
import static com.exedio.cope.pattern.MediaType.JPEG;
import static com.exedio.cope.pattern.MediaType.PDF;
import static com.exedio.cope.pattern.MediaType.PNG;
import static com.exedio.cope.pattern.MediaType.SVG;
import static com.exedio.cope.pattern.MediaType.TIFF;
import static com.exedio.cope.pattern.MediaType.WEBP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.exedio.cope.junit.CopeModelTest;
import java.io.IOException;
import java.io.InputStream;
import org.im4java.core.CommandException;
import org.junit.jupiter.api.Test;

public final class ThumbnailMagickExplicitFormatTest extends CopeModelTest
{
	public ThumbnailMagickExplicitFormatTest()
	{
		super(MODEL);
	}


	@Test void testJpegPng() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.png"), JPEG);
		assertFails(item, getProgramName() + ": negative or zero image size `");
	}

	@Test void testJpegGif() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.gif"), JPEG);
		assertFails(item, getProgramName() + ": negative or zero image size `");
	}


	@Test void testPngJpeg() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.jpg"), PNG);
		assertFails(item, getProgramName() + ": improper image header `");
	}

	@Test void testPngGif() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.gif"), PNG);
		assertFails(item, getProgramName() + ": improper image header `");
	}


	@Test void testGifJpeg() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.jpg"), GIF);
		assertFails(item, getProgramName() + ": improper image header `");
	}

	@Test void testGifPng() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.png"), GIF);
		assertFails(item, getProgramName() + ": improper image header `");
	}


	@Test void testWebpJpeg() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.jpg"), WEBP);
		assumeNotGithub();
		assertFails(item, getDecodingErrorMessageStart());
	}

	@Test void testWebpPng() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.png"), WEBP);
		assumeNotGithub();
		assertFails(item, getDecodingErrorMessageStart());
	}

	@Test void testWebpPdf() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.pdf"), WEBP);
		assumeNotGithub();
		assertFails(item, getDecodingErrorMessageStart());
	}


	@Test void testTiffJpeg() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.jpg"), TIFF);
		assertFails(item, getTiffErrorMessageStart("55551 (0xd8ff)"));
	}

	@Test void testTiffPng() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.png"), TIFF);
		assertFails(item, getTiffErrorMessageStart("20617 (0x5089)"));
	}


	@Test void testPdfJpeg() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.jpg"), PDF);
		assertFails(item, isWindows() ? "convert.exe: PDFDelegateFailed" : "Error: /undefined in ");
	}

	@Test void testPdfPng() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.png"), PDF);
		assertFails(item, isWindows() ? "convert.exe: PDFDelegateFailed" : "Error: /syntaxerror in (binary token, type=137)");
	}

	@Test void testPdfSvg() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.svg"), PDF);
		assertFails(item, isWindows() ? "convert.exe: PDFDelegateFailed" : "Error: /syntaxerror in /----nostringval----");
	}


	@Test void testSvgJpeg() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.jpg"), SVG);
		assumeNotGithub();
		assertFails(item, getSvgErrorMessageStart());
	}

	@Test void testSvgPng() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.png"), SVG);
		assumeNotGithub();
		assertFails(item, getSvgErrorMessageStart());
	}

	@Test void testSvgPdf() throws IOException
	{
		final ThumbnailMagickItem item = new ThumbnailMagickItem();
		item.setFile(resource("thumbnail-test.pdf"), SVG);
		assumeNotGithub();
		assertFails(item, getSvgErrorMessageStart());
	}



	private static InputStream resource(final String name)
	{
		return ThumbnailMagickExplicitFormatTest.class.getResourceAsStream(name);
	}

	private static void assertFails(
			final ThumbnailMagickItem item,
			final String errorMessage)
			throws IOException
	{
		try
		{
			item.getThumb();
			fail();
		}
		catch(final RuntimeException runtimeException)
		{
			assertTrue(
					runtimeException.getMessage().startsWith(
							"-limit thread 1 -resize 20x30> ?img? ?img? ==="),
					runtimeException.getMessage());

			final CommandException commandException = (CommandException)runtimeException.getCause();
			assertEquals(-1, commandException.getReturnCode());
			assertEquals(list(), commandException.getErrorText());
			final CommandException commandException2 = (CommandException)commandException.getCause();

			assertExceptionMessage(commandException2, errorMessage);
			assertEquals(1, commandException2.getReturnCode());
			assertEquals(null, commandException2.getCause());
		}
	}

	private static void assertExceptionMessage(
			final CommandException exception,
			final String message)
	{
		assertTrue(
				exception.getMessage().startsWith(message),
				"The exception message >"+exception.getMessage()+"< did not start with >"+message+"<.");
	}

	private static String getDecodingErrorMessageStart()
	{
		if (isWindows())
		{
			return "convert.exe: corrupt image ";
		}
		else
		{
			return "convert-im6.q16: corrupt image `";
		}
	}

	private static String getTiffErrorMessageStart(final String magic)
	{
		return getProgramName() + ": Not a TIFF " + (isWindows()?"":"or MDI ") + "file, bad magic number " + magic + ". `";
	}

	private static String getSvgErrorMessageStart()
	{
		if (isWindows())
		{
			return "convert.exe: negative or zero image size";
		}
		else
		{
			return "Error reading SVG:XML parse error: Error domain 1 code 4 on line 1 column 1 of data: Start tag expected, '<' not found";
		}
	}
}
