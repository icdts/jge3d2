package thing.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ModelComp extends Component {
	public ModelInstance modelInst;
	public ShaderProgram shader;

	public ModelComp(Model m) {
		this(m, null);
	}

	public ModelComp(Model m, ShaderProgram shader) {
		this.modelInst = new ModelInstance(m);
		this.shader = shader;

		if (this.shader == null) {
			// this.shader = ;
		}
	}
}
