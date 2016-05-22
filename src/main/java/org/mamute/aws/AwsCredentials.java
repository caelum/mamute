package org.mamute.aws;

import org.mamute.validators.Validator;

/**
 * Created by siddjain on 5/20/16.
 */
class AwsCredentials
{
    private String s3_access_key_id;
    private String s3_secret_access_key;

    public String getS3_access_key_id()
    {
        return s3_access_key_id;
    }

    public String getS3_secret_access_key()
    {
        return s3_secret_access_key;
    }

    public AwsCredentials(String accessKey, String secretKey)
    {
        this.s3_access_key_id = accessKey;
        this.s3_secret_access_key = secretKey;
        validate();
    }

    private void validate()
    {
        Validator.notNullOrEmpty("s3_access_key_id", s3_access_key_id);
        Validator.notNullOrEmpty("s3_secret_access_key", s3_secret_access_key);
    }
}
