package ru.projects.prog_ja.logic.services.files.implementations;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.projects.prog_ja.logic.services.files.interfaces.FileService;
import ru.projects.prog_ja.services.files.FileResponseMessages;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DocsUploadService extends FileResponseMessages implements FileService {


    @Override
    public ResponseEntity<?> upload(MultipartFile file, String uploadPath, String name, String ext) {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        JsonGenerator generator = Json.createGenerator(result);

        try (FileOutputStream fos = new FileOutputStream(uploadPath + name + ext);
             InputStream is = file.getInputStream();
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int read;
            while((read = is.read(buffer)) != -1){
                os.write(buffer, 0, read);
            }
            fos.write(os.toByteArray());
            fos.flush();

        }catch (Exception e){
            return serverError();
        }


        generator.writeStartObject().write("path", name + ext).writeEnd().flush();

        return accepted(new String(result.toByteArray()));
    }
}
