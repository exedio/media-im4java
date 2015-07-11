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

import static com.exedio.cope.SchemaInfo.getPrimaryKeyColumnValue;
import static com.exedio.cope.util.StrictFile.delete;
import static java.io.File.createTempFile;

import com.exedio.cope.Item;
import com.exedio.cope.instrument.Wrap;
import com.exedio.cope.pattern.Media;
import com.exedio.cope.pattern.MediaFilter;
import com.exedio.cope.pattern.MediaTestable;
import com.exedio.cope.pattern.MediaType;
import com.exedio.cope.pattern.MediaUtil;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.im4java.core.IMOps;

public final class MediaImageMagickFilter extends MediaFilter implements MediaTestable
{
	private static final long serialVersionUID = 1l;

	private static final HashSet<MediaType> supportedContentTypes =
			new HashSet<MediaType>(Arrays.asList(
					MediaType.forName(MediaType.JPEG),
					MediaType.forName(MediaType.PNG),
					MediaType.forName(MediaType.GIF),
					MediaType.forName(MediaType.PDF),
					MediaType.forName("image/svg+xml")
			));

	private final Media source;

	@SuppressFBWarnings("SE_BAD_FIELD") // OK: writeReplace
	private final Actions actions;

	public MediaImageMagickFilter(final Media source, final IMOps operation)
	{
		this(source, operation, null);
	}

	public MediaImageMagickFilter(
			final Media source,
			final IMOps operation,
			final String outputContentType)
	{
		super(source);
		this.source = source;
		this.actions = new Actions(new Action(operation, outputContentType));
	}

	static MediaType supported(final MediaType type)
	{
		if(type==null)
			return null;
		return
			supportedContentTypes.contains(type) ? type : null;
	}

	public MediaImageMagickFilter forType(
			final String inputContentType,
			final IMOps operation,
			final String outputContentType)
	{
		if(inputContentType==null)
			throw new NullPointerException("inputContentType");
		final MediaType type = MediaImageMagickFilter.supported(MediaType.forName(inputContentType));
		if(type==null)
			throw new IllegalArgumentException("unsupported inputContentType >" + inputContentType + '<');

		return new MediaImageMagickFilter(source, actions.forType(type, operation, outputContentType));
	}

	private MediaImageMagickFilter(
			final Media source,
			final Actions actions)
	{
		super(source);
		this.source = source;
		this.actions = actions;
	}

	@Override
	public Set<String> getSupportedSourceContentTypes()
	{
		final HashSet<String> result = new HashSet<String>();
		for(final MediaType type : supportedContentTypes)
		{
			result.add(type.getName());
			result.addAll(type.getAliases());
		}
		return Collections.unmodifiableSet(result);
	}

	/**
	 * Returns the content type of this filter.
	 * Return the same as {@link #getContentType(Item)}
	 * iff {@link #getContentType(Item)} return the same values for all items not return null.
	 * Otherwise returns null.
	 */
	public String getOutputContentType()
	{
		return actions.getOutputContentType();
	}

	@Override
	public String getContentType(final Item item)
	{
		final String contentType = source.getContentType(item);

		if(contentType==null)
			return null;

		final MediaType type = supported(MediaType.forNameAndAliases(contentType));
		if(type==null)
			return null;

		return actions.get(type).getContentType(type);
	}

	@Override
	public boolean isContentTypeWrapped()
	{
		return getOutputContentType()==null && source.isContentTypeWrapped();
	}

	@Override
	public void doGetAndCommit(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final Item item)
	throws IOException, NotFound
	{
		final String contentType = source.getContentType(item);
		if(contentType==null)
			throw notFoundIsNull();

		final MediaType type = supported(MediaType.forNameAndAliases(contentType));
		if(type==null)
			throw notFoundNotComputable();

		final Action action = actions.get(type);
		final File outFile = execute(item, type, action, true);
		try
		{
			MediaUtil.send(action.outputContentType(type).getName(), outFile, response);
		}
		finally
		{
			delete(outFile);
		}
	}

	@SuppressFBWarnings("PZLA_PREFER_ZERO_LENGTH_ARRAYS")
	@Wrap(order=10, doc="Returns the body of {0}.", thrown=@Wrap.Thrown(IOException.class))
	public byte[] get(final Item item) throws IOException
	{
		final String contentType = source.getContentType(item);
		if(contentType==null)
			return null;

		final MediaType type = supported(MediaType.forNameAndAliases(contentType));
		if(type==null)
			return null;

		final File outFile = execute(item, type, actions.get(type), false);

		final long contentLength = outFile.length();
		if(contentLength<=0)
			throw new RuntimeException(String.valueOf(contentLength));
		if(contentLength>=Integer.MAX_VALUE)
			throw new RuntimeException("too large");

		final byte[] result = new byte[(int)contentLength];

		final FileInputStream body = new FileInputStream(outFile);
		try
		{
			final int readResult = body.read(result);
			if(readResult!=contentLength)
				throw new RuntimeException(String.valueOf(contentLength) + '/' + readResult);
		}
		finally
		{
			body.close();
			delete(outFile);
		}
		return result;
	}

	@Override
	public void test() throws IOException
	{
		for(final MediaType type : supportedContentTypes)
		{
			if(checkSourceContentType(type))
				actions.get(type).test(type, toString());
		}
	}

	private boolean checkSourceContentType(final MediaType type)
	{
		if(source.checkContentType(type.getName()))
			return true;
		for(final String alias : type.getAliases())
			if(source.checkContentType(alias))
				return true;

		return false;
	}

	private final File execute(
			final Item item,
			final MediaType contentType,
			final Action action,
			final boolean commit)
		throws IOException
	{
		final String name = "com.exedio.cope.im4java_" + getID() + '_' + getPrimaryKeyColumnValue(item);
		final File  in = createTempFile(name + "_inp_", ".data");
		final File out = createTempFile(name + "_out_", action.outputContentType(contentType).getExtension());

		source.getBody(item, in);

		if(commit)
			commit();

		action.execute(in, out);

		delete(in);

		return out;
	}

	public String getScript(final String contentType)
	{
		return actions.get(MediaType.forName(contentType)).getScript();
	}
}
