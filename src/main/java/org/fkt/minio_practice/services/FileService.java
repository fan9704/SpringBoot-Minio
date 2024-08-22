package org.fkt.minio_practice.services;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class FileService {
    @Autowired
    private MinioClient minioClient;

    public void uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                                    inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("[MinIO-Service ]Error occurred: " + e.getMessage());
        }
    }
    public List<String> listBucket() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        ListBucketsArgs args = ListBucketsArgs.builder()
                        .build();
        List<String> bucketNameList = new ArrayList<>();
        for(Bucket bucket :minioClient.listBuckets(args)){
            bucketNameList.add(bucket.name());
        }
        return bucketNameList;
    }

    public List<Item> listBucketItem(String bucketName){
        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix("")
                .recursive(false)
                .build();
        Iterable<Result<Item>> myObjects = minioClient.listObjects(args);
        return this.getItems(myObjects);
    }
    public void createBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MakeBucketArgs args = MakeBucketArgs.builder()
                .bucket(bucketName)
                .build();
        minioClient.makeBucket(args);
    }

    private List<Item> getItems(Iterable<Result<Item>> myObjects) {
        return StreamSupport
                .stream(myObjects.spliterator(), true)
                .map(itemResult -> {
                    try {
                        return itemResult.get();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }
    public void getAndSave(Path source, String fileName,String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        DownloadObjectArgs args = DownloadObjectArgs.builder()
                .bucket(bucketName)
                .object(source.toString())
                .filename(fileName)
                .build();
        minioClient.downloadObject(args);

    }
    public void remove(Path source,String bucketName) throws MinioException {
        try {
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(source.toString())
                    .build();
            minioClient.removeObject(args);
        } catch (Exception e) {
            throw new MinioException("Error while fetching files in Minio");
        }
    }
}
