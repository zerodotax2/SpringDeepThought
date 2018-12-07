'use strict';
const formInputs = {
    loginInput: document.getElementById('login_input'),
    passwordInput: document.getElementById('password_input'),
    error: document.querySelector('.login_panel .error'),
    submit: document.querySelector('.sign-div input'),
    form: document.querySelector('.login_form')
};

function initAuth(){
	formInputs.submit.addEventListener('click', function(e){
		if(validLogin() && validPassword()){
			formInputs.form.submit();
		}else{
			e.preventDefault();
			formInputs.error.style.display = 'block';
		}
	});
}

function validLogin() {
    return valid({
        check: [{
            bool: formInputs.loginInput.value.length >= 5 && formInputs.loginInput.value.length <= 32
        },{
            bool: /^[A-z|0-9|_]+$/.test(formInputs.loginInput.value)
        }]
    })
}
function validPassword() {
    return valid({
        check: [{
            bool: formInputs.passwordInput.value.length >= 5 && formInputs.passwordInput.value.length <= 32
        },{
            bool: /^[A-z|А-я|0-9|_]+$/.test(formInputs.passwordInput.value)
        }]
    });
}
function valid(options) {
    let bool = true;
    for(let i = 0; i < options.check.length; i++){
        if(!options.check[i].bool){
            bool = false;
            return false;
        }
    }
    return bool;
}
initAuth();