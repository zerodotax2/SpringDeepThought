var body = document.body,
    html = document.documentElement,
    height = Math.max( body.scrollHeight, body.offsetHeight,
        html.clientHeight, html.scrollHeight, html.offsetHeight ),
    postContainer = document.getElementById("post_container");


document.addEventListener("DOMContentLoaded" ,function () {

    var isRendering = false,
    postsOnPage = 10;

    window.addEventListener("scroll", function (ev) {

        if(document.documentElement.clientHeight + document.documentElement.scrollTop >= (document.body.scrollHeight - 200) && !isRendering){

            var ajax = xhr.get();

            isRendering = true;

            ajax.onreadystatechange = function () {

                if(this.readyState != 4)
                    return;

                if(this.status != 200)
                    return;

                var data = JSON.parse(ajax.responseText);
                var array = data;

                if(array.length > 0) {
                    for (var i = 0; i < array.length; i++) {
                        var div = document.createElement("div");
                        div.className = " card col z-depth-2 s12 ";
                        div.style.marginBottom = "20px";
                        div.innerHTML = "<div class='card-image row' >" +
                            "                    <img class='articleImage' src='http://locahost:81/"+array[i].articleImage+"'/>" +
                            "                </div>" +
                            "                <div class='card-content' style='margin-top: -40px'>" +
                            "                    <h5 class='card-title grey-text text-darken-4' >"+array[i].title+"</h5>" +
                            "                    <div class='date valign-wrapper' style='display: flex;margin-top: -10px;'>" +
                            "                        <h6 class='grey-text' >"+array[i].createDate+"</h6>" +
                            "                        <img src='/resources/images/date.svg' style='width: 17px; height: 17px;margin-left: 5px;margin-top: 5px;'/>" +
                            "                    </div>" +
                            "                </div>" +
                            "                <div class='divider'></div>" +
                            "                <div class='row' style='display: flex;padding: 15px 20px 0 20px'>" +
                            "                    <div class='views' style='display: flex'>" +
                            "                        <h6 class='grey-text' style='margin-left: 15px;'>"+array[i].views+"</h6>" +
                            "                        <img src='/resources/images/eye.svg'  style='width: 17px; height: 17px; margin: 13px; margin-left: 5px' />" +
                            "                    </div>" +
                            "                    <div class='comments' style='display: flex'>" +
                            "                        <h6 class='grey-text' style='margin-left: 15px;'>"+array[i].commentsNum+"</h6>" +
                            "                        <img src='/resources/images/comment.svg' style='width: 17px; height: 17px; margin: 13px;margin-left: 5px'/>" +
                            "                    </div>" +
                            "                    <a href='/articles/"+array[i].id+"' class='btn col s5 l3 red waves-effect waves-light'>Читать</a>" +
                            "                </div>";

                        postContainer.appendChild(div);
                    }
                }

            }

            ajax.open('POST', 'http://localhost:8080/articles/render', true);

            ajax.setRequestHeader("Content-Type", "application/json");
            ajax.setRequestHeader("Cache-Control", "max-age=604800");
            xhr.headers(ajax);

            var sendData = JSON.stringify({
                size: 10,
                postsOnPage: postsOnPage
            });

            ajax.send(sendData);

            postsOnPage += 10;
            isRendering = false;
        }
    });
});