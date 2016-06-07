package org.mamute.aws;

import br.com.caelum.vraptor.environment.Property;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.mamute.interfaces.IAttachmentStorage;
import org.mamute.model.Attachment;
import org.mamute.validators.Validator;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Locale;

/**
 * Created by siddjain on 5/19/16.
 */
@ApplicationScoped
@Alternative // make this an alternative (i.e., non-default implementation of the interface)
public class S3Storage implements IAttachmentStorage
{
    @Inject
    @Property("aws.s3.bucket")
    private String bucket;
    private AmazonS3Client client;
    private static long cacheControlMaxAge = Duration.standardHours(2).getStandardSeconds();

    @Inject
    private AwsCredentialsFactory awsCredentialsProvider;

    @Deprecated
    public S3Storage()
    {
        // CDI needs a public no args ctor otherwise it can't proxy the class
        // you will get a Injected normal scoped bean is not proxyable error
    }

    /* this method is there for unit tests */
    public S3Storage(String bucket, String accessKey, String secretKey)
    {
        Validator.notNullOrEmpty("bucket", bucket);
        Validator.notNullOrEmpty("accessKey", accessKey);
        Validator.notNullOrEmpty("secretKey", secretKey);

        this.bucket = bucket;

        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.client = new AmazonS3Client(credentials);  // let it throw if credentials are wrong
        if (!this.client.doesBucketExist(bucket))
        {
            throw new IllegalArgumentException("bucket " + bucket + " does not exist");
        }
    }

    @PostConstruct
    public void postConstruct()
    {
        this.client = new AmazonS3Client(this.awsCredentialsProvider.getCredentials());
        if (!this.client.doesBucketExist(bucket))
        {
            throw new IllegalArgumentException("bucket " + bucket + " does not exist");
        }
    }

    public void save(Attachment attachment)
    {
        UploadedFile uploadedFile = attachment.getUploadedFile();
        if (uploadedFile != null && attachment.getName() != null)
        {
            long length = uploadedFile.getSize();
            if (length <= 0)
            {
                throw new IllegalArgumentException("size of uploaded file is less than or equal to 0");
            }
            this.uploadToS3(attachment, uploadedFile.getFile(), length, uploadedFile.getContentType());
        }
    }

    public void saveImage(Attachment attachment)
    {
        BufferedImage image = attachment.getImage();
        if (image != null && attachment.getName() != null)
        {
            // http://stackoverflow.com/questions/4251383/how-to-convert-bufferedimage-to-inputstream
            // https://groups.google.com/forum/#!topic/play-framework/Lk3miwcajHY
            try (ByteArrayOutputStream os = new ByteArrayOutputStream())
            {
                ImageIO.write(image, "png", os);
                byte[] array = os.toByteArray();
                if (array != null && array.length > 0)
                {
                    try (InputStream is = new ByteArrayInputStream(array))
                    {
                        uploadToS3(attachment, is, array.length, "image/png");
                    }
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public InputStream open(Attachment attachment) throws IOException
    {
        // note that the caller needs to dispose the InputStream returned by this method after using it
        // Vraptor4 does not do this (see https://github.com/caelum/vraptor4/blob/cef168586497ffa9c1777ae5d6112efbab988f4d/vraptor-core/src/main/java/br/com/caelum/vraptor/observer/download/InputStreamDownload.java)
        // so you need to convert the S3 stream to a managed byte array. The garbage collector will clean up the byte
        // array eventually even if its not manually disposed. That is much safer than undisposed S3 stream which may
        // be holding onto http connections underneath

        try (InputStream is = this.client.getObject(this.bucket, attachment.getS3Key()).getObjectContent();
             ByteArrayOutputStream os = new ByteArrayOutputStream())
        {
            copyStreams(is, os);
            return new ByteArrayInputStream(os.toByteArray());
        }
    }

    public void delete(Attachment attachment)
    {
        // don't delete anything
    }

    // making public for unit testing
    public void uploadToS3(Attachment attachment, InputStream is, long contentLength, String contentType)
    {
        // Content length must be specified before data can be uploaded to Amazon S3.
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        metadata.setContentType(contentType);
        // https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9
        // http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/Expiration.html
        metadata.setCacheControl("max-age=" + cacheControlMaxAge);
        String key = makeUnique(createFolderPrefix() + attachment.getName());
        client.putObject(this.bucket, key, is, metadata);
        attachment.setUrl(client.getResourceUrl(this.bucket, key));
        attachment.setS3Key(key);
    }

    private static void copyStreams(InputStream is, OutputStream os) throws IOException
    {
        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = is.read(buf)) != -1)
        {
            // I used IOUtils.copy() before but there was a specific use case where if I started downloading a large
            // file from S3 and then for some reason if that thread was interrupted, the download would not stop and
            // it would go on and on until the whole file was downloaded.
            if (Thread.interrupted())
            {
                throw new IOException("thread interrupted");
            }
            os.write(buf, 0, count);
        }
    }

    private String createFolderPrefix()
    {
        return DateTime.now().toString(DateTimeFormat.forPattern("YYYY/MM/dd/").withLocale(Locale.US));
    }

    private String makeUnique(String key)
    {
        // truncate since the s3Key column is a varchar(200)
        String answer = truncate(key, 195);
        int i = 1;
        while (client.doesObjectExist(this.bucket, answer))
        {
            answer = key + "-" + i++;
        }
        return answer;
    }

    /**
     Truncate a String to the given length with no warnings
     or error raised if it is bigger.

     @param  value String to be truncated
     @param  length  Maximum length of string

     @return Returns value if value is null or value.length() is less or equal to than length, otherwise a String representing
     value truncated to length.
     */
    private static String truncate(String value, int length)
    {
        if (value != null && value.length() > length)
        {
            value = value.substring(0, length);
        }
        return value;
    }
}
