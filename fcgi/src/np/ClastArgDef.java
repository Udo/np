package np;

import np.Interpreter.InterpreterException;

public class ClastArgDef extends ClastNode
{

	public ClastArgDef(Token t)
    {
	    super(t);
    }
	
	/*
	 * evaluates the argument definition header of a function. pretty straight-forward
	 * todo: mechanism for declaring named variables
	 * @see np.ClastNode#run(np.CoreObject)
	 */
	public CoreObject run(CoreObject obj) throws InterpreterException
	{
		ClastNode arg = this.child;
		CoreCall cc = (CoreCall) obj;
		while(arg != null)
		{
			if(arg.getClass() != ClastIdentifier.class)
				throw new InterpreterException("identifier expected in function arguments", arg.token);
			cc.members.put(arg.token.value, cc.argPop());
			//Interpreter.instance.debugTrace.append("arg def "+arg.token.value+"="+cc.members.get(arg.token.value)+"\n");
			arg = arg.next;
		}
		return null;
	}
	
}
