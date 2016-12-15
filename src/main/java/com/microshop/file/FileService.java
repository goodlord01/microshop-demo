package com.microshop.file;

import com.microshop.utils.ResourceInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Created by yan on 11/23/2016.
 */
@Service
public class FileService {

    private RestTemplate fileApiTemplate;

    @Value("${yunsoo.client.file_api}")
    private String fileApiUrl;

    public FileService() {
        fileApiTemplate = new RestTemplate();
    }

    protected ResponseExtractor<ResourceInputStream> getResourceInputStreamResponseExtractor() {
        return response -> {
            HttpHeaders httpHeaders = response.getHeaders();
            InputStream inputStream = response.getBody();
            long contentLength = httpHeaders.getContentLength();
            String contentType = httpHeaders.getContentType().toString();
            return new ResourceInputStream(new ByteArrayInputStream(StreamUtils.copyToByteArray(inputStream)), contentLength, contentType);
        };
    }

    public ResourceInputStream getFile(String path){
        String url = fileApiUrl + "file?bucket_name=microshop&path=" + path;
        RequestCallback requestCallback = request -> request.getHeaders().set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        ResourceInputStream resourceInputStream = fileApiTemplate.execute(url, HttpMethod.GET, requestCallback, getResourceInputStreamResponseExtractor());
        return resourceInputStream;
    }
}
