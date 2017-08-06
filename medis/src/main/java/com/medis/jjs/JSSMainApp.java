package com.medis.jjs;

import java.io.FileReader;
import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSSMainApp 
{
	public static void main(String[] args) throws ScriptException, IOException {
		//load JS engine
		ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
		//load REST functions
		try(FileReader fr = new FileReader(JSSMainApp.class.getResource("rest.js").getFile() ) ){
			scriptEngine.eval(fr);
			
			/*You need to have rest service with Content-Type application/json */
			scriptEngine.eval("var restResponse =REST.getAsString('http://ip.jsontest.com');");
			scriptEngine.eval("print(restResponse);");
		}
	}
}