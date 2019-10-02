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

import static java.nio.file.Files.probeContentType;

import com.exedio.cope.pattern.MediaType;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.spi.FileTypeDetector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ServiceLoader;
import junit.framework.TestCase;

public class ProbeContentTypeTest extends TestCase
{
	public void testJPEG() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.JPEG, type("MediaImageMagickFilter-test.jpg"));
	}
	public void testJPEGAnon() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.JPEG, typeAnon("MediaImageMagickFilter-test.jpg"));
	}
	public void testPNG() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.PNG,  type("MediaImageMagickFilter-test.png"));
	}
	public void testPNGAnon() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.PNG,  typeAnon("MediaImageMagickFilter-test.png"));
	}
	public void testGIF() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.GIF,  type("MediaImageMagickFilter-test.gif"));
	}
	public void testGIFAnon() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.GIF,  typeAnon("MediaImageMagickFilter-test.gif"));
	}
	public void testWEBP() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.WEBP, type("MediaImageMagickFilter-test.webp"));
	}
	public void testWEBPAnon() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.WEBP, typeAnon("MediaImageMagickFilter-test.webp"));
	}
	public void testTIFF() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.TIFF, type("MediaImageMagickFilter-test.tif"));
	}
	public void testTIFFAnon() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.TIFF, typeAnon("MediaImageMagickFilter-test.tif"));
	}
	public void testPDF() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.PDF,  type("MediaImageMagickFilter-test.pdf"));
	}
	public void testPDFAnon() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.PDF,  typeAnon("MediaImageMagickFilter-test.pdf"));
	}
	public void testSVG() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.SVG,  type("MediaImageMagickFilter-test.svg"));
	}
	public void testSVGAnon() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.SVG,  typeAnon("MediaImageMagickFilter-test.svg"));
	}

	private static String type(final String name) throws URISyntaxException, IOException
	{
		return probeContentType(path(name));
	}

	private static String typeAnon(final String name) throws URISyntaxException, IOException
	{
		final Path withoutExtension = Files.createTempFile(ProbeContentTypeTest.class.getName(), ".data");
		Files.copy(path(name), withoutExtension, StandardCopyOption.REPLACE_EXISTING);
		return probeContentType(withoutExtension);
	}

	private static Path path(final String name) throws URISyntaxException
	{
		return Paths.get(ProbeContentTypeTest.class.getResource(name).toURI());
	}


	public void testDetectors()
	{
		final ArrayList<String> actual = new ArrayList<>();
		for(final FileTypeDetector detector : ServiceLoader.load(FileTypeDetector.class))
			actual.add(detector.getClass().getName());
		assertEquals(Arrays.asList(), actual);
	}
}
