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

import org.im4java.core.IMOps;

import com.exedio.cope.pattern.Media;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public final class MediaImageMagickThumbnail extends MediaImageMagickFilter
{
	private static final long serialVersionUID = 1l;

	@SuppressFBWarnings("SE_BAD_FIELD") // OK: writeReplace
	private final IMOps operation;

	public MediaImageMagickThumbnail(final Media source, final IMOps operation)
	{
		this(source, operation, null);
	}

	private MediaImageMagickThumbnail(
			final Media source,
			final IMOps operation,
			final String outputContentType)
	{
		super(
				source,
				outputContentType,
				operation);
		this.operation = operation;
	}

	public MediaImageMagickThumbnail outputContentType(final String contentType)
	{
		if(contentType==null)
			throw new NullPointerException("outputContentType");
		return new MediaImageMagickThumbnail(getSource(), operation, contentType);
	}
}
