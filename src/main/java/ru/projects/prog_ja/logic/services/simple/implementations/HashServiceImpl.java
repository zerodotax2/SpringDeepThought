package ru.projects.prog_ja.logic.services.simple.implementations;

import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.services.simple.interfaces.HashService;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
@Scope("prototype")
public class HashServiceImpl implements HashService {


    @Override
    public String hash(String str, HashType type) {

        return hash(str.getBytes(StandardCharsets.UTF_8) ,type);
    }

    @Override
    public String hash(byte[] data, HashType type) {

        if(data == null)
            return null;

        return DatatypeConverter.printHexBinary(getCrypter(type).digest(data));
    }

    @Override
    public byte[] getHashBytes(String str, HashType type) {

        return getHashBytes(str.getBytes(StandardCharsets.UTF_8), type);
    }

    @Override
    public byte[] getHashBytes(byte[] data, HashType type) {

        return getCrypter(type).digest(data);
    }

    @Override
    public String bcrypt(String str) {

        return new BCryptPasswordEncoder(10).encode(str);
    }

    @Override
    public String getSalt(int length) {
        /*
         * Создаём буфер для соли, получаем соль из класса SecureRandom,
         * парсим байты в строку и возвращаем
         * */
        byte[] saltBytes = new byte[length];
        new SecureRandom().nextBytes(saltBytes);

        return DatatypeConverter.printHexBinary(saltBytes);
    }

    private MessageDigest getCrypter(HashType type){
        try {
           return MessageDigest.getInstance(type.toString());
        } catch (NoSuchAlgorithmException e) {
            try {
                return MessageDigest.getInstance(HashType.SHA_256.toString());
            } catch (NoSuchAlgorithmException e1) {
                return null;
            }
        }
    }
}
