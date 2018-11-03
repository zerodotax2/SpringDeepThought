
const formInputs = {
    loginInput: document.getElementById('login_input'),
    passwordInput: document.getElementById('password_input'),
    error: document.querySelector('.login_panel .error')
};
function initSubmit() {

    const submit = document.querySelector('.sign-div input'),
        form = document.querySelector('.login_form');
    submit.onclick = function (e) {
      if(validLogin()
      && validPassword()){
          form.submit();
      }else{
          formInputs.error.style.display = 'block';
      }
    };


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
        Array.forEach(options.check, function (check) {
            if(!check.bool){
                bool = false;
                return false;
            }
        });
        return bool;
    }
}

initSubmit();