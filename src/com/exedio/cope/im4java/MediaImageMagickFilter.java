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

import static com.exedio.cope.util.StrictFile.delete;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IMOps;

import com.exedio.cope.DataField;
import com.exedio.cope.Item;
import com.exedio.cope.instrument.Wrap;
import com.exedio.cope.pattern.Media;
import com.exedio.cope.pattern.MediaFilter;
import com.exedio.cope.pattern.MediaTestable;
import com.exedio.cope.pattern.MediaType;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public final class MediaImageMagickFilter extends MediaFilter implements MediaTestable
{
	private static final long serialVersionUID = 1l;

	private static final HashSet<MediaType> supportedContentTypes =
			new HashSet<MediaType>(Arrays.asList(
					MediaType.forName(MediaType.JPEG),
					MediaType.forName(MediaType.PNG),
					MediaType.forName(MediaType.GIF)
			));

	private final Media source;
	@SuppressFBWarnings("SE_BAD_FIELD") // OK: writeReplace
	private final IMOperation operationWithImage;
	@SuppressFBWarnings("SE_BAD_FIELD") // OK: writeReplace
	private final MediaType constantOutputContentType;

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

		if(operation==null)
			throw new NullPointerException("operation");
		this.operationWithImage = new IMOperation();
		this.operationWithImage.addOperation(operation);
		this.operationWithImage.addImage(2);

		if(outputContentType!=null)
		{
			this.constantOutputContentType = supported(MediaType.forName(outputContentType));
			if(constantOutputContentType==null)
				throw new IllegalArgumentException("unsupported outputContentType >" + outputContentType + '<');
		}
		else
		{
			this.constantOutputContentType = null;
		}
	}

	private static MediaType supported(final MediaType type)
	{
		if(type==null)
			return null;
		return
			supportedContentTypes.contains(type) ? type : null;
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
		return constantOutputContentType!=null ? constantOutputContentType.getName() : null;
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

		return (constantOutputContentType!=null?constantOutputContentType:type).getName();
	}

	@Override
	public boolean isContentTypeWrapped()
	{
		return constantOutputContentType==null && source.isContentTypeWrapped();
	}

	@Override
	public Media.Log doGetIfModified(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final Item item)
	throws IOException
	{
		final String contentType = source.getContentType(item);
		if(contentType==null)
			return isNull;

		final MediaType type = supported(MediaType.forNameAndAliases(contentType));
		if(type==null)
			return notComputable;

		final File outFile = execute(item, type);

		final long contentLength = outFile.length();
		if(contentLength<=0)
			throw new RuntimeException(String.valueOf(contentLength));
		if(contentLength<=Integer.MAX_VALUE)
			response.setContentLength((int)contentLength);

		response.setContentType(outputContentType(type).getName());

		final byte[] b = new byte[DataField.min(100*1024, contentLength)];
		final FileInputStream body = new FileInputStream(outFile);
		try
		{
			final ServletOutputStream out = response.getOutputStream();
			try
			{
				for(int len = body.read(b); len>=0; len = body.read(b))
					out.write(b, 0, len);

				return delivered;
			}
			finally
			{
				if(out!=null)
					out.close();
			}
		}
		finally
		{
			body.close();
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

		final File outFile = execute(item, type);

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
		final File  inFile = File.createTempFile(MediaImageMagickFilter.class.getName() + ".in."  + getID(), ".data");
		final File outFile = File.createTempFile(MediaImageMagickFilter.class.getName() + ".out." + getID(), outputContentType(MediaType.forName(MediaType.JPEG)).getExtension());


		final byte[] b = new byte[1580]; // size of the file plus 2 to detect larger file
		{
			final InputStream inStream = MediaImageMagickFilter.class.getResourceAsStream("MediaImageMagickFilter-test.jpg");
			try
			{
				final int inLength = inStream.read(b);
				if(inLength!=1578) // size of the file
					throw new RuntimeException(String.valueOf(inLength));
			}
			finally
			{
				inStream.close();
			}
		}
		{
			final FileOutputStream outStream = new FileOutputStream(inFile);
			try
			{
				outStream.write(b);
			}
			finally
			{
				outStream.close();
			}
		}

		execute(inFile, outFile);

		delete(inFile);
		delete(outFile);
	}

	private MediaType outputContentType(final MediaType inputContentType)
	{
		return
				this.constantOutputContentType!=null
				? this.constantOutputContentType
				: inputContentType;
	}

	private final File execute(final Item item, final MediaType contentType) throws IOException
	{
		final File  inFile = File.createTempFile(MediaImageMagickFilter.class.getName() + ".in."  + getID(), ".data");
		final File outFile = File.createTempFile(MediaImageMagickFilter.class.getName() + ".out." + getID(), outputContentType(contentType).getExtension());

		source.getBody(item, inFile);

		execute(inFile, outFile);

		delete(inFile);

		return outFile;
	}

	private void execute(final File inFile, final File outFile) throws IOException
	{
		final ConvertCmd cmd = new ConvertCmd();
		//System.out.println("------script-----" + getScript());
		try
		{
			cmd.run(operationWithImage, inFile.getAbsolutePath(), outFile.getAbsolutePath());
		}
		catch(final InterruptedException e)
		{
			throw new RuntimeException(e);
		}
		catch(final IM4JavaException e)
		{
			throw new RuntimeException(e);
		}
	}

	public String getScript()
	{
		final ConvertCmd cmd = new ConvertCmd();
		final StringWriter string = new StringWriter();
		final PrintWriter pw = new PrintWriter(string);
		cmd.createScript(pw, operationWithImage, new Properties());
		pw.flush();
		return string.toString();
	}
}
