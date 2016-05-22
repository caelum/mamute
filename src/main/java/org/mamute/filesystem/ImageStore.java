package org.mamute.filesystem;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import org.imgscalr.Scalr;
import org.mamute.dao.AttachmentDao;
import org.mamute.infra.ClientIp;
import org.mamute.interfaces.IAttachmentStorage;
import org.mamute.model.Attachment;
import org.mamute.model.User;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageStore {

	@Inject private IAttachmentStorage fileStorage;
	@Inject private Environment environment;
	@Inject private AttachmentDao attachments;

	public Attachment processAndStore(UploadedFile avatar, User user, ClientIp clientIp) throws IOException {
		BufferedImage resized = processImage(avatar);
		Attachment attachment = new Attachment(resized, user, clientIp.get(), avatar.getFileName());
		// order of statements is very important. first image needs to be saved to S3 so that we obtain
		// the key before saving to db
		fileStorage.saveImage(attachment);
		attachments.save(attachment);
		return attachment;
	}

	private BufferedImage processImage(UploadedFile avatar) throws IOException {
		BufferedImage image = ImageIO.read(avatar.getFile());
		if (image == null) {
			throw new IOException();
		}
		int min = Math.min(image.getHeight(), image.getWidth());
		BufferedImage cropped = Scalr.crop(image, min, min);
		return Scalr.resize(cropped, Scalr.Method.ULTRA_QUALITY, 150);
	}

	public void delete(Attachment image) {
		attachments.delete(image);
		fileStorage.delete(image);
	}
}
