let sending = false;
function  initRestore() {
    const email = document.getElementById('email'),
        restore = document.querySelector('.restore');
    restore.addEventListener('click', function () {
        if(sending){
            return;
        }
        const value = email.value;
        if(value.length < 5 || value.length > 50){
            modal.error('Неверный размер E-mail');
            return;
        }else if(!value.match(/^[A-z|0-9|_]+?@[A-z]+?\.[A-z]+?$/)){
            modal.error('Неверный формат E-mail');
            return;
        }
        sending = true;
        xhr.request({
            path: '/services/restore',
            method: 'POST',
            content: JSON.stringify({
                email: value
            })
        }, function (response, error) {
            if(response){
                let ok = document.createElement('a');
                ok.href = '/login';
                ok.classList.add('action-btn');
                ok.innerText = 'Авторизация';
                ok.style.background = modal.infoColor;
                modal.customActions(ok);
                modal.head.style.background = modal.loadColor;
                modal.head.innerText = 'Успешно';
                modal.content.innerHTML = 'Письмо было отправлено';
                modal.show();
            }else if(error){
                modal.error('Не удалось отправить письмо, попробуйте позже');
                return;
            }
            sending = false;
        });
        modal.load('Отправляется письмо');
    });
}

initRestore();