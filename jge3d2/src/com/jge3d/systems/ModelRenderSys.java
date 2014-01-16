package com.jge3d.systems;

import com.jge3d.components.ModelComp;
import com.jge3d.components.PhysicsComp;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class ModelRenderSys extends EntitySystem {
	@Mapper
	ComponentMapper<PhysicsComp> physicsMap;
	@Mapper
	ComponentMapper<ModelComp> modelMap;

	private PerspectiveCamera camera;
	private ModelBatch batch;

	@SuppressWarnings("unchecked")
	public ModelRenderSys(PerspectiveCamera camera) {
		super(Aspect.getAspectForAll(PhysicsComp.class, ModelComp.class));
		this.camera = camera;
	}

	@Override
	protected void initialize() {
		batch = new ModelBatch();
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
			batch.render(model.modelInst);
		}
	}

	@Override
	protected void end() {
		batch.end();
	}

}