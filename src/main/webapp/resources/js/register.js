'use strict';
let freeEmail = true, freeLogin = true, lastEmail = '', lastLogin ='';
document.addEventListener('DOMContentLoaded', function(){

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
	    formInputs.submit = document.querySelector('.actions .send input'),
	    formInputs.form = document.querySelector('.reg-form');
	
	    formInputs.submit.addEventListener('click', function(e){

		if(validLogin()
	        	&& validPassword()
	        	&& validConfirmPassword()
	        	&& validEmail()){
	        	formInputs.form.submit();
	    	}else{
			e.preventDefault();
		}

	    });

	    formInputs.emailInput.addEventListener('blur', function () {
	        isFree({
	            type: 'email',
	            label: formInputs.emailError,
	            input: formInputs.emailInput,
	            text: 'Аккаунт с таким email уже существует'
	        });
	    });
	
	    formInputs.loginInput.addEventListener('blur', function () {
	       isFree({
	           type: 'login',
	           label: formInputs.loginError,
	           input: formInputs.loginInput,
	           text: 'Логин уже занят'
	       });
	    });
	
	    deleteServerError(formInputs.loginInput);
	    deleteServerError(formInputs.passwordInput);
	    deleteServerError(formInputs.confirmPasswordInput);
	    deleteServerError(formInputs.emailInput);
	}
	
	    function isFree(options) {
	
	        let value = options.input.value;
	
	        if(options.type === 'email'){
	            freeEmail = true;
	            if(!validEmail() || value === '' || value === lastEmail)
	                return;
	            else
	                lastEmail = value;
	        }else if(options.type === 'login'){
	            freeLogin = true;
	            if(!validLogin() || value === '' || value === lastLogin)
	                return;
	            else
	                lastLogin = value;
	        }
	        xhr.request({
	            method: 'POST',
	            path: '/services/register/exists',
	            headers: {'Content-Type': 'application/json'},
	            content: JSON.stringify({
	                type: options.type,
	                value: options.input.value
	            })
	        }, function (response, error, status) {
	            if(status !== 204){
	                options.label.innerText = options.text;
	                options.label.style.display = 'block';
	                removeErrorListener(options.input);
	                if(options.type === 'email')
	                    freeEmail = false;
	                else if(options.type = 'login')
	                    freeLogin = false;
	            }else{
	                if(options.type === 'email')
	                    freeEmail = true;
	                else if(options.type = 'login')
	                    freeLogin = true;
	            }
	        })
	    }
	
	    function deleteServerError(elem) {
	        elem.addEventListener('focus', deleteListener);
	        function deleteListener(e){
	            const error = e.currentTarget.parentElement.querySelector('.server-error');
	            if(error !== undefined && error !== null){
	                error.style.display = 'none';
	            }
	            e.currentTarget.removeEventListener('focus', deleteListener);
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
	        }) && freeLogin;
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
	                bool: /^[A-z|0-9|_|.]+?@[A-z]+?\.[A-z]+?$/.test(formInputs.emailInput.value),
	                text: 'Неверный формат e-mail'
	            }]
	        }) && freeEmail;
	    }
	    function valid(options) {
	        let bool = true, checks = options.check;
	        for(let i = 0; i < checks.length; i++){
                if(!checks[i].bool){
                    options.error.innerText = checks[i].text;
                    options.error.style.display = 'block';
                    removeErrorListener(options.elem);
                    bool = false;
                    return false;
                }
			}
	        return bool;
	    }
	
	    function removeErrorListener(elem){
	        elem.addEventListener('focus', deleteSelf);
	        function deleteSelf(e) {
	            e.currentTarget.nextElementSibling.style.display = 'none';
	            e.currentTarget.removeEventListener('focus', deleteSelf);
	        }
	    }
	
	initSubmit();
});