var tabs = {},
    currentTab = null,
    userId = 0,
    fileServerLocation = "";

document.addEventListener("DOMContentLoaded", function () {

    initStats();
    initTabs();

    userId = document.getElementById("user_id").getAttribute("content");
    if(userId === undefined || userId == null){
        userId = 1;
    }

    fileServerLocation = document.getElementById("fileServerLocation").getAttribute("content");

});

function initStats() {

    var stats = document.getElementsByClassName("stat-number");
    for(var i = 0; i < stats.length; i++){
        if(stats[i].innerHTML > 0){
            stats[i].classList.add("red");
            stats[i].style.color = 'white';
        }
    }

}

function initTabs() {

    currentTab = document.getElementById("article-tab");

    tabs.articles = {
        tab: document.getElementById('articles'),
        path: '/services/articles/user/',
        item: articleCollection,
        items: undefined
    };
    tabs.questions = {
        tab: document.getElementById('questions'),
        path: '/services/questions/user/',
        item: questionCollection,
        items: undefined
    };
    tabs.problems = {
        tab: document.getElementById('problems'),
        path: '/services/articles/user/',
        item: problemCollection,
        items: undefined
    };


    var tab_items = document.getElementsByClassName("tab-element");
    for(var i = 0; i<tab_items.length;i++){

        tab_items[i].addEventListener("click", setActiveTab)

    }

}

var tab_changed = false;

function setActiveTab() {

    if(currentTab === this || tab_changed){
        return
    }
    tab_changed = true;

    /*
    * Изменяем стиль выделленного таба
    * */

    var oldImg, newImg;
    oldImg = currentTab.firstChild;
    newImg = this.firstChild;

    currentTab.classList.remove("active-tab");
    oldImg.src = oldImg.src.substring(0, oldImg.src.lastIndexOf(".svg")-1) + '.svg';

    this.classList.add("active-tab");
    newImg.src = newImg.src.substring(0, newImg.src.lastIndexOf(".svg")) + '2.svg';

    /*
    * Получаем ссылку на таб
    * */
    var new_tab_open_id = this.getAttribute('content'),
        old_tab_open_id = currentTab.getAttribute('content');

    tabs[old_tab_open_id].tab.style.display = 'none';

    var element = tabs[new_tab_open_id];
    element.tab.style.display = 'block';

    /*
    * Если итемов нет в коллекции отправляем запрос
    * */
    if(element.items === undefined){
        sendRequest(element);
        /*
        * Если итемы уже есть, то проверяем, показываем итемы либо пустую панель
        * */
    }else if(element.items != null && element.items.length > 0){
        fullCollection(element.items, element.tab, element.item);
    }else{
        showEmpty(element.tab);
    }


    currentTab = this;
    tab_changed = false;

}

function sendRequest(element) {

    var tab = element.tab,
        path = element.path,
        collectionItem = element.item;

    /*
    * Делаем картинку загрузки
    * */
    tab.innerHTML = loadDiv();

    var ajax = xhr.get();

    ajax.onreadystatechange = function (ev) {

        if(ajax.readyState != 4){
            return;
        }

        /*
        * Если статус не 200 выводим ошибку
        * */
        if(ajax.status != 200){
            tab.innerHTML = "<div class='grey-text error-div'><span>&#9785;</span>Error</div>";
            return;
        }

        /*
        * Делаем parse ответа и присваиваем результат в массив панелей
        * */
        var array = JSON.parse(ajax.responseText);
        element.items = array;

        /*
        * Если массив не нулевой, то заполняем панель, иначе выводим пустую панель
        * */
        if(array != null && array.length > 0){
            fullCollection(array, tab, collectionItem);
        }else{
            showEmpty(tab);
        }


    };

   ajax.open("GET", path, true);
   ajax.send();
}

function fullCollection(array, tab, collectionItem) {

    tab.innerHTML = "<div class='collection' style='border: none'>";

    for (var i = 0; i < array.length; i++) {
        tab.innerHTML = tab.innerHTML + collectionItem(array, i);
    }

    tab.innerHTML = tab.innerHTML + "</div>";

}

function showEmpty(tab) {

    tab.innerHTML = "<div style='display: flex;padding: 10%;justify-content: center;'>" +
        "<h5 class='grey-text'>Нет информации</h5>" +
        "</div>";

}

function loadDiv(){

    return "<div class='load-div' style='display: flex;justify-content: center;padding: 10%;'>" +
        "                                <div class='preloader-wrapper active'>" +
        "                                    <div class='spinner-layer spinner-blue-only'>" +
        "                                          <div class='circle-clipper left'>" +
        "                                            <div class='circle'></div>" +
        "                                        </div><div class='gap-patch'>" +
        "                                        <div class='circle'></div>" +
        "                                    </div><div class='circle-clipper right'>" +
        "                                        <div class='circle'></div>" +
        "                                    </div>" +
        "                                    </div>" +
        "                                </div>" +
        "                            </div>";
}


function articleCollection(array, i) {

    var element = array[i];

    var chips = element.chips;
    var chipsHtml = "";
    for(var j = 0; j < chips.length; j++){
        chipsHtml += " <a href='/tags/"+chips[j].id+"'><div style='background: "+chips[j].color+"' class='chip'>"+chips[j].name+"</div></a>"
    }

    return "<div class='collection-item' style='display: flex;'>" +
        "                                    <img src='"+fileServerLocation+element.articleImage+"'/>" +
        "                                    <div class='article-text'>" +
        "                                        <span class='title grey-text text-darken-2'>"+element.title+"</span>" +
        "                                        <div class='chips-line' style='margin-top: 5px;'>" +
                                                    chipsHtml +
        "                                        </div>" +
        "                                    </div>" +
        "                                </div>";

}

function questionCollection(array, i) {

    var element = array[i];

    var chips = element.chips;
    var chipsHtml = "";
    for(var j = 0; j< chips.length; j++){
        chipsHtml += "<a href='/tags/"+chips[j].id+"' >" +
            " <div class='chip' style='background: "+chips[j].color+"'>" +
            ""+chips[j].name+"</div></a>";
    }


    return "<div class='collection-item'>" +
        "                                    <div class='title grey-text text-darken-2' style='padding: 0 5px 0 5px;'>"+element.title+"</div>" +
        "                                    <div class='question-line'>" +
        "                                        <div class='user-asker'><img src='/resources/images/user-regular.svg'/> <span class='grey-text'>"+element.user.login+"</span> </div>" +
        "                                        <div class='question-date'><img src='/resources/images/date.svg'><span class='grey-text'>"+element.createDate+"</span></div>" +
        "                                    </div>" +
        "                                    <div class='question-tags'>" +
                                                chipsHtml +
        "                                    </div>" +
        "                                </div>";
    
}

function problemCollection(array, i) {

    var element = array[i];

    var chips = element.chips;
    var chipsHtml = "";
    for(var j = 0; j < chips.length; j++){
        chipsHtml = "<a href='/tags/"+chips[j].id+"'><div class='chip' style='background: "+chips[j].color+"' >"+chips[j].name+"</div></a>";
    }

    return "<div class='collection-item'>" +
        "                                    <a href='/problems/"+element.id+"' class='title grey-text'>"+element.title+"</a>" +
        "                                    <div class='problem-line'>" +
        "                                        <div class='problem-element'>" +
        "                                            <img src='/resources/images/star-regular.svg'/>" +
        "                                            <span class='grey-text' >"+element.rating+"</span>" +
        "                                        </div>" +
        "                                        <div class='problem-element'>" +
        "                                            <img src='/resources/images/user-regular.svg'/>" +
        "                                            <span class='grey-text' >"+element.decided+"</span>" +
        "                                        </div>" +
        "                                        <div class='problem-element'>" +
        "                                            <span class='white-text "+element.difficult+"' style='padding: 5px 10px 5px 10px;border-radius: 5px;margin-top: -5px;'>Easy</span>" +
        "                                        </div>" +
        "                                    </div>" +
        "                                    <div class='problem-chips'>" +
                                                chipsHtml +
        "                                    </div>" +
        "                                </div>";

}