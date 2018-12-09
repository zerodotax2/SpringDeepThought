package ru.projects.prog_ja.services.rest.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.view.PasswordDTO;
import ru.projects.prog_ja.dto.view.TagsContainerDTO;
import ru.projects.prog_ja.dto.view.UpdateDTO;
import ru.projects.prog_ja.logic.services.files.interfaces.UploadService;
import ru.projects.prog_ja.logic.services.simple.implementations.RegexUtil;
import ru.projects.prog_ja.logic.services.transactional.interfaces.AuthService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.Arrays;

@RestController
@RequestMapping("/services/user/update")
public class UserUpdateService extends AbstractRestService {

    private final UserWriteService userWriteService;
    private final AuthService authService;
    private final UploadService uploadService;

    @Autowired
    public UserUpdateService(UserWriteService userWriteService, AuthService authService, UploadService uploadService) {
        this.userWriteService = userWriteService;
        this.authService = authService;
        this.uploadService = uploadService;
    }

    @PostMapping("/bday")
    public ResponseEntity<?>  changeBirthdate(@RequestBody UpdateDTO updateDTO,
                                              @SessionAttribute(name = "user", required = false)UserDTO userDTO){

        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        else if(updateDTO == null || updateDTO.getValue() == null){
           return badRequest();
        }

        else if(!updateDTO.getValue().matches("^[\\d]{4}-[\\d]{2}-[\\d]{2}$"))
          return incorrectFormat();

        else if(userWriteService.updateBirthDate(userDTO.getId(), updateDTO.getValue())){
            return ok();
        }

        return serverError();

    }

    @PostMapping("/lname")
    public ResponseEntity<?> updateLastName(@RequestBody UpdateDTO updateDTO,
                                            @SessionAttribute(name = "user", required = false) UserDTO userDTO){
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        else if(updateDTO == null || updateDTO.getValue() == null)
            return badRequest();

        else if(!RegexUtil.rusName(updateDTO.getValue()).matches() ||
                updateDTO.getValue().length() < 3 || updateDTO.getValue().length() > 32)
            return incorrectFormat();

        else if(userWriteService.updateLastName(userDTO.getId(), updateDTO.getValue())){
            return ok();
        }

        return serverError();
    }

    @PostMapping("/fname")
    public ResponseEntity<?> updateFirstName(@RequestBody UpdateDTO updateDTO,
                                            @SessionAttribute(name = "user", required = false) UserDTO userDTO){
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        else if(updateDTO == null || updateDTO.getValue() == null)
            return badRequest();

        else if(!RegexUtil.rusName(updateDTO.getValue()).matches() ||
                updateDTO.getValue().length() < 3 || updateDTO.getValue().length() > 32)
            return incorrectFormat();

        else if(userWriteService.updateFirstName(userDTO.getId(), updateDTO.getValue())){
            return ok();
        }

        return serverError();
    }

    @PostMapping("/about")
    public ResponseEntity<?> updateAbout(@RequestBody UpdateDTO updateDTO,
                                            @SessionAttribute(name = "user", required = false) UserDTO userDTO){
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        else if(updateDTO == null || updateDTO.getValue() == null)
            return badRequest();

        else if(updateDTO.getValue().length() < 10 || updateDTO.getValue().length() > 1000
                || !RegexUtil.string(updateDTO.getValue()).matches())
            return incorrectFormat();

        else if(userWriteService.updateAbout(userDTO.getId(), updateDTO.getValue())){
            return ok();
        }

        return serverError();
    }

    @PostMapping("/email")
    public ResponseEntity<?> updateEmail(@RequestBody UpdateDTO updateDTO,
                                        @SessionAttribute(name = "user", required = false) UserDTO userDTO){
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        else if(updateDTO == null || updateDTO.getValue() == null)
            return badRequest();

        else if(updateDTO.getValue().length() < 5 || updateDTO.getValue().length() > 50
                || !updateDTO.getValue().matches("^[\\w|_|.]+?@[\\w]+?\\.[\\w]+?$"))
            return incorrectFormat();

        else if(authService.updateEmail(userDTO.getId(), updateDTO.getValue()))
            return ok();

        return serverError();
    }
    @PostMapping("/interests")
    public ResponseEntity<?> updateInterests(@RequestBody TagsContainerDTO tagsContainerDTO,
                                         @SessionAttribute(name = "user", required = false) UserDTO userDTO){
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        else if(tagsContainerDTO == null || tagsContainerDTO.getTags() == null)
            return badRequest();

        else if(tagsContainerDTO.getTags().size() < 2 || tagsContainerDTO.getTags().size() > 5)
            return incorrectFormat();

        else if(userWriteService.updateInterests(userDTO.getId(), tagsContainerDTO.getTags()))
            return ok();

        return serverError();
    }

    @PostMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordDTO passwordDTO,
                                             @SessionAttribute(name = "user", required = false) UserDTO userDTO){
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        else if(passwordDTO == null || passwordDTO.getOldPassword() == null || passwordDTO.getNewPassword() == null
                || !RegexUtil.string(passwordDTO.getNewPassword()).matches())
            return badRequest();

        else if(authService.updatePass(passwordDTO.getNewPassword(), userDTO.getId()))
            return ok();

        return serverError();
    }

    @PostMapping("/image")
    public ResponseEntity<?> updateImage(@RequestParam("file") MultipartFile file,
                                         @RequestHeader(name = "Upload-Type", defaultValue = "image.default") String uploadType,
                                         @SessionAttribute(name = "user", required = false) UserDTO userDTO){
        if(userDTO == null || userDTO.getId() == -1)
            return badRequest();

        ResponseEntity response = uploadService.uploadFile(file, uploadType);
        if(response == null || response.getStatusCode().isError()){
            return serverError();
	    }

        JsonReader parser = Json.createReader(new StringReader((String) response.getBody()));
        JsonObject obj = parser.readObject();

        userWriteService.updateUserImage(userDTO.getId(), Arrays.asList(
                obj.getString("small"), obj.getString("middle"), obj.getString("large")
        ));

        userDTO.setUserImage(obj.getString("small"));

        return accepted(response.getBody());
    }
}
