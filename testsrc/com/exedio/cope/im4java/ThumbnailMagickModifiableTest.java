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

import com.exedio.cope.junit.CopeAssert;
import com.exedio.cope.pattern.Media;
import java.io.IOException;
import org.im4java.core.IMOperation;
import org.im4java.core.IMOps;

public final class ThumbnailMagickModifiableTest extends CopeAssert
{
	@SuppressWarnings("static-method")
	public void testThumbs() throws IOException
	{
		final Media file = new Media().optional().lengthMax(10000);
		@SuppressWarnings("boxing")
		final IMOps operation = new IMOperation().resize(20, 30, '>');
		final MediaImageMagickFilter thumb = new MediaImageMagickFilter(file, operation);

		thumb.test();
		// deliverately break operation
		operation.addRawArgs("-kaputt");
		// check, whether operation is still unmodified
		thumb.test();
	}
}
