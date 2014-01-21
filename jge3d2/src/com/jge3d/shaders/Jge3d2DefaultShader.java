package com.jge3d.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Jge3d2DefaultShader implements Shader {
	ShaderProgram program;
	Camera camera;
	RenderContext context;
	
	@Override
	public void dispose() {
		program.dispose();
	}

	@Override
	public void init() {
		program = new ShaderProgram(
			Gdx.files.internal("shaders/DefaultShader.vertex.shader"),
			Gdx.files.internal("shaders/DefaultShader.fragment.shader")
		);
	}

	@Override
	public int compareTo(Shader other) {
		//TODO ?
		return 0;
	}

	@Override
	public boolean canRender(Renderable instance) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void begin(Camera camera, RenderContext context) {
		// TODO Auto-generated method stub
		//context.
		this.camera = camera;
		this.context = context;
		program.begin();
	}

	@Override
	public void render(Renderable renderable) {
		// TODO Auto-generated method stub
		renderable.mesh.render(
			program, 
			renderable.primitiveType
		);
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		program.end();
	}

}
