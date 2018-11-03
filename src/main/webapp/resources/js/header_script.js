document.addEventListener("DOMContentLoaded", function () {

    initMaterialize();
    initUserMenu();

});

function initUserMenu() {
    const userButton = document.querySelector('.user-account .user'),
        menu = document.querySelector('.user-account .menu');
    if(userButton !== null && menu !== null){
        userButton.addEventListener('click', function (e) {
            menu.style.display = 'block';
        });
        document.addEventListener('click', function (e) {
            menu.style.display = 'none';
        }, true)
    }
}

function initMaterialize() {
    //Materialize initialize
    let sidenavOptions = {

    }
    let elemsSidenav = document.querySelectorAll('.sidenav');
    let instancesSidenav = M.Sidenav.init(elemsSidenav, sidenavOptions);

}