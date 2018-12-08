'use strict';
const tag_id = document.getElementById("tag_id").getAttribute("content");
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
        "                    </div>"
};
const components = {
    currentPanel: undefined,
    currentTab: undefined,
    'articles-panel':{
        rendered: false,
        path: '/services/articles/tag/' + tag_id,
        container: document.getElementById('articles-panel'),
        content: undefined,
        render: function () {
            if(this.content === undefined){
                return;
            }
            const container = this.container,
                articles = this.content.list;

            container.innerHTML = '';
            for(let i = 0; i < articles.length; i++){
                let article = articles[i];
                container.innerHTML += "<a href='/articles/"+article.id+"' class='article card'>" +
                    "                        <div class='card-image'><img src='/"+article.articleImage+"'/></div>" +
                    "                        <div class='card-content'>"+article.title+"</div>" +
                    "                    </a>";
            }
            if(1 < this.content.pages.total){
                container.appendChild(genericComponents.moreBtn('/articles/tag/'+tag_id));
            }
        }
    },
    'questions-panel':{
        rendered: false,
        path: '/services/questions/tag/' + tag_id,
        container: document.getElementById('questions-panel'),
        content: undefined,
        render: function () {
            if(this.content === undefined){
                return;
            }
            const container = this.container,
                questions = this.content.list;
            container.innerHTML = '';
            for(let i = 0; i < questions.length; i++){
                let question = questions[i],
                    tags = question.tags,
                    tagsHtml = "", rightImg = "";
                for(let j = 0; j < tags.length; j++){
                    let tag = tags[i];
                    tagsHtml += "<a href='/tags/"+tag.id+"' style='background: "+tag.color+"' class='chip'>"+tag.name+"</a>";
                }
                if(question.right !== 0){
                    rightImg = "<svg role='img' style='width: 15px;height: 15px;' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 512 512'><path fill='#4CAF50' d='M256 8C119.033 8 8 119.033 8 256s111.033 248 248 248 248-111.033 248-248S392.967 8 256 8zm0 48c110.532 0 200 89.451 200 200 0 110.532-89.451 200-200 200-110.532 0-200-89.451-200-200 0-110.532 89.451-200 200-200m140.204 130.267l-22.536-22.718c-4.667-4.705-12.265-4.736-16.97-.068L215.346 303.697l-59.792-60.277c-4.667-4.705-12.265-4.736-16.97-.069l-22.719 22.536c-4.705 4.667-4.736 12.265-.068 16.971l90.781 91.516c4.667 4.705 12.265 4.736 16.97.068l172.589-171.204c4.704-4.668 4.734-12.266.067-16.971z'></path></svg>";
                }
                container.innerHTML += "<div class='question '>" +
                    "                        <div class='left-side'>" +
                    "                            <div class='text'>" +
                    "                                <div class='title'>" +
                    "                                    <a href='/questions/"+question.id+"'>"+question.title+"</a>" +
                    "                                </div>" +
                    "                                <div class='info'>" +
                    "                                    Задан <a class='blue-text text-darken-1'>"+question.user.login+"</a>, <span>"+question.createDate+"</span>"+rightImg+
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
                container.appendChild(genericComponents.moreBtn('/questions/tag/'+tag_id));
            }
        }
    },
    'problems-panel':{
        rendered: false,
        path: '/services/problems/tag/' + tag_id,
        container: document.getElementById('problems-panel'),
        content: undefined,
        render: function () {
            if(this.content === undefined){
                return;
            }
            const container = this.container,
                problems = this.content.list;
            container.innerHTML = '';
            for(let i = 0; i < problems.length; i++){
                let problem = problems[i],
                    tags = problem.tags, tagsHtml = "";
                for(let j = 0; j < tags.length; j++){
                    let tag = tags[j];
                    tagsHtml += "<a href='/tags/"+tag.id+"' style='background: "+tag.color+"' class='chip'>"+tag.name+"</a>";
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
            };
            if(this.content.pages.total > 1){
                container.appendChild(genericComponents.moreBtn('/problems/tag/'+tag_id));
            }
        }
    },
    'users-panel':{
        rendered: false,
        path: '/services/users/tag/' + tag_id,
        container: document.getElementById('users-panel'),
        content: undefined,
        render: function () {
            if(this.content === undefined){
                return;
            }
            const container = this.container,
                users = this.content.list;

            container.innerHTML = '';
            for(let i = 0; i < users.length; i++) {
                let user = users[i];
                container.innerHTML += "<a href='/users/'"+user.id+" class='user'>" +
                    "                        <div class='ava'><img src='/"+user.userImage +"' /></div>" +
                    "                        <div class='info'>" +
                    "                            <div class='login'>"+user.login+"</div>" +
                    "                            <div class='rating'>" +
                    "                                <svg aria-hidden='true' data-prefix='fas' data-icon='star' class='svg-inline--fa fa-star fa-w-18' role='img' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 576 512'><path fill='#ffcb05' d='M259.3 17.8L194 150.2 47.9 171.5c-26.2 3.8-36.7 36.1-17.7 54.6l105.7 103-25 145.5c-4.5 26.3 23.2 46 46.4 33.7L288 439.6l130.7 68.7c23.2 12.2 50.9-7.4 46.4-33.7l-25-145.5 105.7-103c19-18.5 8.5-50.8-17.7-54.6L382 150.2 316.7 17.8c-11.7-23.6-45.6-23.9-57.4 0z'></path></svg>" +
                    "                                <span>"+user.rating+"</span>" +
                    "                            </div>" +
                    "                        </div>" +
                    "                    </a>";
            };
            if(this.content.pages.total > 1){
                container.appendChild(genericComponents.moreBtn('/users/tag/'+tag_id));
            }
        }
    },
    'facts-panel':{
        rendered: false,
        path: '/services/facts/tag/' + tag_id,
        container: document.getElementById('facts-panel'),
        content: undefined,
        render: function () {
            if(this.content === undefined){
                return;
            }
            const container = this.container,
                facts = this.content.list;
            container.innerHTML = '';
            for(let i = 0; i < facts.length; i++) {
                let fact = facts[i],
                    tags = fact.tags, tagsHtml = "";
                for(let t = 0; t < tags.length; t++){
                    let tag = tags[t];
                    tagsHtml += "<a href='/tags/"+tag.id+"' style='background: "+tag.color+"' class='chip'>"+tag.name+"</a>";
                }
                container.innerHTML += "<div class='fact z-depth-1-half hoverable'>" +
                    "                        <div class='content'>" +
                    "                            "+fact.text+"" +
                    "                        </div>" +
                    "                        <div class='tags'>" +
                    tagsHtml +
                    "                        </div>" +
                    "                    </div>" ;
            };
            if(this.content.pages.total > 1){
                container.appendChild(genericComponents.moreBtn('/facts/tag/'+tag_id));
            }
        }
    }
};
function initPanels() {

    const tabs = document.querySelectorAll('.stat'),
        firstTab = tabs[0],
        firstPanel = components[firstTab.getAttribute('content')];

    components.currentTab = firstTab;
    components.currentTab.classList.add('active');
    components.currentPanel = firstPanel;
    components.currentPanel.container.style.display = 'flex';
    loadPanel( firstPanel );

    for(let i = 0; i < tabs.length; i++){
        let tab = tabs[i];
        tab.addEventListener("click", function (e) {
            let target = e.currentTarget,
                content = target.getAttribute('content'),
                panel = components[content];

            components.currentTab.classList.remove('active');
            components.currentTab = target;
            components.currentTab.classList.add('active');

            components.currentPanel.container.style.display = 'none';
            components.currentPanel = panel;
            components.currentPanel.container.style.display = 'flex';

            if(!components.currentPanel.rendered){
                loadPanel(components.currentPanel);
            }
        });
    }

    function loadPanel(panel) {
        if(panel.content === undefined){
            xhr.request({
                method: 'GET',
                path: panel.path,
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
                }else if(error){
                    panel.rendered = true;
                    panel.content = null;
                    panel.content = genericComponents.error;
                }
            });
        }else{
            panel.render();
            panel.rendered = true;
        }
    }
}
initPanels();
