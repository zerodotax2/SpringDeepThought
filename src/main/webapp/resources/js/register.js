
const formInputs = {
    loginInput: document.getElementById('login'),
    loginError: document.getElementById('login_error'),

    passwordInput: document.getElementById('password'),
    passwordError: document.getElementById('password_error'),

    confirmPasswordInput: document.getElementById('confirm-password') ,
    confirmPasswordError: document.getElementById('confirm_password_error'),

    emailInput: document.getElementById('email'),
    emailError: document.getElementById('email_error')
};
function initSubmit() {
    const submit = document.querySelector('.actions .send input'),
    form = document.querySelector('.reg-form');

    submit.onclick = function (e) {
        if(validLogin()
            && validPassword()
            && validConfirmPassword()
            && validEmail()){
            form.submit();
        }
    };

    deleteServerError(formInputs.loginInput);
    deleteServerError(formInputs.passwordInput);
    deleteServerError(formInputs.confirmPasswordInput);
    deleteServerError(formInputs.emailInput);
}
    function deleteServerError(elem) {
        elem.addEventListener('click', deleteListener);
        function deleteListener(e){
            const error = e.currentTarget.parentElement.querySelector('.server-error');
            if(error !== undefined && error !== null){
                error.style.display = 'none';
            }
            e.currentTarget.removeEventListener('click', deleteListener);
        }
    }
    function validLogin(){
        return valid({
            elem: formInputs.loginInput,
            error: formInputs.loginError,
            check: [{
                    bool: formInputs.loginInput.value.length >= 5 && formInputs.loginInput.value.length <= 32,
                    text: 'Длина логина должна быть от 5 до 32 символов'
                },{
                    bool: /^[A-z|0-9|_]+$/.test(formInputs.loginInput.value),
                    text: 'Логин может состоять только из символов A-z, 0-9, _'
                }]
        });
    }

    function validPassword() {
        return valid({
            elem: formInputs.passwordInput,
            error: formInputs.passwordError,
            check: [{
                bool: formInputs.passwordInput.value.length >= 5 && formInputs.passwordInput.value.length <= 32,
                text: 'Длина пароля должна быть от 5 до 32 символов'
            },{
                bool: /^[A-z|А-я|0-9|_]+$/.test(formInputs.passwordInput.value),
                text: 'Пароль может состоять только из символов A-z, А-я, 0-9, _'
            }]
        });
    }
    function validConfirmPassword() {
        return valid({
            elem: formInputs.confirmPasswordInput,
            error: formInputs.confirmPasswordError,
            check: [{
                bool: formInputs.passwordInput.value === formInputs.confirmPasswordInput.value,
                text: 'Пароли не совпадают'
            }]
        });
    }
    function validEmail() {
        return valid({
            elem: formInputs.emailInput,
            error: formInputs.emailError,
            check: [{
                bool: formInputs.emailInput.value.length >= 5 && formInputs.emailInput.value.length <= 50,
                text: 'Длина e-mail должна быть от 5 до 50 символов'
            },{
                bool: /^[A-z|0-9|_]+?@[A-z]+?\.[A-z]+?$/.test(formInputs.emailInput.value),
                text: 'Неверный формат e-mail'
            }]
        });
    }
    function valid(options) {
        let bool = true;
        Array.forEach(options.check, function (check) {
            if(!check.bool){
                options.error.innerText = check.text;
                options.error.style.display = 'block';
                removeErrorListener(options.elem);
                bool = false;
                return false;
            }
        });
        return bool;
    }

    function removeErrorListener(elem){
        elem.addEventListener('click', deleteSelf);
        function deleteSelf(e) {
            e.currentTarget.nextElementSibling.style.display = 'none';
            e.currentTarget.removeEventListener('click', deleteSelf);
        }
    }

initSubmit();