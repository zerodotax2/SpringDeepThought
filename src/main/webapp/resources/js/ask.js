'use strict';
const questionEditor    = document.querySelector('#question-editor');
const quill = new Quill(questionEditor, {
    theme: 'bubble',
    placeholder: 'Опишите ваш вопрос',
    fontSize: '20',
    modules: {
        toolbar: [
            [{size : ['small', false, 'large', 'huge']}],
            ['bold', 'italic', 'underline', 'strike'],
            ['direction', {align: ['','center', 'right', 'justify']}],
            ['code-block', 'blockquote', {'list': 'ordered'}, {'list': 'bullet'}],
            ['link', 'image']
        ]
    }
});
initQuillEditor(questionEditor);

const inputs = {
    titleInput: document.querySelector('#header'),
    tagsObjects: tags.tagsCount,
    qlEditor: document.querySelector('.editor-container .ql-editor'),
    edit:true
};

function initIfEdit() {
    if(document.getElementById('_edit') === null){
        inputs.edit = false;
        return;
    }

    inputs.questionId = document.getElementById('question_id').getAttribute('content');

    let titleEdit = document.getElementById('title_edit'),
        tagsEdit = document.getElementById('tags_edit'),
        tagsEditChilds = tagsEdit.children,
        questionEdit = document.getElementById('question_edit');

    inputs.titleInput.value = titleEdit.innerText;
    for(let i = 0; i < tagsEditChilds.length; i++){
        let tag = tagsEditChilds[i],
            selectedTag = document.createElement('div'),
            name = tag.getAttribute('name'),
            color = tag.getAttribute('color'),
            id = tag.getAttribute('content');
        selectedTag.classList.add('selected-tag', 'chip');
        selectedTag.style.background = color;
        selectedTag.innerText = name;
        selectedTag.style.color = '#ffffff';
        selectedTag.setAttribute('content', id);
        selectedTag.addEventListener('click', deleteTagOnClick);
        tags.tagsCount[id] = 1;
        tags.tagsInputContainer.insertBefore(selectedTag, tags.tagsInputContainer.lastElementChild);
    }
    upPlaceholder();
    inputs.qlEditor.innerHTML = questionEdit.innerHTML.trim();

    titleEdit.parentNode.removeChild(titleEdit);
    tagsEdit.parentNode.removeChild(tagsEdit);
    questionEdit.parentNode.removeChild(questionEdit);
}

let creating = false;
function initCreator(){
    const sendBtn = document.querySelector('.send-question');

    sendBtn.addEventListener('click', function (e) {
        if(!creating){
            let title = inputs.titleInput.value,
                tagsIDS = Object.keys(tags.tagsCount),
                htmlContent = inputs.qlEditor.innerHTML,
                textContent = quill.getText();
            if(title.length < 10 || title.length > 100){
                modal.error('Длина заголовка не должна быть меньше 10 и превышать 100 символов');
                return;
            }else if(tagsIDS.length < 2 || tagsIDS.length > 5){
                modal.error('Количество тегов должно быть от 2-х до 5');
                return;
            }else if(textContent.length > 10000 || textContent.length < 100){
                modal.error('Длина вопроса не может быть меньше 100 или больше 10000 символов');
                return;
            }
            creating = true;
            const question = {
                title: title,
                tags: tagsIDS,
                htmlContent: htmlContent
            };
            if(!inputs.edit){
                createQuestion(question);
            }else{
                updateQuestion(question);
            }
        }
    });
}
    function createQuestion(question){
        xhr.request({
            method: 'POST',
            path: '/services/questions',
            headers: {
                'Content-Type':'application/json'
            },
            content: JSON.stringify(question)
        }, function (response, error) {
            if(response){
                let obj = JSON.parse(response);
                location.href = 'http://localhost:80/questions/' + obj.id;
            }else if(error){
                modal.error('Не удалось создать вопрос');
            }
            creating  = false;
        });
        modal.load('Идёт создание вопроса...');
    }
    function updateQuestion(question){
        question.questionId = inputs.questionId;
        xhr.request({
            method: 'PUT',
            path: '/services/questions',
            headers: {
                'Content-Type':'application/json'
            },
            content: JSON.stringify(question)
        }, function (response, error) {
            if(response){
                location.href = 'http://localhost:80/questions/' + inputs.questionId;
            }else if(error){
                modal.error('Не удалось обновить вопрос');
            }
        });
        modal.load('Идёт обновление вопроса...');
    }

/*
* Init functions
* */
initTags();
initIfEdit();
initCreator();