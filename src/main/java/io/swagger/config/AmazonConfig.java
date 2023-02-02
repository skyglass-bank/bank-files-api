package io.swagger.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;

@Configuration
public class AmazonConfig {

    @Value("${s3_bucket.access_key}")
    private String accessKey;

    @Value("${s3_bucket.secret_key}")
    private String secretKey;

    @Value("${s3_bucket.region}")
    private String region;

    @Value("${s3_bucket.base_url}")
    private String serviceEndpoint;
    
    @Bean
    public AmazonS3 s3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        EndpointConfiguration endpointConfiguration = new EndpointConfiguration(serviceEndpoint, region);
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    @Bean
    public KmsClient kmsClient() {
        Region region = Region.EU_CENTRAL_1;
        return KmsClient.builder()
                .region(region)
                .build();
    }
}