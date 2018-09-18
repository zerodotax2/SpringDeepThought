package ru.projects.prog_ja.services.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Set;
import java.util.SortedMap;

@Deprecated
public class ClearTempFilesDaemon extends Thread{

    private static SortedMap<Date, String> tempFiles;

    public ClearTempFilesDaemon(SortedMap<Date, String> map){
        tempFiles = map;
    }

    @Override
    public void run(){

        /*
         * Время для засыпания потока, равное 10 минутам
         * */
        final long sleepTimeMillis = 1000 * 60 * 10;

        try {

            /*
             * Поток демон работает все время
             * */
            while (true) {

                /*
                 * Получаем текущее время и, если карта не пуста,
                 * берем из неё файлы, у которых дата раньше чем текущая и удаляем их,
                 * далее засыпаем на 10 минут
                 * */
                Date currentTime = new Date();

                if(!tempFiles.isEmpty()){

                    Set<Date> keys = tempFiles.keySet();
                    for(Date time : keys){
                        if(time.before(currentTime)){

                            try {
                                /*
                                * Если файл устарел, и ещё существует, то получаем на него ссылку
                                * и удаляем из временной директории
                                * */
                                Files.deleteIfExists(Paths.get(tempFiles.get(time)));
                                tempFiles.remove(time);

                            }catch (IOException e){}

                        }else{
                            break;
                        }
                    }
                }

                Thread.sleep(sleepTimeMillis);
            }


        }catch (InterruptedException e){}

    }

}
