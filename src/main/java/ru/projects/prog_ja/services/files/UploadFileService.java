package ru.projects.prog_ja.services.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.logic.services.files.interfaces.UploadService;

@Controller
@RequestMapping(value = "/upload")
public class UploadFileService extends FileResponseMessages {


    private final UploadService uploadService;

    @Autowired
    public UploadFileService (UploadService uploadService){
        this.uploadService = uploadService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestHeader(name = "Upload-Type", defaultValue = "image.default") String uploadType,
                                        @SessionAttribute(name = "user", required = false) UserDTO userDTO){

        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        ResponseEntity response = uploadService.uploadFile(file, uploadType);

        return  response != null ? response : serverError();
    }


}
