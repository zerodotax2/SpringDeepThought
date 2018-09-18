document.addEventListener("DOMContentLoaded", function () {

    var bg_image = document.getElementById("article_bg_image").getAttribute("content");
    document.body.style.background = ' url('+bg_image+') no-repeat center / cover fixed';

});