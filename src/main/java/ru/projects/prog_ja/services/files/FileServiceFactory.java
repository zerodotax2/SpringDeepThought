package ru.projects.prog_ja.services.files;

import ru.projects.prog_ja.services.files.fileServices.FileService;
import ru.projects.prog_ja.services.files.fileServices.archives.ArchivesFileService;
import ru.projects.prog_ja.services.files.fileServices.docs.DocsFileService;
import ru.projects.prog_ja.services.files.fileServices.images.ImageConverter;
import ru.projects.prog_ja.services.files.fileServices.images.ImageFileService;

import java.util.HashMap;
import java.util.Map;

public abstract class FileServiceFactory {

    private static final Map<String, Object> uploadTypes
            = new HashMap<String, Object>(){{
                put("type.post.main", ImageFileService.ImageUploadType.POST_MAIN_IMAGE);
                put("type.user.profile", ImageFileService.ImageUploadType.USER_PROFILE_IMAGE);
                put("type.image", ImageFileService.ImageUploadType.DEFAULT_IMAGE );
    }};


    public static synchronized FileService getServiceFromExtension(String ext, String uploadType){

        /*
        * Если расширение соответствует изображению, то возвращаем сервис для обработки изображений
        * */
        if(ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png") || ext.equals("gif")){

            if(!uploadType.contains(uploadType)){
                return null;
            }

            return new ImageFileService( (ImageFileService.ImageUploadType) uploadTypes.get(uploadType), new ImageConverter());
        }
        /*
        * Если же расширение соответствуют документу,
        * возврщаем сервис для обработки документов
        * */
        else if(ext.equals("xls") || ext.equals("txt") || ext.equals("docs") || ext.equals("doc")
                || ext.equals("xlsx")) {

            return new DocsFileService();
        }
        /*
        * Если расширение соответствует архиву,
        * возвращаем сервис, обрабатывающий архивы
        * */
        else if(ext.equals("zip") || ext.equals("rar") || ext.equals("gzip") || ext.equals("tar")){

            return new ArchivesFileService();
        }

        /*
        * Если сервиса не нашлось, то возвращаем null,
        * пользователь получит сообщение об ошибке,
        * не соответстиве разрешения файла
        * */
        return null;
    }

}
