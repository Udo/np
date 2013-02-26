package np;

import java.lang.reflect.Method;

import np.Interpreter.InterpreterException;

public class ClastIdentifier extends ClastNode
{

	public ClastIdentifier(Token t)
    {
	    super(t);
	    // TODO Auto-generated constructor stub
    }
	
	/*
	 * identifier objects are interesting. they point to an
	 * object that must be visible within the current call
	 * context and those often need to be resolved deeper into
	 * the tree.
	 * @see np.ClastNode#run()
	 */
	public CoreObject run(CoreObject objectContext) throws InterpreterException
	{
		/*
		 * first step, look this identifier up within the current environment
		 */
		CoreObject result = objectContext.getMember(token.value);
		if(result != null)
			return result;

		result = CoreBuiltin.lookUp(token.value);
		if(result != null)
			return result;
		
		result = new CoreObject();

		if(Interpreter.instance.assignmentMode)
			new AssignmentTag(result, objectContext, token.value);

		return result;
	}
	
}
