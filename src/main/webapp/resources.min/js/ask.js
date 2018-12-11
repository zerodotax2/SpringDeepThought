"use strict";const questionEditor=document.querySelector("#question-editor"),quill=new Quill(questionEditor,{theme:"bubble",placeholder:"Опишите ваш вопрос",fontSize:"20",modules:{toolbar:[[{size:["small",!1,"large","huge"]}],["bold","italic","underline","strike"],["direction",{align:["","center","right","justify"]}],["code-block","blockquote",{list:"ordered"},{list:"bullet"}],["link","image"]]}});initQuillEditor(questionEditor);const inputs={titleInput:document.querySelector("#header"),tagsObjects:tags.tagsCount,qlEditor:document.querySelector(".editor-container .ql-editor"),edit:!0};function initIfEdit(){if(null===document.getElementById("_edit"))return void(inputs.edit=!1);inputs.questionId=document.getElementById("question_id").getAttribute("content");let t=document.getElementById("title_edit"),e=document.getElementById("tags_edit"),n=e.children,i=document.getElementById("question_edit");inputs.titleInput.value=t.innerText;for(let t=0;t<n.length;t++){let e=n[t],i=document.createElement("div"),o=e.getAttribute("name"),r=e.getAttribute("color"),l=e.getAttribute("content");i.classList.add("selected-tag","chip"),i.style.background=r,i.innerText=o,i.style.color="#ffffff",i.setAttribute("content",l),i.addEventListener("click",deleteTagOnClick),tags.tagsCount[l]=1,tags.tagsInputContainer.insertBefore(i,tags.tagsInputContainer.lastElementChild)}upPlaceholder(),inputs.qlEditor.innerHTML=i.innerHTML.trim(),t.parentNode.removeChild(t),e.parentNode.removeChild(e),i.parentNode.removeChild(i)}let creating=!1;function initCreator(){document.querySelector(".send-question").addEventListener("click",function(t){if(!creating){let t=inputs.titleInput.value,e=Object.keys(tags.tagsCount),n=inputs.qlEditor.innerHTML,i=quill.getText();if(t.length<10||t.length>100)return void modal.error("Длина заголовка не должна быть меньше 10 и превышать 100 символов");if(!t.match(/^[А-я|Ё|ё|\w|\s|\d|.|,|:|-]+$/))return void modal.error("Заголовок может содержать только буквы, цифры и знаки препинания");if(e.length<2||e.length>5)return void modal.error("Количество тегов должно быть от 2-х до 5");if(i.length>1e4||i.length<100)return void modal.error("Длина вопроса не может быть меньше 100 или больше 10000 символов");creating=!0;const o={title:t,tags:e,htmlContent:n};inputs.edit?updateQuestion(o):createQuestion(o)}})}function createQuestion(t){xhr.request({method:"POST",path:"/services/questions",headers:{"Content-Type":"application/json"},content:JSON.stringify(t)},function(t,e){if(t){let e=JSON.parse(t);location.href=location.href.replace("ask","")+e.id}else e&&modal.error("Не удалось создать вопрос");creating=!1}),modal.load("Идёт создание вопроса...")}function updateQuestion(t){t.questionId=inputs.questionId,xhr.request({method:"PUT",path:"/services/questions",headers:{"Content-Type":"application/json"},content:JSON.stringify(t)},function(t,e){t?location.href=location.href.replace("edit","")+inputs.questionId:e&&modal.error("Не удалось обновить вопрос"),creating=!1}),modal.load("Идёт обновление вопроса...")}initTags(),initIfEdit(),initCreator();