package thing.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
<<<<<<< HEAD
import com.badlogic.gdx.graphics.g3d.Shader;

public class ModelComp extends Component {
	public ModelInstance modelInst;
	public Shader shader;

	public ModelComp(Model m) {
		this(m, null);
	}

	public ModelComp(Model m, Shader shader) {
=======

public class ModelComp extends Component {
	public ModelInstance modelInst;

	public ModelComp(Model m) {
>>>>>>> parent of 2bae7d9... ShaderTest added
		this.modelInst = new ModelInstance(m);
	}
}
