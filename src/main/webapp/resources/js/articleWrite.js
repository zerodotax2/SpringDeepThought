'use strict';
const articleEditor = document.querySelector('#article-editor'),
    quill = new Quill(articleEditor, {
        theme: 'bubble',
        placeholder: 'Напишите о чём нибудь интересном',
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

    const images = {

        imageHover:  document.querySelector('#image-hover'),
        topImageSection: document.querySelector('.head-info .card-image'),
        uploadMainImage: document.querySelector('#uploadMainImage'),

    };
    const inputs = {
        titleInput: document.querySelector('#title'),
        subtitleInput: document.querySelector('#subtitle'),
        tagsObjects: tags.tagsCount,
        qlEditor: document.querySelector('.ql-editor'),
        mainImage: document.querySelector('.head-info .card-image img'),
        edit: true
    };

    function initIfEdit() {
        if(document.getElementById('_edit') === null){
            inputs.edit = false;
            return;
        }
        inputs.articleId = document.getElementById("article_id").getAttribute("content")

        let titleEdit = document.getElementById('title_edit'),
            subtitleEdit = document.getElementById('subtitle_edit'),
            tagsEdit = document.getElementById('tags_edit'),
            tagsEditChilds = tagsEdit.children,
            articleEdit = document.getElementById('article_edit'),
            imageEdit = document.getElementById('image_edit');

        inputs.titleInput.value = titleEdit.innerText;
        inputs.subtitleInput.value = subtitleEdit.innerText;
        if(tagsEditChilds.length>0){
            tags.tagLabel.style.top = '-14px';
            tags.tagLabel.style.fontSize = '12px';
        }
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
        inputs.qlEditor.innerHTML = articleEdit.innerHTML.trim();
        inputs.mainImage.src = '/'+imageEdit.getAttribute('largeImagePath');

        images.smallImagePath = imageEdit.getAttribute('smallImagePath');
        images.middleImagePath = imageEdit.getAttribute('middleImagePath');
        images.largeImagePath = imageEdit.getAttribute('largeImagePath');

        titleEdit.parentNode.removeChild(titleEdit);
        subtitleEdit.parentNode.removeChild(subtitleEdit);
        tagsEdit.parentNode.removeChild(tagsEdit);
        articleEdit.parentNode.removeChild(articleEdit);
        imageEdit.parentNode.removeChild(imageEdit);
    }

const qlToolbar = articleEditor.querySelector(".ql-tooltip"),
    qlToolbarArrow = qlToolbar.querySelector(".ql-tooltip-arrow");
const editor = {

    isShow: false,
    qlToolbarWidth: 0,
    articleEditorWidth: articleEditor.clientWidth,

    showToolbar: function(x, y) {
        let bounds = articleEditor.getBoundingClientRect(),
            xMid = editor.articleEditorWidth/2 - editor.qlToolbarWidth/2,
            yOffset = bounds.top;

        qlToolbar.style.left = xMid + 'px';
        qlToolbar.style.top = y - yOffset + 'px';

        qlToolbarArrow.style.marginLeft = 0;

        qlToolbar.classList.remove('ql-editing');
        qlToolbar.classList.remove('ql-hidden');
        editor.isShow = true;
    },
    hideToolbar: function() {
        qlToolbar.classList.add('ql-hidden');
        editor.isShow = false;
    }
};

function initEditor() {

    qlToolbar.classList.remove("ql-hidden");
    editor.qlToolbarWidth = qlToolbar.clientWidth;
    qlToolbar.classList.add("ql-hidden");

    articleEditor.addEventListener('click', function (e) {
        if(editor.isShow){
            editor.hideToolbar();
        }else{
            editor.showToolbar(e.clientX, e.clientY);
        }
    });
    qlToolbar.addEventListener('click', function (e) {
        e.stopPropagation();
    });
}
function initImageManager() {

    images.topImageSection.onmouseenter = function (e) {
        images.imageHover.style.display = 'flex';
    };
    images.topImageSection.onmouseleave = function (e) {
        images.imageHover.style.display = 'none';
    };
    images.uploadMainImage.onchange = function (e) {

        let file = new FormData();
        file.append('file',images.uploadMainImage.files[0]);

        xhr.request({
                path:'/upload',
                method:'POST',
                headers:{
                    'Upload-Type':'article.main'
                },
                content: file
            }, function (response, error) {
                if(response){
                    let result = JSON.parse(response);

                    images.smallImagePath = result.small;
                    images.middleImagePath = result.middle;
                    images.largeImagePath = result.large;

                    inputs.mainImage.src = '/'+result.large;
                }else if (error){
                    modal.error('Не удалось загрузить файл');
                }
            });
    }
}

let  creating = false;
function initCreate() {

    const createButton = document.querySelector('.create-article');

    createButton.addEventListener('click', function (e) {
        if(creating){
            return;
        }
        let innerHtml = inputs.qlEditor.innerHTML,
            innerText = quill.getText(),
            tagsIDS = Object.keys(inputs.tagsObjects);
        if(inputs.titleInput.value.length > 100 || inputs.titleInput.value.length < 10){
            modal.error('Длина заголовка должна быть больше 10 и не превышать 100 символов');
            return;
        }else if(inputs.subtitleInput.length < 10 || inputs.subtitleInput.length > 100){
            modal.error('Длина подзаголовка должна быть больше 10 и не превышать 100 символов');
            return;
        } else if(tagsIDS.length < 3 || tagsIDS.length > 5){
            modal.error('Количество тегов должно быть больше 3-х и меньше 5-ти');
            return;
        }else if(innerText.length < 2 || innerText.length > 10000){
            modal.error('Длина статьи должна быть больше 500 и не превышать 10000 символов');
            return;
        }else if(images.smallImagePath === undefined || images.middleImagePath === undefined
                || images.largeImagePath === undefined){
            modal.error('Не удалось загрузить изображение');
            return;
        }
        creating = true;
        const article = {
            title: inputs.titleInput.value,
            subtitle: inputs.subtitleInput.value,
            tags: tagsIDS,
            htmlContent: innerHtml,
            smallImagePath: images.smallImagePath,
            middleImagePath: images.middleImagePath,
            largeImagePath: images.largeImagePath
        };

        if(!inputs.edit){
            createArticle(article);
        }else{
            updateArticle(article);
        }
    });
}

    function createArticle(article){
        modal.load('Идёт создание статьи...');
        xhr.request({
            path: '/services/articles',
            method: 'POST',
            headers: {
                'Content-Type':'application/json'
            },
            content: JSON.stringify(article)
        }, function (result, error) {
            if(result){
                let obj = JSON.parse(result);
                window.location.href = 'http://localhost/articles/' + obj.id;
            }else if(error){
                modal.error('Не удалось создать статью');
            }
        });
    }

    function updateArticle(article){
        article.articleId = inputs.articleId;
        modal.load('Идёт обновление статьи...');
        xhr.request({
            path: '/services/articles',
            method: 'PUT',
            headers: {
                'Content-Type':'application/json'
            },
            content: JSON.stringify(article)
        }, function (result, error) {
            if(result){
                window.location.href = 'http://localhost:80/articles/' + inputs.articleId;
            }else if(error){
                modal.error('Не удалось обновить статью');
            }
        });
    }
/*
* Init functions
* */
initTags();
initIfEdit();
initEditor();
initImageManager();
initCreate();

