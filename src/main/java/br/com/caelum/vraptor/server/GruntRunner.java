package br.com.caelum.vraptor.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GruntRunner implements Runnable{

	@Override
	public void run() {
		System.out.println("Executing grunt...");
		try {
			Process exec = Runtime.getRuntime().exec("mvn grunt:grunt -Dmamute.grunt.task=run");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}
			bufferedReader.close();
		} catch (IOException e) {
			throw new RuntimeException("Couldn't run grunt", e);
		}
		
	}

}
