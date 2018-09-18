package ru.projects.prog_ja.logic.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HasherClass {

    private MessageDigest digest;
    private BCryptPasswordEncoder cryptPasswordEncoder;

    /**
     * initializing message digest to encrypt data
     * */
    public HasherClass(HashType type){

        try {

            String providerName;

            switch (type){
                case MD5:
                    digest = MessageDigest.getInstance("MD5");
                    break;

                case SHA_256:
                    digest = MessageDigest.getInstance("SHA-256");
                    break;

                case BCRYPT:
                    cryptPasswordEncoder = new BCryptPasswordEncoder(10);

                default:
                    digest = MessageDigest.getInstance("SHA-256");

            }


        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }


    }

    /**
     * hashing String
     * @return hashed string
     * */
    public String hash(String str){
        if(str == null || str.equals(""))
            return "";

        return hash(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * hash sum of bytes
     * @return hashed string
     * */
    public String hash(byte[] data){
        if(data == null)
            return "";

        return DatatypeConverter.printHexBinary(
                digest.digest(data)
        );
    }

    /**
     *
     * */
    public byte[] getHashBytes(String str){

        return getHashBytes(str.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] getHashBytes(byte[] data){

        return digest.digest(data);
    }

    public String getSalt(int length){

        /*
        * Создаём буфер для соли, получаем соль из класса SecureRandom,
        * парсим байты в строку и возвращаем
        * */
        byte[] saltBytes = new byte[length];
        new SecureRandom().nextBytes(saltBytes);

        return DatatypeConverter.printHexBinary(saltBytes);
    }

    public String bCrypt(String str){
        return cryptPasswordEncoder.encode(str);
    }

}
