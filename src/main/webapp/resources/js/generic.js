'use strict';
window.appConf = {
    fileServerLocation: document.getElementById('fileServerLocation').getAttribute('content'),
    csrfHeader: document.getElementById('_csrf_header').getAttribute('content'),
    csrfToken: document.getElementById('_csrf').getAttribute('content'),
    user_id: document.getElementById('user_id').getAttribute('content')
};
const tagsCache = {},
    tagPopup = document.createElement('div'),
    chips = document.querySelectorAll('.chip'),
    main = document.getElementById("main") || document.querySelector(".main");
let timer = null;
function initChips(){

    if(main === null)
	    return;

    tagPopup.setAttribute('id', 'tag-popup');
    tagPopup.style.display = 'none';
    tagPopup.addEventListener('click', function(){
	tagPopup.style.display = 'block';
    });
    document.addEventListener('click', function(){
	tagPopup.style.display = 'none';
    }, true);
    main.appendChild(tagPopup);

    for(let i = 0; i < chips.length; i++) {
        let chip = chips[i];
        chip.addEventListener("mouseenter", function (event) {
            clearInterval(timer);

            let target = event.currentTarget,
                href = target.getAttribute("href"),
                tagID = href.substring(href.lastIndexOf("/") + 1, href.length);

            if(tagsCache[tagID] === undefined){
                timer = setTimeout(loadTag, 500, tagID, target);
            }else{
                timer = setTimeout(showTag, 500, tagsCache[tagID], target);
            }
        });
        chip.addEventListener("mouseout", function () {
            tagPopup.style.display = 'none';
            tagPopup.style.opacity = '0';
        });
    }
}

function loadTag(tagID, chip) {
    let ajax = new XMLHttpRequest();
    ajax.onreadystatechange = function(){
        if(ajax.readyState !== 4){
            return;
        }
        if(ajax.status !== 200){
            return;
        }
        let tag = JSON.parse(ajax.responseText);
        tagsCache[tagID] = tag;
        showTag(tag, chip);
    };
    ajax.open("GET", "/services/tags/" + tagID, true);
    ajax.setRequestHeader(appConf.csrfHeader, appConf.csrfToken);
    ajax.setRequestHeader("X-Requested-With", "XMLHttpRequest");
    ajax.send();
}

function showTag(tag, chip) {
    tagPopup.innerHTML = "<div class='card-content'>" +
        "                <div class='describe'>"
                            + tag.description +
        "                </div>" +
        "                <div class='nums'>" +
        "                    <div class='num-element'>" +
        "                        <div class='header'>Статьи</div><div class='num'>"+tag.articles+"</div>" +
        "                    </div>" +
        "                    <div class='num-element'>" +
        "                        <div class='header'>Вопросы</div><div class='num'>"+tag.questions+"</div>" +
        "                    </div>" +
        "                    <div class='num-element'>" +
        "                        <div class='header'>Задачи</div><div class='num'>"+tag.problems+"</div>" +
        "                    </div>" +
        "                </div>" +
        "            </div>";
    tagPopup.style.border = '2px solid ' + tag.color;

    let coordinates = getCoords(chip);

    tagPopup.style.left = coordinates.left - chip.clientWidth/2 - tagPopup.clientWidth/2 + "px";
    tagPopup.style.top = coordinates.top + chip.clientHeight + 5+ "px";

    tagPopup.style.display = 'block';

    let count = 0;
    setInterval(function () {
        count += 0.05;
        if(count <= 1.04){
            tagPopup.style.opacity = count;
        }else{
            clearInterval(this);
        }
    },1000/60);
}

window.getCoords = function(elem) {
    let box = elem.getBoundingClientRect();

    let body = document.body;
    let docEl = document.documentElement;
    
    let scrollTop = window.pageYOffset || docEl.scrollTop || body.scrollTop;
    let scrollLeft = window.pageXOffset || docEl.scrollLeft || body.scrollLeft;
    
    let clientTop = docEl.clientTop || body.clientTop || 0;
    let clientLeft = docEl.clientLeft || body.clientLeft || 0;
    
    let top = box.top + scrollTop - clientTop;
    let left = box.left + scrollLeft - clientLeft;

    return {
        top: top,
        left: left
    };
};

window.get_parameters = function getParameters() {
    return window.location
        .search
        .replace('?','')
        .split('&')
        .reduce(
            function(p,e){

                let a = e.split('=');
                if(a[0] === "")
                    return p;

                p[ decodeURIComponent(a[0])] = decodeURIComponent(a[1]);
                return p;
            },
            {}
        );
};

window.change_get = function (parameter, value) {

    let url = location.search,
        firstPartOfUrl = url.substring(0, url.lastIndexOf(parameter) + parameter.length + 1),
        secondPartOfUrl = url.substring(url.lastIndexOf(parameter) + parameter.length + 1, url.length);

    let newSecondPartOfUrl;
    if(secondPartOfUrl.indexOf("&")!==-1){
       newSecondPartOfUrl  = value + secondPartOfUrl.substring(secondPartOfUrl.indexOf("&"), secondPartOfUrl.length);
    }else{
        newSecondPartOfUrl = value;
    }


    location.search = firstPartOfUrl + newSecondPartOfUrl;
};

window.delete_get = function(parameter){

    let parameters = get_parameters();
    let query = "?";
    for(let key in parameters){
        if(key === parameter){
            continue;
        }
        query += key + "=" + parameters[key] + "&";
    }
    query = query.substring(0, query.length - 1);

    location.search = query;
};

window.construct_get = function(params){

    let query = "?", param_names = Object.keys(params);
    for(let i = 0; i < param_names.length; i++) {
       query += "&"+param_names[i]+"="+params[param_names[i]];
    }

    return query;
}

initChips();