package np;

import np.Interpreter.InterpreterException;

public class CoreClassRequest extends RTObject
{
	public CoreClassRequest(Interpreter itp) throws InterpreterException
	{
		super();
		addLocal("header", new RTObject(itp.builtin.pmethods.get("req_header"), itp.builtin));
		addLocal("config", new RTObject(itp.builtin.pmethods.get("req_config"), itp.builtin));
		addLocal("env", new RTObject(new RTObjectList(itp.srvEnv)));
	}
}
