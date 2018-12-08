package ru.projects.prog_ja.logic.services.files.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.projects.prog_ja.logic.services.files.interfaces.FileService;
import ru.projects.prog_ja.logic.services.files.interfaces.UploadService;
import ru.projects.prog_ja.logic.services.simple.implementations.HashType;
import ru.projects.prog_ja.logic.services.simple.interfaces.HashService;
import ru.projects.prog_ja.services.files.FileResponseMessages;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
@Scope("prototype")
public class UploadServiceImpl extends FileResponseMessages implements UploadService {

    private static final Map<String, List<FileSize>> sizes
                = new ConcurrentHashMap<String, List<FileSize>>(){{
                    put("user.profile", Arrays.asList(
                            new FileSize(200, 200, "small"),
                            new FileSize(400, 400, "middle"),
                            new FileSize(600, 600, "large")
                    ));
                    put("article.main", Arrays.asList(
                            new FileSize(200, 200, "small"),
                            new FileSize(720, 400, "middle"),
                            new FileSize(1200, 700, "large")
                    ));
                    put("image.default", Arrays.asList(
                            new FileSize(1280, 700, "path")
                    ));
    }};
    private static final Pattern DOCS = Pattern.compile("\\.(docs?|xls|xlsn)$");
    private static final Pattern IMGS = Pattern.compile("\\.(gif|jpe?g|png|tiff)$");

    private static String uploadPath;
    private HashService hashService;

    @Autowired
    public UploadServiceImpl(HashService hashService) {
        this.hashService = hashService;
    }

    @Override
    public ResponseEntity<?> uploadFile(MultipartFile file, String uploadType) {

        try {
            String originalName = file.getOriginalFilename();
            String ext = getExtension(originalName);

            StringBuilder hash = new StringBuilder(hashService.hash(
                    originalName + new Date() , HashType.SHA_256));

            StringBuilder result = new StringBuilder();
            result  .append("files/")
                    .append(hash.subSequence(0, 2)).append("/")
                    .append(hash.subSequence(2, 4)).append("/")
                    .append(hash.subSequence(4, 6)).append("/");


            if(!createDirectory(uploadPath + result.toString())) {
                return serverError();
            }

            result.append(hash.subSequence(6, hash.length()));

            FileService service = getServise(ext, uploadType);
            if(service == null)
                return incorrectUploadType();

            return service.upload(file, uploadPath, result.toString(), ext);
        }catch (Exception e){
            return null;
        }
    }

    private boolean createDirectory(String path){
        File file = new File(path);
        if(!file.exists())
            return file.mkdirs();

        return true;
    }

    private String getExtension(String fileName){

        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    private FileService getServise(String ext, String type){

        if(IMGS.matcher(ext).matches()){
            return new ImageUploadService(sizes.get(type));
        }
        else if(DOCS.matcher(ext).matches()){
            return new DocsUploadService();
        }

        return null;
    }

    @Value("${path.file.upload}")
    private void setUploadPath(String path){
        uploadPath = path;
    }
}
