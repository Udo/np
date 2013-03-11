package np;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.markdown4j.Markdown4jProcessor;

import np.Interpreter.InterpreterException;

public class LibEnc extends CoreObject
{

	public LibEnc() throws InterpreterException
	{
		putMember("htmlEncode", new CoreBuiltin("xHtmlEncode", this), true);
		putMember("htmlDecode", new CoreBuiltin("xHtmlDecode", this), true);
		putMember("csvEncode", new CoreBuiltin("xCsvEncode", this), true);
		putMember("csvDecode", new CoreBuiltin("xCsvDecode", this), true);
		putMember("scriptEncode", new CoreBuiltin("xScriptEncode", this), true);
		putMember("scriptDecode", new CoreBuiltin("xScriptDecode", this), true);
		putMember("xmlEncode", new CoreBuiltin("xXmlEncode", this), true);
		putMember("xmlDecode", new CoreBuiltin("xXmlDecode", this), true);
		putMember("random", new CoreBuiltin("xRandom", this), true);
		putMember("urlMapDecode", new CoreBuiltin("xUrlMapDecode", this), true);
		putMember("urlEncode", new CoreBuiltin("xUrlEncode", this), true);
		putMember("urlDecode", new CoreBuiltin("xUrlDecode", this), true);
		putMember("base64Encode", new CoreBuiltin("xBase64Encode", this), true);
		putMember("base64Decode", new CoreBuiltin("xBase64Decode", this), true);
		putMember("nl2br", new CoreBuiltin("xNl2br", this), true);
		putMember("markdownToHtml", new CoreBuiltin("xMarkdownToHtml", this), true);
		putMember("jsonValue", new CoreBuiltin("xJsonValue", this), true);
		putMember("jsonEncode", new CoreBuiltin("xJsonEncode", this), true);
		putMember("jsonDecode", new CoreBuiltin("xJsonDecode", this), true);
	}
	
    private Object jsonRecursiveEncode(CoreObject co)
	{
		if(co.getClass() == CoreMap.class)
		{
			CoreMap cm = (CoreMap) co;
			HashMap<String, Object> result = new HashMap<String, Object>();
			Iterator<String> i = cm.items.keySet().iterator();
			while(i.hasNext())
			{
				String key = i.next();
				result.put(key, jsonRecursiveEncode(cm.items.get(key)));
			}
			return result;
		}
		else if(co.getClass() == CoreList.class)
		{
			CoreList cl = (CoreList) co;
			LinkedList<Object> result = new LinkedList<Object>();
			int itemCount = cl.items.size();
			for(int i = 0; i < itemCount; i++)
			{
				result.add(jsonRecursiveEncode(cl.items.get(i)));
			}
			return result;
		}
		else
		{
			return co.value;
		}
	}
	
    private CoreObject jsonRecursiveDecode(Object o) throws InterpreterException
    {
    	return new CoreString("not yet implemented");
    }
    
	public CoreObject xJsonDecode(CoreCall cc) throws InterpreterException 
	{
		JSONParser parser=new JSONParser();
		Object obj;
        try
        {
	        obj = parser.parse(cc.argPop().toString());
        }
        catch (ParseException e)
        {
        	return new CoreObject();
        }
		
		return jsonRecursiveDecode(obj);
	}
	
	public CoreObject xJsonEncode(CoreCall cc) throws InterpreterException 
	{
		Object js = jsonRecursiveEncode(cc.argPop());
		return new CoreString(JSONValue.toJSONString(js));
	}
	
	public CoreObject xJsonValue(CoreCall cc) throws InterpreterException 
	{
		return new CoreString(JSONObject.escape(cc.argPop().toString()));
	}

	public CoreObject xMarkdownToHtml(CoreCall cc) throws InterpreterException 
	{
		Markdown4jProcessor mdp = new Markdown4jProcessor();
		try
        {
	        return new CoreString(mdp.process(cc.argPop().toString()));
        }
        catch (IOException e)
        {
	        return new CoreString();
        }
	}
	
	public CoreObject xNl2br(CoreCall cc) throws InterpreterException 
	{
		return new CoreString(cc.argPop().toString().replaceAll("\n", "<br/>"));
	}
	
	public CoreObject xBase64Encode(CoreCall cc) throws InterpreterException 
	{
		return new CoreString(Base64.encodeBase64String(cc.argPop().toString().getBytes()));
	}
	
	public CoreObject xBase64Decode(CoreCall cc) throws InterpreterException 
	{
		return new CoreString(new String(Base64.decodeBase64(cc.argPop().toString())));
	}
	
	public CoreObject xUrlDecode(CoreCall cc) throws InterpreterException 
	{
		try
        {
	        return new CoreString(URLDecoder.decode(cc.argPop().toString(), "UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
	        return new CoreString();
        }
	}
	
	public CoreObject xUrlEncode(CoreCall cc) throws InterpreterException 
	{
		try
        {
	        return new CoreString(URLEncoder.encode(cc.argPop().toString(), "UTF-8").replaceAll("\\+", "%20"));
        }
        catch (UnsupportedEncodingException e)
        {
	        return new CoreString();
        }
	}
	
	public CoreObject xUrlMapDecode(CoreCall cc) throws InterpreterException 
	{
		CoreMap result = new CoreMap();
		List<NameValuePair> postPairs = URLEncodedUtils.parse(cc.argPop().toString(), Charset.forName("UTF-8"));
		for(int i = 0; i < postPairs.size(); i++)
		{
			NameValuePair nv = postPairs.get(i);
			result.items.put(nv.getName(), new CoreString(nv.getValue()));
		}
		return result;
	}
	
	public CoreObject xRandom(CoreCall cc) throws InterpreterException 
	{ 
		String rType = cc.getMemberDefaultString("type", "alphanumeric");
		if(rType.equals("alpha"))
			return new CoreString(RandomStringUtils.randomAlphabetic(cc.argPop().toDouble().intValue()));
		if(rType.equals("numeric"))
			return new CoreString(RandomStringUtils.randomNumeric(cc.argPop().toDouble().intValue()));
		if(rType.equals("ascii"))
			return new CoreString(RandomStringUtils.randomAscii(cc.argPop().toDouble().intValue()));
		return new CoreString(RandomStringUtils.randomAlphanumeric(cc.argPop().toDouble().intValue()));
	}
	
	public CoreObject xHtmlEncode(CoreCall cc) throws InterpreterException 
	{ 
		return new CoreString(StringEscapeUtils.escapeHtml4(cc.argPop().toString()));
	}
	
	public CoreObject xHtmlDecode(CoreCall cc) throws InterpreterException 
	{ 
		return new CoreString(StringEscapeUtils.unescapeHtml4(cc.argPop().toString()));
	}
	
	public CoreObject xXmlEncode(CoreCall cc) throws InterpreterException 
	{ 
		return new CoreString(StringEscapeUtils.escapeXml(cc.argPop().toString()));
	}
	
	public CoreObject xXmlDecode(CoreCall cc) throws InterpreterException 
	{ 
		return new CoreString(StringEscapeUtils.unescapeXml(cc.argPop().toString()));
	}
	
	public CoreObject xCsvEncode(CoreCall cc) throws InterpreterException 
	{ 
		return new CoreString(StringEscapeUtils.escapeCsv(cc.argPop().toString()));
	}
	
	public CoreObject xCsvDecode(CoreCall cc) throws InterpreterException 
	{ 
		return new CoreString(StringEscapeUtils.unescapeCsv(cc.argPop().toString()));
	}
	
	public CoreObject xScriptEncode(CoreCall cc) throws InterpreterException 
	{ 
		return new CoreString(StringEscapeUtils.escapeEcmaScript(cc.argPop().toString()));
	}
	
	public CoreObject xScriptDecode(CoreCall cc) throws InterpreterException 
	{ 
		return new CoreString(StringEscapeUtils.unescapeEcmaScript(cc.argPop().toString()));
	}
	
	
	
}
