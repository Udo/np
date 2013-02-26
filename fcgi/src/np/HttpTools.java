package np;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import np.Interpreter.InterpreterException;

public class HttpTools
{
	/*public static RTObjectList getQueryParameters(RTObjectList commonList, String query, Interpreter itp) throws InterpreterException  
	{  
		RTObjectList result = new RTObjectList();

		query = query.replaceAll("\\+", "%2B");
		
		List<NameValuePair> postPairs = URLEncodedUtils.parse(query, Charset.forName("UTF-8"));
		for(int i = 0; i < postPairs.size(); i++)
		{
			NameValuePair nv = postPairs.get(i);
			RTObject paramValue = RTObjectFactory.newObject(nv.getValue(), itp);
			result.addWithKey(nv.getName(), paramValue);
			commonList.addWithKey(nv.getName(), paramValue);
		}
		
	    return result;  
	}  
	
	public static RTObjectList getPostParameters(RTObjectList commonList, String rawInput, Interpreter itp) throws InterpreterException  
	{  
		RTObjectList result = new RTObjectList();

		List<NameValuePair> postPairs = URLEncodedUtils.parse(rawInput, Charset.forName("UTF-8"));
		for(int i = 0; i < postPairs.size(); i++)
		{
			NameValuePair nv = postPairs.get(i);
			RTObject paramValue = RTObjectFactory.newObject(nv.getValue(), itp);
			result.addWithKey(nv.getName(), paramValue);
			commonList.addWithKey(nv.getName(), paramValue);
		}
		
	    return result;  
	}  */
}
