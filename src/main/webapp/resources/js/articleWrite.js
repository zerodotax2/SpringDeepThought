const articleId = document.getElementById("article_id").getAttribute("content");

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

        let titleEdit = document.getElementById('title_edit'),
            subtitleEdit = document.getElementById('subtitle_edit'),
            tagsEdit = document.getElementById('tags_edit'),
            tagsEditChilds = tagsEdit.children,
            articleEdit = document.getElementById('article_edit'),
            imageEdit = document.getElementById('image_edit');

        inputs.titleInput.value = titleEdit.innerText;
        inputs.subtitleInput.value = subtitleEdit.innerText;
        Array.forEach(tagsEditChilds, function (tag) {
            let selectedTag = document.createElement('div'),
                name = tag.getAttribute('name'),
                color = tag.getAttribute('color'),
                id = tag.getAttribute('content');
            selectedTag.classList.add('selected-tag', 'chip');
            selectedTag.style.background = color;
            selectedTag.innerText = name;
            selectedTag.style.color = '#ffffff';
            selectedTag.setAttribute('content', id);
            selectedTag.onclick = deleteTagOnClick;
            tags.tagsCount[id] = 1;
            tags.tagsInputContainer.insertBefore(selectedTag, tags.tagsInputContainer.lastElementChild);
        });
        upPlaceholder();
        inputs.qlEditor.innerHTML = articleEdit.innerHTML.trim();
        inputs.mainImage.src = appConf.fileServerLocation + imageEdit.getAttribute('largeImagePath');

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

    articleEditor.onclick = function (e) {
        if(editor.isShow){
            editor.hideToolbar();
        }else{
            editor.showToolbar(e.clientX, e.clientY);
        }
    };
    qlToolbar.onclick = function (e) {
        e.stopPropagation();
    }
}
const images = {

    imageHover:  document.querySelector('#image-hover'),
    topImageSection: document.querySelector('.head-info .card-image'),
    uploadMainImage: document.querySelector('#uploadMainImage'),

};
function initImageManager() {

    images.topImageSection.onmouseenter = function (e) {
        images.imageHover.style.display = 'flex';
    };
    images.topImageSection.onmouseleave = function (e) {
        images.imageHover.style.display = 'none';
    };
    images.uploadMainImage.onchange = function (e) {
        xhr.file('/upload', images.uploadMainImage.files[0], function (response, error) {
            if(response){
                let result = JSON.parse(response);

                images.smallImagePath = result.smallImagePath;
                images.middleImagePath = result.middleImagePath;
                images.largeImagePath = result.largeImagePath;

                inputs.mainImage.src = result.fileServerLocation + result.largeImagePath;
            }else if(error){

            }
        });
    }
}

let  creating = false;
function initCreate() {

    const createButton = document.querySelector('.create-article');

    createButton.onclick = function (e) {
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
        creator.creating = true;
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
    }
}

    function createArticle(article){
        xhr.request({
            path: '/services/articles',
            method: 'POST',
            content: JSON.stringify(article)
        }, function (result, error) {
            if(result){
                let obj = JSON.parse(result);
                window.location.href = 'http://localhost:8080/articles/' + obj.id;
            }else if(error){
                modal.error('Не удалось создать статью');
            }
        });
        modal.load('Идёт создание статьи...');
    }

    function updateArticle(article){
        article.articleId = articleId;
        xhr.request({
            path: '/services/articles',
            method: 'PUT',
            content: JSON.stringify(article)
        }, function (result, error) {
            if(result){
                let obj = JSON.parse(result);
                window.location.href = 'http://localhost:8080/articles/' + obj.id;
            }else if(error){
                modal.error('Не удалось обновить статью');
            }
        });
        modal.load('Идёт обновление статьи...');
    }
/*
* Init functions
* */
initTags();
initIfEdit();
initEditor();
initImageManager();
initCreate();

