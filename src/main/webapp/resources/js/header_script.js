document.addEventListener("DOMContentLoaded", function () {

    //Materialize initialize
    var sidenavOptions = {

    }
    var elemsSidenav = document.querySelectorAll('.sidenav');
    var instancesSidenav = M.Sidenav.init(elemsSidenav, sidenavOptions);

    var dropdownOptions = {
        constrainWidth: false,
        inDuration: 300,
        outDuration: 300,
        coverTrigger: false,
        hover: true,
        alignment: 'left'
    }
    var elemsDropdown = document.querySelectorAll('.dropdown-trigger');
    var instancesDropdown = M.Dropdown.init(elemsDropdown, dropdownOptions);

    var modalOptions = {
        endingTop: "5%"
    };
    var elemsModal = document.querySelectorAll('.modal');
    var instancesModal = M.Modal.init(elemsModal, modalOptions);


})