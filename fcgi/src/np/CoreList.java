package np;

import java.util.ArrayList;

import np.Interpreter.InterpreterException;

public class CoreList extends CoreObject
{
	
	public ArrayList<CoreObject> items = new ArrayList<CoreObject>();

	public CoreList() throws InterpreterException
	{
		value = items;
		outer = getOuterCore();
	}

	public CoreObject init() throws InterpreterException
	{
		CoreObject ir = new CoreObject();
		// todo add core class methods
		ir.members.put("item", new CoreBuiltin("xItem", this));
		return ir;
	}
	
	public CoreObject xItem(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(1);
	}
	
	public CoreObject add(CoreObject ni)
	{
		items.add(ni);
		return ni;
	}
	
	public CoreObject add(int atIndex, CoreObject ni)
	{
		try
		{
			items.set(atIndex, ni);
		}
		catch (Exception e)
		{
			items.add(ni);
		}
		return ni;
	}
	
	public int count()
	{
		return items.size();
	}
	
	public void clear()
	{
		items.clear();
	}
	
	private CoreObject itemAccess(int idx, boolean del)
	{
		CoreObject result;
		try
		{
			result = items.get(idx);
			items.remove(idx);
		}
		catch (Exception e)
		{
			result = null;
		}
		if(result == null)
			result = new CoreObject();
		return result;
	}
	
	public CoreObject item(int idx)
	{
		return itemAccess(idx, false);
	}
	
	public CoreObject popFirst()
	{
		CoreObject result = itemAccess(0, true);		
		return result;
	}
	
	public CoreObject popLast()
	{
		CoreObject result = itemAccess(items.size()-1, true);		
		return result;
	}
	
	public CoreObject removeByIdx(int idx)
	{
		return itemAccess(idx, true);
	}
	
	public CoreObject removeByObject(CoreObject o)
	{
		try
		{
			items.remove(o);
		}
		catch (Exception e) { }
		return o;
	}
	
	public String getType()
	{
		return("List");
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
        sb.append("(list ");

        for(int i = 0; i < items.size(); i++)
        {
                if(i > 0) sb.append(" ");
                CoreObject itemObject = items.get(i);
                sb.append("'"+itemObject.toString()+"'");
        }

        sb.append(")");
        return sb.toString();
	}

}
