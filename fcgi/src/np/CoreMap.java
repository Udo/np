package np;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import np.Interpreter.InterpreterException;

public class CoreMap extends CoreObject {

	public HashMap<String, CoreObject> items = new HashMap<String, CoreObject>();

	public CoreMap() throws InterpreterException
	{
		value = items;
		outer = getOuterCore();
	}
	
	public CoreMap(Properties p) throws InterpreterException
	{
		value = items;
		outer = getOuterCore();
		Iterator<Object> i = p.keySet().iterator();
	    while(i.hasNext())
	    {
	      Object ko = i.next();
	      if(ko.getClass() == String.class)
	      {
	        Object so = p.getProperty((String) ko);
	        if(so.getClass() == String.class)
	          items.put((String) ko, new CoreString((String) so));
	      }
	    }
	}

	public CoreObject init() throws InterpreterException
	{
		CoreObject ir = new CoreObject();
		// todo add core class methods
		ir.members.put("item", new CoreBuiltin("xItem", this));
		return ir;
	}
	
	public CoreObject xItem(CoreCall cc) throws InterpreterException { return item(cc.argPop().toString()); }
	
	public CoreObject xAdd(CoreCall cc) throws InterpreterException	{ return add(cc.argPop()); }

	public CoreObject item(String key)
	{
		CoreObject result = items.get(key);
		if(result == null)
			result = new CoreObject();
		return result;
	}
	
	public CoreObject add(CoreObject o)
	{
		items.put(o.name, o);
		return o;
	}
	
	public CoreObject addWithKey(String s, CoreObject o)
	{
		items.put(s, o);
		return o;
	}
	
	public String toString()
	{
		return items.toString();
	}
	
}
