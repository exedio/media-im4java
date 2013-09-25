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

import com.exedio.cope.ActivationParameters;
import com.exedio.cope.Item;
import com.exedio.cope.Type;
import com.exedio.cope.TypesBound;
import com.exedio.cope.junit.CopeAssert;
import com.exedio.cope.pattern.Media;
import java.io.IOException;
import org.im4java.core.IMOperation;
import org.im4java.core.IMOps;

public final class ThumbnailMagickModifiableTest extends CopeAssert
{
	@SuppressWarnings({"static-method", "boxing"})
	public void testThumbs() throws IOException
	{
		AnItem.thumb.test();
		// deliverately break operation
		AnItem.operation.resize(40, 50, '#');
		// check, whether operation is still unmodified
		AnItem.thumb.test();
	}

	static final class AnItem extends Item
	{
		static final Media file = new Media().optional().lengthMax(10000);

		@SuppressWarnings("boxing")
		static final IMOps operation = new IMOperation().resize(20, 30, '>');

		static final MediaImageMagickFilter thumb = new MediaImageMagickFilter(file, operation);

		static final Type<AnItem> TYPE = TypesBound.newType(AnItem.class);

		AnItem(final ActivationParameters ap) { super(ap); }
		private static final long serialVersionUID = 1l;
	}
}
