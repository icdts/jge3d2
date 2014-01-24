package com.jge3d.systems;

import java.lang.reflect.Method;

public class InputRunnable extends Thread {
	private long previousTime;
	private Method methodToRun;
	private Object objToUse;
	private Double increment;
	private volatile boolean shouldStop;

	public InputRunnable(String methodName, Object objToUse, Double inc)
		throws SecurityException, NoSuchMethodException {
		methodToRun = objToUse.getClass().getMethod(methodName, inc.getClass());
		this.objToUse = objToUse;
		increment = inc;
		shouldStop = false;
	}

	@Override
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
