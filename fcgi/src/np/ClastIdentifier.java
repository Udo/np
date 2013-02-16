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
	
	public RTObject evaluate(Interpreter itp, RTObject objectContext, CallContext cc) throws InterpreterException
	{
		Method builtinMethod = itp.builtin.resolveMethod(content);
		if(builtinMethod != null)
		{
			//itp.debugTrace.append("identifier built-in (" + content + ") \n");
			return new RTObject(builtinMethod, itp.builtin);
		}
		else
		{
			//itp.debugTrace.append("identifier resolve (" + content + ") \n");
			RTObject result = objectContext.resolve(content);
			if(result != null)
				return result;
			else 
				return new RTObject();
		}
	}

	
}
