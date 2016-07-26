package edu.ucsb.nceas.mdqengine.dispatch;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import edu.ucsb.nceas.mdqengine.model.Result;

public class JavaScriptDispatcherTest {
	
	private Dispatcher dispatcher = null;
	
	private String dataUrl = "https://cn.dataone.org/cn/v2/resolve/doi:10.5063/AA/wolkovich.29.1";

	
	@Before
	public void init() {
		dispatcher = Dispatcher.getDispatcher("JavaScript");
	}
	
	@Test
	public void testEquality() {
		Map<String, Object> names = new HashMap<String, Object>();
		names.put("x", 2);
		names.put("y", 2);
		String code = "x == y";
		Result result = null;
		try {
			result = dispatcher.dispatch(names, code);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertEquals("true", result.getValue());
	}
	
	@Test
	public void testMethodReturn() {
		Map<String, Object> names = new HashMap<String, Object>();
		names.put("x", 2);
		names.put("y", 2);
		String code = 
				"function call() { return (x == y) }";
		Result result = null;
		try {
			result = dispatcher.dispatch(names, code);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertEquals("true", result.getValue());
	}
	
	@Test
	public void testCache() {
		Map<String, Object> names = new HashMap<String, Object>();
		names.put("x", 2);
		names.put("y", 2);
		InputStream library = this.getClass().getResourceAsStream("/code/mdq-cache.js");
		
		String code = "get('" + dataUrl + "')";
		
		Result result = null;
		try {		
			code = IOUtils.toString(library, "UTF-8") + code;
			result = dispatcher.dispatch(names, code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
		// make sure the file is named as expected
		assertTrue(result.getValue().endsWith(DigestUtils.md5Hex(dataUrl)));
	}
}
