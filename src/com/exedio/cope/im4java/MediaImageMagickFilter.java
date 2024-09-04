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

import static com.exedio.cope.SchemaInfo.getPrimaryKeyColumnValueL;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.StandardOpenOption.READ;
import static java.util.Objects.requireNonNull;

import com.exedio.cope.CopyMapper;
import com.exedio.cope.Copyable;
import com.exedio.cope.Item;
import com.exedio.cope.instrument.Wrap;
import com.exedio.cope.pattern.Media;
import com.exedio.cope.pattern.MediaFilter;
import com.exedio.cope.pattern.MediaPreviewable;
import com.exedio.cope.pattern.MediaTestable;
import com.exedio.cope.pattern.MediaType;
import com.exedio.cope.pattern.MediaUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.im4java.core.IMOps;

public final class MediaImageMagickFilter extends MediaFilter implements Copyable, MediaPreviewable, MediaTestable
{
	@Serial
	private static final long serialVersionUID = 1l;

	private static final HashSet<MediaType> supportedContentTypes =
			new HashSet<>(Arrays.asList(
					MediaType.forName(MediaType.JPEG),
					MediaType.forName(MediaType.PNG),
					MediaType.forName(MediaType.GIF),
					MediaType.forName(MediaType.WEBP),
					MediaType.forName(MediaType.TIFF),
					MediaType.forName(MediaType.PDF),
					MediaType.forName(MediaType.SVG)
			));

	private final Media source;

	private final Set<String> identityContentTypes;
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
		this(source, Collections.emptySet(), new Actions(new Action(operation, outputContentType)), true);
	}

	private MediaImageMagickFilter(
			final Media source,
			final Set<String> identityContentTypes,
			final Actions actions,
			final boolean withLocator)
	{
		super(source, withLocator);
		this.source = source;
		this.identityContentTypes = identityContentTypes;
		this.actions = actions;
	}

	static MediaType supported(final MediaType type)
	{
		if(type==null)
			return null;
		return
			supportedContentTypes.contains(type) ? type : null;
	}

	/**
	 * @param withLocator should this filter be accessible via MediaServlet
	 */
	public MediaImageMagickFilter withLocator(final boolean withLocator)
	{
		return new MediaImageMagickFilter(source, identityContentTypes, actions, withLocator);
	}

	public MediaImageMagickFilter forType(
			final String inputContentType,
			final IMOps operation,
			final String outputContentType)
	{
		requireNonNull(inputContentType, "inputContentType");

		if(isIdentity(inputContentType))
			throw new IllegalArgumentException("duplicate inputContentType " + inputContentType);
		final MediaType type = supported(MediaType.forName(inputContentType));
		if(type==null)
			throw new IllegalArgumentException("unsupported inputContentType >" + inputContentType + '<');

		return new MediaImageMagickFilter(source, identityContentTypes, actions.forType(type, operation, outputContentType), isWithLocator());
	}

	public MediaImageMagickFilter forTypeIdentity(
			final String inputContentType)
	{
		requireNonNull(inputContentType, "inputContentType");

		if(actions.supportsNonDefault(MediaType.forNameAndAliases(inputContentType)))
			throw new IllegalArgumentException("duplicate inputContentType " + inputContentType);
		final HashSet<String> identityContentTypes = new HashSet<>(this.identityContentTypes);
		final MediaType type = supported(MediaType.forName(inputContentType));
		if(type==null)
			add(identityContentTypes, inputContentType);
		else
		{
			add(identityContentTypes, type.getName());
			for(final String alias : type.getAliases())
				add(identityContentTypes, alias);
		}

		return new MediaImageMagickFilter(source, identityContentTypes, actions, isWithLocator());
	}

	private static void add(final HashSet<String> identityContentTypes, final String inputContentType)
	{
		if(!identityContentTypes.add(inputContentType))
			throw new IllegalArgumentException("duplicate inputContentType " + inputContentType);
	}

	@Override
	public MediaImageMagickFilter copy(final CopyMapper mapper)
	{
		return new MediaImageMagickFilter(mapper.get(source), identityContentTypes, actions, isWithLocator());
	}

	@Override
	public Set<String> getSupportedSourceContentTypes()
	{
		final HashSet<String> result = new HashSet<>(identityContentTypes);
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
		if(!identityContentTypes.isEmpty())
			return null;

		return actions.getOutputContentType();
	}

	/**
	 * @param inputContentType the {@link #getSource() source} content type
	 */
	public String getOutputContentType(final String inputContentType)
	{
		if(inputContentType==null)
			return null;

		if(isIdentity(inputContentType))
			return inputContentType;

		final MediaType type = supported(MediaType.forNameAndAliases(inputContentType));
		if(type==null)
			return null;

		return actions.get(type).getOutputContentType(type);
	}

	@Override
	public String getContentType(final Item item)
	{
		final String contentType = source.getContentType(item);

		if(contentType==null)
			return null;

		if(isIdentity(contentType))
			return contentType;

		final MediaType type = supported(MediaType.forNameAndAliases(contentType));
		if(type==null)
			return null;

		return actions.get(type).getOutputContentType(type);
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
		if (!isWithLocator())
			throw new RuntimeException("not isWithLocator() - unexpected call: " + this + ' ' + item.getCopeID());
		final String contentType = source.getContentType(item);
		if(contentType==null)
			throw notFoundIsNull();

		if(isIdentity(contentType))
		{
			source.doGetAndCommit(request, response, item);
			return;
		}

		final MediaType type = supported(MediaType.forNameAndAliases(contentType));
		if(type==null)
			throw notFoundIsNull();

		final Action action = actions.get(type);
		final Path outFile = execute(item, type, action, true);
		try
		{
			MediaUtil.send(action.outputContentType(type).getName(), outFile, response);
		}
		finally
		{
			delete(outFile);
		}
	}

	@Wrap(order=10, doc="Returns the body of {0}.", thrown=@Wrap.Thrown(IOException.class))
	public byte[] get(final Item item) throws IOException
	{
		final String contentType = source.getContentType(item);
		if(contentType==null)
			return null;

		if(isIdentity(contentType))
			return source.getBody(item);

		final MediaType type = supported(MediaType.forNameAndAliases(contentType));
		if(type==null)
			return null;

		final Path outFile = execute(item, type, actions.get(type), false);

		final long contentLength = Files.size(outFile);
		if(contentLength<=0)
			throw new RuntimeException(String.valueOf(contentLength));
		if(contentLength>=Integer.MAX_VALUE)
			throw new RuntimeException("too large");

		final byte[] result = new byte[(int)contentLength];

		try(InputStream body = newInputStream(outFile, READ))
		{
			final int readResult = body.read(result);
			if(readResult!=contentLength)
				throw new RuntimeException(String.valueOf(contentLength) + '/' + readResult);
		}
		finally
		{
			delete(outFile);
		}
		return result;
	}

	@Override
	public String preview(
			final Path sourceBody,
			final String sourceContentType,
			final Path target)
			throws IOException
	{
		if(isIdentity(sourceContentType))
		{
			Files.copy(sourceBody, target);
			return sourceContentType;
		}

		final MediaType type = supported(MediaType.forNameAndAliases(sourceContentType));
		if(type==null)
			throw new IllegalArgumentException("unsupported content type " + sourceContentType);

		final Action action = actions.get(type);
		final MediaType outputContentType = action.outputContentType(type);

		action.execute(sourceBody, type, target, outputContentType);

		return outputContentType.getName();
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
		if(!isIdentity(type.getName()) && source.checkContentType(type.getName()))
			return true;
		for(final String alias : type.getAliases())
			if(!isIdentity(alias) && source.checkContentType(alias))
				return true;

		return false;
	}

	private Path execute(
			final Item item,
			final MediaType contentType,
			final Action action,
			final boolean commit)
		throws IOException
	{
		final String name = "com.exedio.cope.im4java_" + getID() + '_' + getPrimaryKeyColumnValueL(item);
		final MediaType outputContentType = action.outputContentType(contentType);
		final Path  in = createTempFile(name + "_inp_", ".data");
		final Path out = createTempFile(name + "_out_", outputContentType.getDefaultExtension());

		source.getBody(item, in);

		if(commit)
			commit();

		action.execute(in, contentType, out, outputContentType);

		delete(in);

		return out;
	}

	List<String> getCmdArgs(final String contentType)
	{
		if(isIdentity(contentType))
			return null;

		return action(contentType).getCmdArgs();
	}

	public String getScript(final String contentType)
	{
		if(isIdentity(contentType))
			return null;

		return action(contentType).getScript();
	}

	private boolean isIdentity(final String contentType)
	{
		return identityContentTypes.contains(contentType);
	}

	private Action action(final String contentType)
	{
		return actions.get(MediaType.forName(contentType));
	}
}
