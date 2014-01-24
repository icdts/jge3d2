package com.jge3d.systems;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.jge3d.utils.Log;

public class InputMap implements InputProcessor{
	//private HashMap<String,String> key_map;

	final HashMap<Integer, String> lwjgl_keyboard_enums;
	final HashMap<String, String> enums_to_function;

	public InputMap() throws Exception{
		//key_map = new HashMap<String,String>();

		lwjgl_keyboard_enums = new HashMap<Integer,String>();
		enums_to_function = new HashMap<String,String>();
		
		for(Field f : Input.Keys.class.getFields()){
			try {
				lwjgl_keyboard_enums.put((Integer)f.get(null),f.getName());
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {}
		}
	}

	void parseKeyboardSettings(ArrayList<Node> key_settings) throws Exception{
		for(Node n : key_settings){
			String id = ((Element) n).getAttribute("ID");
			String event = ((Element) n).getAttribute("EVENT");
			
			try {
				if(this.getClass().getMethod(n.getTextContent(),Event.class) != null){
					enums_to_function.put(
						String.valueOf(lwjgl_keyboard_enums.get("KEY_" + id.toUpperCase())) + "KEY_" + event.toUpperCase(),
						n.getTextContent()
					);
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}

	void parseMouseSettings(ArrayList<Node> mouse_settings) throws Exception{
		for(Node n : mouse_settings){
			String event = ((Element) n).getAttribute("EVENT");
			if(n.getTextContent() != ""){
				try {
					if(this.getClass().getMethod(n.getTextContent(),Event.class) != null){
						enums_to_function.put(
							"MOUSE_" + event,  
							n.getTextContent()
						);
					}
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}
  
  	private ArrayList<Node> findChildrenByName(Node root, String... names) {
		ArrayList<Node> list = new ArrayList<Node>();
		for (int i = 0; i < names.length; i++) {
			Node e = root.getFirstChild();
			while (e != null) {
				if (e.getNodeName().equals(names[i])) {
					list.add(e);
				}
				e = e.getNextSibling();
			}
		}
		return list;
	}
  	
  	private void throwException(String message) throws Exception {
	    Exception e = new Exception();
	    e.initCause(new Throwable(message));
	    throw e;
  	}
  	
  	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	public void loadFromFile(InputStream is, String extension) throws Exception {
		Document dom;
  		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    
  		//Create Dom Structure
  		DocumentBuilder db;
		db = dbf.newDocumentBuilder();
		dom = db.parse(is);
	
		Element root_element = dom.getDocumentElement();
		if(root_element.getNodeName().equalsIgnoreCase("keymap")){
			ArrayList<Node> keyboards = findChildrenByName(root_element, "keyboard");
			for(Node keyboard : keyboards){
				ArrayList<Node> key_settings = findChildrenByName(keyboard, "key");
				parseKeyboardSettings(key_settings);
			}

			ArrayList<Node> mice = findChildrenByName(root_element,"mouse");
			for(Node mouse : mice){
				ArrayList<Node> mouse_settings = findChildrenByName(mouse, "event");
				parseMouseSettings(mouse_settings);
			}
		}else{
			throwException("KeyMap tag should be root element.");
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		for(String f: enums_to_function.keySet()) {
			if(lwjgl_keyboard_enums.get(keycode).equals(f)) {
				//Object[] params = new Object[1];
				//params[0] = e;
				//try {
				@SuppressWarnings("unused")
				Method m = null;
				try {
					m = InputMap.class.getMethod(enums_to_function.get(f),Event.class);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.debug("Caught: " + f + " || Running: " + enums_to_function.get(f));
				
				try{
					//m.invoke(this,params);
				}catch(Exception ex){
					throw ex;
				}				
			}				
		}
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
