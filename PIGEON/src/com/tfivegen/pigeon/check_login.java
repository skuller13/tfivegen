package com.tfivegen.pigeon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class check_login {
	static String id = null;
	static String username = null;
	static String password = null;
	static String email = null;
	static String phone = null;
	static String error_status = null;
	static Double latitude=null;
	static Double longitude=null;
	public static void logout()
	{
		 id = null;
		 username = null;
		 password = null;
		 email = null;
		 phone = null;
		 error_status = null;
		 longitude=null;
		 latitude=null;
	}
	public static String con2json(String str)
	{		
		JSONObject jsonObj = new JSONObject();
		 	try {
		        
				jsonObj.put("name", str);
				} 
		 	catch (JSONException e) 
		 	{
					e.printStackTrace();
			} 
		         

		String result = jsonObj.toString();
		return result;
	
}
}
