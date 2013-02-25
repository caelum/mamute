package br.com.caelum.brutal.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.net.URL;

import org.junit.Test;

import br.com.caelum.vraptor.environment.DefaultEnvironment;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class S3FileProviderTest {
    
    @Test
    public void shoud_create_bucket_and_send_file_to_s3() throws Exception {
        DefaultEnvironment env = new DefaultEnvironment("testing");
        AmazonS3Client client = mock(AmazonS3Client.class);
        S3FileProvider s3FileProvider = new S3FileProvider(client);
        
        s3FileProvider.mkdir("guj-avatar");
        
        URL resource = env.getResource("/sample.txt");
        File file = new File(resource.getFile());
        
        URL uri = s3FileProvider.store(file, "guj-avatar", file.getName());
        
        verify(client).createBucket("guj-avatar");
        verify(client).putObject(any(PutObjectRequest.class));
        assertEquals(new URL("http://guj-avatar.s3.amazonaws.com/sample.txt"), uri);
    }

}
