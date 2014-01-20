package com.jge3d.managers;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g3d.Shader;

public class ShaderManager {
	HashMap<String,Shader> shaders;
	
	ShaderManager(){
		shaders = new HashMap<String,Shader>();
	}
	
	public boolean addShader(String name, Class<Shader> shaderClass){
		boolean success = true;
		
		try {
			Shader s = shaderClass.newInstance();
			shaders.put(name, s);
		} catch (Exception e) {
			//TODO Log exception
			success = false;
		}
		
		return success;
		
	}
	
	public void dispose(){
		for(Shader s : shaders.values()){
			s.dispose();
		}
	}

}
