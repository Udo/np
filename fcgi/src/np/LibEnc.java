package np;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

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
		putMember("base64Encode", new CoreBuiltin("xBase64Encode", this), true);
		putMember("base64Decode", new CoreBuiltin("xBase64Decode", this), true);
		putMember("csvValueEncode", new CoreBuiltin("xCsvValueEncode", this), true);
		putMember("csvValueDecode", new CoreBuiltin("xCsvValueDecode", this), true);
		putMember("date", new CoreBuiltin("xDate", this), true);
		putMember("htmlEncode", new CoreBuiltin("xHtmlEncode", this), true);
		putMember("htmlDecode", new CoreBuiltin("xHtmlDecode", this), true);
		putMember("jsonEncode", new CoreBuiltin("xJsonEncode", this), true);
		putMember("jsonDecode", new CoreBuiltin("xJsonDecode", this), true);
		putMember("jsonValue", new CoreBuiltin("xJsonValue", this), true);
		putMember("markdownToHtml", new CoreBuiltin("xMarkdownToHtml", this), true);
		putMember("md5", new CoreBuiltin("xMd5", this), true);
		putMember("sha1", new CoreBuiltin("xSha1", this), true);
		putMember("uuid", new CoreBuiltin("xUuid", this), true);
		// putMember("numberParse", new CoreBuiltin("xParseNumber", this), true);
		putMember("numberFormat", new CoreBuiltin("xFormatNumber", this), true);
		putMember("nl2br", new CoreBuiltin("xNl2br", this), true);
		putMember("random", new CoreBuiltin("xRandom", this), true);
		putMember("scriptEncode", new CoreBuiltin("xScriptEncode", this), true);
		putMember("scriptDecode", new CoreBuiltin("xScriptDecode", this), true);
		putMember("urlDecode", new CoreBuiltin("xUrlDecode", this), true);
		putMember("urlEncode", new CoreBuiltin("xUrlEncode", this), true);
		putMember("urlMapDecode", new CoreBuiltin("xUrlMapDecode", this), true);
		putMember("xmlEncode", new CoreBuiltin("xXmlEncode", this), true);
		putMember("xmlDecode", new CoreBuiltin("xXmlDecode", this), true);
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

	private CoreObject xNumberFormatOrParse(CoreCall cc, boolean parse) throws InterpreterException 
	{
		int decimals = Integer.parseInt(cc.getMemberDefaultString("decimals", "2"));
		int padTo = Integer.parseInt(cc.getMemberDefaultString("padTo", "0"));
		String decPoint = cc.getMemberDefaultString("point", ".");
		String thousandSep = cc.getMemberDefaultString("thousands", ",");
		String padWith = cc.getMemberDefaultString("padWith", " ");
		String decimalString = "";
		if(decimals > 0)
		{
			decimalString = ".";
			for(int i = 0; i < decimals; i++) decimalString = decimalString + "0";
		}
		
		DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,##0"+decimalString+";-###,###,###,###,###,###,###,##0"+decimalString);

		if(parse)
		{
			CoreNumber result = new CoreNumber(0);
			try
            {
				// this doesn't work at all, so it's not publicly exposed right now
	            result.value = df.parse(cc.argPop().toString()).doubleValue();
            }
            catch (java.text.ParseException e) 
            { 
            	result.putMember("error", new CoreBoolean(true), true);
            }
			return result;
		}
		else
		{
			String result = df.format(cc.argPop().toDouble());
			result = result.replaceAll("\\.", decPoint).replaceAll("\\,", thousandSep);

			while(padTo > 0 && result.length() < padTo && padWith.length() > 0)
				result = padWith + result;
			
			return new CoreString(result);
		}
	}
    
    public CoreObject xDate(CoreCall cc) throws InterpreterException 
    {
    	CoreObject ts = cc.argPop();
    	long timeStamp = ts.toDouble().longValue()*1000;
    	/*
    	 * assume unix timestamp as default (in seconds)
    	 */
    	//if(ts.members.get("granularity") != null)
    	//	timeStamp = timeStamp * ts.members.get("granularity").toDouble().longValue();
    	//else
    	//	timeStamp = timeStamp * 1000;

    	if(timeStamp == 0) timeStamp = System.currentTimeMillis();
    	
    	String format = cc.getMemberDefaultString("format", "yyyy-MM-dd HH:mm:ss");
    	String timezone = cc.getMemberDefaultString("timezone", "GMT");
    	DateFormat df = new SimpleDateFormat(format);
    	df.setTimeZone(TimeZone.getTimeZone(timezone));
    	try
    	{
    		return new CoreString(df.format(new Date(timeStamp)));
    	}
    	catch (Exception e)
    	{
    		return new CoreString("(date) failed: "+e.toString());
    	}
    	
    }
	
	public CoreObject xFormatNumber(CoreCall cc) throws InterpreterException 
    {
    	return xNumberFormatOrParse(cc, false);
    }
    
    public CoreObject xParseNumber(CoreCall cc) throws InterpreterException 
    {
    	return xNumberFormatOrParse(cc, true);
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

	private CoreObject xHash(CoreCall cc, String algo) throws InterpreterException 
	{
		byte[] result;
		try
        {
	        MessageDigest md = MessageDigest.getInstance(algo);
	        result = md.digest(cc.argPop().toString().getBytes());
        }
        catch (NoSuchAlgorithmException e)
        {
	        throw new InterpreterException(e.toString(), cc.firstArgNode.token);
        }
		
		String resultString;
		
		String format = cc.getMemberDefaultString("format", "hex");
		if(format.equals("numeric"))
			resultString = new BigInteger(1, result).toString();
		else if(format.equals("binary"))
			resultString = new String(result);
		else
			resultString = new BigInteger(1, result).toString(16);
		
		return new CoreString(resultString);
	}

	public CoreObject xMd5(CoreCall cc) throws InterpreterException 
	{
		return xHash(cc, "MD5");
	}
	
	public CoreObject xSha1(CoreCall cc) throws InterpreterException 
	{
		return xHash(cc, "SHA-1");
	}
	
	public CoreObject xUuid(CoreCall cc) throws InterpreterException 
	{
		return new CoreString(UUID.randomUUID().toString());
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
	
	public CoreObject xCsvValueEncode(CoreCall cc) throws InterpreterException 
	{ 
		return new CoreString(StringEscapeUtils.escapeCsv(cc.argPop().toString()));
	}
	
	public CoreObject xCsvValueDecode(CoreCall cc) throws InterpreterException 
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
