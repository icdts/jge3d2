package com.jge3d;

import java.util.ArrayList;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader.ObjLoaderParameters;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBox2dShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.jge3d.components.ModelComp;
import com.jge3d.components.PhysicsComp;
import com.jge3d.systems.ModelRenderSys;
import com.jge3d.systems.PhysicsSys;
import com.jge3d.systems.SpriteRenderSys;

public class ShaderTest implements ApplicationListener {
	Environment lights;

	OrthographicCamera spriteCamera;
	PerspectiveCamera modelCamera;
	private World world;

	private SpriteRenderSys spriteRenderSys;
	private ModelRenderSys modelRenderSys;
	private PhysicsSys bulletPhysicsSys;

	private float delta = 0;
	private float previousTimeStamp = 0;

	@SuppressWarnings("unused")
	private int frames = 0;

	// For keeping track of what needs to be disposed
	ArrayList<Model> models;

	private void cameraSetup() {
		modelCamera = new PerspectiveCamera(45, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		modelCamera.position.set(10f, 10f, 10f);
		modelCamera.lookAt(0, 0, 0);
		modelCamera.near = 0.1f;
		modelCamera.far = 300f;

		spriteCamera = new OrthographicCamera();
		spriteCamera.setToOrtho(false, 1280, 900);
	}

	@Override
	public void create() {
		world = new World();

        cameraSetup();
        systemsSetup();
        lightingSetup();

        world.initialize();

        entitySetup();
        shaderSetup();
	}

	@Override
	public void dispose() {
		bulletPhysicsSys.dispose();
		modelRenderSys.dispose();
		for (Model m : models) {
			m.dispose();
		}
	}

	private void entitySetup() {
		// Start adding Models...
		ModelBuilder mb = new ModelBuilder();
		models = new ArrayList<Model>();

		// Falling box
		Entity e = world.createEntity();

		// Model m = mb.createBox(5f, 5f, 5f, new
		// Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position |
		// Usage.Normal);
		ModelLoader<ObjLoaderParameters> loader = new ObjLoader();
		Model m = loader.loadModel(Gdx.files
				.internal("models/shuttle.obj"));
		models.add(m);

		e.addComponent(new ModelComp(m));
		e.addComponent(this.makeBox());

		e.addToWorld();

		// Land
		e = world.createEntity();

		m = mb.createRect(-10f, 0f, -10f,// x00, y00, z00,
				-10f, 0f, 10f,// x10, y10, z10,
				10f, 0f, 10f,// x11, y11, z11,
				10f, 0f, -10f,// x01, y01, z01,
				0f, 1f, 0f,// normalX, normalY, normalZ,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),// material,
				Usage.Position | Usage.Normal // attributes
		);
		models.add(m);
		e.addComponent(new ModelComp(m));
		e.addComponent(this.makeLand());

		e.addToWorld();
	}

	public float getDelta() {
		if (previousTimeStamp != 0) {
			delta = (System.nanoTime() - previousTimeStamp);
		}

		previousTimeStamp = System.nanoTime();
		frames++;

		return delta;
	}

	private void lightingSetup() {
		lights = new Environment();
		lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.1f, 0.1f,
				0.1f, 0.5f));
		lights.add(new DirectionalLight().set(Color.WHITE, new Vector3(-1, -1,
				0)));
		modelRenderSys.setEnviroment(lights);
	}

	public PhysicsComp makeBox() {
		float mass = 1f;
		Vector3 inertia = new Vector3(0, 0, 0);

		btMotionState motionState = new btDefaultMotionState();
		btCollisionShape collisionShape = new btSphereShape(1f);

		btRigidBody rigidBody = new btRigidBody(mass, motionState,
				collisionShape);
		rigidBody.setWorldTransform(new Matrix4(new Vector3(0, 10, 0),
				new Quaternion(0, 0, 0, 1), new Vector3(1, 1, 1)));

		collisionShape.calculateLocalInertia(mass, inertia);
		rigidBody.setMassProps(mass, inertia);

		PhysicsComp b = new PhysicsComp(rigidBody);

		bulletPhysicsSys.dynamicsWorld.addRigidBody(rigidBody);

		rigidBody.updateInertiaTensor();

		return b;
	}

	private Component makeLand() {
		float mass = 0f;
		Vector3 inertia = new Vector3(0, 0, 0);

		btMotionState motionState = new btDefaultMotionState();
		btCollisionShape collisionShape = new btBox2dShape(new Vector3(10, 1,
				10));

		btRigidBody rigidBody = new btRigidBody(mass, motionState,
				collisionShape);
		rigidBody.setWorldTransform(new Matrix4(new Vector3(0, -10, 0),
				new Quaternion(0, 0, 0, 1), new Vector3(1, 1, 1)));

		collisionShape.calculateLocalInertia(mass, inertia);
		rigidBody.setMassProps(mass, inertia);

		PhysicsComp b = new PhysicsComp(rigidBody);

		bulletPhysicsSys.dynamicsWorld.addRigidBody(rigidBody);

		rigidBody.updateInertiaTensor();

		return b;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		world.setDelta(getDelta());
		bulletPhysicsSys.process();

		modelCamera.update();
		// if(use3D){
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		modelCamera.update();
		// }else{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		spriteCamera.update();
		// }
		world.process();

		// if(use3D){
		modelRenderSys.process();
		// }else{
		spriteRenderSys.process();
		// }
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	private void shaderSetup() {
		ShaderProgram shader = new ShaderProgram(
				Gdx.files.internal("shaders/default.vertex.shader"),
				Gdx.files.internal("shaders/default.fragment.shader"));

		System.out.println("Did shader compile? " + Boolean.toString(shader.isCompiled()));
		if(!shader.isCompiled()){
			System.out.println(shader.getLog());
		}
	}

	private void systemsSetup() {
		spriteRenderSys = world.setSystem(new SpriteRenderSys(spriteCamera),
				true);
		modelRenderSys = world.setSystem(new ModelRenderSys(modelCamera), true);
		bulletPhysicsSys = world.setSystem(new PhysicsSys());
	}
}
