package np;

import java.util.*;

public class RTObjectList
{
	public ArrayList<RTObject> items = new ArrayList<RTObject>();
	public HashMap<String, RTObject> keys = new HashMap<String, RTObject>();
	
	public RTObject add(RTObject item)
	{
		if(item.name == null)
		{
			items.add(item);
		}
		else
		{
			RTObject oldSlot = keys.get(item.name);
			if(oldSlot != null)
				items.remove(oldSlot);
			items.add(item);
			keys.put(item.name, item);
		}
		return item;
	}
	
	public RTObjectList()
	{

	}
	
	public RTObjectList(Properties p)
	{
		Iterator<Object> i = p.keySet().iterator();
		while(i.hasNext())
		{
			Object ko = i.next();
			if(ko.getClass() == String.class)
			{
				Object so = p.getProperty((String) ko);
				if(so.getClass() == String.class)
					keys.put((String) ko, new RTObject((String) so));
			}
		}
	}
	
	public String toString()
	{
		return items.toString() + "--" + keys.toString();
	}
}
