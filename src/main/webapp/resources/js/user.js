'use strict';
const userId = document.getElementById('owner_id').getAttribute('content'),
    host = document.getElementById('host').getAttribute('content'),
    ownerLogin = document.getElementById('owner_login').getAttribute('content'),
    ownerImg = document.getElementById('owner_img').getAttribute('content');

const genericComponents = {
    moreBtn : function (path) {
        const btn = document.createElement('div');
        btn.classList.add('more-results');
        btn.innerHTML = '<a href="'+path+'" class="more z-depth-1">Смотреть дальше</a>';
        return btn;
    },
    loadDiv : "<div class='preview'>" +
    "                        <div class='load-indicator'><div></div><div></div><div></div><div></div></div>" +
    "                    </div>",
    noContent: "<div class='no-content grey-text'>" +
    "                        Нет информации" +
    "                    </div>",
    error: "<div class='exception'>" +
    "                        <svg style='width: 20px;height: 20px;' role='img' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 576 512'><path fill='#F44336' d='M569.517 440.013C587.975 472.007 564.806 512 527.94 512H48.054c-36.937 0-59.999-40.055-41.577-71.987L246.423 23.985c18.467-32.009 64.72-31.951 83.154 0l239.94 416.028zM288 354c-25.405 0-46 20.595-46 46s20.595 46 46 46 46-20.595 46-46-20.595-46-46-46zm-43.673-165.346l7.418 136c.347 6.364 5.609 11.346 11.982 11.346h48.546c6.373 0 11.635-4.982 11.982-11.346l7.418-136c.375-6.874-5.098-12.654-11.982-12.654h-63.383c-6.884 0-12.356 5.78-11.981 12.654z'></path></svg>" +
    "                        <div class='message red-text'>Произошла ошибка</div>" +
    "                    </div>",
    societyShare: ''
};

function shareBtn() {
    genericComponents.societyShare = '<div class="modal-container grey-text" style="display: flex;flex-flow: column;justify-content: center;text-align: center;">' +
        '                <h4 style="margin: 20px 30px 20px 30px;">Поделиться профилем</h4>' +
        '                <div class="social-btns" style="display: flex;align-items: center;justify-content: center;">' +
        '                    <a href="https://vk.com/share.php?url='+window.location.href+'&title='+ownerLogin+'%20-%20DeepThought&description=%D0%9C%D0%BE%D0%B9%20%D0%BF%D1%80%D0%BE%D1%84%D0%B8%D0%BB%D1%8C%20%D0%B2%20DeepThought&image='+host+ownerImg+'&noparse=true">' +
        '                        <?xml version="1.0" encoding="UTF-8"?>' +
        '                        <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 48 48" version="1.1" width="48px" height="48px">' +
        '                            <g id="surface1">' +
        '                                <path style=" fill:#0288D1;" d="M 42 37 C 42 39.761719 39.761719 42 37 42 L 11 42 C 8.238281 42 6 39.761719 6 37 L 6 11 C 6 8.238281 8.238281 6 11 6 L 37 6 C 39.761719 6 42 8.238281 42 11 Z "/>' +
        '                                <path style=" fill:#FFFFFF;" d="M 33.183594 27.359375 C 35.265625 29.308594 35.699219 30.261719 35.769531 30.378906 C 36.628906 31.824219 34.816406 31.9375 34.816406 31.9375 L 31.34375 31.988281 C 31.34375 31.988281 30.59375 32.136719 29.613281 31.453125 C 28.3125 30.554688 26.535156 28.210938 25.582031 28.515625 C 24.613281 28.824219 24.921875 31.28125 24.921875 31.28125 C 24.921875 31.28125 24.929688 31.582031 24.710938 31.824219 C 24.46875 32.078125 24 31.980469 24 31.980469 L 23 31.980469 C 23 31.980469 19.292969 31.996094 16.269531 28.816406 C 12.976563 25.351563 10.222656 18.871094 10.222656 18.871094 C 10.222656 18.871094 10.054688 18.460938 10.234375 18.242188 C 10.441406 17.996094 11 18 11 18 L 14.714844 18 C 14.714844 18 15.0625 18.046875 15.3125 18.234375 C 15.523438 18.386719 15.636719 18.675781 15.636719 18.675781 C 15.636719 18.675781 16.085938 19.882813 16.882813 21.273438 C 18.433594 23.984375 19.15625 24.578125 19.683594 24.285156 C 20.449219 23.859375 20.21875 20.453125 20.21875 20.453125 C 20.21875 20.453125 20.234375 19.214844 19.832031 18.664063 C 19.519531 18.234375 18.9375 18.109375 18.679688 18.074219 C 18.46875 18.050781 18.8125 17.558594 19.257813 17.339844 C 19.925781 17.011719 21.109375 16.988281 22.503906 17.003906 C 23.589844 17.015625 24 17 24 17 C 25.28125 17.3125 24.847656 18.523438 24.847656 21.414063 C 24.847656 22.34375 24.679688 23.648438 25.34375 24.078125 C 25.628906 24.269531 26.652344 24.296875 28.390625 21.308594 C 29.21875 19.890625 29.871094 18.558594 29.871094 18.558594 C 29.871094 18.558594 30.011719 18.257813 30.21875 18.132813 C 30.4375 18 30.726563 18 30.726563 18 L 34.636719 18.019531 C 34.636719 18.019531 35.8125 17.871094 36 18.410156 C 36.199219 18.972656 35.519531 19.957031 33.925781 22.109375 C 31.300781 25.644531 31.007813 25.316406 33.183594 27.359375 Z "/>' +
        '                            </g>' +
        '                        </svg>' +
        '                    </a>' +
        '                    <a href="https://twitter.com/intent/tweet?text='+ownerLogin+'%20-%20DeepThought&url='+window.location.href+'&via=TWITTER-HANDLE">' +
        '                        <?xml version="1.0" encoding="UTF-8"?>' +
        '                        <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 48 48" version="1.1" width="48px" height="48px">' +
        '                            <g id="surface1">' +
        '                                <path style=" fill:#03A9F4;" d="M 42 12.429688 C 40.675781 13.015625 39.253906 13.40625 37.753906 13.589844 C 39.277344 12.683594 40.453125 11.238281 41.003906 9.53125 C 39.574219 10.371094 37.992188 10.984375 36.3125 11.308594 C 34.96875 9.882813 33.050781 9 30.925781 9 C 26.847656 9 23.539063 12.277344 23.539063 16.320313 C 23.539063 16.890625 23.605469 17.449219 23.730469 17.988281 C 17.59375 17.683594 12.148438 14.765625 8.507813 10.335938 C 7.867188 11.417969 7.507813 12.683594 7.507813 14.023438 C 7.507813 16.5625 8.808594 18.800781 10.792969 20.117188 C 9.582031 20.082031 8.441406 19.742188 7.445313 19.203125 C 7.445313 19.226563 7.445313 19.257813 7.445313 19.289063 C 7.445313 22.839844 9.992188 25.796875 13.367188 26.472656 C 12.75 26.640625 12.097656 26.734375 11.425781 26.734375 C 10.949219 26.734375 10.484375 26.679688 10.035156 26.597656 C 10.972656 29.5 13.699219 31.621094 16.933594 31.683594 C 14.402344 33.644531 11.21875 34.820313 7.757813 34.820313 C 7.160156 34.820313 6.574219 34.785156 5.996094 34.714844 C 9.269531 36.785156 13.152344 38 17.320313 38 C 30.90625 38 38.339844 26.84375 38.339844 17.164063 C 38.339844 16.847656 38.328125 16.53125 38.3125 16.222656 C 39.761719 15.195313 41.011719 13.90625 42 12.429688 "/>' +
        '                            </g>' +
        '                        </svg>' +
        '                    </a>' +
        '                    <a href="https://www.facebook.com/sharer/sharer.php?u='+window.location.href+'">' +
        '                        <?xml version="1.0" encoding="UTF-8"?>' +
        '                        <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 48 48" version="1.1" width="48px" height="48px">' +
        '                            <g id="surface1">' +
        '                                <path style=" fill:#3F51B5;" d="M 42 37 C 42 39.761719 39.761719 42 37 42 L 11 42 C 8.238281 42 6 39.761719 6 37 L 6 11 C 6 8.238281 8.238281 6 11 6 L 37 6 C 39.761719 6 42 8.238281 42 11 Z "/>' +
        '                                <path style=" fill:#FFFFFF;" d="M 34.367188 25 L 31 25 L 31 38 L 26 38 L 26 25 L 23 25 L 23 21 L 26 21 L 26 18.589844 C 26.003906 15.082031 27.460938 13 31.59375 13 L 35 13 L 35 17 L 32.714844 17 C 31.105469 17 31 17.601563 31 18.722656 L 31 21 L 35 21 Z "/>' +
        '                            </g>' +
        '                        </svg>' +
        '                    </a>' +
        '                    <a href="https://plus.google.com/share?url='+window.location.href+'">' +
        '                        <?xml version="1.0" encoding="iso-8859-1"?>' +
        '                        <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" id="Layer_1" x="0px" y="0px" viewBox="0 0 48 48" style="enable-background:new 0 0 48 48;" xml:space="preserve" width="48px" height="48px">' +
        '                            <circle style="fill:#1E88E5;" cx="24" cy="24" r="20"/>' +
        '                            <path style="fill:#1565C0;" d="M27.723,23.039c0-1.563,3.319-2.012,3.319-5.684c0-2.203-0.196-3.531-1.871-4.313  c0-0.546,2.957-0.187,2.957-1.209c-0.513,0-6.476,0-6.476,0S19.095,12,19.095,17.748c0,5.752,5.732,5.088,5.732,5.088  s0,0.865,0,1.453c0,0.594,0.77,0.391,0.864,1.583c-0.388,0-7.964-0.208-7.964,4.998s6.679,4.959,6.679,4.959  s7.722,0.365,7.722-6.104C32.128,25.854,27.723,24.604,27.723,23.039z M22.127,18.086c-0.604-2.312,0.195-4.543,1.786-4.992  c1.593-0.453,3.374,1.059,3.981,3.367c0.605,2.309-0.192,4.543-1.785,4.992C24.517,21.904,22.734,20.391,22.127,18.086z   M25.444,34.167c-2.671,0.188-4.946-1.295-5.077-3.316c-0.133-2.016,1.927-3.805,4.6-3.996c2.674-0.188,4.947,1.297,5.08,3.314  C30.178,32.193,28.118,33.98,25.444,34.167z"/>' +
        '                            <path style="fill:#E8EAF6;" d="M25.995,23.039c0-1.563,3.318-2.012,3.318-5.684c0-2.203-0.195-3.531-1.87-4.313  c0-0.546,2.956-0.187,2.956-1.209c-0.512,0-6.476,0-6.476,0S17.367,12,17.367,17.748c0,5.752,5.731,5.088,5.731,5.088  s0,0.865,0,1.453c0,0.594,0.771,0.391,0.865,1.583c-0.388,0-7.964-0.208-7.964,4.998s6.679,4.959,6.679,4.959  s7.721,0.365,7.721-6.104C30.399,25.854,25.995,24.604,25.995,23.039z M20.399,18.086c-0.604-2.312,0.194-4.543,1.785-4.992  c1.593-0.453,3.374,1.059,3.982,3.367c0.604,2.309-0.193,4.543-1.786,4.992C22.789,21.904,21.007,20.391,20.399,18.086z   M23.717,34.167c-2.671,0.188-4.946-1.295-5.077-3.316c-0.133-2.016,1.927-3.805,4.599-3.996c2.675-0.188,4.948,1.297,5.08,3.314  C28.45,32.193,26.391,33.98,23.717,34.167z"/>' +
        '                        </svg>' +
        '                    </a>' +
        '                </div>' +
        '            </div>';
    const share = document.querySelector(".share"),
        shareImgPath = share.querySelector('path');
    share.addEventListener('click', function (e) {
       modal.info(genericComponents.societyShare);
       modal.head.innerText = "Социальные сети";
    });
    share.addEventListener("mouseenter", function (evt) {
        shareImgPath.style.fill = '#ffffff';
    });
    share.addEventListener("mouseleave", function (evt) {
        shareImgPath.style.fill = '#16576d';
    });
}

const components = {
    currentContainer: undefined,
    lastTab: undefined,
    "article-panel":{
        path: "/services/articles/user/" + userId,
        container: document.getElementById("article-panel"),
        rendered: false,
        render: function () {
            if(this.content === undefined){
                return;
            }
            const container = this.container,
                articles = this.content.list;
            container.innerHTML = "";
            for(let i = 0; i < articles.length; i++){
                let article = articles[i];
                container.innerHTML += "<a href='/articles/"+article.id+"' class='article card'>" +
                    "                        <div class='card-image'><img src='/"+article.articleImage+"'/></div>" +
                    "                        <div class='card-content'>"+article.title+"</div>" +
                    "                    </a>";
            }
            if(this.content.pages.total > 1){
                container.appendChild(genericComponents.moreBtn('/articles/user/' + userId));
            }
        }
    },
    "question-panel":{
        path: "/services/questions/user/" + userId,
        container: document.getElementById("question-panel"),
        rendered: false,
        render: function () {
            if(this.content === undefined){
                return;
            }
            const container = this.container,
                questions = this.content.list;

            container.innerHTML = "";
            for(let i = 0; i < questions.length; i++){
                let question = questions[i],
                    tags = question.tags, tagsHtml = "";
                for(let j = 0; j < tags.length; j++){
                    let tag = tags[j];
                    tagsHtml += "<a href='/tags/"+tag.id+"' class='chip' style='background: "+tag.color+"'>"+tag.name+"</a>";
                }
                let right_img = "";
                if(question.right !== 0){
                    right_img = "<svg style='height: 15px; width: 15px' role='img' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 512 512'><path fill='#4CAF50' d='M256 8C119.033 8 8 119.033 8 256s111.033 248 248 248 248-111.033 248-248S392.967 8 256 8zm0 48c110.532 0 200 89.451 200 200 0 110.532-89.451 200-200 200-110.532 0-200-89.451-200-200 0-110.532 89.451-200 200-200m140.204 130.267l-22.536-22.718c-4.667-4.705-12.265-4.736-16.97-.068L215.346 303.697l-59.792-60.277c-4.667-4.705-12.265-4.736-16.97-.069l-22.719 22.536c-4.705 4.667-4.736 12.265-.068 16.971l90.781 91.516c4.667 4.705 12.265 4.736 16.97.068l172.589-171.204c4.704-4.668 4.734-12.266.067-16.971z'></path></svg>";
                }
                container.innerHTML += "<div class='question '>" +
                    "                        <div class='left-side'>" +
                    "                            <div class='text'>" +
                    "                                <div class='title'>" +
                    "                                    <a href='/questions/"+question.id+"'>"+question.title+"</a>" +
                    "                                </div>" +
                    "                                <div class='info'>" +
                    "                                    Задан <a class='blue-text text-darken-1'>"+question.user.login+"</a>, <span>"+question.createDate+"</span> " +right_img +
                    "                                </div>" +
                    "                            </div>" +
                    "                            <div class='action'>" +
                    "                                <div class='tags'>" +
                    tagsHtml +
                    "                                </div>" +
                    "                            </div>" +
                    "                        </div>" +
                    "                        <div class='right-side'>" +
                    "                            <div class='stat-elem'>" +
                    "                                <span class='name'>рейтинг</span>" +
                    "                                <span class='num'>"+question.rating+"</span>" +
                    "                            </div>" +
                    "                            <div class='stat-elem'>" +
                    "                                <span class='name'>просмотры</span>" +
                    "                                <span class='num'>"+question.views+"</span>" +
                    "                            </div>" +
                    "                        </div>" +
                    "                    </div>";
            }
            if(this.content.pages.total > 1){
                container.appendChild(genericComponents.moreBtn('/questions/user/' + userId));
            }
        }
    },
    "answer-panel":{
        path: "/services/questions/user/answers/" + userId,
        container: document.getElementById("answer-panel"),
        rendered: false,
        render: function () {
            if(this.content === undefined){
                return;
            }
            const container = this.container,
                answers = this.content.list;

            container.innerHTML = "";
            for(let i = 0; i < answers.length; i++){
                let answer = answers[i],
                    isRightImg = "";
                if(answer.right === true){
                    isRightImg = "<svg   class='is-right-img' role='img' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 512 512'><path fill='#4CAF50' d='M256 8C119.033 8 8 119.033 8 256s111.033 248 248 248 248-111.033 248-248S392.967 8 256 8zm0 48c110.532 0 200 89.451 200 200 0 110.532-89.451 200-200 200-110.532 0-200-89.451-200-200 0-110.532 89.451-200 200-200m140.204 130.267l-22.536-22.718c-4.667-4.705-12.265-4.736-16.97-.068L215.346 303.697l-59.792-60.277c-4.667-4.705-12.265-4.736-16.97-.069l-22.719 22.536c-4.705 4.667-4.736 12.265-.068 16.971l90.781 91.516c4.667 4.705 12.265 4.736 16.97.068l172.589-171.204c4.704-4.668 4.734-12.266.067-16.971z'></path></svg>";
                }
                container.innerHTML += "<div class='answer'>" +
                    "                        <div class='left'>" +
                    "                            <div class='pre-title'>" +
                    "                                В ответ на вопрос" +
                    "                            </div>" +
                    "                            <a href='/questions/"+answer.question_id+"' class='title'>" +
                    answer.question.title +
                    isRightImg +
                    "                            </a>" +
                    "                        </div>" +
                    "                        <div class='right'>" +
                    "                            <div class='create-date'>" +
                    "                                <svg aria-hidden='true' data-prefix='far' data-icon='calendar-alt' class='svg-inline--fa fa-calendar-alt fa-w-14' role='img' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 448 512'><path fill='#adb5bd' d='M148 288h-40c-6.6 0-12-5.4-12-12v-40c0-6.6 5.4-12 12-12h40c6.6 0 12 5.4 12 12v40c0 6.6-5.4 12-12 12zm108-12v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm96 0v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm-96 96v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm-96 0v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm192 0v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm96-260v352c0 26.5-21.5 48-48 48H48c-26.5 0-48-21.5-48-48V112c0-26.5 21.5-48 48-48h48V12c0-6.6 5.4-12 12-12h40c6.6 0 12 5.4 12 12v52h128V12c0-6.6 5.4-12 12-12h40c6.6 0 12 5.4 12 12v52h48c26.5 0 48 21.5 48 48zm-48 346V160H48v298c0 3.3 2.7 6 6 6h340c3.3 0 6-2.7 6-6z'></path></svg>" +
                    "                                <span>"+answer.createDate+"</span>" +
                    "                            </div>" +
                    "                            <div class='rating blue'>" +
                    answer.rating+
                    "                            </div>" +
                    "                        </div>" +
                    "                    </div>";
            }
        }
    },
    "problems-solved":{
        path: "/services/problems/user/solved/" + userId,
        container: document.getElementById("problems-solved"),
        rendered: false,
        render: function () {
            if(this.content === undefined){
                return;
            }
            const container = this.container,
                problems = this.content.list;

            container.innerHTML = "";
            for(let i = 0; i < problems.length; i++){
                let problem = problems[i],
                    tags = problem.tags, tagsHtml = "";
                for(let j = 0; j < tags.length; j++){
                    let tag = tags[j];
                    tagsHtml += "<a href='/tags/"+tag.id+"' class='chip' style='background: "+tag.color+"'>"+tag.name+"</a>";
                }
                container.innerHTML += "<div class='problem'>" +
                    "                        <div class='left-side'>" +
                    "                            <a href='/problems/"+problem.id+"' class='title'>"+problem.title+"</a>" +
                    "                            <div class='tags'>" +
                    tagsHtml +
                    "                            </div>" +
                    "                        </div>" +
                    "                        <div class='right-side'>" +
                    "                            <div class='info'>" +
                    "                                <div class='info-element'><span class='header'>Сложность</span><span class='difficult "+problem.difficult+"'>"+problem.difficult+"</span></div>" +
                    "                                <div class='info-element'> <span class='header'>Решили</span> <span class='num'>"+problem.decided+"</span></div>" +
                    "                                <div class='info-element'> <span class='header'>Рейтинг</span> <span class='num'>"+problem.rating+"</span></div>" +
                    "                            </div>" +
                    "                        </div>" +
                    "                    </div>";
            }
            if(this.content.pages.total > 1){
                container.appendChild(genericComponents.moreBtn('/problems/user/solved/' + userId));
            }
        }
    },
    "problems-created":{
        path: "/services/problems/user/created/" + userId,
        container: document.getElementById("problems-created"),
        rendered: false,
        render: function () {
            if(this.content === undefined){
                return;
            }
            const container = this.container,
                problems = this.content.list;

            container.innerHTML = "";
            for(let i = 0; i < problems.length; i++){
                let problem = problems[i],
                    tags = problem.tags, tagsHtml = '';
                for(let j = 0; j < tags.length; j++){
                    let tag = tags[j];
                    tagsHtml += "<a href='/tags/"+tag.id+"' class='chip' style='background: "+tag.color+"'>"+tag.name+"</a>";
                };
                container.innerHTML += "<div class='problem'>" +
                    "                        <div class='left-side'>" +
                    "                            <a href='/problems/"+problem.id+"' class='title'>"+problem.title+"</a>" +
                    "                            <div class='tags'>" +
                    tagsHtml +
                    "                            </div>" +
                    "                        </div>" +
                    "                        <div class='right-side'>" +
                    "                            <div class='info'>" +
                    "                                <div class='info-element'><span class='header'>Сложность</span><span class='difficult green'>"+problem.difficult+"</span></div>" +
                    "                                <div class='info-element'> <span class='header'>Решили</span> <span class='num'>"+problem.decided+"</span></div>" +
                    "                                <div class='info-element'> <span class='header'>Рейтинг</span> <span class='num'>"+problem.rating+"</span></div>" +
                    "                            </div>" +
                    "                        </div>" +
                    "                    </div>";
            }
            if(this.content.pages.total > 1){
                container.appendChild(genericComponents.moreBtn('/problems/user/created/' + userId));
            }
        }
    },
    "tag-panel":{
        path: "/services/tags/user/" + userId,
        container: document.getElementById("tag-panel"),
        rendered: false,
        render: function () {
            if(this.content === undefined){
                return;
            }
            const container = this.container,
                tags = this.content.list;

            container.innerHTML = "";
            for(let i = 0; i < tags.length; i++){
                let tag = tags[i];
                container.innerHTML += "<a href='/tags/"+tag.id+"' class='card tag-card hoverable'>" +
                    "                        <div class='card-title' style='background: "+tag.color+";'>" +
                    tag.name +
                    "                        </div>" +
                    "                        <div class='card-content'>" +
                    "                            <p>"+tag.description+"</p>" +
                    "                        </div>" +
                    "                        <div class='card-action'>" +
                    "                            <div class='tag-num'>" +
                    "                                <svg aria-hidden='true' data-prefix='fas' data-icon='pen-alt' class='svg-inline--fa fa-pen-alt fa-w-16' role='img' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 512 512'><path fill='#7E7E7E' d='M497.94 74.17l-60.11-60.11c-18.75-18.75-49.16-18.75-67.91 0l-56.55 56.55 128.02 128.02 56.55-56.55c18.75-18.75 18.75-49.15 0-67.91zm-246.8-20.53c-15.62-15.62-40.94-15.62-56.56 0L75.8 172.43c-6.25 6.25-6.25 16.38 0 22.62l22.63 22.63c6.25 6.25 16.38 6.25 22.63 0l101.82-101.82 22.63 22.62L93.95 290.03A327.038 327.038 0 0 0 .17 485.11l-.03.23c-1.7 15.28 11.21 28.2 26.49 26.51a327.02 327.02 0 0 0 195.34-93.8l196.79-196.79-82.77-82.77-84.85-84.85z'></path></svg>" +
                    "                                <span>"+tag.articles+"</span>" +
                    "                            </div><div class='tag-num'>" +
                    "                            <svg aria-hidden='true' data-prefix='fas' data-icon='question' class='svg-inline--fa fa-question fa-w-12' role='img' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 384 512'><path fill='#7E7E7E' d='M202.021 0C122.202 0 70.503 32.703 29.914 91.026c-7.363 10.58-5.093 25.086 5.178 32.874l43.138 32.709c10.373 7.865 25.132 6.026 33.253-4.148 25.049-31.381 43.63-49.449 82.757-49.449 30.764 0 68.816 19.799 68.816 49.631 0 22.552-18.617 34.134-48.993 51.164-35.423 19.86-82.299 44.576-82.299 106.405V320c0 13.255 10.745 24 24 24h72.471c13.255 0 24-10.745 24-24v-5.773c0-42.86 125.268-44.645 125.268-160.627C377.504 66.256 286.902 0 202.021 0zM192 373.459c-38.196 0-69.271 31.075-69.271 69.271 0 38.195 31.075 69.27 69.271 69.27s69.271-31.075 69.271-69.271-31.075-69.27-69.271-69.27z'></path></svg>" +
                    "                            <span>"+tag.questions+"</span>" +
                    "                        </div><div class='tag-num'>" +
                    "                            <svg aria-hidden='true' data-prefix='fas' data-icon='puzzle-piece' class='svg-inline--fa fa-puzzle-piece fa-w-18' role='img' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 576 512'><path fill='#7E7E7E' d='M519.442 288.651c-41.519 0-59.5 31.593-82.058 31.593C377.409 320.244 432 144 432 144s-196.288 80-196.288-3.297c0-35.827 36.288-46.25 36.288-85.985C272 19.216 243.885 0 210.539 0c-34.654 0-66.366 18.891-66.366 56.346 0 41.364 31.711 59.277 31.711 81.75C175.885 207.719 0 166.758 0 166.758v333.237s178.635 41.047 178.635-28.662c0-22.473-40-40.107-40-81.471 0-37.456 29.25-56.346 63.577-56.346 33.673 0 61.788 19.216 61.788 54.717 0 39.735-36.288 50.158-36.288 85.985 0 60.803 129.675 25.73 181.23 25.73 0 0-34.725-120.101 25.827-120.101 35.962 0 46.423 36.152 86.308 36.152C556.712 416 576 387.99 576 354.443c0-34.199-18.962-65.792-56.558-65.792z'></path></svg>" +
                    "                            <span>"+tag.problems+"</span>" +
                    "                        </div>" +
                    "                        </div>" +
                    "                    </a>";
            }
            if(this.content.pages.total > 1){
                container.appendChild(genericComponents.moreBtn('/tags/user/' + userId));
            }
        }
    },
    "fact-panel":{
        path: "/services/facts/user/" + userId,
        container: document.getElementById("fact-panel"),
        rendered: false,
        render: function () {
            if(this.content === undefined){
                return;
            }
            const container = this.container,
                facts = this.content.list;

            container.innerHTML = "";
            for(let i = 0; i < facts.length; i++){
                let fact = facts[i],
                    tags = fact.tags, tagsHtml = "";
                for(let j = 0; i < tags.length; j++) {
                    let tag = tags[j];
                    tagsHtml += "<a href='/tags/"+tag.id+"' class='chip' style='background: "+tag.color+"'>"+tag.name+"</a>";
                };
                container.innerHTML += "<div class='fact z-depth-1-half hoverable'>" +
                    "                        <div class='content'>" +
                    fact.text +
                    "                        </div>" +
                    "                        <div class='tags'>" +
                    tagsHtml +
                    "                        </div>" +
                    "                    </div>"
            }
            if(this.content.pages.total > 1){
                container.appendChild(genericComponents.moreBtn('/facts/user/' + userId));
            }
        }
        }
    };
function initTabs() {

    const panels = document.querySelectorAll(".stat"),
        firstTab = panels[0],
        tabId = firstTab.getAttribute('content'),
        tabContent = components[tabId].container;

    components.lastTab = firstTab;
    components.lastTab.classList.add('active');
    components.currentTab = tabContent;
    components.currentTab.style.display = 'flex';
    loadPanel(tabId);

    for(let i = 0; i < panels.length; i++){
        panels[i].addEventListener("click", clickPanelListener);
    }

    function clickPanelListener(e){
        const target = e.currentTarget,
            panelId = target.getAttribute('content'),
            panel = components[panelId];

        components.lastTab.classList.remove('active');
        components.lastTab = target;
        components.lastTab.classList.add('active');

        components.currentTab.style.display = "none";
        components.currentTab = panel.container;
        components.currentTab.style.display = "flex";

        if(panel.rendered === false){
            loadPanel(panelId);
        }
    }
    function loadPanel(panelId)
    {
        const panel = components[panelId];

        if(panel.content === undefined){
            xhr.request({
                path: panel.path,
                method: 'GET',
                headers: {
                    'Content-Type':'application/json'
                }
            }, function (response, error) {
                if(response){
                    panel.content = JSON.parse(response);
                    panel.rendered = true;
                    if(panel.content !== undefined && panel.content !== '' && panel.content.list.length > 0){
                        panel.render();
                    }else{
                        panel.container.innerHTML = genericComponents.noContent;
                    }
                }
                else if(error){
                    panel.container.innerHTML = genericComponents.error;
                    panel.rendered = true;
                    panel.content = null;
                }
            });
        }else{
            panel.render();
        }
    }
}

shareBtn();
initTabs();

