package com.fastcgi;

import java.util.HashMap;

@SuppressWarnings("serial")
public class Response
{
    public HashMap<String, String> headers = new HashMap<String, String>() {{
		put("content-type", "Content-Type: text/html; charset=utf-8");
	}};
	
    public HashMap<String, String> config = new HashMap<String, String>() {{

	}};
	
}