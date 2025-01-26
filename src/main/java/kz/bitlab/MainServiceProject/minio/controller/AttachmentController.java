package kz.bitlab.MainServiceProject.minio.controller;

import kz.bitlab.MainServiceProject.minio.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/file")
@RequiredArgsConstructor
public class AttachmentController {
    private final FileService fileService;

    @PostMapping(value = "/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(file);
    }
    @GetMapping(value = "/download/{file}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "file") String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,"atachment; filename=\"" + fileName + "\"")
                .body(fileService.downloadFile(fileName));
    }
}
