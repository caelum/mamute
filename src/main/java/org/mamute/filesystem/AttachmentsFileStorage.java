package org.mamute.filesystem;

import br.com.caelum.vraptor.environment.Property;

import org.apache.commons.io.IOUtils;
import org.mamute.model.Attachment;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.*;

public class AttachmentsFileStorage {

	private File attachmentsRoot;

	@Deprecated
	public AttachmentsFileStorage() {
	}

	@Inject
	public AttachmentsFileStorage(@Property("attachments.root.fs.path") String fsPath) {
		this.attachmentsRoot = new File(fsPath);
	}

	public void save(Attachment attachment) {
		try {
			File destination = attachmentPath(attachment);
			try (FileOutputStream output = new FileOutputStream(destination)) {
				IOUtils.copy(attachment.getUploadedFile().getFile(),
						output);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public InputStream open(Attachment attachment) throws FileNotFoundException {
		File file = attachmentPath(attachment);
		return new FileInputStream(file);
	}

	private File attachmentPath(Attachment attachment) {
		return new File(attachmentsRoot, attachment.getId().toString());
	}

	public void delete(Attachment attachment) {
		File file = attachmentPath(attachment);
		file.delete();
	}

	public void saveImage(Attachment attachment) {
		try {
			BufferedImage image = attachment.getImage();
			File destination = attachmentPath(attachment);
			ImageIO.write(image, "png", new FileOutputStream(destination));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public void delete(Iterable<Attachment> attachments) {
		for (Attachment attachment : attachments) {
			this.delete(attachment);
		}
	}
}
