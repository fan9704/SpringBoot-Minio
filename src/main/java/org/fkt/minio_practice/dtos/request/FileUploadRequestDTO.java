package org.fkt.minio_practice.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadRequestDTO {
    private String bucketName;
    private String objectName;
    private MultipartFile file;
    private String contentType;
}
