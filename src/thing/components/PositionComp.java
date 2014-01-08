package thing.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

public class PositionComp extends Component {
	public Vector3 vector;

	public PositionComp(float x, float y, float z) {
		vector = new Vector3(x, y, z);
	}

	public PositionComp() {
		this(0, 0, 0);
	}
}
