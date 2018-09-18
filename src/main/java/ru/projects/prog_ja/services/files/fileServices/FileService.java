package ru.projects.prog_ja.services.files.fileServices;


import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * interface that contains 1 method to upload file on server
 * depends on type of file
 * */
public interface FileService {


    /**
     * method will upload file to server and
     * @return result of upload (error status or successful status with parameters)
     * */
    ResponseEntity<?> doUpload(MultipartFile file, String uploadPath, String extension, String userID);

}
