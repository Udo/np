import com.fastcgi.*;

import java.io.*;
import java.text.NumberFormat;

import np.*; 

class npfcgi
{ 
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

	public static void handleRequest(FCGIRequest req)
	{
		try
		{
			Interpreter interp = new Interpreter(req);
			Interpreter.instance = interp;
			interp.load(System.getProperty("SCRIPT_FILENAME"));

			System.out.println(interp.response.headers.get("content-type"));
			System.out.println("");
 
			System.out.println(interp.output.toString());

			//if(interp.fatalError != null)
			//	  System.out.println("\nFatal interpreter error: "+interp.fatalError.toString());
			
		    if(interp.response.config.get("debugDump") != null && interp.response.config.get("debugDump").equals("true"))
		    {
		    	System.out.println("<!--");
		    	System.out.println(interp.response.config.get("debugDump"));
				System.out.println("Memory usage: "+((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024) + "kB");
				if(interp.response.config.get("debugTree") != null && interp.response.config.get("debugTree").equals("true"))
				{
					System.out.println("---");
					System.out.println(interp.tree.showTree());
				}
			    System.out.println("---");
				System.out.println("debug trace: " + interp.debugTrace.toString());
			    System.out.println("---");
				//System.out.println("root object: " + interp.rootObject.members.toString());
		    	System.out.println("-->");
		    }
		}
		catch (Exception e)
		{
			System.out.println("Content-type: text/plain");
			System.out.println("");
			System.out.println(Interpreter.instance.output.toString());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}		
	}
	
	public static void main(String args[])
	{

		FCGIInterface intf = new FCGIInterface();
		while (intf.FCGIaccept() >= 0)
		{
			handleRequest(intf.request);
		}
	}

}
