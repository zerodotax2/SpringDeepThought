document.addEventListener("DOMContentLoaded", function () {
    initModal();
})

window.info = {
    open : openModal
};

var modal_window, modal_instance;
function initModal() {

    var elems = document.querySelectorAll(".modal");
    var instances = M.Modal.init(elems, {
        startingTop: '50%',
        endingTop: '20%'
    });

    modal_instance = instances[0];

    modal_window = document.getElementById("modal-info");

    document.getElementById("modal-info-close").addEventListener("click", function (ev) {
        modal_instance.close();
    });
}

function openModal(html, type) {

    var color;
    switch (type){
        case 'error':
            color = '#F44336';
            break;
        case 'info':
            color = '#42A5F5';
            break;
        default: return;
    }

    var modal_content = modal_window.getElementsByClassName("modal-content")[0];
    modal_content.innerHTML = html;

    modal_window.getElementsByClassName("btn")[0].style.background = color;

    modal_instance.open();
}