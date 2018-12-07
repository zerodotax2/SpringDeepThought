'use strict';
let updating = false;
function initRestore() {
    const token = document.getElementById('token').getAttribute('content'),
        passwordField = document.getElementById('password'),
        confirmPasswordField = document.getElementById('confirmPassword'),
        updateButton = document.querySelector('.restore .action');

    updateButton.addEventListener('click', function () {

        if(updating)
            return;

        if(passwordField.value.length < 8 || passwordField.value.length > 64 ||
            !passwordField.value.match(/^[A-z|А-я|0-9|_]+$/)){
            modal.error('Неверный формат пароля'); return;
        }
        if(confirmPasswordField.value !== passwordField.value){
            modal.error('Пароли не совпадают'); return;
        }

        updating = true;
        modal.load('Идёт обновление пароля');
        xhr.request({
            path: '/services/restore/update',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            content: JSON.stringify({
                password: passwordField.value,
                confirmPassword: confirmPasswordField.value,
                token: token
            })
        }, function (response, error) {
            if(response){
                let auth = document.createElement('a');
                auth.href = '/login';
                auth.classList.add('auth', 'blue');
                auth.innerText = 'Войти';
                modal.customActions(auth);
                modal.head.color = modal.infoColor;
                modal.head.innerText = 'Информация';
                modal.content.innerHTML = 'Ваш пароль был успешно обновлен, авторизуйтесь на сайте.';
                modal.show();
            }else if(error){
                modal.error('Не удалось обновить пароль, попробуйте позже')
            }
        });
        updating = false;
    });
}

initRestore();