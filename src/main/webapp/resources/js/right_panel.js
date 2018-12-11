'use strict';
const fact = {
    factPanel: document.querySelector('.fact'),
};
function initFact() {
    fact.factID = fact.factPanel.getAttribute('content');
    fact.factText = fact.factPanel.querySelector('#factText');
    fact.tagContainer = fact.factPanel.querySelector('.card-action');

    fact.factHover = document.createElement('div');
    fact.factHover.classList.add('fact-hover');
    fact.factHover.style.display = 'none';
    fact.factHover.innerHTML = '<div class="fact-rate"><svg class="factUP" role="img" xmlns="http://www.w3.org/2000/svg" fill="#ffffff" viewBox="0 0 512 512"><path  d="M104 224H24c-13.255 0-24 10.745-24 24v240c0 13.255 10.745 24 24 24h80c13.255 0 24-10.745 24-24V248c0-13.255-10.745-24-24-24zM64 472c-13.255 0-24-10.745-24-24s10.745-24 24-24 24 10.745 24 24-10.745 24-24 24zM384 81.452c0 42.416-25.97 66.208-33.277 94.548h101.723c33.397 0 59.397 27.746 59.553 58.098.084 17.938-7.546 37.249-19.439 49.197l-.11.11c9.836 23.337 8.237 56.037-9.308 79.469 8.681 25.895-.069 57.704-16.382 74.757 4.298 17.598 2.244 32.575-6.148 44.632C440.202 511.587 389.616 512 346.839 512l-2.845-.001c-48.287-.017-87.806-17.598-119.56-31.725-15.957-7.099-36.821-15.887-52.651-16.178-6.54-.12-11.783-5.457-11.783-11.998v-213.77c0-3.2 1.282-6.271 3.558-8.521 39.614-39.144 56.648-80.587 89.117-113.111 14.804-14.832 20.188-37.236 25.393-58.902C282.515 39.293 291.817 0 312 0c24 0 72 8 72 81.452z"></path></svg>' +
        '<svg role="img" class="factDOWN" xmlns="http://www.w3.org/2000/svg" fill="#ffffff" viewBox="0 0 512 512"><path  d="M0 56v240c0 13.255 10.745 24 24 24h80c13.255 0 24-10.745 24-24V56c0-13.255-10.745-24-24-24H24C10.745 32 0 42.745 0 56zm40 200c0-13.255 10.745-24 24-24s24 10.745 24 24-10.745 24-24 24-24-10.745-24-24zm272 256c-20.183 0-29.485-39.293-33.931-57.795-5.206-21.666-10.589-44.07-25.393-58.902-32.469-32.524-49.503-73.967-89.117-113.111a11.98 11.98 0 0 1-3.558-8.521V59.901c0-6.541 5.243-11.878 11.783-11.998 15.831-.29 36.694-9.079 52.651-16.178C256.189 17.598 295.709.017 343.995 0h2.844c42.777 0 93.363.413 113.774 29.737 8.392 12.057 10.446 27.034 6.148 44.632 16.312 17.053 25.063 48.863 16.382 74.757 17.544 23.432 19.143 56.132 9.308 79.469l.11.11c11.893 11.949 19.523 31.259 19.439 49.197-.156 30.352-26.157 58.098-59.553 58.098H350.723C358.03 364.34 384 388.132 384 430.548 384 504 336 512 312 512z"></path></svg></div>' +
        '<div class="fact-link"><a class="" href="/facts">Факты</a></div>';
    fact.factPanel.appendChild(fact.factHover);
    fact.factPanel.querySelector('.factUP').addEventListener('click', function () {
       clickFact(1);
    });
    fact.factPanel.querySelector('.factDOWN').addEventListener('click', function () {
        clickFact(-1);
    });
    fact.factPanel.addEventListener('mouseenter', openFactHover);
    fact.factPanel.addEventListener('mouseleave', closeFactHover);
    clickFact(0);
}

function clickFact(rate){
    closeFactHover();
    fact.factPanel.removeEventListener('mouseenter', openFactHover);
    fact.factPanel.removeEventListener('mouseleave', closeFactHover);
    xhr.request({
        path: '/services/facts/random?rate='+rate+'&fact='+fact.factID,
        method: 'GET'
    }, function (response, error) {
        if(response){
            const nFact = JSON.parse(response),
                factTags = nFact.tags;

            fact.factID = nFact.id;
            fact.factText.innerHTML = nFact.text;

            for(let i = 0; i < factTags.length; i++){
                let factTag = factTags[i],
                    newTag = document.createElement('a');
                newTag.href = '/tags/' + factTag.id;
                newTag.classList.add('chip');
                newTag.style.background = factTag.color;
                newTag.innerText = factTag.name;
                fact.tagContainer.appendChild(newTag);
            }
        }else if(error){
            fact.factText.innerHTML = '<svg role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="#FF5556" d="M504 256c0 136.997-111.043 248-248 248S8 392.997 8 256C8 119.083 119.043 8 256 8s248 111.083 248 248zm-248 50c-25.405 0-46 20.595-46 46s20.595 46 46 46 46-20.595 46-46-20.595-46-46-46zm-43.673-165.346l7.418 136c.347 6.364 5.609 11.346 11.982 11.346h48.546c6.373 0 11.635-4.982 11.982-11.346l7.418-136c.375-6.874-5.098-12.654-11.982-12.654h-63.383c-6.884 0-12.356 5.78-11.981 12.654z"></path></svg>';
        }
        fact.factPanel.addEventListener('mouseenter', openFactHover);
        fact.factPanel.addEventListener('mouseleave', closeFactHover);
    });
    fact.factText.innerHTML = '<div class="progress green lighten-4"><div class="indeterminate green"></div></div>';
    fact.tagContainer.innerHTML = '';
}
function closeFactHover() {
    fact.factHover.style.display = 'none';
}
function openFactHover() {
    fact.factHover.style.display = 'flex';
}


initFact();