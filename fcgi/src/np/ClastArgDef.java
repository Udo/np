package np;

import np.Interpreter.InterpreterException;

public class ClastArgDef extends ClastNode
{

	public ClastArgDef(Token t)
    {
	    super(t);
	    // TODO Auto-generated constructor stub
    }
	
	public RTObject evaluate(Interpreter itp, RTObject objectContext, CallContext cc) throws InterpreterException
	{
		ClastNode current = child;
		
		while(current != null)
		{
			if(current.getClass() == ClastIdentifier.class)
			{
				// pop unnamed argument and fill it into this variable
				if(objectContext.members.get(current.token.value) == null)
				{
					RTObject argVal = cc.argPop(itp);
					//itp.debugTrace.append("call argument "+current.token.value + "=" + argVal.value.toString()+"\n");
					objectContext.addLocal(current.token.value, argVal);
				}
				else
					throw new InterpreterException("can't overwrite reserved variable '"+ current.token.value +"'", current.token);
			}
			else if(current.getClass() == ClastString.class)
			{
				// string argument
			}
			else
			{
				throw new InterpreterException("identifier expected", current.token);
			}
			
			current = current.next;
		}
		
		return new RTObject();
	}

}
