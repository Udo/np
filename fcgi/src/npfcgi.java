import com.fastcgi.*;

import java.io.*;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Iterator;

import np.*; 
import np.Interpreter.InterpreterException;

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

	public static void putResponseHeaders(Interpreter itp)
	{
		if(itp.responseHeaders.items.get("content-type") == null) 
        {
			System.out.println("content-type: "+Configuration.get("content-type", "text/html; charset=utf-8"));
        }
		Iterator<String> i = itp.responseHeaders.items.keySet().iterator();
		while(i.hasNext())
		{
			String key = i.next();
			System.out.println(key+": "+itp.responseHeaders.items.get(key));
		}
		CoreList plainHeaders = (CoreList) itp.responseHeaders.members.get("flat");
		if(plainHeaders != null)
		for(int idx = 0; idx < plainHeaders.items.size(); idx++)
		{
			System.out.println(plainHeaders.item(idx));
		}
		System.out.println("");
	}
	
	@SuppressWarnings("unused")
    public static void handleRequest(FCGIRequest req)
	{
		try
		{
			Interpreter interp = new Interpreter(req);
			Interpreter.instance = interp;
			interp.load(System.getProperty("SCRIPT_FILENAME"));

			putResponseHeaders(interp);
 
			System.out.println(interp.output.toString());

			//if(interp.fatalError != null)
			//	  System.out.println("\nFatal interpreter error: "+interp.fatalError.toString());
			
		    if(false)
		    {
		    	System.out.println("<!-- ");
		    	//System.out.println(interp.lexer.tokens.toString());
				System.out.println("Memory usage: "+((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024) + "kB");
				
				System.out.println("---");
				System.out.println(interp.tree.showTree());
				
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
		if(Charset.defaultCharset().name() != "UTF-8")
			System.out.println("Warning: default encoding should be UTF-8, but "+Charset.defaultCharset().name()+" detected.");
			
		Configuration.init();
		
		FCGIInterface intf = new FCGIInterface();
		while (intf.FCGIaccept() >= 0)
		{
			handleRequest(intf.request);
		}
	}

}
