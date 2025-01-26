package kz.bitlab.MainServiceProject.minio.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public String uploadFile(MultipartFile file);

    public ByteArrayResource downloadFile(String fileName);
}
