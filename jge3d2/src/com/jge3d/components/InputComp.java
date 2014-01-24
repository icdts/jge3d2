package com.jge3d.components;

import java.lang.reflect.Method;

import com.artemis.Component;

public class InputComp extends Component{
	private long previousTime;
	private Method methodToRun;
	private Object objToUse;
	private Double increment;
	private volatile boolean shouldStop;

	public InputComp(String methodName, Object objToUse, Double inc)
		throws SecurityException, NoSuchMethodException {
		methodToRun = objToUse.getClass().getMethod(methodName, inc.getClass());
		this.objToUse = objToUse;
		increment = inc;
		shouldStop = false;
	}

	public void run() {
		previousTime = System.nanoTime();
		while (!shouldStop) {
			double inc = (System.nanoTime() - previousTime) * -increment;
			// System.out.println(inc);
			try {
				methodToRun.invoke(objToUse, inc);
			} catch (Exception e) {
				e.printStackTrace();
			}
			previousTime = System.nanoTime();
		}
	}

	public void end() {
		shouldStop = true;
	}
}
