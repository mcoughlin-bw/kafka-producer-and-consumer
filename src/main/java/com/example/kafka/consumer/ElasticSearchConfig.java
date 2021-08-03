package com.example.kafka.consumer;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
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
    //    @Value("${aws.es.endpoint}")
    private final String elasticSearchEndpoint = "https://search-call-search-poc2-fgzfa5iva56446homkkvf5vqwq.us-east-1.es.amazonaws.com";

    @Bean
    public RestHighLevelClient client(final StsAssumeRoleCredentialsProvider credentialsProvider) {
        final AWS4Signer signer = new AWS4Signer();
        signer.setServiceName("es");
        signer.setRegionName("us-east-1");
        final HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor("es", signer, new AwsCredentialsProviderAdaptor(credentialsProvider));
        return new RestHighLevelClient(
            RestClient.builder(HttpHost.create(elasticSearchEndpoint)).setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor))
        );
    }

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