package ru.projects.prog_ja.logic.services.files.implementations;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImageConverter {

    public byte[] convertAndGet(byte[] image, int width, int height){

        /*
        * Проверяем что изображение валидно, иначе возвращаем null
        * */
        if(image == null || image.length == 0){
            return null;
        }
        try (InputStream is = new ByteArrayInputStream(image)) {
            /*
            * Пытаемся конвертировать изображение
            * */
            return convertImage(is, width, height).toByteArray();

        }catch (IOException e){ }

        return null;
    }


    /**
     * creating buffered image from byte input stream,
     * scaling image,
     * @return new image
     * */
    private ByteArrayOutputStream convertImage(InputStream image, int width, int height) throws IOException{


            /*
            * Читаем изображение из поступившего массива байтов,
            * и сразу обрезаем его, чтобы во время сжатия не нарушить пропорции
            * */
            BufferedImage oldImage = createProportion(ImageIO.read( image ), width, height);

            /*
            * Сжимаем поступившее изображение до указанных размеров с помощью стандартных алгоритмов в java,
            * записываем полученное изображение в созданный BufferedImage
            * */
            Image scaledImage = oldImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            newImage.createGraphics().drawImage(scaledImage, 0, 0, null);

            /*
            * Записываем изменённое изображение в поток вывода и возвращаем его
            * */
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(newImage, "jpg", os);

            return os;
    }

    private BufferedImage createProportion(BufferedImage image, int width, int height){

        /*
        * Вычисляем отношение старой ширины и высоты к новым,
        * а также вычисляем их разницу
        * */
        double widthRate = (double) image.getWidth() / (double) width;
        double heightRate = (double) image.getHeight() / (double) height;

        double difference = widthRate - heightRate;

        /*
        * Если разница меньше одной десятой, то возвращаем это изображение
        * */
        if( Math.abs(difference) <= 0.1F ){

            return image;
            /*
            * Если отношение ширины меньше отношения высоты,
            * то значит ширина изображения недостаточна и т.к.
            * мы не можем увеличивать изображение, то мы уменьшим высоту
            * */
        }else if(difference < 0){

            /*
            * Вычисляем уменьшенную высоту изображения, создаём по нему полотно
            * и размещаем на него старое изображение, центрируя его
            * */
            int newHeight = (int) (height * widthRate);
            BufferedImage newImage = new BufferedImage(image.getWidth(),
                    newHeight, BufferedImage.TYPE_INT_RGB);
            newImage.getGraphics().drawImage(image, 0 , - (image.getHeight() - newHeight) / 2, null);

            return newImage;
            /*
            * Если же меньше отношение высоты, то значит высоты недостаточно
            * и т.к. мы не может изображение увеличить, то мы обрежем
            * ширину изображения
            * */
        }else{
            /*
            * Вычисляем новую уменьшенную ширину изображения, создаём по нему полотно
            * и пишем туда, центрируя изображение
            * */
            int newWidth = (int) (width * heightRate);
            BufferedImage newImage = new BufferedImage(newWidth,
                    image.getHeight(), BufferedImage.TYPE_INT_RGB);
            newImage.getGraphics().drawImage(image, - (image.getWidth() - newWidth) / 2, 0, null);

            return newImage;

        }


    }
}
