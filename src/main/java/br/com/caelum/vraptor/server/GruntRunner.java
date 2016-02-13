package br.com.caelum.vraptor.server;

import java.io.IOException;

public class GruntRunner implements Runnable{

	private final VRaptorServer server;

	public GruntRunner(VRaptorServer server) {
		this.server = server;
	}

	@Override
	public void run() {
		System.out.println("Executing grunt...");
		try {
			Process exec = new ProcessBuilder("mvn", "grunt:grunt", "-Dmamute.grunt.task=run").inheritIO().start();
			int value = exec.waitFor();
			if (value != 0) {
				System.err.println("Grunt call failed with exit value: " + value);
			}
			server.stop();
		} catch (IOException | InterruptedException  e) {
			throw new RuntimeException("Couldn't run grunt", e);
		}

	}

}
