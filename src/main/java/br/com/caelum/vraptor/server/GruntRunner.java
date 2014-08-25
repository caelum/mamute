package br.com.caelum.vraptor.server;

import java.io.*;
import java.util.Scanner;

public class GruntRunner implements Runnable{

	@Override
	public void run() {
		System.out.println("Executing grunt...");
		try {
			Process exec = Runtime.getRuntime().exec("mvn grunt:grunt -Dmamute.grunt.task=run");
			new Thread(new ProcessOutputWriter(exec.getInputStream(), System.out)).run();
			new Thread(new ProcessOutputWriter(exec.getErrorStream(), System.err)).run();
		} catch (IOException e) {
			throw new RuntimeException("Couldn't run grunt", e);
		}

	}

	private static class ProcessOutputWriter implements Runnable {
		private final InputStream stream;
		private final PrintStream out;

		public ProcessOutputWriter(InputStream stream, PrintStream out) {
			this.stream = stream;
			this.out = out;
		}

		@Override
		public void run() {
			try (Scanner s = new Scanner(stream)) {
				while (s.hasNextLine()) {
					out.println(s.nextLine());
				}
			}


		}
	}

}
