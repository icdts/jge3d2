package com.jge3d.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Jge3d2DefaultShader implements Shader {
	ShaderProgram program;
	Camera camera;
	RenderContext context;
	
	//Store locations of uniforms/etc to save time
	int u_projTrans;
	int u_worldTrans;

	@Override
	public void init() {
		program = new ShaderProgram(
			Gdx.files.external("workspace/jge3d2/jge3d2/resources/shaders/DefaultShader.vertex.shader").readString(),
		    Gdx.files.external("workspace/jge3d2/jge3d2/resources/shaders/DefaultShader.fragment.shader").readString()
		/*
			Gdx.files.internal("shaders/DefaultShader.vertex.shader"),
			Gdx.files.internal("shaders/DefaultShader.fragment.shader")
		*/
		);
		
		if(program.isCompiled()){
			u_projTrans = program.getUniformLocation("u_projViewTrans");
			u_worldTrans = program.getUniformLocation("u_worldTrans");
			if(u_projTrans == -1 || u_worldTrans == -1){
				System.out.println("Missing uniform");
			}
		}else{
			System.out.println(program.getLog());
		}
	}

	@Override
	public void begin(Camera camera, RenderContext context) {
		//These won't change until end() is called
		this.camera = camera;
		this.context = context;
		
		program.begin();

		program.setUniformMatrix(u_projTrans, camera.combined);
		
		context.setDepthTest(GL20.GL_LEQUAL);
        context.setCullFace(GL20.GL_BACK);
	}

	@Override
	public void render(Renderable renderable) {
		program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
		
		renderable.mesh.render(
			program, 
			renderable.primitiveType,
			renderable.meshPartOffset,
			renderable.meshPartSize
		);
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		program.end();
	}

	@Override
	public void dispose() {
		program.dispose();
	}
	
	@Override
	public int compareTo(Shader other) {
		//Used by model batch to determine which shader should be returned first
		return 0; //Just always say its equal
	}
	
	@Override
	public boolean canRender(Renderable instance) {
		// TODO Auto-generated method stub
		return true;
	}
}
