package br.com.caelum.brutal.util;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import br.com.caelum.vraptor.ioc.Component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class S3FileProvider {

    private final AmazonS3Client amazonS3Client;

    public S3FileProvider(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public URL store(File file, String dir, String key) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(dir, key, file)
            .withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(putObjectRequest);
        return urlFor(dir, key);
    }
    
    public URL store(InputStream is, String dir, String key, String mimeType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(mimeType);
        PutObjectRequest putObjectRequest = new PutObjectRequest(dir, key, is, metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(putObjectRequest);
        return urlFor(dir, key);
    }
    
    public void mkdir(String dir) {
        amazonS3Client.createBucket(dir);
    }

    private URL urlFor(String dir, String key) {
        try {
            return new URL("http://" + dir + ".s3.amazonaws.com/" + key);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
