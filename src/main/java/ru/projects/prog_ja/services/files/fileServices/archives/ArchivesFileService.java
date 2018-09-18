package ru.projects.prog_ja.services.files.fileServices.archives;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.projects.prog_ja.dto.view.DefaultFilePathDTO;
import ru.projects.prog_ja.services.files.fileServices.FileAndDirectory;
import ru.projects.prog_ja.services.files.fileServices.AbstractFileService;

import java.io.*;

public class ArchivesFileService extends AbstractFileService {

    @Override
    public ResponseEntity<?> doUpload(MultipartFile file, String uploadPath, String extension, String userID) {

        FileAndDirectory fileAndDirectory = getPathFromAttributes(file, userID);

        try (InputStream is = file.getInputStream()) {

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read;
            while((read = is.read(buffer)) != -1){
                os.write(buffer, 0, read);
            }

            String relativePath = writeFile(fileAndDirectory, uploadPath, "archives", os.toByteArray());
            if(relativePath == null){
                return exist();
            }

            return accepted(new DefaultFilePathDTO(relativePath));
        }catch (IOException e) {
        }

        /*
         * Если не вернулся успех, значит случилось исключение,
         * возвращаем ошибку
         * */
        return serverError();
    }

}
