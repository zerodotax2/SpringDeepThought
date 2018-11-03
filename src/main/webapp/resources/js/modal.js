window.modal = {
    errorColor: '#e53935',
    infoColor: '#2196F3',
    loadColor: '#4CAF50',
    warningColor: '#ffa726',
    info: function (text) {
        modal.head.style.background = modal.infoColor;
        modal.head.innerText = 'Информация';
        modal.content.innerHTML = text;
        modal.show();
    },
    error: function (text) {
        modal.head.style.background = modal.errorColor;
        modal.head.innerText = 'Ошибка';
        modal.content.innerHTML = text;
        modal.show();
    },
    load: function (name){
        modal.head.style.background = modal.loadColor;
        modal.head.innerText = 'Загрузка';
        modal.content.innerHTML = '<div style="text-align: center;margin-bottom: 10px;">'+name+'</div>' +
            '<div class="progress blue lighten-3" style="margin: 0;"><div class="indeterminate green"></div></div>';
        modal.show();
    },
    warning: function(action){
        modal.head.style.background = modal.warningColor;
        modal.head.innerText = 'Предупреждение';
        modal.content.innerHTML = action;
        modal.show();
    },
    close: function () {
        modal.m.style.display = 'none';
        document.removeEventListener('keyup', closeOnEsc);
        modal.actions.innerHTML = '';
        modal.content.innerHTML = '';
        modal.actions.appendChild(modal.acceptBtn);
    },
    show: function () {
        modal.m.style.display = 'flex';
        document.addEventListener('keyup', closeOnEsc);
    },
    feedback: function (name, color, path, id) {
        let sendFeedback = document.createElement('div'),
            feedbackDIV = document.createElement('div');

        feedbackDIV.classList.add('feedback');
        feedbackDIV.innerHTML = '<textarea></textarea>' +
            '<div class="counter">Осталось символов: <span>500</span></div>';
        modal.content.appendChild(feedbackDIV);
        let textArea = feedbackDIV.querySelector('textarea'),
            counter = feedbackDIV.querySelector('.counter span');
        textArea.addEventListener('input', function () {
            if(textArea.value.length >= 500){
                textArea.value = textArea.value.substring(0, 500);
            }
            counter.innerText = 500 - textArea.value.length;
        });
        sendFeedback.classList.add('action-btn', 'accept');
        sendFeedback.innerText = 'Отправить';
        sendFeedback.addEventListener('click', function () {
            xhr.request({
                path: path,
                method: 'POST',
                content: JSON.stringify({
                    text: textArea.value.substring(0, 1000),
                    id: id || 0
                })
            }, function () {});
            modal.close();
        });
        modal.customActions(sendFeedback);
        modal.head.style.background = color;
        modal.head.innerText = name;
        modal.show();
    },
    customActions(){
        modal.actions.innerHTML = '';
        Array.forEach(arguments, function (arg) {
           modal.actions.appendChild(arg);
        });
    },
    createDefaultButton: function (name, color) {
        let btn = document.createElement('div');
        btn.classList.add('action-btn');
        btn.innerText = name;
        btn.style.background = color;
        btn.onclick = modal.close;
        return btn;
    }
};
document.addEventListener('DOMContentLoaded', function () {

   modal.m = document.createElement('div');
   modal.m.setAttribute('id', 'modal-wrapper');
   modal.m.style.display = 'none';
   modal.m.innerHTML =
       '        <div class="modal-window">' +
       '            <div class="modal-head">' +
       '            </div>' +
       '            <div class="modal-content">' +
       '            </div>' +
       '            <div class="actions">' +
       '                <div class="action-btn accept">Ок</div>' +
       '            </div>' +
       '        </div>'; 
   document.body.appendChild(modal.m);

    modal.head = modal.m.querySelector('.modal-head');
    modal.content = modal.m.querySelector('.modal-content');
    modal.actions = modal.m.querySelector('.actions');

    modal.m.addEventListener('click', function (e) {
        modal.close();
    });
    modal.m.querySelector('.modal-window').addEventListener('click', function (e) {
        e.stopPropagation();
    });
    modal.actions.querySelector('.accept').addEventListener('click', function () {
       modal.close();
    });

    modal.acceptBtn  = document.createElement('div');
    modal.acceptBtn.classList.add('action-btn', 'accept');
    modal.acceptBtn.innerText = 'Ок';
    modal.acceptBtn.addEventListener('click', function () {
        modal.close();
    });
});

    function closeOnEsc(e) {
        if(e.keyCode === 27){
            modal.close();
        }
    }
