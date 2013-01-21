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

import java.util.HashMap;

import org.im4java.core.IMOps;

import com.exedio.cope.pattern.MediaType;

final class Actions
{
	private final Action default_;
	private final HashMap<MediaType, Action> byInputContentType;

	Actions(final Action default_)
	{
		this.default_ = default_;
		this.byInputContentType = new HashMap<MediaType, Action>();
		assert default_!=null;
	}

	Action get(final MediaType type)
	{
		final Action byInputContentType = this.byInputContentType.get(type);
		return byInputContentType!=null ? byInputContentType : default_;
	}

	String getOutputContentType()
	{
		final String defaultResult = default_.getOutputContentType();
		if(defaultResult==null)
			return null;
		for(final Action action : byInputContentType.values())
		{
			if(!defaultResult.equals(action.getOutputContentType()))
				return null;
		}
		return defaultResult;
	}

	Actions forType(
			final MediaType inputContentType,
			final IMOps operation,
			final String outputContentType)
	{
		final HashMap<MediaType, Action> byInputContentType =
				new HashMap<MediaType, Action>(this.byInputContentType);
		if(byInputContentType.put(inputContentType, new Action(operation, outputContentType))!=null)
			throw new IllegalArgumentException("duplicate inputContentType " + inputContentType);
		return new Actions(default_, byInputContentType);
	}

	private Actions(final Action default_, final HashMap<MediaType, Action> byInputContentType)
	{
		this.default_ = default_;
		this.byInputContentType = byInputContentType;
		assert default_!=null;
		assert byInputContentType!=null;
	}
}