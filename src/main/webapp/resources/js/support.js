'use strict';
function  initAsk() {
    const ask = document.querySelector('.ask');
    ask.addEventListener('click', function () {
        let helper = document.createElement('div');
        helper.classList.add('helper');
        helper.innerText = 'Отвёт придёт к вам в уведомления';
        modal.feedback('Задать вопрос', modal.loadColor, '/services/support/ask');
        modal.content.insertBefore(helper, modal.content.firstElementChild);
    });
}

initAsk();