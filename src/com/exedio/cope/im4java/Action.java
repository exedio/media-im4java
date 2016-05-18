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
import static java.util.Objects.requireNonNull;

import com.exedio.cope.pattern.MediaType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
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
		requireNonNull(operation, "operation");

		this.operationWithImage = limit(new IMOperation());
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

	/**
	 * See http://imagemagick.org/script/command-line-options.php#limit
	 * Reduces time and CPU cycles needed by convert.
	 * Replaces MAGICK_THREAD_LIMIT=1 in a more robust way.
	 */
	@SuppressFBWarnings("BC_UNCONFIRMED_CAST_OF_RETURN_VALUE")
	private static IMOperation limit(final IMOperation op)
	{
		return (IMOperation)op.limit("thread").addRawArgs("1");
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

		final int size = sizeOfTestDummy(inputContentType);
		final byte[] b = new byte[size+2];
		int transferredLength = 0;
		try(
			InputStream inStream = MediaImageMagickFilter.class.getResourceAsStream("MediaImageMagickFilter-test" + inputContentType.getDefaultExtension());
			FileOutputStream outStream = new FileOutputStream(in))
		{
			for(int len = inStream.read(b); len>=0; len = inStream.read(b))
			{
				transferredLength += len;
				outStream.write(b, 0, len);
			}
		}
		if(transferredLength!=size) // size of the file
			throw new RuntimeException(String.valueOf(transferredLength));

		execute(in, out);

		delete(in);
		delete(out);
	}

	private static int sizeOfTestDummy(final MediaType inputContentType)
	{
		switch(inputContentType.getName())
		{
			case MediaType.JPEG: return 1578;
			case MediaType.PNG : return 5526;
			case MediaType.GIF : return 2982;
			case MediaType.PDF : return 10607;
			case MediaType.SVG : return 1934;
			default:
				throw new RuntimeException("" + inputContentType);
		}
	}

	List<String> getCmdArgs()
	{
		return operationWithImage.getCmdArgs();
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
