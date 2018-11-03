package ru.projects.prog_ja.services.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.logic.services.files.interfaces.UploadService;

@RestController
@RequestMapping(value = "/upload")
public class UploadFileService extends FileResponseMessages {


    private final UploadService uploadService;

    @Autowired
    public UploadFileService (UploadService uploadService){
        this.uploadService = uploadService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("type") String uploadType,
                                        @SessionAttribute("user") UserDTO userDTO){

        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();


        return uploadService.uploadFile(file, uploadType);
    }


}
