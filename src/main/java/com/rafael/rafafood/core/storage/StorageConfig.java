package com.rafael.rafafood.core.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.rafael.rafafood.core.storage.StorageProperties.TipoStorage;
import com.rafael.rafafood.domain.service.FotoStorageService;
import com.rafael.rafafood.infrastructure.service.storage.LocalFotoStorageService;
import com.rafael.rafafood.infrastructure.service.storage.S3FotoStorageService;

@Configuration
public class StorageConfig {

	@Autowired
	private StorageProperties storageProperties;
	
	@Bean
	@ConditionalOnProperty(name = "rafafood.storage.tipo", havingValue = "s3")
	public AmazonS3 amazonS3() {
		var credentials = new BasicAWSCredentials(
				storageProperties.getS3().getIdChaveAcesso(), 
				storageProperties.getS3().getChaveAcessoSecreta());
		
		return AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(storageProperties.getS3().getRegiao())
				.build();
	}
	
	@Bean
	public FotoStorageService fotoStorageService() {
		if (TipoStorage.S3.equals(storageProperties.getTipo())) {
			return new S3FotoStorageService();
		} else {
			return new LocalFotoStorageService();
		}
	}
	
}
