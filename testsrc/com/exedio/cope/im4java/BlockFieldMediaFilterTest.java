/*
 * Copyright (C) 2004-2015  exedio GmbH (www.exedio.com)
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

import static com.exedio.cope.instrument.Visibility.NONE;
import static com.exedio.cope.pattern.MediaType.GIF;
import static com.exedio.cope.pattern.MediaType.JPEG;
import static com.exedio.cope.pattern.MediaType.PNG;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.exedio.cope.Model;
import com.exedio.cope.instrument.Wrapper;
import com.exedio.cope.instrument.WrapperType;
import com.exedio.cope.junit.CopeModelTest;
import com.exedio.cope.pattern.Block;
import com.exedio.cope.pattern.BlockField;
import com.exedio.cope.pattern.Media;
import org.im4java.core.IMOperation;
import org.junit.jupiter.api.Test;

public class BlockFieldMediaFilterTest extends CopeModelTest
{
	static final Model MODEL = new Model(AnItem.TYPE);

	static
	{
		MODEL.enableSerialization(BlockFieldMediaFilterTest.class, "MODEL");
	}

	BlockFieldMediaFilterTest()
	{
		super(MODEL);
	}

	@Test void testIt()
	{
		final MediaImageMagickFilter blocFilter = ABlock.filter;
		final Media blocSource = ABlock.source;
		final Media einsSource = AnItem.eins.of(blocSource);
		final MediaImageMagickFilter einsFilter = AnItem.eins.of(blocFilter);
		final Media zweiSource = AnItem.zwei.of(blocSource);
		final MediaImageMagickFilter zweiFilter = AnItem.zwei.of(blocFilter);
		assertEquals(blocSource, blocFilter.getSource());
		assertEquals(einsSource, einsFilter.getSource());
		assertEquals(zweiSource, zweiFilter.getSource());
		assertEquals(PNG, blocFilter.getOutputContentType(JPEG));
		assertEquals(PNG, einsFilter.getOutputContentType(JPEG));
		assertEquals(PNG, zweiFilter.getOutputContentType(JPEG));
		assertEquals(GIF, blocFilter.getOutputContentType(PNG));
		assertEquals(GIF, einsFilter.getOutputContentType(PNG));
		assertEquals(GIF, zweiFilter.getOutputContentType(PNG));
		final String PRE = "[-limit, thread, 1, ";
		final String POST = ", ?img?, ?img?]";
		assertEquals(PRE + "-resize, 20x30>" + POST, blocFilter.getCmdArgs(JPEG).toString());
		assertEquals(PRE + "-resize, 20x30>" + POST, einsFilter.getCmdArgs(JPEG).toString());
		assertEquals(PRE + "-resize, 20x30>" + POST, zweiFilter.getCmdArgs(JPEG).toString());
		assertEquals(PRE + "-resize, 30x40>" + POST, blocFilter.getCmdArgs(PNG).toString());
		assertEquals(PRE + "-resize, 30x40>" + POST, einsFilter.getCmdArgs(PNG).toString());
		assertEquals(PRE + "-resize, 30x40>" + POST, zweiFilter.getCmdArgs(PNG).toString());

		final AnItem i1 = new AnItem();
		final ABlock b1a = i1.eins();
		final ABlock b1b = i1.zwei();
		assertEquals(null, b1a.getSourceLocator());
		assertEquals(null, b1a.getFilterLocator());
		assertEquals(null, b1b.getSourceLocator());
		assertEquals(null, b1b.getFilterLocator());

		b1a.setSource(new byte[]{1,2,3}, JPEG);
		assertEquals("AnItem/eins-source/"+i1+".jpg", b1a.getSourceLocator().getPath());
		assertEquals("AnItem/eins-filter/"+i1+".png", b1a.getFilterLocator().getPath());
		assertEquals(null, b1b.getSourceLocator());
		assertEquals(null, b1b.getFilterLocator());
	}

	@WrapperType(indent=2, comments=false)
	private static final class ABlock extends Block
	{
		@Wrapper(wrap="getURL", visibility=NONE)
		static final Media source = new Media().optional().contentTypes(JPEG, PNG);
		@Wrapper(wrap="getURL", visibility=NONE)
		static final MediaImageMagickFilter filter = new MediaImageMagickFilter(
				source,
				new IMOperation().resize(20, 30, '>'), PNG).
				forType(PNG, new IMOperation().resize(30, 40, '>'), GIF);


		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final com.exedio.cope.pattern.MediaPath.Locator getSourceLocator()
		{
			return field().of(ABlock.source).getLocator(item());
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final java.lang.String getSourceContentType()
		{
			return field().of(ABlock.source).getContentType(item());
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final boolean isSourceNull()
		{
			return field().of(ABlock.source).isNull(item());
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final java.util.Date getSourceLastModified()
		{
			return field().of(ABlock.source).getLastModified(item());
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final long getSourceLength()
		{
			return field().of(ABlock.source).getLength(item());
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final byte[] getSourceBody()
		{
			return field().of(ABlock.source).getBody(item());
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final void getSourceBody(final java.io.OutputStream body)
				throws
					java.io.IOException
		{
			field().of(ABlock.source).getBody(item(),body);
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final void getSourceBody(final java.nio.file.Path body)
				throws
					java.io.IOException
		{
			field().of(ABlock.source).getBody(item(),body);
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final void getSourceBody(final java.io.File body)
				throws
					java.io.IOException
		{
			field().of(ABlock.source).getBody(item(),body);
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final void setSource(final com.exedio.cope.pattern.Media.Value source)
				throws
					java.io.IOException
		{
			field().of(ABlock.source).set(item(),source);
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final void setSource(final byte[] body,final java.lang.String contentType)
		{
			field().of(ABlock.source).set(item(),body,contentType);
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final void setSource(final java.io.InputStream body,final java.lang.String contentType)
				throws
					java.io.IOException
		{
			field().of(ABlock.source).set(item(),body,contentType);
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final void setSource(final java.nio.file.Path body,final java.lang.String contentType)
				throws
					java.io.IOException
		{
			field().of(ABlock.source).set(item(),body,contentType);
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final void setSource(final java.io.File body,final java.lang.String contentType)
				throws
					java.io.IOException
		{
			field().of(ABlock.source).set(item(),body,contentType);
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final com.exedio.cope.pattern.MediaPath.Locator getFilterLocator()
		{
			return field().of(ABlock.filter).getLocator(item());
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final java.lang.String getFilterContentType()
		{
			return field().of(ABlock.filter).getContentType(item());
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final byte[] getFilter()
				throws
					java.io.IOException
		{
			return field().of(ABlock.filter).get(item());
		}

		@com.exedio.cope.instrument.Generated
		private static final long serialVersionUID = 1l;

		@com.exedio.cope.instrument.Generated
		private static final com.exedio.cope.pattern.BlockType<ABlock> TYPE = com.exedio.cope.pattern.BlockType.newType(ABlock.class);

		@com.exedio.cope.instrument.Generated
		private ABlock(final com.exedio.cope.pattern.BlockActivationParameters ap){super(ap);}
	}

	@WrapperType(indent=2, comments=false)
	private static final class AnItem extends com.exedio.cope.Item // TODO use import, but this is not accepted by javac
	{
		static final BlockField<ABlock> eins = BlockField.create(ABlock.TYPE);
		static final BlockField<ABlock> zwei = BlockField.create(ABlock.TYPE);


		@com.exedio.cope.instrument.Generated
		private AnItem()
		{
			this(new com.exedio.cope.SetValue<?>[]{
			});
		}

		@com.exedio.cope.instrument.Generated
		private AnItem(final com.exedio.cope.SetValue<?>... setValues){super(setValues);}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final ABlock eins()
		{
			return AnItem.eins.get(this);
		}

		@com.exedio.cope.instrument.Generated
		@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
		final ABlock zwei()
		{
			return AnItem.zwei.get(this);
		}

		@com.exedio.cope.instrument.Generated
		private static final long serialVersionUID = 1l;

		@com.exedio.cope.instrument.Generated
		private static final com.exedio.cope.Type<AnItem> TYPE = com.exedio.cope.TypesBound.newType(AnItem.class);

		@com.exedio.cope.instrument.Generated
		private AnItem(final com.exedio.cope.ActivationParameters ap){super(ap);}
	}
}

