

let params = window.get_parameters();

function initCategories() {

    for(let i = 0; i < arguments.length; i++){
        initCategory(arguments[i]);
    }

    function initCategory(contentId){

        const contentPanel = document.getElementById(contentId),
             elementNodes = contentPanel.getElementsByClassName("category-select");

        Array.forEach(elementNodes, function (element) {

            if(element.getAttribute("content") === params[element.getAttribute("datatype")])
            {
                element.classList.add("active-category");
                contentPanel.getElementsByTagName("input")[0].value = element.getAttribute("content");
            }

            element.addEventListener("click", function (e){
                let element = e.currentTarget,
                    param = element.getAttribute("datatype"),
                    value = element.getAttribute("content");

                if(params[param] !== undefined && params[param] === value){
                    delete_get(param);
                }
                else if (params[param] !== undefined){
                    change_get(param, value);
                }else{
                    if(Object.keys(params).length === 0){
                        location.search = "?"+param+"="+value;
                    } else{
                        location.search += "&"+param+"="+value;
                    }
                }
            });
        });
    }

    /*
    * Добавляем переменную для текущей панели,
    * при клике на кнопку открытия закрываем текущую и открываем новую,
    * делаем её текущей
    * */
    let current = undefined,
        categoryBtns = document.getElementsByClassName("category-btn");
    Array.forEach(categoryBtns, function (categoryBtn) {
        categoryBtn.addEventListener("click", function (e) {
            const target = e.currentTarget,
                id = target.getAttribute("content");
            let newPanel = document.getElementById(id);

            if(current === undefined){
                current = newPanel;
                current.style.display = "none";
            }

            if(current !== newPanel){

                current.style.display = "none";

                current = newPanel;
                current.style.display = "block";
                current.classList.add("edited");

            }else{
                /*
                * Добавлен класс маркер edited, чтобы при клике на кнопку
                * не срабатывало закрытие с документа
                * */
                if(newPanel.style.display === "none"){
                    newPanel.style.display = "block";
                    current.classList.add("edited");
                }else{
                    current.style.display = "none";
                }
            }
        })
    });
    /*
    * При любом клике вне кнопки закрываем панель
    * */
    document.addEventListener("click", function (ev) {
        if(current !== undefined ){
            if(!current.classList.contains("edited")){
                current.style.display = "none";
            }
            current.classList.remove("edited");
        }
    }, false);

}

function initSearch() {
    let query = params["q"];
    if(query !== undefined){
        document.getElementsByClassName("search-input")[0].value = query;
    }
}
const smallHelpers = {
    helpersContainer: document.querySelector(".small-helpers")
};
    function initSmallHelpers() {
        for(let param in params)
        {
           addHelper(param);
        };
    }
    function addHelper(param) {
        if(params[param] === ""){
            return;
        }
        let helper = document.createElement("div");

        helper.classList.add("small-helper", "blue");
        helper.setAttribute("content", param);
        helper.innerHTML = getNameByTypeAndValue(param, params[param]);

        helper.addEventListener("click", function (event) {
            delete_get(this.getAttribute("content"));
            smallHelpers.helpersContainer.removeChild(this);
        });

        smallHelpers.helpersContainer.appendChild(helper);
    }
    function getNameByTypeAndValue(type, value) {
        let typeNames = {
            "type": {
                "rating" : "Рейтинг",
                "date": "Новые",
                "moder": "Модераторы"
            },
            "sort" : {
                "1" : "По убыванию",
                "0" : "По возрастанию"
            },
            "difficult": {
                "easy": "Легкие",
                "normal": "Средние",
                "hard": "Сложные",
                "hell": "Невозможные"
            }
        };

        if(type === "q"){
            return value;
        }
        else{
            return typeNames[type][value];
        }
    }

function removeEmptyFields(event) {
    const inputs = document.querySelectorAll(".sort-form input");
    Array.forEach(inputs, function (input) {
        if(input.value === undefined || input.value === ""){
            input.setAttribute("disabled", "disabled")
        }
    });
}

function initSpecialSorted(){
    const special_sort = document.getElementById('special_sort');
    if(special_sort === undefined || special_sort === null){
        return;
    }
    const helper = document.createElement("div");

    helper.classList.add("small-helper", "blue");
    helper.innerText = special_sort.getAttribute('name');

    helper.addEventListener("click", function (event) {
        location.href = location.href.replace(special_sort.getAttribute('content')+'/', '');
    });

    smallHelpers.helpersContainer.appendChild(helper);
}


initSearch();
initSpecialSorted();
initSmallHelpers();

