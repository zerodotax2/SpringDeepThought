package ru.projects.prog_ja.logic.caches.local;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.caches.interfaces.UsersCache;

@Service
@Scope("singleton")
public class UsersLocalCache implements UsersCache {
}
