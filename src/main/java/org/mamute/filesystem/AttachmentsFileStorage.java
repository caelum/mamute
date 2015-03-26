package org.mamute.filesystem;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.environment.Property;
import org.apache.commons.io.IOUtils;
import org.mamute.model.Attachment;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AttachmentsFileStorage {

	@Inject
	private Environment environment;

	public void save(Attachment attachment) {
		String attachmentsRoot = environment.get("attachments.root.fs.path");
		try {
			File destination = new File(attachmentsRoot, attachment.getId().toString());
			IOUtils.copy(attachment.getUploadedFile().getFile(),
					new FileOutputStream(destination));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
