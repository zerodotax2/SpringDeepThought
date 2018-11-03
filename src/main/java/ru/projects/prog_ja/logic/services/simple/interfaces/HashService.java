package ru.projects.prog_ja.logic.services.simple.interfaces;

import ru.projects.prog_ja.logic.services.simple.implementations.HashType;

public interface HashService {

    String hash(String str, HashType type);

    String hash(byte[] data, HashType type);

    byte[] getHashBytes(String str, HashType type);

    byte[] getHashBytes(byte[] data, HashType type);

    String bcrypt(String str);

    String getSalt(int length);

}
