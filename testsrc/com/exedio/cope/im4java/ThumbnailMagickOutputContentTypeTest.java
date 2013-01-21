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

import org.im4java.core.IMOperation;
import org.im4java.core.IMOps;

import com.exedio.cope.junit.CopeAssert;
import com.exedio.cope.pattern.Media;

public final class ThumbnailMagickOutputContentTypeTest extends CopeAssert
{
	static final Media file = new Media().optional().lengthMax(10000);
	@SuppressWarnings("boxing")
	static final IMOps op = new IMOperation().resize(20, 30, '>');

	@SuppressWarnings("static-method")
	public void testThumbs()
	{
		assertEquals(null, new MediaImageMagickFilter(file, op).getOutputContentType());
		assertEquals(JPEG, new MediaImageMagickFilter(file, op, JPEG).getOutputContentType());
		assertEquals(PNG,  new MediaImageMagickFilter(file, op, PNG).getOutputContentType());
		assertEquals(GIF,  new MediaImageMagickFilter(file, op, GIF).getOutputContentType());

		final MediaImageMagickFilter f1 = new MediaImageMagickFilter(file, op, JPEG);
		assertEquals(null, f1.forType(PNG, op, PNG).getOutputContentType());

		final MediaImageMagickFilter f2 = f1.forType(PNG, op, JPEG);
		assertEquals(JPEG, f2.getOutputContentType());
		assertEquals(null, f2.forType(GIF, op, PNG ).getOutputContentType());
		assertEquals(JPEG, f2.forType(GIF, op, JPEG).getOutputContentType());
	}
}
