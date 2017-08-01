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
import java.nio.file.Paths;
import junit.framework.TestCase;

public class ProbeContentTypeTest extends TestCase
{
	public void testJPEG() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.JPEG, type("MediaImageMagickFilter-test.jpg"));
	}
	public void testPNG() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.PNG,  type("MediaImageMagickFilter-test.png"));
	}
	public void testGIF() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.GIF,  type("MediaImageMagickFilter-test.gif"));
	}
	public void testTIFF() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.TIFF, type("MediaImageMagickFilter-test.tif"));
	}
	public void testPDF() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.PDF,  type("MediaImageMagickFilter-test.pdf"));
	}
	public void testSVG() throws URISyntaxException, IOException
	{
		assertEquals(MediaType.SVG,  type("MediaImageMagickFilter-test.svg"));
	}

	private static String type(final String name) throws URISyntaxException, IOException
	{
		return probeContentType(Paths.get(ProbeContentTypeTest.class.getResource(name).toURI()));
	}
}
