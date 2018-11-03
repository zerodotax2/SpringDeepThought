
const editor = {
    instance: undefined,
    input: undefined,
    save: undefined
};
const editValues = {
    'firstName':{
        path: '/services/user/update/fname',
        check: function () {
            const value = editor.input.value;
            if(value.length < 3 || value.length > 32){
                modal.error('Длина имени должна быть от 3 до 32 символов');
                return false;
            }
            else if(!value.match(/^[A-z|А-я]+$/)){
                modal.error('Имя может состоять только из букв');
                return false;
            }
            return true;
        }
    },
    'lastName':{
        path: '/services/user/update/lname',
        check: function () {
            const value = editor.input.value;
            if(value.length < 3 || value.length > 32){
                modal.error('Длина фамилии должна быть от 3 до 32 символов');
                return false;
            }
            else if(!value.match(/^[A-z|А-я]+$/)){
                modal.error('Фамилия может состоять только из букв');
                return false;
            }
            return true;
        }
    },
    'birthDay': {
        path: '/services/user/update/bday',
        check: function () {
            const value = editor.input.value,
                dates = value.split('-');
            if(!value.match(/^[0-9]{2}-[0-9]{2}-[0-9]{4}$/)){
                modal.error('Неверный формат даты');
                return false;
            }else if(Number(dates[0])>31 || Number(dates[0]) === 0){
                modal.error('Неверный формат дня');
                return false;
            }else if(Number(dates[1]) > 12 || Number(dates[1]) === 0) {
                modal.error('Неверный формат месяца');
                return false;
            }
            return true;
        }
    },
    'email': {
        path: '/services/user/update/email',
        check: function () {
            const value = editor.input.value;
            if(value.length < 6 || value.length > 50){
                modal.error('Размер email должен быть от 6 до 50 символов');
                return false;
            }else if(!value.match(/^[A-z|0-9|_]+?@[A-z]+?\.[A-z]+?$/)){
                modal.error('Неверный формат email');
                return false;
            }
            modal.info('Подтвердите почту для успешной смены email');
            return true;
        }
    },
    current: undefined,
};
function initEditor() {
    const editorDIV = document.createElement('div');
    editorDIV.classList.add('editor');
    editorDIV.innerHTML = '<div class="content"><input type="text"/> </div>' +
        '                                <div class="actions">' +
        '                                    <div class="save action">Сохранить</div>' +
        '                                    <div class="unsave action">Отмена</div>' +
        '                                </div>';
    editorDIV.querySelector('.unsave').addEventListener('click', function () {
        editorDIV.style.display = 'none';
        editorDIV.previousElementSibling.style.display = 'flex';
    });
    editorDIV.querySelector('.save').addEventListener('click', function () {
       if(editValues.current.check()){
           xhr.request({
               path: editValues.current.path,
               method: 'POST',
               content: JSON.stringify({
                   value: editor.input.value
               })
           }, function (response,error) {
               if(response){
                   editor.instance.parentNode.querySelector('.value span').innerText = editor.input.value;
                   editor.instance.parentNode.querySelector('.value').style.display = 'flex';
                   editor.instance.style.display = 'none';
               }else if(error){
                   xhr.error('Не удалось обновить данные');
                   editor.instance.parentNode.querySelector('.value').style.display = 'flex';
                   editor.instance.style.display = 'none';
               }
           });
       }
    });
    editor.instance = editorDIV;
    editor.input = editorDIV.querySelector('input');
    editor.save = editorDIV.querySelector('.save');
}
function initEditValues(){
    const editPanels = document.querySelectorAll('.edit-marker');
    Array.forEach(editPanels, function (panel) {
        const edit = panel.querySelector('.edit');
        edit.addEventListener('click', function (e) {
            const target = e.currentTarget;
            if(editor.instance.parentNode !== null){
                editor.instance.parentNode.querySelector('.value').style.display = 'flex';
            }
            editValues.current = editValues[target.getAttribute('content')];
            target.parentNode.style.display = 'none';
            target.parentNode.parentNode.appendChild(editor.instance);
            editor.instance.style.display ='block';
            editor.input.value = target.parentNode.querySelector('span').innerText;
        });
    });
}
    const userImage = {
        imageDiv: document.querySelector('.user-image')
    };
    function initUserImage() {
        userImage.img = userImage.imageDiv.querySelector('.ava');
        userImage.cover = userImage.imageDiv.querySelector('.image-cover');
        userImage.fileInput = document.getElementById('userImageInput');
        userImage.fileInput.addEventListener('change', function () {
            let formData = new FormData();
            formData.append('file', userImage.fileInput.files[0]);
            formData.append('type', 'type.user.profile');
            xhr.request({
                path: '/services/upload',
                method: 'POST',
                content: formData
            }, function (response, error) {
                if(response){
                    userImage.img.src = JSON.parse(response).largeImagePath;
                }else if(error){
                    modal.error('Не удалось обновить фотографию');
                }
            })
        });
        userImage.imageDiv.addEventListener('mouseenter', function () {
           userImage.cover.style.display = 'flex';
        });
        userImage.imageDiv.addEventListener('mouseleave', function () {
           userImage.cover.style.display = 'none';
        });
    }

if(window.location.href.toLowerCase().lastIndexOf('user') !== -1){
    const tagEdit = {
        elem: document.querySelector('.tag-element'),
    };
    tagEdit.value = tagEdit.elem.querySelector('.value');
    tagEdit.tagContainer = tagEdit.value.querySelector('.tag-container');
    tagEdit.editor = tagEdit.elem.querySelector('.tags');
    addTagsToEditor();
    tags.tagInput.addEventListener('keyup', onTagInput);
    tagEdit.value.querySelector('.edit').addEventListener('click', function (e) {
        tagEdit.value.style.display = 'none';
        tagEdit.value.nextElementSibling.style.display = 'block';
    });
    tagEdit.elem.querySelector('.save').addEventListener('click', function (e) {
        const tagsIDS = Object.keys(tags.tagsCount),
              selectedTags = tags.tagsInputContainer.querySelectorAll('.selected-tag');
        xhr.request({
            path: '/services/user/update/interests',
            method: 'POST',
            content: JSON.stringify({
                tags: tagsIDS
            })
        }, function (response, error) {
            if(response){
                tagEdit.tagContainer.innerHTML = '';
                Array.forEach(selectedTags, function (selectedTag) {
                    let addedTag = document.createElement('a'),
                        tagID = selectedTag.getAttribute('content');
                    addedTag.classList.add('tag');
                    addedTag.setAttribute('color', selectedTag.style.background);
                    addedTag.setAttribute('content', tagID);
                    addedTag.href = '/tags/'+tagID;
                    addedTag.innerText = selectedTag.innerText;
                    tagEdit.tagContainer.appendChild(addedTag);
                });
            }else if(error){
                modal.error('Не удалось обновить интересы');
            }
            closeEditor();
        });
    });
    tagEdit.elem.querySelector('.unsave').addEventListener('click', function (e) {
        closeEditor();
    });
    function closeEditor() {
        tagEdit.editor.style.display = 'none';
        tagEdit.value.style.display = 'flex';
        addTagsToEditor();
    }
    function addTagsToEditor() {
        let tagsOnDocument = tagEdit.tagContainer.querySelectorAll('.tag'),
            tagsOnEditor = tags.tagsInputContainer.querySelectorAll('.selected-tag');
        Array.forEach(tagsOnEditor, function (tagOnEditor) {
           tagOnEditor.parentNode.removeChild(tagOnEditor);
        });
        Array.forEach(tagsOnDocument, function (tagOnDocument) {
            const selectedTag = document.createElement('div');
            selectedTag.classList.add('chip', 'chip-suggest', 'selected-tag');
            selectedTag.innerText = tagOnDocument.innerText;
            selectedTag.onclick = deleteTagOnClick;
            selectedTag.setAttribute('content', tagOnDocument.getAttribute('content'));
            selectedTag.style.background = tagOnDocument.getAttribute('color');
            selectedTag.style.minWidth = (selectedTag.innerText.length * 8) + 'px';
            tags.tagsCount[tagOnDocument.getAttribute('content')] = 1;
            tags.tagsInputContainer.insertBefore(selectedTag, tags.tagInput);
        });
    }
}else if(window.location.href.toLowerCase().lastIndexOf('account') !== -1){
    const passwordEdit = {
        elem: document.querySelector('.password-element'),
    };
    const editorDIV = document.createElement('editor');
    editorDIV.classList.add('editor');
    editorDIV.style.display = 'none';
    editorDIV.innerHTML = '<div class="content">' +
        '   <input id="oldPassword" placeholder="Старый пароль" type="password"/> ' +
        '   <input id="newPassword" placeholder="Новый пароль" type="password"/> ' +
        '   <input id="confirmPassword" placeholder="Подтвердите пароль" type="password"/> ' +
        '</div>' +
        '<div class="actions">' +
        '    <div class="save action">Сохранить</div>' +
        '    <div class="unsave action">Отмена</div>' +
        '</div>';
    passwordEdit.editor = editorDIV;
    passwordEdit.elem.appendChild(passwordEdit.editor);
    passwordEdit.oldPassword = editorDIV.querySelector('#oldPassword');
    passwordEdit.newPassword = editorDIV.querySelector('#newPassword');
    passwordEdit.confirmPassword = editorDIV.querySelector('#confirmPassword');
    passwordEdit.value = passwordEdit.elem.querySelector('.value');
    editorDIV.querySelector('.save').addEventListener('click', function () {
        if(passwordEdit.oldPassword.value.length < 5 || passwordEdit.oldPassword.value.length > 32
            || !passwordEdit.oldPassword.value.match(/^[A-z|0-9|А-я]+$/)){
            modal.error('Неправильный пароль');
            return;
        }else if(passwordEdit.newPassword.value.length < 5 || passwordEdit.newPassword.value.length > 32
            || !passwordEdit.newPassword.value.match(/^[A-z|0-9|А-я]+$/)){
            modal.error('Неправильный новый пароль');
            return;
        }else if(passwordEdit.newPassword.value !== passwordEdit.confirmPassword.value){
            modal.error('Пароли не совпадают');
            return;
        }
        xhr.request({
            path: '/services/user/update/password',
            method: 'POST',
            content: JSON.stringify({
                oldPassword: passwordEdit.oldPassword.value,
                newPassword: passwordEdit.newPassword.value
            })
        }, function (response, error) {
            if(response){
                modal.info('Пароль был успешно изменен');
            }else if(error){
                modal.error('Не удалось изменить пароль');
            }
            passwordEdit.editor.style.display = 'none';
            passwordEdit.value.style.display = 'flex';
        })
    });
    editorDIV.querySelector('.unsave').addEventListener('click', function () {
        passwordEdit.value.style.display = 'flex';
        passwordEdit.editor.style.display = 'none';
    });
    passwordEdit.elem.querySelector('.edit').addEventListener('click', function () {
        passwordEdit.value.style.display = 'none';
        passwordEdit.editor.style.display = 'block';
    });
}

initEditor();
initEditValues();
initUserImage();
