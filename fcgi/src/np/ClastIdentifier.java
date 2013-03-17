package np;

import np.Interpreter.InterpreterException;

public class ClastIdentifier extends ClastNode
{

	public ClastIdentifier(Token t)
    {
	    super(t);
	    // TODO Auto-generated constructor stub
    }

	public static CoreObject iEval(ClastNode node, String identifier, CoreObject objectContext) throws InterpreterException
	{
		CoreObject result = null;
		// resolve reserved words
		if(node.token.value.equals("true"))
			return new CoreBoolean(true);
		if(node.token.value.equals("false"))
			return new CoreBoolean(false);
		// resolve builtins
		result = CoreBuiltin.lookUp(identifier);
		if(result != null)
			return result;
		// resolve within current context (hierarchical)
		result = objectContext.getMember(identifier);
		if(result == null)
			result = new CoreObject();

		if(Interpreter.instance.assignmentMode)
			new AssignmentTag(result, objectContext, identifier);

		return result;
	}
	
	public static CoreObject iEvalLC(ClastNode node, String identifier, CoreObject objectContext) throws InterpreterException
	{
		CoreObject result = null;

		result = objectContext.getMember(identifier);

		if(result == null)
			result = new CoreObject();

		if(Interpreter.instance.assignmentMode)
			new AssignmentTag(result, objectContext, identifier);

		return result;
	}

	/*
	 * identifier objects are interesting. they point to an
	 * object that must be visible within the current call
	 * context and those often need to be resolved deeper into
	 * the tree.
	 * @see np.ClastNode#run()
	 */
	public CoreObject run(CoreObject objectContext, CoreObject lookupContainer) throws InterpreterException
	{
		if(lookupContainer == null)
			return iEval(this, token.value, objectContext);
		
		CoreObject result = iEvalLC(this, token.value, lookupContainer);
		result.putMember("container", lookupContainer, true);
		return result;
	}
	
}
