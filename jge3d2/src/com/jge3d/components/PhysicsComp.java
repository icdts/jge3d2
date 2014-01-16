package com.jge3d.components;

import com.artemis.Component;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public class PhysicsComp extends Component{
	public btCollisionObject collisionObject;
	
	public PhysicsComp(btCollisionObject object){
		collisionObject = object;
	}
}
