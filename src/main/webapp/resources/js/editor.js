let tags = document.getElementsByClassName("tags")[0],
    hidden_tags = document.getElementsByClassName("hidden_tags")[0],
    chips = document.getElementsByClassName("chips")[0],
    upload = document.getElementById("upload"),
    previewImage = document.getElementById("previewImage");

document.addEventListener("DOMContentLoaded", function () {


    let title_input = document.getElementsByClassName("title_input")[0];
    title_input.addEventListener("input keyup", function () {
        if(title_input.value.length >= 100){
            title_input.value = title_input.value.substring(0, 100);
        }
    });
    let subtitle_input = document.getElementsByClassName("subtitle_input_input")[0];
    subtitle_input.addEventListener("input keyup", function () {
        if(subtitle_input.value.length >= 100){
            subtitle_input.value = subtitle_input.value.substring(0, 100);
        }
    });


    let chipsOptions = {
        secondaryPlaceholder: '+ Добавить',
        limit: 10,
        minLength: 3,
        onChipAdd: chipAdd(),
        onChipDelete: chipDelete()
    }
    let elemsChips = document.querySelectorAll('.chips');
    let instances = M.Chips.init(elemsChips,chipsOptions);

    upload.addEventListener("change", uploadFile);

});

    function chipDelete() {

        let count = document.getElementsByClassName("chip").length;

        /*
       * Удаляем текст, который описывает вводимые тэги
       */
        document.getElementsByClassName("tag_helper")[0].remove();

        if(count == 0){
            tags.innerHTML = tags.innerHTML + " <h6 class='grey-text text-lighten-1 tag_helper'>" +
                "                            Введите название и нажмите enter" +
                "                        </h6>";
        }else{
            tags.innerHTML = tags.innerHTML +" <h6 class='grey-text text-lighten-2 tag_helper'>" +
                "                            Тегов осталось: "+(5-count)+"" +
                "                        </h6>";
        }

        /*После каждого добавления тега, записываем их в hidden форму, чтобы можно было отправить*/
        hidden_tags.value = chips.innerHTML.substring(0, chips.innerHTML.indexOf("<input"));

    }

    function chipAdd() {
        /*Получаем длину текущих тегов*/
        let count = document.getElementsByClassName("chip").length;
        /*
        * Удаляем текст, который описывает вводимые тэги
        */
        document.getElementsByClassName("tag_helper")[0].remove();

        /*
        Проверяем,если кол-во тегов больше 9, то
        * делаем красную надпись, что достигнуто макс. кол-во
        */
        if(count > 4){
            tags.innerHTML = tags.innerHTML + " <h6 class='red-text text-lighten-2 tag_helper'>" +
                "                            Достигнуто максимальное количество тегов: 5" +
                "                        </h6>";
            /*Если тегов не на максимум, то пишем сколько тегов осталось*/
        }else{
            tags.innerHTML = tags.innerHTML +" <h6 class='grey-text text-lighten-1 tag_helper'>" +
                "                            Тегов осталось: "+(5-count)+"" +
                "                        </h6>";
        }

        //Исключаем слово close из тега
        let smallChips = document.getElementsByClassName("chip");
        let lastChip = smallChips[smallChips.length-1];

        lastChip.innerHTML = lastChip.innerHTML.substring(0, lastChip.innerHTML.indexOf("<i")) + "<span class='close' >x</span>";
        //Делаем для последнего тега рандомный цвет
        lastChip.classList.add(randomClass());

        /*После каждого добавления тега, записываем их в hidden форму, чтобы можно было отправить*/
        hidden_tags.value = chips.innerHTML.substring(0, chips.innerHTML.indexOf("<input"));
    }

    //Функция, которая присваивае рандомный фон тегу из доступных в Materialize css
    function randomClass() {
        let classes = ["cyan", "red", "pink", "purple", "blue", "indigo", "deep-purple", "teal", "light-blue",
                "green", "lime", "light-green", "yellow", "amber", "orange", "deep-orange", "brown",
                "grey", "black", "blue-grey"],
            styles = ["lighten-1", //"lighten-5", "lighten-4", "lighten-3", "lighten-2",
                "darken-1", "darken-2", "darken-3", "darken-4",
                "accent-1", "accent-2", "accent-3", "accent-4"];
        let random1 = Math.ceil(Math.random() * classes.length);
        let random2 = Math.ceil(Math.random() * styles.length);
        let s = classes[random1] + " white-text " + styles[random2];

        return s;
    }

    function uploadFile() {

        let ajax = xhr.get();
        let file = upload.files[0];

        ajax.onreadystatechange = function (){

        };

        xhr.file("/upload", upload.files[0], ajax);

    }