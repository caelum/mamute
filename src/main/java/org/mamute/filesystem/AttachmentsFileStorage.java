package org.mamute.filesystem;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.environment.Property;
import org.apache.commons.io.IOUtils;
import org.mamute.model.Attachment;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.*;

public class AttachmentsFileStorage {

	@Inject
	private Environment environment;
	private File attachmentsRoot;

	@PostConstruct
	public void setup() {
		this.attachmentsRoot = new File(environment.get("attachments.root.fs.path"));
	}

	public void save(Attachment attachment) {
		try {
			File destination = new File(attachmentsRoot, attachment.fileName());
			try (FileOutputStream output = new FileOutputStream(destination)) {
				IOUtils.copy(attachment.getUploadedFile().getFile(),
						output);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public InputStream open(Attachment attachment) throws FileNotFoundException {
		File file = new File(attachmentsRoot, attachment.fileName());
		return new FileInputStream(file);
	}
}
