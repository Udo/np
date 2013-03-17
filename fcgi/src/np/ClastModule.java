package np;

import np.Interpreter.InterpreterException;

public class ClastModule extends ClastNode
{

	public long fileMTime = 0;
	
	public ClastModule(Token t)
    {
	    super(t);
    }
	
	/*
	 * the run method of a module does little, except handing flow
	 * over to the first expression contained within itself. Of 
	 * course, a typical program contains a long list of expressions,
	 * so we need to loop through them.
	 * @see np.ClastNode#run()
	 */
	public CoreObject run(CoreObject objectContext, CoreObject lookupContainer) throws InterpreterException
	{
		Interpreter.instance.debugTrace.append("module context "+objectContext+"\n");
		CoreObject result = null;
		ClastNode current = child;
		while(current != null)
		{
			result = current.run(objectContext, null);
			current = current.next;
		}
		return result;
	}
	
}
