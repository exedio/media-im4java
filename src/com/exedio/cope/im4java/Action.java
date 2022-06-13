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

import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.Objects.requireNonNull;

import com.exedio.cope.pattern.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.Set;
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

		operationWithImage = limit(new IMOperation());
		operationWithImage.addOperation(operation);
		operationWithImage.addImage(2);

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
	 * See <a href="https://imagemagick.org/script/command-line-options.php#limit">Command-line Options</a>
	 * Reduces time and CPU cycles needed by convert.
	 * Replaces MAGICK_THREAD_LIMIT=1 in a more robust way.
	 */
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
				constantOutputContentType!=null
				? constantOutputContentType
				: inputContentType;
	}

	void execute(
			final Path in, final MediaType inContentType,
			final Path out, final MediaType outContentType)
	throws IOException
	{
		final ConvertCmd cmd = new ConvertCmd();
		//System.out.println("------script-----" + getScript());
		try
		{
			cmd.run(operationWithImage,
					explicitFormat( in,  inContentType),
					explicitFormat(out, outContentType));
		}
		catch(InterruptedException | IM4JavaException e)
		{
			throw new RuntimeException(
					operationWithImage + "===" +
					in .toAbsolutePath() + "===" + inContentType + "===" +
					out.toAbsolutePath(), e);
		}
	}

	/**
	 * See "Explicit Image Format" in
	 * <a href="https://www.imagemagick.org/script/command-line-processing.php">Anatomy of the Command-line</a>
	 * Improves security:
	 * <a href="https://lcamtuf.blogspot.com/2016/05/clearing-up-some-misconceptions-around.html">Clearing up some misconceptions around the "ImageTragick" bug</a>
	 * and "Other Security Considerations" in
	 * <a href="https://www.imagemagick.org/script/security-policy.php#other">Other Security Considerations</a>
	 */
	private static String explicitFormat(final Path file, final MediaType contentType)
	{
		return
				contentType.getDefaultExtension().substring(1) + // substring drops leading dot
				':' +
				file.toAbsolutePath();
	}

	void test(final MediaType inputContentType, final String id) throws IOException
	{
		final String name = MediaImageMagickFilter.class.getName() + '_' + id + "_test";
		final MediaType outputContentType = outputContentType(inputContentType);
		final Path  in = createTempFile(name + "_inp_", ".data");
		final Path out = createTempFile(name + "_out_", outputContentType.getDefaultExtension());

		final int size = sizeOfTestDummy(inputContentType);
		final byte[] b = new byte[size+2];
		int transferredLength = 0;
		try(
			InputStream inStream = requireNonNull(MediaImageMagickFilter.class.getResourceAsStream("MediaImageMagickFilter-test" + inputContentType.getDefaultExtension()), inputContentType.toString());
			OutputStream outStream = newOutputStream(in, WRITE, TRUNCATE_EXISTING))
		{
			for(int len = inStream.read(b); len>=0; len = inStream.read(b))
			{
				transferredLength += len;
				outStream.write(b, 0, len);
			}
		}
		if(transferredLength!=size) // size of the file
			throw new RuntimeException(String.valueOf(transferredLength));

		execute(in, inputContentType, out, outputContentType);

		delete(in);

		final Set<MediaType> outProbedTypes = MediaType.forMagics(out);
		if(!outProbedTypes.contains(outputContentType))
			throw new RuntimeException(
					"mismatch outProbe " + id + " ===" + inputContentType + "===" +
							outputContentType + "===" + outProbedTypes + "===");

		delete(out);
	}

	private static int sizeOfTestDummy(final MediaType inputContentType)
	{
		switch(inputContentType.getName())
		{
			case MediaType.JPEG: return 1578;
			case MediaType.PNG : return 5526;
			case MediaType.GIF : return 2982;
			case MediaType.WEBP: return 1046;
			case MediaType.TIFF: return 5506;
			case MediaType.PDF : return 10607;
			case MediaType.SVG : return 1891;
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
