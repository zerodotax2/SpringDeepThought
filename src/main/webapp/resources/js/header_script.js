'use strict';
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
const notifications = {
    loading: false,
    init: false
};
function initUserNotifications() {

    if(appConf.user_id === '-1')
        return;

    notifications.container = document.querySelector('.user-notifications');
    notifications.btn = notifications.container.querySelector('.action');
    notifications.panel = notifications.container.querySelector('.panel');
    notifications.loader = notifications.container.querySelector('.loader');
    notifications.empty = notifications.container.querySelector('.empty');
    notifications.error = notifications.container.querySelector('.error');

    notifications.btn.addEventListener('click', function () {
        if(notifications.loading || notifications.init){
            notifications.panel.style.display = 'block';
            return;
        }
        notifications.panel.style.display = 'block';
        notifications.loading = true;
        notifications.loader.style.display = 'flex';
        xhr.request({
            method: 'GET',
            path: '/services/users/notices/'+appConf.user_id
        }, function (response, error) {
            if(response){
                const notices = JSON.parse(response);
                if(notices.length === 0){
                    notifications.empty.style.display='block';
                }else{
                    for(let i = 0; i < notices.length; i++){
                        let div = document.createElement('div');
                        div.classList.add('element');
                        div.innerHTML = notices[i].message;
                        notifications.panel.insertBefore(div, notifications.loader);
                    }
                }
                notifications.container.querySelector('.nums').style.display = 'none';
                notifications.init = true;
            }else if(error){
                notifications.error.style.display = 'block';
            }
            notifications.loading = false;
            notifications.loader.style.display = 'none';
        });
    });
    document.addEventListener('click', function () {
       notifications.panel.style.display = 'none';
    }, true);

}

function initMaterialize(){

    let sidenavElements = document.querySelectorAll('.sidenav');
    M.Sidenav.init(sidenavElements, {});

    let collapsibleElems = document.querySelectorAll('.collapsible'),
        userPanel = null;
    if(collapsibleElems.length > 0){
        userPanel = collapsibleElems[0].querySelector('.collapsible-body');
    }
    M.Collapsible.init(collapsibleElems, {
        accordion: false,
        onCloseEnd: function(){userPanel.style.display='none';},
        onOpenStart: function(){userPanel.style.display='block';}
    });
}
document.addEventListener('DOMContentLoaded', function () {
    initUserMenu();
    initUserNotifications();
    initMaterialize();
});


