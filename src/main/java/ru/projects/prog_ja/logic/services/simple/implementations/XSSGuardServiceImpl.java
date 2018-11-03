package ru.projects.prog_ja.logic.services.simple.implementations;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.services.simple.interfaces.XSSGuardService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Scope("prototype")
public class XSSGuardServiceImpl implements XSSGuardService {

    @Override
    public String replaceScript(String html) {
        /*check if string is not valid*/
        if(html == null || html.length() == 0){
            return html;
        }

        /*create pattern and final strings*/
        final String PATTERN_STRING = "<[. | \\n]*?script[. | \\n]*?>";
        final String replace1 = "&lt;"; // <
        final String replace2 = "&gt;"; // >

        /*create matcher to this string*/
        Pattern pattern = Pattern.compile(PATTERN_STRING);
        Matcher matcher = pattern.matcher(html.trim());

        /*replacing all scripts*/
        StringBuilder builder = new StringBuilder(html);
        while(matcher.find()){
            int start = matcher.start(), end = matcher.end();

            builder.replace(start , start, replace1);
            builder.replace(end + replace1.length(), end + replace1.length(), replace2 );

        }

        /*return new string*/
        return builder.toString();
    }
}
