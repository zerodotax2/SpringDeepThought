
const factId = document.getElementById("fact_id").getAttribute("content");

const editor = {
    area: document.getElementById('describeInput'),
    counter: document.querySelector('.content .describe .counter span'),
    create: document.querySelector('.create'),
    creating: false,
    edit: false,
};
function initEditor(){
    editor.area.addEventListener('input', function () {
       const value = editor.area.value;
       if(value.length > 1000){
           editor.value = value.substring(0, 1000);
       }
       editor.counter.innerText = 1000 - value.length;
    });
}
function create(){
    editor.create.addEventListener('click', function () {
       let tagsIDS = Object.keys(tags.tagsCount),
            describe = editor.area.value;
       if(editor.creating){
           return;
       }
       if(tagsIDS.length < 3 || tagsIDS.length > 5){
           modal.error('Кол-во тегов должно быть от 3 до 5');
           return;
       }else if(describe.length < 100 || describe.length > 1000){
           modal.error('Длина описания должна быть больше 100 и не превышать 1000 символов');
           return;
       }
       editor.creating = true;
       if(editor.edit){
           xhr.request({
               path: '/services/facts',
               method: 'PUT',
               content: JSON.stringify({
                   factId: factId,
                   tags: tagsIDS,
                   text: describe
               })
           }, function (response, error) {
               if(response){
                   let href = window.location.href,
                       fact = JSON.parse(response);
                   window.location.href = href.substring(0, href.lastIndexOf('add')) + fact.id;
               }else if(error){
                   modal.error('Не удалось обновить факт');
               }
               editor.creating = false;
           });
           modal.load('Идёт обновление факта...');
       }else{
           xhr.request({
               path: '/services/facts',
               method: 'POST',
               content: JSON.stringify({
                   tags: tagsIDS,
                   text: describe
               })
           }, function (response, error) {
               if(response){
                   let href = window.location.href,
                       fact = JSON.parse(response);
                   window.location.href = href.substring(0, href.lastIndexOf('add')) + fact.id;
               }else if(error){
                   modal.error('Не удалось создать факт');
               }
               editor.creating = false;
           });
           modal.load('Идёт создание факта...');
       }
    });
}

function initIfEdit(){
     if(document.getElementById('_edit') == null){
         return;
     }
     editor.edit = true;
     const textEdit = document.getElementById('text_edit'),
            tagsEdit = document.getElementById("tags_edit");
     Array.forEach(tagsEdit.children, function (metaTag) {
         let selectedTag = document.createElement('div'),
             name = metaTag.getAttribute('name'),
             color = metaTag.getAttribute('color'),
             id = metaTag.getAttribute('content');
         selectedTag.classList.add('selected-tag', 'chip');
         selectedTag.style.background = color;
         selectedTag.innerText = name;
         selectedTag.style.color = '#ffffff';
         selectedTag.setAttribute('content', id);
         selectedTag.onclick = deleteTagOnClick;
         tags.tagsCount[id] = 1;
         tags.tagsInputContainer.insertBefore(selectedTag, tags.tagInput);
     });
     upPlaceholder();
     editor.area.value = textEdit.getAttribute('content').trim();

     textEdit.parentNode.removeChild(textEdit);
     tagsEdit.parentNode.removeChild(tagsEdit);
}

initTags();
initIfEdit();
initEditor();
create();