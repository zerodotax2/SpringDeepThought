package ru.projects.prog_ja.services.files.fileServices;

import org.springframework.web.multipart.MultipartFile;
import ru.projects.prog_ja.logic.util.HashType;
import ru.projects.prog_ja.logic.util.HasherClass;
import ru.projects.prog_ja.services.files.FileResponseMessages;
import ru.projects.prog_ja.services.files.fileServices.FileAndDirectory;
import ru.projects.prog_ja.services.files.fileServices.FileService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractFileService extends FileResponseMessages implements FileService {

    protected FileAndDirectory getPathFromAttributes(MultipartFile file, String userID){

        FileAndDirectory fileAndDirectory = new FileAndDirectory();
        /*
        * Создаём хеш на основе атрибутов файла и id пользователя,
        * он будет использован для создания уникального имени файла
        * */
        HasherClass hasherClass = new HasherClass(HashType.SHA_256);
        String hashPath = hasherClass.hash(file.getContentType() + file.getName() + file.getOriginalFilename() +
                file.getSize() + userID);

        /*
        * Получаем оригинальное имя файла
        * */
        String fileName = file.getOriginalFilename();

        /*
        * Создаём билдер для пути и создаём директорию
        * */
        StringBuilder result = new StringBuilder();
        result.append(hashPath.substring(0, 2)).append(File.separator)
                .append(hashPath.substring(2, 4)).append(File.separator)
                .append(hashPath.substring(4, 6)).append(File.separator);
        fileAndDirectory.setDirectoryPath(result.toString());

        /*
        * Записываем имя файла, добавляя разрешение оригинала
        * */
        result.append(hashPath.substring(6, hashPath.length()))
            .append(fileName.substring(fileName.lastIndexOf("."), fileName.length()));
        fileAndDirectory.setFilePath(result.toString());

        /*
        * Возвращаем результат
        * */
        return fileAndDirectory;
    }

    protected String writeFile(FileAndDirectory fileAndDirectory, String uploadPath, String prefix, byte[] fileBytes) throws IOException {

        /*
         * Создаём директорию, если ее нет
         * */

        String dirPath = prefix + File.separator + fileAndDirectory.getDirectoryPath();
        String filePath =  prefix + File.separator +  fileAndDirectory.getFilePath();

        File dir = new File(uploadPath + dirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        /*
         * Создаём файл, если он есть то пишем ошибку и возвращаем false
         * */
        File file = new File(uploadPath + filePath);
        if(file.exists()){
            return null;
        }

        OutputStream os = new FileOutputStream(file);
        os.write(fileBytes);
        os.flush();
        os.close();

        return filePath;
    }

}
