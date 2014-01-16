package com.jge3d.systems;

import com.jge3d.components.PositionComp;
import com.jge3d.components.SpriteComp;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteRenderSys extends EntitySystem {
	@Mapper
	ComponentMapper<PositionComp> pm;
	@Mapper
	ComponentMapper<SpriteComp> sm;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	@SuppressWarnings("unchecked")
	public SpriteRenderSys(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(PositionComp.class, SpriteComp.class));
		this.camera = camera;
	}

	@Override
	protected void initialize() {
		batch = new SpriteBatch();
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
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
	}

	protected void process(Entity e) {
		if (pm.has(e)) {
			PositionComp position = pm.getSafe(e);
			SpriteComp sprite = sm.get(e);

			batch.setColor(sprite.r, sprite.g, sprite.b, sprite.a);
			float posx = position.vector.x;
			float posy = position.vector.y;

			batch.draw(sprite.sprite, posx, posy);
		}
	}

	@Override
	protected void end() {
		batch.end();
	}

}
