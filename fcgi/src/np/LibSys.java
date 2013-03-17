package np;

import java.util.Random;

import np.Interpreter.InterpreterException;

public class LibSys extends CoreObject
{
	
	public LibSys() throws InterpreterException
	{
		putMember("time", new CoreBuiltin("xTime", this), true);
		putMember("bench", new CoreBuiltin("xBench", this), true);
		putMember("execTime", new CoreBuiltin("xExecTime", this), true);

	}
	
	public CoreObject xExecTime(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(new Double(System.currentTimeMillis() - Interpreter.instance.execTimeStart));
	}
	
	public CoreObject xTime(CoreCall cc) throws InterpreterException
	{
		CoreNumber result;
		if(cc.getMemberDefaultString("unit", "sec").equals("msec"))
		{
			result = new CoreNumber(new Double(System.currentTimeMillis()));
			result.members.put("granularity", new CoreNumber(1000));
		}
		else
		{
			result = new CoreNumber(new Double(System.currentTimeMillis() / 1000L));
			result.members.put("granularity", new CoreNumber(1));
		}
		return result;
	}
	
	public CoreObject xBench(CoreCall cc) throws InterpreterException
	{
		long startTime = System.nanoTime();
		CoreList rl = new CoreList();
		
		for(int i = 0; i < cc.argCount; i++)
		{
			rl.add(cc.argPop());
		}

		CoreNumber result = new CoreNumber(new Double(
				((System.nanoTime() - startTime)/1000L)
				)/1000);
		result.members.put("result", rl);
		return result;
	}
	

}
