package np;

import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/* 
 * runtime file object (such as a source code file)
 */
public class RTFile
{
	public String content = null;
	public String error = null;
	public String fileName = null;
	public Exception errorEx = null;
	public long mTime = 0;
	
	public void readFile(String path)
	{
		try
		{
			fileName = path;
			File fle = new File(path);
			mTime = fle.lastModified();
			FileInputStream stream = new FileInputStream(fle);
			try
			{
				FileChannel fc = stream.getChannel();
				MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
				content = Charset.defaultCharset().decode(bb).toString();
			}
			catch (Exception e)
			{
				error = e.toString();
				errorEx = e;
			}
			finally
			{
				stream.close();
			}
		}
		catch (Exception e)
		{
			error = e.toString();
			errorEx = e;
		}
	}
	
	public RTFile(String fn)
	{
		readFile(fn);
	}
}
