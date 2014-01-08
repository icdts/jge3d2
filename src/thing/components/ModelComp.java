package thing.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class ModelComp extends Component {
	public ModelInstance modelInst;

	public ModelComp(Model m) {
		this.modelInst = new ModelInstance(m);
	}
}
