package np;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import com.fastcgi.FCGIRequest;

/*
 * the interpreter is the heart of np, it bundles together the entire
 * runtime. it's responsible for handing raw code off to the Lexer and
 * then to the Parser, before executing the resulting classed AST tree.
 */
public class Interpreter
{
	public static Interpreter instance = null;
	
	public Lexer lexer = new Lexer();
	public Parser tree = new Parser();
	public FCGIRequest req;
	public com.fastcgi.Response response = new com.fastcgi.Response();
	
	public StringBuilder output = new StringBuilder();
	public StringBuilder debugTrace = new StringBuilder();
	public RTErrorMessage fatalError = null;
	
	public HashMap<CoreObject, AssignmentTag> assignmentList = new HashMap<CoreObject, AssignmentTag>();
	public boolean assignmentMode = false;
	
	public Interpreter(FCGIRequest r) 
	{
		// to-do: this has to go *sigh*
		Interpreter.instance = this;
		req = r;
	}
	
	public void beginAssignmentMode()
	{
		assignmentMode = true;
	}
	
	public AssignmentTag endAssignmentMode(CoreObject o)
	{
		AssignmentTag result = assignmentList.get(o);
		assignmentList.clear();
		assignmentMode = false;
		return result;
	}
	
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

	public void run(ClastNode node) throws InterpreterException
	{
		new LibNP();
		node.run(new CoreCall(new CoreObject(), new CoreObject(), null));
	}
	
	public void load(String fileName)
	{
		try
		{
			ClastNode cnode = ClastFactory.getClastFromCache(fileName);
			if(cnode == null)
			{
				RTFile srcFile = new RTFile(fileName);
				if(srcFile.error != null)
				{
					output.append(srcFile.error+"\n");
					req.appStatus = 404;
					return;
				}		
				lexer.readText(srcFile.content);
				tree.parse(lexer.tokens, fileName);
				cnode = ClastFactory.makeClastFromTree(tree.root, srcFile);
			}
			run(cnode);
		}
		catch (InterpreterException e)
		{
			fatalError = new RTErrorMessage(e.msg, e.token);
			output.append("Error: "+fatalError);
		}
	}
	
	public void eval(String src)
	{
		try
		{
			lexer.readText(src);
			tree.parse(lexer.tokens, "(eval)");
			ClastNode cnode = ClastFactory.makeClastFromTree(tree.root, null);
			run(cnode);
		}
		catch (InterpreterException e)
		{
			fatalError = new RTErrorMessage(e.msg, e.token);
			output.append("Error: "+fatalError);
		}
	}

}
