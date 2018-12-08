'use strict';
const userId = document.getElementById('user_id').getAttribute('content'),
        href = location.href,
        questionId = href.substring(href.lastIndexOf('/')+1, href.length),
        answersDIV = document.querySelector('.answers'),
        answerContainer = answersDIV.querySelector('.answers-container'),
        ownerId = document.getElementById('owner_id').getAttribute('content');

const editor = {
    editWrapper: undefined,
    sendButton: undefined,
    cancelButton: undefined,
    answerQuill: undefined,
    quillEditorElement: undefined,
    creating: false,
    quillOptions: {
        theme: 'snow',
        placeholder: 'Напишите ваш ответ',
        fontSize: '20',
        modules: {
            toolbar: [
                ['bold', 'italic', 'underline', 'strike'],
                ['direction', {align: ['','center', 'right', 'justify']}],
                ['code-block', 'blockquote', {'list': 'ordered'}, {'list': 'bullet'}],
                ['link', 'image']
            ]
        }
    }
};
function initAnswerEditor() {
    const   editWrapper = document.querySelector('.edit-wrapper'),
            answerEditor = editWrapper.querySelector('.answer-editor'),
            sendButton = editWrapper.querySelector('.send-btn'),
            cancelBtn = editWrapper.querySelector('.cancel');

    editor.editWrapper = editWrapper;
    editor.sendButton = sendButton;
    editor.cancelButton = cancelBtn;
    editor.answerQuill = new Quill(answerEditor, editor.quillOptions);
    editor.quillEditorElement = editWrapper.querySelector('.ql-editor');

    editor.sendButton.addEventListener('click', sendAnswer);
    editor.cancelButton.addEventListener('click', cancelChange);
}
    function sendAnswer(){
        if(editor.creating){
            return;
        }
        const text = editor.answerQuill.getText();
        if(text.length < 100 || text.length > 1000){
            modal.error('Длина ответа должна быть меньше 100 и не превышать 1000 символов');
            return;
        }
        editor.creating = true;
        xhr.request({
            method: 'POST',
            path: '/services/questions/answer',
            headers: {
                'Content-Type':'application/json'
            },
            content: JSON.stringify({
                htmlContent: editor.quillEditorElement.innerHTML,
                id: questionId
            })
        }, function (response, error) {
            if(response){
                const responseAnswer = JSON.parse(response),
                    div = document.createElement('div');
                if(responseAnswer !== undefined && responseAnswer !== ''){
                    div.classList.add('answer');
                    div.setAttribute('user', responseAnswer.user.id);
                    div.setAttribute('id', 'answer-'+responseAnswer.id);
                    div.innerHTML = '<div class="e-line"></div>' +
                        '                            <div class="content">' +
                        '                                <div class="left-side">' +
                        '                                    <svg  class="is-right" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="#E5E8EA" d="M256 8C119.033 8 8 119.033 8 256s111.033 248 248 248 248-111.033 248-248S392.967 8 256 8zm0 48c110.532 0 200 89.451 200 200 0 110.532-89.451 200-200 200-110.532 0-200-89.451-200-200 0-110.532 89.451-200 200-200m140.204 130.267l-22.536-22.718c-4.667-4.705-12.265-4.736-16.97-.068L215.346 303.697l-59.792-60.277c-4.667-4.705-12.265-4.736-16.97-.069l-22.719 22.536c-4.705 4.667-4.736 12.265-.068 16.971l90.781 91.516c4.667 4.705 12.265 4.736 16.97.068l172.589-171.204c4.704-4.668 4.734-12.266.067-16.971z"></path></svg>' +
                        '                                    <div class="answer-rate">' +
                        '                                        <svg aria-hidden="true" data-prefix="fas" data-icon="caret-up" class="svg-inline--fa fa-caret-up fa-w-10" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512"><path fill="#A9A9A9" d="M288.662 352H31.338c-17.818 0-26.741-21.543-14.142-34.142l128.662-128.662c7.81-7.81 20.474-7.81 28.284 0l128.662 128.662c12.6 12.599 3.676 34.142-14.142 34.142z"></path></svg>' +
                        '                                        <span>'+responseAnswer.rating+'</span>' +
                        '                                        <svg aria-hidden="true" data-prefix="fas" data-icon="caret-down" class="svg-inline--fa fa-caret-down fa-w-10" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512"><path fill="#A9A9A9" d="M31.3 192h257.3c17.8 0 26.7 21.5 14.1 34.1L174.1 354.8c-7.8 7.8-20.5 7.8-28.3 0L17.2 226.1C4.6 213.5 13.5 192 31.3 192z"></path></svg>' +
                        '                                    </div>' +
                        '                                </div>' +
                        '                                <div class="right-side">' +
                        responseAnswer.htmlContent +
                        '                                </div>' +
                        '                            </div>' +
                        '                            <div class="action">' +
                        '                                <a href="/users/'+responseAnswer.user.id+'" class="answer-user">' +
                        '                                    <img src="/'+responseAnswer.user.userImage+'"/>' +
                        '                                    <div class="info">' +
                        '                                        <span class="login">'+responseAnswer.user.login+'</span>' +
                        '                                        <div class="rating">' +
                        '                                            <svg aria-hidden="true" data-prefix="fas" data-icon="star" class="svg-inline--fa fa-star fa-w-18" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path fill="#ffcb05" d="M259.3 17.8L194 150.2 47.9 171.5c-26.2 3.8-36.7 36.1-17.7 54.6l105.7 103-25 145.5c-4.5 26.3 23.2 46 46.4 33.7L288 439.6l130.7 68.7c23.2 12.2 50.9-7.4 46.4-33.7l-25-145.5 105.7-103c19-18.5 8.5-50.8-17.7-54.6L382 150.2 316.7 17.8c-11.7-23.6-45.6-23.9-57.4 0z"></path></svg>' +
                        '                                            <span>'+responseAnswer.user.rating+'</span>' +
                        '                                        </div>' +
                        '                                    </div>' +
                        '                                </a>' +
                        '                                <div class="answer-date">' +
                        '                                    <svg aria-hidden="true" data-prefix="far" data-icon="calendar-alt" class="svg-inline--fa fa-calendar-alt fa-w-14" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path fill="#adb5bd" d="M148 288h-40c-6.6 0-12-5.4-12-12v-40c0-6.6 5.4-12 12-12h40c6.6 0 12 5.4 12 12v40c0 6.6-5.4 12-12 12zm108-12v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm96 0v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm-96 96v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm-96 0v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm192 0v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm96-260v352c0 26.5-21.5 48-48 48H48c-26.5 0-48-21.5-48-48V112c0-26.5 21.5-48 48-48h48V12c0-6.6 5.4-12 12-12h40c6.6 0 12 5.4 12 12v52h128V12c0-6.6 5.4-12 12-12h40c6.6 0 12 5.4 12 12v52h48c26.5 0 48 21.5 48 48zm-48 346V160H48v298c0 3.3 2.7 6 6 6h340c3.3 0 6-2.7 6-6z"></path></svg>' +
                        '                                    <span>'+responseAnswer.createDate+'</span> <span class="hide-on-med-and-down">в 22:03</span>' +
                        '                                </div>' +
                        '                            </div>';
                    editor.creating = false;
                    answerContainer.insertBefore(div, answerContainer.firstChild);
                    userEdit(div);
                }
            } else if(error){
                modal.error('Не удалось отправить ответ');
                editor.creating = false;
            }
        });
    }

let rateUping = false, voted = false;
function initAnswers(){
    const actions = document.querySelector('.rate-div'),
        up = actions.firstElementChild,
        down = actions.lastElementChild,
        num = actions.querySelector('.num'),
        answerRates = document.querySelectorAll('.answer-rate-btn'),
        answers = document.querySelectorAll('.answer');

    up.addEventListener('click', function () {
        changeRate(1);
    });
    down.addEventListener('click', function () {
        changeRate(-1)
    });
    function changeRate(rate) {
        if(rateUping || voted){
            return;
        }else if(userId === '-1'){
	    modal.error('Вы не зарегистрированы, чтобы голосовать');
	    return;
	}else if(userId === ownerId){
            modal.error('Нельзя голосовать за свой вопрос');
            return;
        }
        rateUping = true;
        xhr.request({
            method: 'POST',
            path: '/services/questions/rating',
            headers: {
                'Content-Type':'application/json'
            },
            content: JSON.stringify({
                id: questionId,
                rate: rate
            })
        }, function (response, error, status) {
            if(response){
                voted = true;
                if(status === 208){
                    modal.info('Вы уже голосовали');
                    return;
                }
                num.innerHTML = Number(num.innerText) + Number(rate);
            }
        })
    }
    for(let i = 0; i < answerRates.length; i++){
        answerRates[i].addEventListener('click', upAnswerRate);
    }
    function upAnswerRate(e) {
        let target = e.currentTarget,
            rateHelp = target.getAttribute('content').split(':'),
            answerId = rateHelp[0],
            rate = rateHelp[1],
            answerOwner = target.parentElement.parentElement.parentElement.parentElement.parentElement.getAttribute('user');
            if(userId === '-1'){
		modal.error('Вы не зарегистрированы, чтобы голосовать');
		return;
	    }else if(answerOwner === userId){
                modal.error('Нельзя голосовать за свой ответ');
                return;
            }
        xhr.request({
            method: 'POST',
            path: '/services/questions/answer/rating',
            headers: {
                'Content-Type':'application/json'
            },
            content: JSON.stringify({
                id: answerId,
                rate: rate
            })
        },function (response, error, status) {
            if(response){
                if(status === 208){
                    modal.info('Вы уже голосовали');
                    return;
                }
                let span = target.parentElement.querySelector('span');
                span.innerText = Number(span.innerText) + Number(rate);
            }
        });
    }
    for(let i = 0; i < answers.length; i++){
        if(answers[i].getAttribute('user') === userId){
            userEdit(answers[i]);
        }
    }
}

    const answerEdit = {
        answerContent: undefined,
        contentWrapper: undefined,
    };
    function userEdit(answer){
        const edit = document.createElement('i'),
            remove = document.createElement('i'),
            editLine = answer.querySelector('.e-line'),
            answerId = answer.id.split('-')[1];
        edit.classList.add('e-elem', 'edit');
        edit.setAttribute('content', answerId);
        remove.classList.add('e-elem', 'remove');
        remove.setAttribute('content', answerId);
        remove.addEventListener('click', function (e) {
           const target = e.currentTarget;
           xhr.request({
               path: '/services/questions/answer',
               method: 'DELETE',
               headers: {
                   'Content-Type':'application/json'
               },
               content: JSON.stringify({
                   id: target.getAttribute('content')
               })
           }, function (response) {
               if(response){
                  const answer = document.getElementById('answer-'+answerId);
                  answer.parentNode.removeChild(answer);
               }
           });
        });
        edit.addEventListener('click', function (e) {
           const answer = document.getElementById('answer-'+e.currentTarget.getAttribute('content')),
                contentWrapper = answer.querySelector('.content-wrapper'),
                answerContent = answer.querySelector('.right-side');
           contentWrapper.style.display = 'none';

           answerEdit.contentWrapper = contentWrapper;
           answerEdit.answerContent = answerContent;

           editor.quillEditorElement.innerHTML = answerContent.innerHTML.trim();
           editor.sendButton.removeEventListener('click', sendAnswer);
           editor.sendButton.addEventListener('click', changeAnswer);

           editor.cancelButton.style.display = 'flex';

           answer.appendChild(editor.editWrapper);
        });
        edit.innerHTML = '<svg role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="#E5E8EA" d="M290.74 93.24l128.02 128.02-277.99 277.99-114.14 12.6C11.35 513.54-1.56 500.62.14 485.34l12.7-114.22 277.9-277.88zm207.2-19.06l-60.11-60.11c-18.75-18.75-49.16-18.75-67.91 0l-56.55 56.55 128.02 128.02 56.55-56.55c18.75-18.76 18.75-49.16 0-67.91z"></path></svg>';
        remove.innerHTML = '<svg role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 352 512"><path fill="#D6DADE" d="M242.72 256l100.07-100.07c12.28-12.28 12.28-32.19 0-44.48l-22.24-22.24c-12.28-12.28-32.19-12.28-44.48 0L176 189.28 75.93 89.21c-12.28-12.28-32.19-12.28-44.48 0L9.21 111.45c-12.28 12.28-12.28 32.19 0 44.48L109.28 256 9.21 356.07c-12.28 12.28-12.28 32.19 0 44.48l22.24 22.24c12.28 12.28 32.2 12.28 44.48 0L176 322.72l100.07 100.07c12.28 12.28 32.2 12.28 44.48 0l22.24-22.24c12.28-12.28 12.28-32.19 0-44.48L242.72 256z"></path></svg>';
        editLine.appendChild(edit);
        editLine.appendChild(remove);
    }
    function changeAnswer(e){
        const answer = editor.editWrapper.parentNode;
        if(!answer.classList.contains('answer')){
            return;
        }
        xhr.request({
            path: '/services/questions/answer',
            method: 'PUT',
            headers: {
                'Content-Type':'application/json'
            },
            content: JSON.stringify({
                id: answer.id.split('-')[1],
                htmlContent: editor.quillEditorElement.innerHTML
            })
        }, function (response, error) {
           if(response){
               answerEdit.answerContent.innerHTML = editor.quillEditorElement.innerHTML;
               answerEdit.contentWrapper.style.display = 'block';

               editor.sendButton.removeEventListener('click', changeAnswer);
               editor.sendButton.addEventListener('click', sendAnswer);
               editor.quillEditorElement.innerHTML = '';
               answersDIV.insertBefore(editor.editWrapper, answersDIV.firstElementChild.nextElementSibling);
           } else if(error){
               modal.error('Не удалось изменить комментарий');
           }
        });
    }
    function cancelChange(e) {
        answerEdit.contentWrapper.style.display = 'block';

        editor.sendButton.removeEventListener('click', changeAnswer);
        editor.sendButton.addEventListener('click', sendAnswer);
        editor.cancelButton.style.display = 'none';

        editor.quillEditorElement.innerHTML = '';
        answersDIV.insertBefore(editor.editWrapper, answersDIV.firstElementChild.nextElementSibling);
    }

    const rightAnswer = {
        owner: document.getElementById('owner'),
      isRights: document.querySelectorAll('.is-right'),
    };
    function initRightAnswers(){
        rightAnswer.isOwner = rightAnswer.owner.getAttribute('content');
        rightAnswer.rightId = rightAnswer.owner.getAttribute('answer');

        let rightAnswers = rightAnswer.isRights;
        for(let i = 0; i < rightAnswers.length; i++){
            let answer = rightAnswers[i];
            if(answer.getAttribute('content') === rightAnswer.rightId){
                answer.classList.add('right');
                rightAnswer.right = answer;
            }else{
                answer.classList.add('noright');
            }
            if(rightAnswer.isOwner){
                answer.classList.add('owner');
                answer.addEventListener('click', function (e) {
                    const target = e.currentTarget;
                    if(rightAnswer.rightId === target.getAttribute('content')){
                        return;
                    }
                    xhr.request({
                        path: '/services/questions/answer/right',
                        method: 'POST',
                        headers: {
                            'Content-Type':'application/json'
                        },
                        content: JSON.stringify({
                            id: target.getAttribute('content')
                        })
                    }, function (response) {
                        if(response){
                            if(rightAnswer.right !== undefined){
                                rightAnswer.right.classList.remove('right');
                                rightAnswer.right.classList.add('noright');
                            }
                            rightAnswer.right = target;
                            rightAnswer.rightId = rightAnswer.right.getAttribute('content');
                            rightAnswer.right.classList.remove('noright');
                            rightAnswer.right.classList.add('right');
                        }
                    })
                });
            }
        }
    }
initAnswerEditor();
initAnswers();
initRightAnswers();