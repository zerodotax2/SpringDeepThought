package ru.projects.prog_ja.logic.singletons.implementations;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.singletons.interfaces.FlatColorsLocal;

import java.util.Random;

@Service
@Scope("singleton")
public class FlatColorsLocalSingleton implements FlatColorsLocal {

    /**
     * array of flat colors
     * */
    private final String[] colors = new String[]{
            "#F44336","#E91E63","#9C27B0","#673AB7","#3F51B5",
            "#2196F3","#03A9F4","#00BCD4","#009688","#4CAF50",
            "#8BC34A","#CDDC39","#FFEB3B","#FFC107","#FF9800",
            "#FF5722","#795548","#9E9E9E","#607D8B","#1abc9c",
            "#2ecc71","#3498db","#9b59b6","#34495e","#16a085",
            "#27ae60","#27ae60","#2980b9","#8e44ad","#2c3e50",
            "#e67e22","#e74c3c","#95a5a6","#f39c12","#d35400",
            "#c0392b","#7f8c8d","#b71540","#38ada9","#079992",
            "#fa983a","#e58e26","#e55039","#fa983a","#78e08f"
    };

    private final Random random = new Random(colors.length);

    /**
     * @return one random color from array
     * */
    @Override
    public String color() {

        return colors[random.nextInt()];
    }


}
