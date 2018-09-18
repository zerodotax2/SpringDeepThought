package ru.projects.prog_ja.services.files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.projects.prog_ja.dto.view.MessageType;
import ru.projects.prog_ja.dto.view.ResponseMessageDTO;
import ru.projects.prog_ja.services.files.fileServices.FileService;

import javax.annotation.PostConstruct;
import javax.json.JsonObject;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

@RestController
@RequestMapping(value = "/upload")
public class UploadFileService extends FileResponseMessages {

    /*
    * Путь для загрузки всех файлов
    * */
    @Value("${path.file.upload}")
    private static String uploadPath;

    /*
     * Карта для хранения даты временных файлов,
     * каждый временный файл хранится на сервере не более часа,
     * где ключ - дата в которую файл истечет, а значение - путь к файлу
     * */
//    private static final SortedMap<Date, String> tempFiles = new TreeMap<>();

    @PostConstruct
    public void initService(){


    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("type") String uploadType,
                                        @RequestParam("userID") String userID){


        /*
        * Получаем имя загруженного файла, чтобы вытащить из него расширение
        * */
        String oldFileName = file.getOriginalFilename();
        String ext = oldFileName.substring(oldFileName.lastIndexOf("."), oldFileName.length());

        /*
        * Получаем по расширению сервис, если такого не нашлось,
        * возвращаем ошибку и сообщение
        * */
        FileService fileService = FileServiceFactory.getServiceFromExtension(ext.substring(1), uploadType);
        if(fileService == null){

            return incorrectFormat();
        }

       return fileService.doUpload(file, uploadPath, ext, userID);
    }



//    public ResponseEntity<?> activateFile(String jsonData){
//
//        /*
//        * Получаем запрос на активацию, парсим его в json object
//        * и достаём из него имя и тип загрузки
//        * */
//        JsonObject data = parseJSONtoObject(jsonData);
//
//        String tempPath = data.getString("fileName");
//        String type = data.getString("type");
//
//        FileService fileService = FileServiceFactory.getServiceFromExtension(
//                tempPath.substring(tempPath.lastIndexOf(".")+1, tempPath.length()),
//                type
//        );
//
//        /*
//        * Если сервис не был найден,
//        * то возвращаем 400 (bad request) с сообщением об ошибке
//        * */
//        if(fileService == null){
//            return incorrectName();
//        }
//
//        /*
//        * Проводим активацию, в зависимости от файла и типа,
//        * */
//        return accepted( new ResponseMessageDTO("Загрузка файла прошла успешно", MessageType.INFO));
//    }

//    private void startClearTempFilesService(){
//
//        ClearTempFilesDaemon clearTempFilesDaemon = new ClearTempFilesDaemon(tempFiles);
//
//        clearTempFilesDaemon.setDaemon(true);
//        clearTempFilesDaemon.setPriority(Thread.MIN_PRIORITY);
//        clearTempFilesDaemon.start();
//    }

}
