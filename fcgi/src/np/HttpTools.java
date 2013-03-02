package np;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import np.Interpreter.InterpreterException;

public class HttpTools
{
	
	public static CoreMap getQueryParameters(CoreMap commonList, String query, Interpreter itp) throws InterpreterException  
	{  
		CoreMap result = new CoreMap();

		query = query.replaceAll("\\+", "%2B");
		
		List<NameValuePair> postPairs = URLEncodedUtils.parse(query, Charset.forName("UTF-8"));
		for(int i = 0; i < postPairs.size(); i++)
		{
			NameValuePair nv = postPairs.get(i);
			result.addWithKey(nv.getName(), new CoreString(nv.getValue()));
			commonList.addWithKey(nv.getName(), new CoreString(nv.getValue()));
		}
		
	    return result;  
	}  
	
	public static CoreMap getPostParameters(CoreMap commonList, String rawInput, Interpreter itp) throws InterpreterException  
	{  
		CoreMap result = new CoreMap();

		List<NameValuePair> postPairs = URLEncodedUtils.parse(rawInput, Charset.forName("UTF-8"));
		for(int i = 0; i < postPairs.size(); i++)
		{
			NameValuePair nv = postPairs.get(i);
			result.addWithKey(nv.getName(), new CoreString(nv.getValue()));
			commonList.addWithKey(nv.getName(), new CoreString(nv.getValue()));
		}
		
	    return result;  
	}  
	
}
