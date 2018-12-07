'use strict';
const result = {
    colorPickerInstance: undefined,
    nameInput: document.getElementById('nameInput'),
    describeInput: document.getElementById('describeInput'),
    tagPreview: document.querySelector('.tag-preview'),
    counter: document.querySelector('.content .right .counter span'),
    createBtn: document.querySelector('.create'),
    rgb: {r: 151, g: 151, b: 151},
    creating: false,
    edit: false
};
function initColorPicker() {
    const colorPickerInstance = new iro.ColorPicker('.color-container',{
              width: 200,
              height: 200,
              color: result.rgb,
          });
    result.colorPickerInstance = colorPickerInstance;
    colorPickerInstance.on('color:change', onColorPickerChange);
    function onColorPickerChange() {
        let rgb = result.colorPickerInstance.color.rgb;
        result.tagPreview.style.background = 'rgb('+rgb.r+','+rgb.g+','+rgb.b+')';
    }
}

function initInputs() {
    result.nameInput.addEventListener('input',function () {
        const value = result.nameInput.value;
        if(value.length > 32){
            result.nameInput.value = value.substring(0, 32);
        }
        result.tagPreview.innerText = value;
    });
    result.describeInput.addEventListener('input', function () {
       const value = result.describeInput.value;
       if(value.length > 1000){
           result.describeInput.value = value.substring(0, 1000);
       }
       result.counter.innerText = 1000 - value.length;
    });
}
function create() {
    result.createBtn.addEventListener('click', function () {
        if(result.creating){
            return;
        }
       const rgb = result.colorPickerInstance.color.rgb,
           color = rgbToHex(rgb.r, rgb.g, rgb.b),
           name = result.nameInput.value,
           describe = result.describeInput.value;
       if(name.length < 3 || name.length > 32){
           modal.error('Длина имени должна быть не меньше 3 и не превышать 32 символа');
           return;
       }else if(!name.match(/^[A-z|А-я|\s|-]+$/)){
           modal.error('Неправильный формат имени');
           return;
       }else if(describe.length < 50 || describe.length > 1000){
           modal.error('Длина описания должна быть не меньше 50 и не превышать 1000 символов');
           return;
       }
       result.creating = true;
       if(result.edit){
           modal.load('Идёт обновление тега...');
           xhr.request({
               path: '/services/tags',
               method: 'PUT',
               headers: {
                   'Content-Type':'application/json'
               },
               content: JSON.stringify({
                   tagId: result.tagId,
                   name: name,
                   description: describe,
                   color: color
               })
           }, function (response, error) {
               if(response){
                   const href = window.location.href;
                   window.location.href = href.substring(0, href.lastIndexOf('edit'));
               }else if(error){
                   modal.error('Не удалось обновить тег');
               }
               result.creating = false;
           });
       }else{
           modal.load('Идёт создание тега');
           xhr.request({
               path: '/services/tags',
               method: 'POST',
               headers: {
                   'Content-Type':'application/json'
               },
               content: JSON.stringify({
                   name: name,
                   description: describe,
                   color: color
               })
           }, function (response, error) {
               if(response){
                   const tag = JSON.parse(response),
                       href = window.location.href;
                   if(tag !== undefined)
                        window.location.href = href.substring(0, href.lastIndexOf('add')) + tag.id
               }else if(error){
                   modal.error('Не удалось создать тег');
               }
               result.creating = false;
           });
       }
    });
}

function initIfEdit(){
    if(document.getElementById('_edit') === null){
        return;
    }
    result.edit = true;
    const colorEdit = document.getElementById('color_edit'),
        rgb = hexToRgb(colorEdit.getAttribute('content')),
        nameEdit = document.getElementById('name_edit'),
        describeEdit = document.getElementById('describe_edit');

    result.tagId = document.getElementById('tag_id').getAttribute('content');

    result.rgb = rgb;
    result.tagPreview.innerText = nameEdit.getAttribute('content');
    result.nameInput.value = nameEdit.getAttribute('content');
    result.describeInput.value = describeEdit.getAttribute('content');

    colorEdit.parentNode.removeChild(colorEdit);
    nameEdit.parentNode.removeChild(nameEdit);
    describeEdit.parentNode.removeChild(describeEdit);
}

    function hexToRgb(hex) {
        let result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
        return result ? {
            r: parseInt(result[1], 16),
            g: parseInt(result[2], 16),
            b: parseInt(result[3], 16)
        } : null;
    }
    function rgbToHex(r, g, b){
        return '#' + dec2hex(r) + dec2hex(g) + dec2hex(b);
    }
    function dec2hex(d){
        if(d>15){
            return d.toString(16);
        }else{
            return '0'+ d.toString(16);
        }
    }
initIfEdit();
initColorPicker();
initInputs();
create();