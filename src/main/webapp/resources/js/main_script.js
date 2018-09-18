document.addEventListener("DOMContentLoaded" ,function () {

    /*Если зашли с планшета или меньше, то ставим нижние блоки други за другом, и центрируем их*/
    var askText = document.getElementsByClassName("ask_text")[0];
    var postText = document.getElementsByClassName("post_text")[0];
    if(screen.width < 993){
        askText.parentNode.insertBefore(document.getElementById("ask_text_prev") ,askText);
        askText.add("center-align").remove("left-align");
        postText.classList.add("center-align").remove("right-align");
    }

    var elemsParallax = document.querySelectorAll('.parallax');
    var instancesParallax = M.Parallax.init(elemsParallax, {
        responsiveThreshold : 200
    });

});