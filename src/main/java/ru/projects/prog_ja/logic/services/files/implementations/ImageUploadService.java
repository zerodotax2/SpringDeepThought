package ru.projects.prog_ja.logic.services.files.implementations;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.projects.prog_ja.logic.services.files.interfaces.FileService;
import ru.projects.prog_ja.services.files.FileResponseMessages;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ImageUploadService extends FileResponseMessages implements FileService {

    private final List<FileSize> fileSizes;
    private final ImageConverter imageConverter;

    public ImageUploadService(List<FileSize> fileSizes) {
        this.fileSizes = fileSizes;
        this.imageConverter = new ImageConverter();
    }

    @Override
    public ResponseEntity<?> upload(MultipartFile file, String uploadPath, String name, String ext) {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        JsonGenerator generator = Json.createGenerator(result);
        generator.writeStartObject();

        try(InputStream is = file.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();) {

            byte[] buffer = new byte[1024];
            int read;


            while((read = is.read(buffer)) != -1){
                os.write(buffer, 0, read);
            }
            byte[] image = os.toByteArray();


            for(FileSize fileSize : fileSizes){

                StringBuilder sb = new StringBuilder();
                sb.append(name).append("-").append(fileSize.getName()).append(ext);

                FileOutputStream fileOutputStream = new FileOutputStream(uploadPath + sb.toString());
                byte[] convertedImage = imageConverter.convertAndGet(image, fileSize.getWidth(), fileSize.getHeight());


                fileOutputStream.write(convertedImage);
                fileOutputStream.flush();
                fileOutputStream.close();

                generator.write(fileSize.getName(), sb.toString());
            }

        }catch (IOException e){
            return serverError();
        }

        generator.writeEnd().flush();

        return accepted(new String(result.toByteArray()));
    }

}
