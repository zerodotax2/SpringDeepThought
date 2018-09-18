package ru.projects.prog_ja.services.files.fileServices.images;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.projects.prog_ja.dto.view.DefaultFilePathDTO;
import ru.projects.prog_ja.dto.view.ThreeImagesPathDTO;
import ru.projects.prog_ja.services.files.fileServices.AbstractFileService;
import ru.projects.prog_ja.services.files.fileServices.FileAndDirectory;

import java.awt.*;
import java.io.*;

public class ImageFileService extends AbstractFileService {

    public enum ImageUploadType{
        USER_PROFILE_IMAGE, POST_MAIN_IMAGE, DEFAULT_IMAGE
    }

    private ImageUploadType uploadType;
    private ImageConverter converter;

    private static final int DEFAULT_WIDTH = 1200;
    private static final int DEFAULT_HEIGHT = 700;

    public ImageFileService(ImageUploadType uploadType, ImageConverter imageConverter){

        this.uploadType = uploadType;
        this.converter = imageConverter;
    }

    @Override
    public ResponseEntity<?> doUpload(MultipartFile file, String uploadPath, String extension, String userID){


        FileAndDirectory fileAndDirectory = getPathFromAttributes(file, userID);

        switch (uploadType){

            case POST_MAIN_IMAGE:
                /*
                * Тип для загрузки основного изображения поста,
                * обрезаем на 3 разных размера и записываем их
                * */

                return writeThreeImages(file, uploadPath, fileAndDirectory,
                        new Dimension(60,60), new Dimension(720, 400), new Dimension(1200, 700));

            case USER_PROFILE_IMAGE:
                /*
                * Тип для загрузки изображения профиля пользователя
                * обрезаем в 3 разных разрешения для отображения на сайте
                * */
                return writeThreeImages(file,  uploadPath, fileAndDirectory,
                        new Dimension(60,60), new Dimension(200, 200), new Dimension(400, 400));

            case DEFAULT_IMAGE:
                /*
                * Стандартный тип загрузки, нужен для картинок внутри контента,
                * обрезает метадату и уменьшает до стандартного размера
                * */
                return writeDefaultImage(file, uploadPath, fileAndDirectory);

            default:

                return incorrectUploadType();
        }

    }

    private ResponseEntity<?> writeThreeImages(MultipartFile file, String uploadPath, FileAndDirectory fileAndDirectory, Dimension... sizes){

        try (InputStream is = file.getInputStream()) {

            /*
            * Считываем всё изображение клиента
            * */
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int read;
            byte[] buffer = new byte[1024];

            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }

            byte[] image = os.toByteArray();

            /*
            * Создаём DTO для ответа
            * */
            ThreeImagesPathDTO threeImagesPathDTO = new ThreeImagesPathDTO();

            /*
            * Пишем все изображения
            * */

            String smallImagePath = writeFile(fileAndDirectory, uploadPath, "images" + File.separator +"small",
                    converter.convertAndGet(image, (int) sizes[0].getWidth(), (int) sizes[0].getHeight()));
            if(smallImagePath == null)
                return exist();
            threeImagesPathDTO.setSmall(smallImagePath);

            threeImagesPathDTO.setMiddle(writeFile(fileAndDirectory, uploadPath, "images" + File.separator +"middle",
                    converter.convertAndGet(image, (int) sizes[1].getWidth(), (int) sizes[1].getHeight())));

            threeImagesPathDTO.setLarge(writeFile(fileAndDirectory, uploadPath, "images" + File.separator +"large",
                    converter.convertAndGet(image, (int) sizes[2].getWidth(), (int) sizes[2].getHeight())));

            return accepted(threeImagesPathDTO);

        }catch (IOException e){ }

        /*
        * Если не вернулся успех, значит что-то пошло не так, возвращаем ошибку
        * */
        return serverError();
    }


    private ResponseEntity<?> writeDefaultImage(MultipartFile file, String uploadPath, FileAndDirectory fileAndDirectory){

        try (InputStream is = file.getInputStream())  {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read;
            byte[] buffer = new byte[1024];
            while((read = is.read(buffer)) != -1){
                baos.write(buffer, 0, read);
            }

            byte[] image = baos.toByteArray();

            String relativeFilePath = writeFile(fileAndDirectory, uploadPath, "images " + File.separator + "default",
                    converter.convertAndGet(image, 1200, 700));
            if(relativeFilePath == null){
                return exist();
            }

            return accepted(new DefaultFilePathDTO(relativeFilePath));

        }catch (IOException e){ }

        /*
        * Если не вернулся успех, значит закончилось неудачей
        * */
        return serverError();
    }




}
