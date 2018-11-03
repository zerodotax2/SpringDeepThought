package ru.projects.prog_ja.logic.services.files.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ResponseEntity<?> upload(MultipartFile file, String uploadPath, String name, String ext);

}
