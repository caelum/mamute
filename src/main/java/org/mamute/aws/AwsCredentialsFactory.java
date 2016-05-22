package org.mamute.aws;

import br.com.caelum.vraptor.environment.Property;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Created by siddjain on 5/22/16.
 */
@ApplicationScoped
public class AwsCredentialsFactory implements AWSCredentialsProvider
{
    @Inject @Property("aws.s3.accessKey")
    private String accessKey;
    @Inject @Property("aws.s3.secretKey")
    private String secretKey;

    private BasicAWSCredentials credentials;

    @PostConstruct
    public void postConstruct()
    {
        this.credentials = new BasicAWSCredentials(accessKey, secretKey);
    }

    @Override
    public AWSCredentials getCredentials()
    {
        return this.credentials;
    }

    @Override
    public void refresh()
    {
        // do nothing
    }
}
