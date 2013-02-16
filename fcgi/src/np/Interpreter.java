package np;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

public class Interpreter
{
	public static Interpreter currentInstance = null;
	
	public StringBuilder output = new StringBuilder();
	public StringBuilder debugTrace = new StringBuilder();

	public RTErrorMessage fatalError = null;
	public RTObject rootObject = null;
	public LibNP builtin = new LibNP();
	
	public com.fastcgi.Response response = new com.fastcgi.Response();
	public Properties srvEnv = null;
	
	public static class InterpreterException extends Throwable {
        private static final long serialVersionUID = 2840895767933757339L;
        public String msg;
        public Token token;
        InterpreterException(String m, Token t)
        {
        	msg = m;
        	token = t;
        }
	}
	
	public String getInternalStackTrace(Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	public RTObject evaluateTree(ClastNode node, RTObject objectContext, CallContext cc) throws InterpreterException
	{
		RTObject lastValue = null;
		while(node != null && cc.returnValue == null)
		{
			lastValue = node.evaluate(this, objectContext, cc);
			node = node.next;
		}
		if(cc.returnValue != null)
			return cc.returnValue;
		else if(lastValue != null)
			return lastValue;
		else
			return new RTObject();
	}
	
	public void run(TreeItem rootNode)
	{
		try
		{
			rootObject = new RTObject();
			rootObject.addLocal("request", new CoreClassRequest(this));
			rootObject.addLocal("List", new CoreClassList(this));
			ClastNode clastRoot = ClastFactory.makeClastFromTree(rootNode);
			evaluateTree(clastRoot, rootObject, new CallContext());
		}
		catch (InterpreterException e)
		{
			fatalError = new RTErrorMessage(e.msg, e.token);
		}
	}
	
	public Interpreter() 
	{
		// to-do: this has to go *sigh*
		Interpreter.currentInstance = this;
	}
	
}
