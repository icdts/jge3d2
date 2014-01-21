package com.jge3d.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class DefaultShader implements Shader {
	ShaderProgram program;
	
	@Override
	public void dispose() {
		program.dispose();
	}

	@Override
	public void init() {
		program = new ShaderProgram(
			Gdx.files.external(Gdx.files.getLocalStoragePath() + "resources/shaders/defaultShader.vertex.shader"),
			Gdx.files.external(Gdx.files.getLocalStoragePath() + "resources/shaders/defaultShader.fragment.shader")
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
		return false;
	}

	@Override
	public void begin(Camera camera, RenderContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Renderable renderable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void end() {
		// TODO Auto-generated method stub

	}

}
