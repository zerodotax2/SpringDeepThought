'use strict';
window.tags = {
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
        tags.tagLabel.style.top = '-18px';
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
    tags.tagInput.addEventListener('keydown', onTagInputStart);
    tags.tagInput.addEventListener('keyup', onTagInputEnd);
}
    let removeFromEmpty = false;
    function onTagInputStart(e) {
        console.log(tags.tagInput.value);
        if(tags.tagInput.value.length < 1) {
            removeFromEmpty = true;
        }else{
            removeFromEmpty = false;
        }
    }
    function onTagInputEnd(e) {
        const value = tags.tagInput.value;
        clearInterval(tags.loadTimer);
        if(e.key === 'Backspace' && value.length < 1 && removeFromEmpty){
            let selectedTag = tags.tagsInputContainer.querySelector('.selected-tag:last-of-type');
            if(selectedTag !== undefined && selectedTag !== null){
                delete tags.tagsCount[selectedTag.getAttribute('content')];
                tags.tagsInputContainer.removeChild(selectedTag);
            }
            return;
        }
        if(value.trim().length<1)
            return;
        if(tags.words[value]){
            showTags(value);
        }else{
            tags.loadTimer = setTimeout(function () {
                xhr.request({
                    method: 'GET',
                    path: '/services/tags/small?q='+value
                }, function (result, error) {
                    if(result){
                        let tagsArray = JSON.parse(result);
                        tags.words[value] = [];
                        for(let i = 0; i < tagsArray.length; i++){
                            let tag = tagsArray[i];
                            tags.words[value].push(tag.id);
                            tags.cache[tag.id] = tag;
                        }
                        showTags(value);
                    }
                });
            }, 500);
        }
    }
    function upPlaceholder() {

        if(tags.tagLabel.style.top.replace('px', '') < 0)
            return;

        let current = 17;
        const max = -18,
            timer = setInterval(function () {
                if(current > max){
                    tags.tagLabel.style.top = current + 'px';
                    tags.tagLabel.style.fontSize = 12 - current/25 + 'px';
                    current -= 5;
                }else{
                    clearInterval(timer);
                }
            }, 30);
    }
    function downPlaceholder() {
        if(Object.keys(tags.tagsCount).length>0)
            return;
        let current = -18;
        const min = 17,
            timer = setInterval(function () {
                if(current < min){
                    tags.tagLabel.style.top = current + 'px';
                    tags.tagLabel.style.fontSize = 15 - current/25 + 'px';
                    current += 5;
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
        for(let i = 0; i < ids.length; i++){
            let tag = tags.cache[ids[i]],
                node = document.createElement('div');
            node.classList.add('chip', 'chip-suggest');
            node.style.background = tag.color;
            node.setAttribute('content', tag.id);
            node.addEventListener('click', clickTag);
            node.innerText = tag.name;
            tags.tagView.appendChild(node);
        }
    }
    function clickTag(e) {
        let target = e.currentTarget,
            selectedTag = target.cloneNode(true),
            id = target.getAttribute('content');
        clearTimeout(tags.closeTagViewTimer);
        selectedTag.classList.add('selected-tag');
        selectedTag.style.minWidth = (selectedTag.innerHTML.length * 9) + "px";
        selectedTag.addEventListener('click',  deleteTagOnClick);
        if(Object.keys(tags.tagsCount).length < 5 && tags.tagsCount[id] === undefined){
            tags.tagsCount[id] = 1;
            tags.tagsInputContainer.insertBefore(selectedTag,
                tags.tagInput);
            tags.tagInput.value = "";
        }
    }
    function deleteTagOnClick(e){
        delete tags.tagsCount[e.currentTarget.getAttribute('content')];
        tags.tagsInputContainer.removeChild(e.currentTarget);
    }