'use strict';
const send = document.getElementById('send');

function initSend() {
    send.addEventListener('click', function () {
        modal.load('Идёт отправка письма');
        xhr.request({
            method: 'POST',
            path: '/services/register/email'
        }, function (response, error) {
            if(response){
                modal.info('Email был успешно отправлен на ' + response.email);
            }else if(error){
                modal.error('Не удалось отправить email');
            }
        })
    });
}
initSend();