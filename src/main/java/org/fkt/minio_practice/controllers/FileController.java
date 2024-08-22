package org.fkt.minio_practice.controllers;

import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.fkt.minio_practice.dtos.request.DeleteFileRequestDTO;
import org.fkt.minio_practice.dtos.request.FileUploadRequestDTO;
import org.fkt.minio_practice.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Tag(name = "File")
@RestController
@RequestMapping("/api/file")
public class FileController {
    private final FileService fileService;


    FileController(@Autowired FileService fileService){
        this.fileService = fileService;
    }
    @GetMapping(value = "/bucket")
    public ResponseEntity<List<String>> listBucket() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return  new ResponseEntity<>(this.fileService.listBucket(),HttpStatus.OK);
    }

    @GetMapping(value = "/bucket/{bucketName}")
    public ResponseEntity<List<Item>> listBucketItem(@PathVariable("bucketName") String bucketName){
        return new ResponseEntity<>(this.fileService.listBucketItem(bucketName),HttpStatus.OK);
    }
    @PostMapping(value = "/bucket")
    public ResponseEntity<String> createBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        this.fileService.createBucket(bucketName);
        return new ResponseEntity<>("Create Successfully",HttpStatus.CREATED);
    }

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@ModelAttribute FileUploadRequestDTO dto){
        try {

            String contentType = !Objects.equals(dto.getContentType(), "") ? dto.getContentType() : "application/octet-stream";
            fileService.uploadFile(dto.getBucketName(), dto.getObjectName(), dto.getFile().getInputStream(), contentType);

            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping(value = "")
    public ResponseEntity<String> deleteFile(@RequestBody DeleteFileRequestDTO dto){
        try{
            fileService.remove(dto.getSource(),dto.getBucketName());
            return new ResponseEntity<>("Delete Successfully",HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
