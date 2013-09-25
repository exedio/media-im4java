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
	static final Media file = new Media().optional().lengthMax(10000);

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
				new IMOperation().resize(20, 30, '>'));

	@SuppressWarnings("boxing")
	static final MediaImageMagickFilter thumbRound =
			new MediaImageMagickFilter(
				file,
				new IMOperation().resize(20, 30, '>')).
				forType(MediaType.JPEG, new IMOperation().resize(30, 40, '>'), MediaType.PNG ).
				forType(MediaType.PNG,  new IMOperation().resize(30, 40, '>'), MediaType.GIF ).
				forType(MediaType.GIF,  new IMOperation().resize(30, 40, '>'), MediaType.JPEG);

	// must not be generated by the instrumentor
	// because thumb has a fixed contentType
	String getThumbContentType()
	{
		return ThumbnailMagickItem.thumb.getContentType(this);
	}

	// must not be generated by the instrumentor
	// because thumbFull has a fixed contentType
	String getThumbFullContentType()
	{
		return ThumbnailMagickItem.thumbFull.getContentType(this);
	}

	/**

	 **
	 * Creates a new ThumbnailMagickItem with all the fields initially needed.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tags <tt>@cope.constructor public|package|protected|private|none</tt> in the class comment and <tt>@cope.initial</tt> in the comment of fields.
	 */
	public ThumbnailMagickItem()
	{
		this(new com.exedio.cope.SetValue<?>[]{
		});
	}/**

	 **
	 * Creates a new ThumbnailMagickItem and sets the given fields initially.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.generic.constructor public|package|protected|private|none</tt> in the class comment.
	 */
	private ThumbnailMagickItem(final com.exedio.cope.SetValue<?>... setValues)
	{
		super(setValues);
	}/**

	 **
	 * Returns a URL the content of {@link #file} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURL public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getFileURL()
	{
		return ThumbnailMagickItem.file.getURL(this);
	}/**

	 **
	 * Returns a Locator the content of {@link #file} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getLocator public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final com.exedio.cope.pattern.MediaPath.Locator getFileLocator()
	{
		return ThumbnailMagickItem.file.getLocator(this);
	}/**

	 **
	 * Returns the content type of the media {@link #file}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getContentType public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getFileContentType()
	{
		return ThumbnailMagickItem.file.getContentType(this);
	}/**

	 **
	 * Returns whether media {@link #file} is null.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.isNull public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final boolean isFileNull()
	{
		return ThumbnailMagickItem.file.isNull(this);
	}/**

	 **
	 * Returns the last modification date of media {@link #file}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getLastModified public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.util.Date getFileLastModified()
	{
		return ThumbnailMagickItem.file.getLastModified(this);
	}/**

	 **
	 * Returns the body length of the media {@link #file}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getLength public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final long getFileLength()
	{
		return ThumbnailMagickItem.file.getLength(this);
	}/**

	 **
	 * Returns the body of the media {@link #file}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getBody public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final byte[] getFileBody()
	{
		return ThumbnailMagickItem.file.getBody(this);
	}/**

	 **
	 * Writes the body of media {@link #file} into the given stream.
	 * Does nothing, if the media is null.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getBody public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void getFileBody(final java.io.OutputStream body)
			throws
				java.io.IOException
	{
		ThumbnailMagickItem.file.getBody(this,body);
	}/**

	 **
	 * Writes the body of media {@link #file} into the given file.
	 * Does nothing, if the media is null.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getBody public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void getFileBody(final java.io.File body)
			throws
				java.io.IOException
	{
		ThumbnailMagickItem.file.getBody(this,body);
	}/**

	 **
	 * Sets the content of media {@link #file}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setFile(final com.exedio.cope.pattern.Media.Value file)
			throws
				java.io.IOException
	{
		ThumbnailMagickItem.file.set(this,file);
	}/**

	 **
	 * Sets the content of media {@link #file}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setFile(final byte[] body,final java.lang.String contentType)
	{
		ThumbnailMagickItem.file.set(this,body,contentType);
	}/**

	 **
	 * Sets the content of media {@link #file}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setFile(final java.io.InputStream body,final java.lang.String contentType)
			throws
				java.io.IOException
	{
		ThumbnailMagickItem.file.set(this,body,contentType);
	}/**

	 **
	 * Sets the content of media {@link #file}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setFile(final java.io.File body,final java.lang.String contentType)
			throws
				java.io.IOException
	{
		ThumbnailMagickItem.file.set(this,body,contentType);
	}/**

	 **
	 * Returns a URL the content of {@link #thumb} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURL public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getThumbURL()
	{
		return ThumbnailMagickItem.thumb.getURL(this);
	}/**

	 **
	 * Returns a Locator the content of {@link #thumb} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getLocator public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final com.exedio.cope.pattern.MediaPath.Locator getThumbLocator()
	{
		return ThumbnailMagickItem.thumb.getLocator(this);
	}/**

	 **
	 * Returns a URL the content of {@link #thumb} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURLWithFallbackToSource public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getThumbURLWithFallbackToSource()
	{
		return ThumbnailMagickItem.thumb.getURLWithFallbackToSource(this);
	}/**

	 **
	 * Returns the body of {@link #thumb}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final byte[] getThumb()
			throws
				java.io.IOException
	{
		return ThumbnailMagickItem.thumb.get(this);
	}/**

	 **
	 * Returns a URL the content of {@link #thumbFull} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURL public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getThumbFullURL()
	{
		return ThumbnailMagickItem.thumbFull.getURL(this);
	}/**

	 **
	 * Returns a Locator the content of {@link #thumbFull} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getLocator public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final com.exedio.cope.pattern.MediaPath.Locator getThumbFullLocator()
	{
		return ThumbnailMagickItem.thumbFull.getLocator(this);
	}/**

	 **
	 * Returns a URL the content of {@link #thumbFull} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURLWithFallbackToSource public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getThumbFullURLWithFallbackToSource()
	{
		return ThumbnailMagickItem.thumbFull.getURLWithFallbackToSource(this);
	}/**

	 **
	 * Returns the body of {@link #thumbFull}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final byte[] getThumbFull()
			throws
				java.io.IOException
	{
		return ThumbnailMagickItem.thumbFull.get(this);
	}/**

	 **
	 * Returns a URL the content of {@link #thumbSame} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURL public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getThumbSameURL()
	{
		return ThumbnailMagickItem.thumbSame.getURL(this);
	}/**

	 **
	 * Returns a Locator the content of {@link #thumbSame} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getLocator public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final com.exedio.cope.pattern.MediaPath.Locator getThumbSameLocator()
	{
		return ThumbnailMagickItem.thumbSame.getLocator(this);
	}/**

	 **
	 * Returns the content type of the media {@link #thumbSame}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getContentType public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getThumbSameContentType()
	{
		return ThumbnailMagickItem.thumbSame.getContentType(this);
	}/**

	 **
	 * Returns a URL the content of {@link #thumbSame} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURLWithFallbackToSource public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getThumbSameURLWithFallbackToSource()
	{
		return ThumbnailMagickItem.thumbSame.getURLWithFallbackToSource(this);
	}/**

	 **
	 * Returns the body of {@link #thumbSame}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final byte[] getThumbSame()
			throws
				java.io.IOException
	{
		return ThumbnailMagickItem.thumbSame.get(this);
	}/**

	 **
	 * Returns a URL the content of {@link #thumbRound} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURL public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getThumbRoundURL()
	{
		return ThumbnailMagickItem.thumbRound.getURL(this);
	}/**

	 **
	 * Returns a Locator the content of {@link #thumbRound} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getLocator public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final com.exedio.cope.pattern.MediaPath.Locator getThumbRoundLocator()
	{
		return ThumbnailMagickItem.thumbRound.getLocator(this);
	}/**

	 **
	 * Returns the content type of the media {@link #thumbRound}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getContentType public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getThumbRoundContentType()
	{
		return ThumbnailMagickItem.thumbRound.getContentType(this);
	}/**

	 **
	 * Returns a URL the content of {@link #thumbRound} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURLWithFallbackToSource public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getThumbRoundURLWithFallbackToSource()
	{
		return ThumbnailMagickItem.thumbRound.getURLWithFallbackToSource(this);
	}/**

	 **
	 * Returns the body of {@link #thumbRound}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final byte[] getThumbRound()
			throws
				java.io.IOException
	{
		return ThumbnailMagickItem.thumbRound.get(this);
	}/**

	 **
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 */
	private static final long serialVersionUID = 1l;/**

	 **
	 * The persistent type information for thumbnailMagickItem.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.type public|package|protected|private|none</tt> in the class comment.
	 */
	public static final com.exedio.cope.Type<ThumbnailMagickItem> TYPE = com.exedio.cope.TypesBound.newType(ThumbnailMagickItem.class);/**

	 **
	 * Activation constructor. Used for internal purposes only.
	 * @see com.exedio.cope.Item#Item(com.exedio.cope.ActivationParameters)
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 */
	private ThumbnailMagickItem(final com.exedio.cope.ActivationParameters ap){super(ap);
}}
