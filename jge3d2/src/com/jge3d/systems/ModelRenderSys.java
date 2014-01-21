package com.jge3d.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.environment.AmbientCubemap;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.jge3d.components.ModelComp;
import com.jge3d.components.PhysicsComp;
import com.jge3d.shaders.Jge3d2DefaultShader;

public class ModelRenderSys extends EntitySystem {
	@Mapper
	ComponentMapper<PhysicsComp> physicsMap;
	@Mapper
	ComponentMapper<ModelComp> modelMap;

	private PerspectiveCamera camera;
	private ModelBatch batch;

	private Environment environment;
	
	private Shader defaultShader;
	private Shader customShader;
	private DefaultShader w;
	@SuppressWarnings("unchecked")
	public ModelRenderSys(PerspectiveCamera camera) {
		super(Aspect.getAspectForAll(PhysicsComp.class, ModelComp.class));
		this.camera = camera;
	}

	@Override
	protected void initialize() {
		batch = new ModelBatch();
		if( defaultShader == null ) {
			defaultShader = new Jge3d2DefaultShader();
			defaultShader.init();
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			process(entities.get(i));
		}
	}

	@Override
	protected void begin() {
		batch.begin(camera);
	}

	protected void process(Entity e) {
		if (modelMap.has(e)) {
			ModelComp model = modelMap.get(e);
			PhysicsComp physics = physicsMap.get(e);
			model.modelInst.transform.set(physics.collisionObject.getWorldTransform());

			if(model.shader == null){
				if(customShader == null){
					model.shader = defaultShader;
				}else{
					model.shader = customShader;
				}
			}
			
			if (environment != null && model.shader != null) {
				batch.render(model.modelInst, environment, model.shader);
			} else if (environment != null && model.shader == null) {
				batch.render(model.modelInst, environment);
			} else if (environment == null && model.shader != null) {
				batch.render(model.modelInst, model.shader);
			} else {
				batch.render(model.modelInst);
			}
		}
	}

	@Override
	protected void end() {
		batch.end();
	}
	
	public void dispose() {
		batch.dispose();
	}

	public void setEnviroment(Environment environment) {
		this.environment = environment;
	}

	public void setDefaultShader(Shader shader) {
		
		this.customShader = shader;
	}

}
