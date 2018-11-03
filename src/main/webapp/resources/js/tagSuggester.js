const tags = {
    tagsInputContainer: document.querySelector('.tags .tags-input'),
    tagInput: document.querySelector('.tags .tags-input input'),
    tagView: document.querySelector('.tags .tags-view'),
    tagLabel: document.querySelector('.tags .name'),
    loadTimer: undefined,
    cache: {},
    words: {},
    tagsCount: {},
    closeTagViewTimer: undefined
};

function initTags(){
    if(tags.tagInput.value !== ''){
        tags.tagLabel.style.bottom = '45px';
        tags.tagLabel.style.fontSize = '12px';
    }
    tags.tagInput.addEventListener('focus', function (e) {
        if(tags.tagInput.value === ''){
            upPlaceholder();
        }
        tags.tagView.style.display = 'block';
    });
    tags.tagInput.addEventListener('blur', function (e) {
        if(tags.tagInput.value === ''){
            downPlaceholder();
        }
       tags.closeTagViewTimer = setTimeout(function () {
            tags.tagView.style.display = 'none';
        }, 200);
    });
    tags.tagInput.addEventListener('keydown', onTagInput);
}
    function onTagInput(e) {
        const value = tags.tagInput.value;
        clearInterval(tags.loadTimer);
        if(e.key === 'Backspace' && value.length < 1){
            let selectedTag = tags.tagsInputContainer.querySelector('.selected-tag:last-of-type');
            if(selectedTag !== undefined && selectedTag !== null){
                delete tags.tagsCount[selectedTag.getAttribute('content')];
                tags.tagsInputContainer.removeChild(selectedTag);
            }
            return;
        }
        if(tags.words[value]){
            showTags(value);
        }else{
            tags.loadTimer = setTimeout(function () {
                xhr.request({
                    method: 'GET',
                    path: '/services/tags/find?q='+value+'&size=small'
                }, function (result, error) {
                    if(result){
                        let tagsArray = JSON.parse(result);
                        tags.words[value] = [];
                        tagsArray.forEach(function (tag) {
                            tags.words[value].push(tag.id);
                            tags.cache[tag.id] = tag;
                        });
                        showTags(value);
                    }
                });
            }, 500);
        }
    }
    function upPlaceholder() {
        let current = 20;
        const max = 50,
            timer = setInterval(function () {
                if(current < max){
                    tags.tagLabel.style.bottom = current + 'px';
                    tags.tagLabel.style.fontSize = 15 - current/17 + 'px';
                    current += 5;
                }else{
                    clearInterval(timer);
                }
            }, 30);
    }
    function downPlaceholder() {
        let current = 50;
        const min = 15,
            timer = setInterval(function () {
                if(current > min){
                    tags.tagLabel.style.bottom = current + 'px';
                    tags.tagLabel.style.fontSize = 16.5 - current/17 + 'px';
                    current -= 5;
                }else{
                    clearInterval(timer);
                }
            }, 30);
    }
    function showTags(value){
        if(tags.tagView.getAttribute('content') === value){
            return;
        }
        let ids = tags.words[value];
        tags.tagView.innerHTML = '';
        ids.forEach(function (id) {
            let tag = tags.cache[id],
                node = document.createElement('div');
            node.classList.add('chip', 'chip-suggest');
            node.style.background = tag.color;
            node.setAttribute('content', tag.id);
            node.onclick = clickTag;
            node.innerText = tag.name;
            tags.tagView.appendChild(node);
        })
    }
    function clickTag(e) {
        let target = e.currentTarget,
            selectedTag = target.cloneNode(true),
            id = target.getAttribute('content');
        clearTimeout(tags.closeTagViewTimer);
        selectedTag.classList.add('selected-tag');
        selectedTag.style.minWidth = (selectedTag.innerHTML.length * 9) + "px";
        selectedTag.onclick = deleteTagOnClick;
        if(Object.keys(tags.tagsCount).length < 5 && tags.tagsCount[id] === undefined){
            tags.tagsCount[id] = 1;
            tags.tagsInputContainer.insertBefore(selectedTag,
                tags.tagInput);
        }
    }
    function deleteTagOnClick(e){
        delete tags.tagsCount[e.currentTarget.getAttribute('content')];
        tags.tagsInputContainer.removeChild(e.currentTarget);
    }