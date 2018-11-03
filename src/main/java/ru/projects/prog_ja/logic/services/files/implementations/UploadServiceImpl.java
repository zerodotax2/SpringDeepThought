package ru.projects.prog_ja.logic.services.files.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.logic.services.files.interfaces.FileService;
import ru.projects.prog_ja.logic.services.files.interfaces.UploadService;
import ru.projects.prog_ja.logic.services.simple.implementations.HashType;
import ru.projects.prog_ja.logic.services.simple.interfaces.HashService;
import ru.projects.prog_ja.services.files.FileResponseMessages;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope("prototype")
public class UploadServiceImpl extends FileResponseMessages implements UploadService {

    private static final Map<String, List<FileSize>> sizes
                = new ConcurrentHashMap<String, List<FileSize>>(){{
                    put("user.profile", Arrays.asList(
                            new FileSize(100, 100, "small"),
                            new FileSize(200, 200, "middle"),
                            new FileSize(400, 400, "large")
                    ));
                    put("article.main", Arrays.asList(
                            new FileSize(100, 100, "small"),
                            new FileSize(720, 400, "middle"),
                            new FileSize(1200, 700, "large")
                    ));
                    put("image.default", Arrays.asList(
                            new FileSize(1280, 700, "path")
                    ));
    }};

    @Value("${path.file.upload}")
    private static String uploadPath;

    private HashService hashService;

    @Autowired
    public UploadServiceImpl(HashService hashService) {
        this.hashService = hashService;
    }

    @Override
    public ResponseEntity<?> uploadFile(MultipartFile file, String uploadType) {

        String originalName = file.getOriginalFilename();
        String ext = getExtension(originalName);

        StringBuilder hash = new StringBuilder(hashService.hash(
                originalName + new Date() , HashType.SHA_256));

        StringBuilder result = new StringBuilder();
        result.append(hash.subSequence(0, 2)).append("/")
                .append(hash.subSequence(2, 4)).append("/")
                .append(hash.subSequence(4, 6)).append("/");

        if(!createDirectory(result.insert(0, uploadPath).toString()))
            return serverError();

        result.append(hash.subSequence(6, hash.length()));

        FileService service = getServise(ext, uploadType);
        if(service == null)
            return incorrectUploadType();

        return service.upload(file, uploadPath, result.toString(), ext);
    }

    private boolean createDirectory(String path){
        File file = new File(path);
        if(!file.exists())
            return file.mkdirs();

        return true;
    }

    private String getExtension(String fileName){

        return fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
    }

    private FileService getServise(String ext, String type){

        if(ext.matches("\\.(gif|jpe?g|png|tiff)$")){
            return new ImageUploadService(sizes.get(type));
        }
        else if(ext.matches("\\.(docs?|xls|xlsn)$")){
            return new DocsUploadService();
        }

        return null;
    }
}
