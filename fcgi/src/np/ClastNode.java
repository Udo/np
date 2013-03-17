package np;

import np.Interpreter.InterpreterException;

/*
 * this is the basic classed AST object of np. the actual
 * AST node types are subclasses
 */
public class ClastNode
{

	public ClastNode next = null;				// next sibling
	public ClastNode child = null;				// first child
	public Token token;							// token passthrough from lexer
		
	public ClastNode(Token t)
	{
		token = t;
	}

	/*
	 * the run method is called to actually evaluate the object at
	 * runtime. again, this is just a stub, the actual implementation
	 * is in the subclasses. the objectContext is a container for
	 * whatever individual nodes can see, and it is forked whenever
	 * an executable runtime object is created.
	 */
	public CoreObject run(CoreObject objectContext, CoreObject lookupContainer)	throws InterpreterException					
	{
		return new CoreObject();
	}
	
	public boolean isNamed()
	{
		return (token.type.equals("Exp") && child != null && child.token.type.equals("Identifier") && child.token.value.equals(":"));
	}
}
