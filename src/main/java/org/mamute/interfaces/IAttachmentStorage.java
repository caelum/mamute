package org.mamute.interfaces;

import org.mamute.model.Attachment;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by siddjain on 5/22/16.
 */
public interface IAttachmentStorage
{
    InputStream open(Attachment attachment) throws IOException;
    void save(Attachment attachment);
    void saveImage(Attachment attachment);
    void delete(Attachment attachment);
}
