import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Security;

import np.CoreCall;
import np.CoreObject;
import np.Interpreter;
import np.Interpreter.InterpreterException;

import com.fastcgi.FCGIInterface;


public class repl
{
	public static void main(String args[]) throws IOException, InterpreterException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		CoreObject rootCtx = new CoreObject();
		while(true)
		{
			Interpreter interp = new Interpreter(null);
			interp.rootContext = rootCtx;
			Interpreter.instance = interp;
			try
            {
				System.out.print("np:> ");
				String input = br.readLine();
	            CoreObject res = interp.eval(input);
	            System.out.println(res.members.get("output").toString());
	            System.out.println(interp.rootCall.members.toString());
            }
            catch (InterpreterException e)
            {
	            // TODO Auto-generated catch block
            	System.out.println(Interpreter.instance.output.toString());
            }
			
		}
	}
}
