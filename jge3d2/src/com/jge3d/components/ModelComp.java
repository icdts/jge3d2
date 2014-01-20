package com.jge3d.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import com.badlogic.gdx.graphics.g3d.Shader;

public class ModelComp extends Component {
	public ModelInstance modelInst;
	public Shader shader;

	public ModelComp(Model m) {
		this(m, null);
	}

	public ModelComp(Model m, Shader shader) {
		this.modelInst = new ModelInstance(m);
	}
}
