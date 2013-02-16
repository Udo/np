import com.fastcgi.*;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.text.NumberFormat;

import np.*;

class npfcgi
{
	private static String readFile(String path)
	{
		try
		{
			FileInputStream stream = new FileInputStream(new File(path));
			try
			{
				FileChannel fc = stream.getChannel();
				MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
				/* Instead of using default, pass in a decoder. */
				return Charset.defaultCharset().decode(bb).toString();
			}
			finally
			{
				stream.close();
			}
		}
		catch (IOException e)
		{
			return "Caught IOException: " + e.getMessage();
		}
		finally
		{

		}
	}
	
	public static String getMemUsage()
	{
		Runtime runtime = Runtime.getRuntime();

	    NumberFormat format = NumberFormat.getInstance();

	    StringBuilder sb = new StringBuilder();
	    long maxMemory = runtime.maxMemory();
	    long allocatedMemory = runtime.totalMemory();
	    long freeMemory = runtime.freeMemory();

	    sb.append("free memory: " + format.format(freeMemory / 1024) + "\n");
	    sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "\n");
	    sb.append("max memory: " + format.format(maxMemory / 1024) + "\n");
	    sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "\n");		
	    
	    return sb.toString();
	}

	public static void main(String args[])
	{

		FCGIInterface intf = new FCGIInterface();
		while (intf.FCGIaccept() >= 0)
		{
			try
			{
				long requestStartTime = System.nanoTime();
				
				Lexer lexer = new Lexer();
				Parser tree = new Parser();
				Interpreter interp = new Interpreter();

				String srcFile = readFile(System.getProperty("SCRIPT_FILENAME"));
				long freadTime = System.nanoTime();

				lexer.readText(srcFile);
				long lexTime = System.nanoTime();
				
				//parser.parseTokens(lexer.tokens);
				tree.parse(lexer.tokens, System.getProperty("SCRIPT_FILENAME"));
				long parseTime = System.nanoTime();
				
				interp.srvEnv = FCGIInterface.request.params;
				interp.run(tree.root);
				long interTime = System.nanoTime();
				
				System.out.println(interp.response.headers.get("content-type"));
				System.out.println("");

				System.out.println(interp.output.toString());

			    if(tree.fatalError != null)
					  System.out.println("\nFatal parse error: "+tree.fatalError.toString());
				else if(interp.fatalError != null)
					  System.out.println("\nFatal interpreter error: "+interp.fatalError.toString());
				
			    if(interp.response.config.get("debugDump") != null && interp.response.config.get("debugDump").equals("true"))
			    {
			    	System.out.println("<!--");
			    	System.out.println(interp.response.config.get("debugDump"));
					System.out.println("File read time: "+((freadTime-requestStartTime)/1000000)+" ms");
					System.out.println("Parser tokens count: "+lexer.tokens.size());
					System.out.println("Lexer time: "+((lexTime-freadTime)/1000000)+" ms");
					System.out.println("Parser time: "+((parseTime-lexTime)/1000000)+" ms");
					System.out.println("Interpreter time: "+((interTime-lexTime)/1000000)+" ms");
					System.out.println("Memory usage: "+((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024) + "kB");
				    System.out.println("---");
					System.out.println(tree.showTree());
				    System.out.println("---");
					System.out.println("debug trace: " + interp.debugTrace.toString());
				    System.out.println("---");
					System.out.println("root object: " + interp.rootObject.members.toString());
			    	System.out.println("-->");
			    }
			}
			catch (Exception e)
			{
				System.out.println("Content-type: text/plain");
				System.out.println("");
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				System.out.println(sw.toString());
			}
		}
	}

}
