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

public final class ContentTypeInstrumentItem extends Item
{
	static final Media all = new Media().optional();
	static final Media one = new Media().optional().contentType(MediaType.JPEG);

	static final MediaImageMagickFilter allToAll = new MediaImageMagickFilter(all, new IMOperation());
	static final MediaImageMagickFilter allToOne = new MediaImageMagickFilter(all, new IMOperation(), MediaType.JPEG);
	static final MediaImageMagickFilter oneToAll = new MediaImageMagickFilter(one, new IMOperation());
	static final MediaImageMagickFilter oneToOne = new MediaImageMagickFilter(one, new IMOperation(), MediaType.JPEG);

	/**
	 * Must not be generated by the instrumentor, because contentType is fixed.
	 */
	@SuppressWarnings("unused") // OK: just ensures that this method is not created by the instrumentor
	String getOneContentType()
	{
		return one.getContentType(this);
	}

	/**
	 * Must not be generated by the instrumentor, because contentType is fixed.
	 */
	@SuppressWarnings("unused") // OK: just ensures that this method is not created by the instrumentor
	String getAllToOneContentType()
	{
		return allToOne.getContentType(this);
	}

	/**
	 * Must not be generated by the instrumentor, because contentType is fixed.
	 */
	@SuppressWarnings("unused") // OK: just ensures that this method is not created by the instrumentor
	String getOneToAllContentType()
	{
		return oneToAll.getContentType(this);
	}

	/**
	 * Must not be generated by the instrumentor, because contentType is fixed.
	 */
	@SuppressWarnings("unused") // OK: just ensures that this method is not created by the instrumentor
	String getOneToOneContentType()
	{
		return oneToOne.getContentType(this);
	}


	/**
	 * Creates a new ContentTypeInstrumentItem with all the fields initially needed.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @WrapperType(constructor=...) and @WrapperInitial
	public ContentTypeInstrumentItem()
	{
		this(new com.exedio.cope.SetValue<?>[]{
		});
	}

	/**
	 * Creates a new ContentTypeInstrumentItem and sets the given fields initially.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @WrapperType(genericConstructor=...)
	private ContentTypeInstrumentItem(final com.exedio.cope.SetValue<?>... setValues)
	{
		super(setValues);
	}

	/**
	 * Returns a URL the content of {@link #all} is available under.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getURL")
	final java.lang.String getAllURL()
	{
		return ContentTypeInstrumentItem.all.getURL(this);
	}

	/**
	 * Returns a Locator the content of {@link #all} is available under.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getLocator")
	final com.exedio.cope.pattern.MediaPath.Locator getAllLocator()
	{
		return ContentTypeInstrumentItem.all.getLocator(this);
	}

	/**
	 * Returns the content type of the media {@link #all}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getContentType")
	final java.lang.String getAllContentType()
	{
		return ContentTypeInstrumentItem.all.getContentType(this);
	}

	/**
	 * Returns whether media {@link #all} is null.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="isNull")
	final boolean isAllNull()
	{
		return ContentTypeInstrumentItem.all.isNull(this);
	}

	/**
	 * Returns the last modification date of media {@link #all}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getLastModified")
	final java.util.Date getAllLastModified()
	{
		return ContentTypeInstrumentItem.all.getLastModified(this);
	}

	/**
	 * Returns the body length of the media {@link #all}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getLength")
	final long getAllLength()
	{
		return ContentTypeInstrumentItem.all.getLength(this);
	}

	/**
	 * Returns the body of the media {@link #all}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getBody")
	final byte[] getAllBody()
	{
		return ContentTypeInstrumentItem.all.getBody(this);
	}

	/**
	 * Writes the body of media {@link #all} into the given stream.
	 * Does nothing, if the media is null.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getBody")
	final void getAllBody(final java.io.OutputStream body)
			throws
				java.io.IOException
	{
		ContentTypeInstrumentItem.all.getBody(this,body);
	}

	/**
	 * Writes the body of media {@link #all} into the given file.
	 * Does nothing, if the media is null.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getBody")
	final void getAllBody(final java.io.File body)
			throws
				java.io.IOException
	{
		ContentTypeInstrumentItem.all.getBody(this,body);
	}

	/**
	 * Sets the content of media {@link #all}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="set")
	final void setAll(final com.exedio.cope.pattern.Media.Value all)
			throws
				java.io.IOException
	{
		ContentTypeInstrumentItem.all.set(this,all);
	}

	/**
	 * Sets the content of media {@link #all}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="set")
	final void setAll(final byte[] body,final java.lang.String contentType)
	{
		ContentTypeInstrumentItem.all.set(this,body,contentType);
	}

	/**
	 * Sets the content of media {@link #all}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="set")
	final void setAll(final java.io.InputStream body,final java.lang.String contentType)
			throws
				java.io.IOException
	{
		ContentTypeInstrumentItem.all.set(this,body,contentType);
	}

	/**
	 * Sets the content of media {@link #all}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="set")
	final void setAll(final java.io.File body,final java.lang.String contentType)
			throws
				java.io.IOException
	{
		ContentTypeInstrumentItem.all.set(this,body,contentType);
	}

	/**
	 * Returns a URL the content of {@link #one} is available under.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getURL")
	final java.lang.String getOneURL()
	{
		return ContentTypeInstrumentItem.one.getURL(this);
	}

	/**
	 * Returns a Locator the content of {@link #one} is available under.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getLocator")
	final com.exedio.cope.pattern.MediaPath.Locator getOneLocator()
	{
		return ContentTypeInstrumentItem.one.getLocator(this);
	}

	/**
	 * Returns whether media {@link #one} is null.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="isNull")
	final boolean isOneNull()
	{
		return ContentTypeInstrumentItem.one.isNull(this);
	}

	/**
	 * Returns the last modification date of media {@link #one}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getLastModified")
	final java.util.Date getOneLastModified()
	{
		return ContentTypeInstrumentItem.one.getLastModified(this);
	}

	/**
	 * Returns the body length of the media {@link #one}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getLength")
	final long getOneLength()
	{
		return ContentTypeInstrumentItem.one.getLength(this);
	}

	/**
	 * Returns the body of the media {@link #one}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getBody")
	final byte[] getOneBody()
	{
		return ContentTypeInstrumentItem.one.getBody(this);
	}

	/**
	 * Writes the body of media {@link #one} into the given stream.
	 * Does nothing, if the media is null.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getBody")
	final void getOneBody(final java.io.OutputStream body)
			throws
				java.io.IOException
	{
		ContentTypeInstrumentItem.one.getBody(this,body);
	}

	/**
	 * Writes the body of media {@link #one} into the given file.
	 * Does nothing, if the media is null.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getBody")
	final void getOneBody(final java.io.File body)
			throws
				java.io.IOException
	{
		ContentTypeInstrumentItem.one.getBody(this,body);
	}

	/**
	 * Sets the content of media {@link #one}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="set")
	final void setOne(final com.exedio.cope.pattern.Media.Value one)
			throws
				java.io.IOException
	{
		ContentTypeInstrumentItem.one.set(this,one);
	}

	/**
	 * Sets the content of media {@link #one}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="set")
	final void setOne(final byte[] body,final java.lang.String contentType)
	{
		ContentTypeInstrumentItem.one.set(this,body,contentType);
	}

	/**
	 * Sets the content of media {@link #one}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="set")
	final void setOne(final java.io.InputStream body,final java.lang.String contentType)
			throws
				java.io.IOException
	{
		ContentTypeInstrumentItem.one.set(this,body,contentType);
	}

	/**
	 * Sets the content of media {@link #one}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="set")
	final void setOne(final java.io.File body,final java.lang.String contentType)
			throws
				java.io.IOException
	{
		ContentTypeInstrumentItem.one.set(this,body,contentType);
	}

	/**
	 * Returns a URL the content of {@link #allToAll} is available under.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getURL")
	final java.lang.String getAllToAllURL()
	{
		return ContentTypeInstrumentItem.allToAll.getURL(this);
	}

	/**
	 * Returns a Locator the content of {@link #allToAll} is available under.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getLocator")
	final com.exedio.cope.pattern.MediaPath.Locator getAllToAllLocator()
	{
		return ContentTypeInstrumentItem.allToAll.getLocator(this);
	}

	/**
	 * Returns the content type of the media {@link #allToAll}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getContentType")
	final java.lang.String getAllToAllContentType()
	{
		return ContentTypeInstrumentItem.allToAll.getContentType(this);
	}

	/**
	 * Returns a URL the content of {@link #allToAll} is available under, falling back to source if necessary.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getURLWithFallbackToSource")
	final java.lang.String getAllToAllURLWithFallbackToSource()
	{
		return ContentTypeInstrumentItem.allToAll.getURLWithFallbackToSource(this);
	}

	/**
	 * Returns a Locator the content of {@link #allToAll} is available under, falling back to source if necessary.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getLocatorWithFallbackToSource")
	final com.exedio.cope.pattern.MediaPath.Locator getAllToAllLocatorWithFallbackToSource()
	{
		return ContentTypeInstrumentItem.allToAll.getLocatorWithFallbackToSource(this);
	}

	/**
	 * Returns the body of {@link #allToAll}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="get")
	final byte[] getAllToAll()
			throws
				java.io.IOException
	{
		return ContentTypeInstrumentItem.allToAll.get(this);
	}

	/**
	 * Returns a URL the content of {@link #allToOne} is available under.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getURL")
	final java.lang.String getAllToOneURL()
	{
		return ContentTypeInstrumentItem.allToOne.getURL(this);
	}

	/**
	 * Returns a Locator the content of {@link #allToOne} is available under.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getLocator")
	final com.exedio.cope.pattern.MediaPath.Locator getAllToOneLocator()
	{
		return ContentTypeInstrumentItem.allToOne.getLocator(this);
	}

	/**
	 * Returns a URL the content of {@link #allToOne} is available under, falling back to source if necessary.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getURLWithFallbackToSource")
	final java.lang.String getAllToOneURLWithFallbackToSource()
	{
		return ContentTypeInstrumentItem.allToOne.getURLWithFallbackToSource(this);
	}

	/**
	 * Returns a Locator the content of {@link #allToOne} is available under, falling back to source if necessary.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getLocatorWithFallbackToSource")
	final com.exedio.cope.pattern.MediaPath.Locator getAllToOneLocatorWithFallbackToSource()
	{
		return ContentTypeInstrumentItem.allToOne.getLocatorWithFallbackToSource(this);
	}

	/**
	 * Returns the body of {@link #allToOne}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="get")
	final byte[] getAllToOne()
			throws
				java.io.IOException
	{
		return ContentTypeInstrumentItem.allToOne.get(this);
	}

	/**
	 * Returns a URL the content of {@link #oneToAll} is available under.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getURL")
	final java.lang.String getOneToAllURL()
	{
		return ContentTypeInstrumentItem.oneToAll.getURL(this);
	}

	/**
	 * Returns a Locator the content of {@link #oneToAll} is available under.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getLocator")
	final com.exedio.cope.pattern.MediaPath.Locator getOneToAllLocator()
	{
		return ContentTypeInstrumentItem.oneToAll.getLocator(this);
	}

	/**
	 * Returns the body of {@link #oneToAll}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="get")
	final byte[] getOneToAll()
			throws
				java.io.IOException
	{
		return ContentTypeInstrumentItem.oneToAll.get(this);
	}

	/**
	 * Returns a URL the content of {@link #oneToOne} is available under.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getURL")
	final java.lang.String getOneToOneURL()
	{
		return ContentTypeInstrumentItem.oneToOne.getURL(this);
	}

	/**
	 * Returns a Locator the content of {@link #oneToOne} is available under.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="getLocator")
	final com.exedio.cope.pattern.MediaPath.Locator getOneToOneLocator()
	{
		return ContentTypeInstrumentItem.oneToOne.getLocator(this);
	}

	/**
	 * Returns the body of {@link #oneToOne}.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @Wrapper(wrap="get")
	final byte[] getOneToOne()
			throws
				java.io.IOException
	{
		return ContentTypeInstrumentItem.oneToOne.get(this);
	}

	@javax.annotation.Generated("com.exedio.cope.instrument")
	private static final long serialVersionUID = 1l;

	/**
	 * The persistent type information for contentTypeInstrumentItem.
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument") // customize with @WrapperType(type=...)
	public static final com.exedio.cope.Type<ContentTypeInstrumentItem> TYPE = com.exedio.cope.TypesBound.newType(ContentTypeInstrumentItem.class);

	/**
	 * Activation constructor. Used for internal purposes only.
	 * @see com.exedio.cope.Item#Item(com.exedio.cope.ActivationParameters)
	 */
	@javax.annotation.Generated("com.exedio.cope.instrument")
	private ContentTypeInstrumentItem(final com.exedio.cope.ActivationParameters ap){super(ap);}
}
