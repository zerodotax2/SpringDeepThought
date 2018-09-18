document.addEventListener("DOMContentLoaded" ,function () {

    var loginDelete = false,
        passwordDelete = false,
        confirmPasswordDelete = false,
        emailDelete = false;

    var datePickerOptions = {
        format: 'yyyy.mm.dd',
        i18n: {
            cancel: 'Отмена',
            clear: 'Очистить',
            done: 'Ок',
            months: [
                "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
            ],
            monthsShort: [
                "Янв", "Фев", "Март", "Апр", "Май", "Июнь", "Июль", "Авг", "Сент", "Окт", "Нояб", "Дек"
            ],
            weekdays:[
                'Суббота',
                'Понедельник',
                'Вторник',
                'Среда',
                'Четверг',
                'Пятница',
                'Воскресенье'
            ],
            weekdaysShort: [
                'Сб',
                'Пн',
                'Вт',
                'Ср',
                'Чт',
                'Пт',
                'Вс'
            ],
            weekdaysAbbrev:[
                'С','П','В','С','Ч','П','В'
            ]
        }
    }, elems = document.querySelectorAll('.datepicker'),
        instances = M.Datepicker.init(elems, datePickerOptions);

    /*Валидаторы для полей: Имя, фамилия, пароль*/
    var loginInput = document.getElementsByClassName("login_input")[0];
    var loginMessage = document.getElementsByClassName("login_text")[0];
    loginInput.addEventListener("blur", function () {
        if(!loginDelete){
            document.getElementById("login_temp").remove();
            loginDelete = true;
        }

        if(loginInput.value.length > 32){
            loginInput.value = loginInput.value.substring(0, 32);
            loginMessage.setAttribute("data-error", "Максимальная длина имени - 32 символа");
            validate(false, loginInput)
        }else if(!loginInput.value.match("^[A-z|1-9|_]+$")){
            loginMessage.setAttribute("data-error","Имя состоит из цифр и букв");
            validate(false, loginInput);
        }else{
            validate(true, loginInput)
        }
    });

    var pInput = document.getElementsByClassName("p_input")[0];
    var passwordMessage = document.getElementsByClassName("password_text")[0];
    pInput.addEventListener("blur", function () {
        if(!passwordDelete){
            document.getElementById("password_temp").remove();
            passwordDelete = true;
        }

        if(pInput.value.length > 32){
            pInput.value = pInput.value.substring(0, 32);
            passwordMessage.setAttribute("data-error", "Максимальная длина пароля - 32 символа");
            validate(false, pInput)
        }else if(!pInput.value.match("^[A-z|А-я|1-9]+$")){
            passwordMessage.setAttribute("data-error", "Пароль состоит из цифр и букв");
            validate(false, pInput);
        }else{
            validate(true, pInput)
        }
    });


    /*Функция валидации, проверяет на соответствие regExp && length*/

    /*Тут проверяется лишь соответсвие паролю*/
    var pcInput = document.getElementsByClassName("pc_input")[0];
    var pcMessage = document.getElementsByClassName("cpassword_text")[0];
    pcInput.addEventListener("blur", function () {
        if(!confirmPasswordDelete){
            document.getElementById("cpassword_temp").remove();
            confirmPasswordDelete = true;
        }

        if(pcInput.value === pInput.value){
            pcMessage.setAttribute("data-error", "Пароли не совпадают");
            validate(pcInput, true);
        }else{
            validate(pcInput, false);
        }
    });

    /*Идет валидация по длине от 8 до 50, а также по regExp для Email*/
    var emailInput = document.getElementsByClassName("email_input")[0];
    var emailMessage = document.getElementsByClassName("email_text")[0];
    emailInput.addEventListener("blur", function () {
        if(!emailDelete){
            document.getElementById("email_temp").remove();
            emailDelete = true;
        }

        if( emailInput.value.match("^[A-z|1-9]+@[A-z|\.]+$") ){
            if( emailInput.value.length <= 50 ){
                validate(emailInput, true);
            }else{
                emailInput.value = emailInput.value.substring(0,50);
                emailMessage.setAttribute("data-error", "Максимальная длина e-mail - 50 символов");
                validate(emailInput, false);
            }
        }else{
            emailMessage.setAttribute("data-error", "Неверный формат e-mail");
            validate(emailInput, false);
        }
    });

    function validate(element, b) {
        if(b){
            element.classList.add("valid");
            element.classList.remove("invalid");
        }else{
            element.classList.add("invalid");
            element.classList.remove("valid");
        }
    }
});