package com.example.kafka.consumer;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.http.AWSRequestSigningApacheInterceptor;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;

@Configuration
@ComponentScan
public class ElasticSearchConfig {

    @Value("${es.local.endpoint}")
    private String elasticSearchEndpoint;

    public static final String SERVICE_NAME = "es";
    public static final String SERVICE_REGION = "us-east-1";

    @Bean
    public RestHighLevelClient client(final StsAssumeRoleCredentialsProvider credentialsProvider) {
        final AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(SERVICE_NAME);
        signer.setRegionName(SERVICE_REGION);
        final HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(SERVICE_NAME, signer, new AwsCredentialsProviderAdaptor(credentialsProvider));
        return new RestHighLevelClient(
            RestClient.builder(HttpHost.create(elasticSearchEndpoint)).setHttpClientConfigCallback(clientBuilder -> clientBuilder.addInterceptorLast(interceptor))
        );
    }

    // Converts between the 2.0 and 1.0 Aws credentials
    // Copied from https://github.com/awslabs/aws-request-signing-apache-interceptor/issues/5
    static class AwsCredentialsProviderAdaptor implements AWSCredentialsProvider {
        private final AwsCredentialsProvider credentialsProvider;

        public AwsCredentialsProviderAdaptor(final AwsCredentialsProvider credentialsProvider) {
            this.credentialsProvider = credentialsProvider;
        }

        @Override
        public AWSCredentials getCredentials() {
            final AwsCredentials credentials = credentialsProvider.resolveCredentials();

            if (credentials instanceof AwsSessionCredentials) {
                return new AWSSessionCredentials() {
                    @Override
                    public String getAWSAccessKeyId() {
                        return credentials.accessKeyId();
                    }

                    @Override
                    public String getAWSSecretKey() {
                        return credentials.secretAccessKey();
                    }

                    @Override
                    public String getSessionToken() {
                        return ((AwsSessionCredentials) credentials).sessionToken();
                    }
                };
            }

            return new AWSCredentials() {
                @Override
                public String getAWSAccessKeyId() {
                    return credentials.accessKeyId();
                }

                @Override
                public String getAWSSecretKey() {
                    return credentials.secretAccessKey();
                }
            };

        }

        @Override
        public void refresh() {
        }
    }
}