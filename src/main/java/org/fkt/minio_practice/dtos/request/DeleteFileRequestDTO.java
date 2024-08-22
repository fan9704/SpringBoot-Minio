package org.fkt.minio_practice.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteFileRequestDTO {
    private Path source;
    private String bucketName;
}
