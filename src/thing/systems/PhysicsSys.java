package thing.systems;

import thing.components.PhysicsComp;

import com.artemis.World;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btAxisSweep3;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

public class PhysicsSys extends EntitySystem{
	World world;
	
	@Mapper
	ComponentMapper<PhysicsComp> bulletPhysicsMapper;
	
	btBroadphaseInterface broadphaseInterface;
	btCollisionConfiguration collisionConfiguration;
	btCollisionDispatcher collisionDispatcher;
	btSequentialImpulseConstraintSolver solver;
	public btDiscreteDynamicsWorld dynamicsWorld;
	
	private Vector3 worldAabbMin;
	private Vector3 worldAabbMax;
	
	@SuppressWarnings("unchecked")
	public PhysicsSys(World world) {
		super(Aspect.getAspectForAll(PhysicsComp.class));
		this.world = world;
	}
	
	@Override
	public void initialize(){
		Bullet.init();
		
		// Min and Max collision boundaries for world (TODO: needs changing?)
		worldAabbMin = new Vector3(-1000, -1000, -1000);
		worldAabbMax = new Vector3(1000, 1000, 1000);
		
		// algorithm for finding collision proximity (there are better ones)
		broadphaseInterface = new btAxisSweep3(worldAabbMin, worldAabbMax);
		
		collisionConfiguration = new btDefaultCollisionConfiguration();
		collisionDispatcher = new btCollisionDispatcher(collisionConfiguration);
		
		solver = new btSequentialImpulseConstraintSolver();
		
		dynamicsWorld = new btDiscreteDynamicsWorld(collisionDispatcher,broadphaseInterface, solver,collisionConfiguration);
		
		dynamicsWorld.setGravity(new Vector3(0,-10,0));
	}
	
	@Override
	protected void begin(){
		dynamicsWorld.stepSimulation(world.getDelta() / 1000000000f, 10, 1/60f);
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	public void dispose(){
		broadphaseInterface.dispose();
		collisionConfiguration.dispose();
		collisionDispatcher.dispose();
		solver.dispose();
		dynamicsWorld.dispose();
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		//Nothing to do...
	}
}
