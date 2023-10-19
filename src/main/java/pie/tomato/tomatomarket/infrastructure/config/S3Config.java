package pie.tomato.tomatomarket.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import pie.tomato.tomatomarket.infrastructure.config.properties.S3Properties;

@Configuration
public class S3Config {

	@Bean
	public AmazonS3Client amazonS3Client(S3Properties s3Properties) {
		BasicAWSCredentials credentials = new BasicAWSCredentials(s3Properties.getCredentials().getAccessKey(),
			s3Properties.getCredentials().getSecretKey());

		return (AmazonS3Client)AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.withRegion(s3Properties.getRegion())
			.build();
	}
}
