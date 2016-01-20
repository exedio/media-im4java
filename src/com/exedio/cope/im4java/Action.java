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
import static java.io.File.createTempFile;

import com.exedio.cope.pattern.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IMOps;

final class Action
{
	private final IMOperation operationWithImage;
	private final MediaType constantOutputContentType;

	Action(
			final IMOps operation,
			final String outputContentType)
	{
		if(operation==null)
			throw new NullPointerException("operation");
		this.operationWithImage = new IMOperation();
		this.operationWithImage.addOperation(operation);
		this.operationWithImage.addImage(2);

		if(outputContentType!=null)
		{
			this.constantOutputContentType = MediaImageMagickFilter.supported(MediaType.forName(outputContentType));
			if(constantOutputContentType==null)
				throw new IllegalArgumentException("unsupported outputContentType >" + outputContentType + '<');
		}
		else
		{
			this.constantOutputContentType = null;
		}
	}

	String getOutputContentType()
	{
		return constantOutputContentType!=null ? constantOutputContentType.getName() : null;
	}

	String getContentType(final MediaType type)
	{
		return (constantOutputContentType!=null?constantOutputContentType:type).getName();
	}

	MediaType outputContentType(final MediaType inputContentType)
	{
		return
				this.constantOutputContentType!=null
				? this.constantOutputContentType
				: inputContentType;
	}

	void execute(final File in, final File out) throws IOException
	{
		final ConvertCmd cmd = new ConvertCmd();
		//System.out.println("------script-----" + getScript());
		try
		{
			cmd.run(operationWithImage, in.getAbsolutePath(), out.getAbsolutePath());
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

	void test(final MediaType inputContentType, final String id) throws IOException
	{
		final String name = MediaImageMagickFilter.class.getName() + '_' + id + "_test";
		final File  in = createTempFile(name + "_inp_", ".data");
		final File out = createTempFile(name + "_out_", outputContentType(inputContentType).getDefaultExtension());

		final String inputContentTypeName = inputContentType.getName();
		final int size;
		if(MediaType.JPEG.equals(inputContentTypeName))
			size = 1578;
		else if(MediaType.PNG.equals(inputContentTypeName))
			size = 5526;
		else if(MediaType.GIF.equals(inputContentTypeName))
			size = 2982;
		else if(MediaType.PDF.equals(inputContentTypeName))
			size = 10607;
		else if("image/svg+xml".equals(inputContentTypeName))
			size = 1934;
		else
			throw new RuntimeException(inputContentTypeName);

		final byte[] b = new byte[size+2];
		int transferredLength = 0;
		{
			final InputStream inStream = MediaImageMagickFilter.class.getResourceAsStream("MediaImageMagickFilter-test" + inputContentType.getDefaultExtension());
			try
			{
				final FileOutputStream outStream = new FileOutputStream(in);
				try
				{
					for(int len = inStream.read(b); len>=0; len = inStream.read(b))
					{
						transferredLength += len;
						outStream.write(b, 0, len);
					}
				}
				finally
				{
					outStream.close();
				}

			}
			finally
			{
				inStream.close();
			}
		}
		if(transferredLength!=size) // size of the file
			throw new RuntimeException(String.valueOf(transferredLength));

		execute(in, out);

		delete(in);
		delete(out);
	}

	String getScript()
	{
		final ConvertCmd cmd = new ConvertCmd();
		final StringWriter string = new StringWriter();
		final PrintWriter pw = new PrintWriter(string);
		cmd.createScript(pw, operationWithImage, new Properties());
		pw.flush();
		return string.toString();
	}
}
