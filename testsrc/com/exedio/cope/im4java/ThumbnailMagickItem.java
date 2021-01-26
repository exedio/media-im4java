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

import com.exedio.cope.Item;
import com.exedio.cope.pattern.Media;
import com.exedio.cope.pattern.MediaType;
import org.im4java.core.IMOperation;

public final class ThumbnailMagickItem extends Item
{
	static final Media file = new Media().optional().lengthMax(15000);

	@SuppressWarnings("boxing")
	static final MediaImageMagickFilter thumb =
			new MediaImageMagickFilter(
				file,
				new IMOperation().resize(20, 30, '>'),
				MediaType.JPEG);

	@SuppressWarnings("boxing")
	static final MediaImageMagickFilter thumbFull =
			new MediaImageMagickFilter(
				file,
				new IMOperation().
					resize(20, 30, '>').
					density(300).units("PixelsPerInch").
					flatten().background("white"),
				MediaType.PNG);

	@SuppressWarnings("boxing")
	static final MediaImageMagickFilter thumbSame =
			new MediaImageMagickFilter(
				file,
				new IMOperation().resize(20, 30, '>')).
				forType(MediaType.SVG, new IMOperation().resize(20, 30, '>'), MediaType.PNG); // do not create SVG as this is a svg-wrapped png anyway and makes trouble on Debian9

	@SuppressWarnings("boxing")
	static final MediaImageMagickFilter thumbRound =
			new MediaImageMagickFilter(
				file,
				new IMOperation().resize(20, 30, '>')).
				forType(MediaType.JPEG, new IMOperation().resize(30, 40, '>'), MediaType.PNG ).
				forType(MediaType.PNG,  new IMOperation().resize(30, 40, '>'), MediaType.GIF ).
				forType(MediaType.GIF,  new IMOperation().resize(30, 40, '>'), MediaType.JPEG).
				forType(MediaType.SVG,  new IMOperation().resize(30, 40, '>'), MediaType.PNG ); // do not create SVG as this is a svg-wrapped png anyway and makes trouble on Debian9

	// must not be generated by the instrumentor
	// because thumb has a fixed contentType
	String getThumbContentType()
	{
		return thumb.getContentType(this);
	}

	// must not be generated by the instrumentor
	// because thumbFull has a fixed contentType
	String getThumbFullContentType()
	{
		return thumbFull.getContentType(this);
	}

	/**
	 * Creates a new ThumbnailMagickItem with all the fields initially needed.
	 */
	@com.exedio.cope.instrument.Generated // customize with @WrapperType(constructor=...) and @WrapperInitial
	public ThumbnailMagickItem()
	{
		this(new com.exedio.cope.SetValue<?>[]{
		});
	}

	/**
	 * Creates a new ThumbnailMagickItem and sets the given fields initially.
	 */
	@com.exedio.cope.instrument.Generated // customize with @WrapperType(genericConstructor=...)
	private ThumbnailMagickItem(final com.exedio.cope.SetValue<?>... setValues){super(setValues);}

	/**
	 * Returns a URL the content of {@link #file} is available under.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getURL")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final java.lang.String getFileURL()
	{
		return ThumbnailMagickItem.file.getURL(this);
	}

	/**
	 * Returns a Locator the content of {@link #file} is available under.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getLocator")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final com.exedio.cope.pattern.MediaPath.Locator getFileLocator()
	{
		return ThumbnailMagickItem.file.getLocator(this);
	}

	/**
	 * Returns the content type of the media {@link #file}.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getContentType")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final java.lang.String getFileContentType()
	{
		return ThumbnailMagickItem.file.getContentType(this);
	}

	/**
	 * Returns whether media {@link #file} is null.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="isNull")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final boolean isFileNull()
	{
		return ThumbnailMagickItem.file.isNull(this);
	}

	/**
	 * Returns the last modification date of media {@link #file}.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getLastModified")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final java.util.Date getFileLastModified()
	{
		return ThumbnailMagickItem.file.getLastModified(this);
	}

	/**
	 * Returns the body length of the media {@link #file}.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getLength")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final long getFileLength()
	{
		return ThumbnailMagickItem.file.getLength(this);
	}

	/**
	 * Returns the body of the media {@link #file}.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getBody")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final byte[] getFileBody()
	{
		return ThumbnailMagickItem.file.getBody(this);
	}

	/**
	 * Writes the body of media {@link #file} into the given stream.
	 * Does nothing, if the media is null.
	 * @throws java.io.IOException if accessing {@code body} throws an IOException.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getBody")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final void getFileBody(final java.io.OutputStream body)
			throws
				java.io.IOException
	{
		ThumbnailMagickItem.file.getBody(this,body);
	}

	/**
	 * Writes the body of media {@link #file} into the given file.
	 * Does nothing, if the media is null.
	 * @throws java.io.IOException if accessing {@code body} throws an IOException.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getBody")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final void getFileBody(final java.nio.file.Path body)
			throws
				java.io.IOException
	{
		ThumbnailMagickItem.file.getBody(this,body);
	}

	/**
	 * Writes the body of media {@link #file} into the given file.
	 * Does nothing, if the media is null.
	 * @throws java.io.IOException if accessing {@code body} throws an IOException.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getBody")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final void getFileBody(final java.io.File body)
			throws
				java.io.IOException
	{
		ThumbnailMagickItem.file.getBody(this,body);
	}

	/**
	 * Sets the content of media {@link #file}.
	 * @throws java.io.IOException if accessing {@code body} throws an IOException.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="set")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final void setFile(final com.exedio.cope.pattern.Media.Value file)
			throws
				java.io.IOException
	{
		ThumbnailMagickItem.file.set(this,file);
	}

	/**
	 * Sets the content of media {@link #file}.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="set")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final void setFile(final byte[] body,final java.lang.String contentType)
	{
		ThumbnailMagickItem.file.set(this,body,contentType);
	}

	/**
	 * Sets the content of media {@link #file}.
	 * @throws java.io.IOException if accessing {@code body} throws an IOException.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="set")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final void setFile(final java.io.InputStream body,final java.lang.String contentType)
			throws
				java.io.IOException
	{
		ThumbnailMagickItem.file.set(this,body,contentType);
	}

	/**
	 * Sets the content of media {@link #file}.
	 * @throws java.io.IOException if accessing {@code body} throws an IOException.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="set")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final void setFile(final java.nio.file.Path body,final java.lang.String contentType)
			throws
				java.io.IOException
	{
		ThumbnailMagickItem.file.set(this,body,contentType);
	}

	/**
	 * Sets the content of media {@link #file}.
	 * @throws java.io.IOException if accessing {@code body} throws an IOException.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="set")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final void setFile(final java.io.File body,final java.lang.String contentType)
			throws
				java.io.IOException
	{
		ThumbnailMagickItem.file.set(this,body,contentType);
	}

	/**
	 * Returns a URL the content of {@link #thumb} is available under.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getURL")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final java.lang.String getThumbURL()
	{
		return ThumbnailMagickItem.thumb.getURL(this);
	}

	/**
	 * Returns a Locator the content of {@link #thumb} is available under.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getLocator")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final com.exedio.cope.pattern.MediaPath.Locator getThumbLocator()
	{
		return ThumbnailMagickItem.thumb.getLocator(this);
	}

	/**
	 * Returns a URL the content of {@link #thumb} is available under, falling back to source if necessary.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getURLWithFallbackToSource")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final java.lang.String getThumbURLWithFallbackToSource()
	{
		return ThumbnailMagickItem.thumb.getURLWithFallbackToSource(this);
	}

	/**
	 * Returns a Locator the content of {@link #thumb} is available under, falling back to source if necessary.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getLocatorWithFallbackToSource")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final com.exedio.cope.pattern.MediaPath.Locator getThumbLocatorWithFallbackToSource()
	{
		return ThumbnailMagickItem.thumb.getLocatorWithFallbackToSource(this);
	}

	/**
	 * Returns the body of {@link #thumb}.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="get")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final byte[] getThumb()
			throws
				java.io.IOException
	{
		return ThumbnailMagickItem.thumb.get(this);
	}

	/**
	 * Returns a URL the content of {@link #thumbFull} is available under.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getURL")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final java.lang.String getThumbFullURL()
	{
		return ThumbnailMagickItem.thumbFull.getURL(this);
	}

	/**
	 * Returns a Locator the content of {@link #thumbFull} is available under.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getLocator")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final com.exedio.cope.pattern.MediaPath.Locator getThumbFullLocator()
	{
		return ThumbnailMagickItem.thumbFull.getLocator(this);
	}

	/**
	 * Returns a URL the content of {@link #thumbFull} is available under, falling back to source if necessary.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getURLWithFallbackToSource")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final java.lang.String getThumbFullURLWithFallbackToSource()
	{
		return ThumbnailMagickItem.thumbFull.getURLWithFallbackToSource(this);
	}

	/**
	 * Returns a Locator the content of {@link #thumbFull} is available under, falling back to source if necessary.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getLocatorWithFallbackToSource")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final com.exedio.cope.pattern.MediaPath.Locator getThumbFullLocatorWithFallbackToSource()
	{
		return ThumbnailMagickItem.thumbFull.getLocatorWithFallbackToSource(this);
	}

	/**
	 * Returns the body of {@link #thumbFull}.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="get")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final byte[] getThumbFull()
			throws
				java.io.IOException
	{
		return ThumbnailMagickItem.thumbFull.get(this);
	}

	/**
	 * Returns a URL the content of {@link #thumbSame} is available under.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getURL")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final java.lang.String getThumbSameURL()
	{
		return ThumbnailMagickItem.thumbSame.getURL(this);
	}

	/**
	 * Returns a Locator the content of {@link #thumbSame} is available under.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getLocator")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final com.exedio.cope.pattern.MediaPath.Locator getThumbSameLocator()
	{
		return ThumbnailMagickItem.thumbSame.getLocator(this);
	}

	/**
	 * Returns the content type of the media {@link #thumbSame}.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getContentType")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final java.lang.String getThumbSameContentType()
	{
		return ThumbnailMagickItem.thumbSame.getContentType(this);
	}

	/**
	 * Returns a URL the content of {@link #thumbSame} is available under, falling back to source if necessary.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getURLWithFallbackToSource")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final java.lang.String getThumbSameURLWithFallbackToSource()
	{
		return ThumbnailMagickItem.thumbSame.getURLWithFallbackToSource(this);
	}

	/**
	 * Returns a Locator the content of {@link #thumbSame} is available under, falling back to source if necessary.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getLocatorWithFallbackToSource")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final com.exedio.cope.pattern.MediaPath.Locator getThumbSameLocatorWithFallbackToSource()
	{
		return ThumbnailMagickItem.thumbSame.getLocatorWithFallbackToSource(this);
	}

	/**
	 * Returns the body of {@link #thumbSame}.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="get")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final byte[] getThumbSame()
			throws
				java.io.IOException
	{
		return ThumbnailMagickItem.thumbSame.get(this);
	}

	/**
	 * Returns a URL the content of {@link #thumbRound} is available under.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getURL")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final java.lang.String getThumbRoundURL()
	{
		return ThumbnailMagickItem.thumbRound.getURL(this);
	}

	/**
	 * Returns a Locator the content of {@link #thumbRound} is available under.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getLocator")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final com.exedio.cope.pattern.MediaPath.Locator getThumbRoundLocator()
	{
		return ThumbnailMagickItem.thumbRound.getLocator(this);
	}

	/**
	 * Returns the content type of the media {@link #thumbRound}.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getContentType")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final java.lang.String getThumbRoundContentType()
	{
		return ThumbnailMagickItem.thumbRound.getContentType(this);
	}

	/**
	 * Returns a URL the content of {@link #thumbRound} is available under, falling back to source if necessary.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getURLWithFallbackToSource")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final java.lang.String getThumbRoundURLWithFallbackToSource()
	{
		return ThumbnailMagickItem.thumbRound.getURLWithFallbackToSource(this);
	}

	/**
	 * Returns a Locator the content of {@link #thumbRound} is available under, falling back to source if necessary.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="getLocatorWithFallbackToSource")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final com.exedio.cope.pattern.MediaPath.Locator getThumbRoundLocatorWithFallbackToSource()
	{
		return ThumbnailMagickItem.thumbRound.getLocatorWithFallbackToSource(this);
	}

	/**
	 * Returns the body of {@link #thumbRound}.
	 */
	@com.exedio.cope.instrument.Generated // customize with @Wrapper(wrap="get")
	@java.lang.SuppressWarnings({"FinalMethodInFinalClass","RedundantSuppression","UnnecessarilyQualifiedStaticUsage"})
	final byte[] getThumbRound()
			throws
				java.io.IOException
	{
		return ThumbnailMagickItem.thumbRound.get(this);
	}

	@com.exedio.cope.instrument.Generated
	private static final long serialVersionUID = 1l;

	/**
	 * The persistent type information for thumbnailMagickItem.
	 */
	@com.exedio.cope.instrument.Generated // customize with @WrapperType(type=...)
	public static final com.exedio.cope.Type<ThumbnailMagickItem> TYPE = com.exedio.cope.TypesBound.newType(ThumbnailMagickItem.class);

	/**
	 * Activation constructor. Used for internal purposes only.
	 * @see com.exedio.cope.Item#Item(com.exedio.cope.ActivationParameters)
	 */
	@com.exedio.cope.instrument.Generated
	private ThumbnailMagickItem(final com.exedio.cope.ActivationParameters ap){super(ap);}
}
