const render = {
    questionContainer: document.querySelector('#question_container'),
    questionsOnPage: 10,
    path: '/services/questions',
    rendering: false,
    height: Math.max(
        document.body.scrollHeight, document.body.offsetHeight,
        document.documentElement.clientHeight, document.documentElement.scrollHeight,
        document.documentElement.offsetHeight),
    params: get_parameters()
};
function initSpecialSort() {
    const specSort = document.getElementById('special_sort');
    if(specSort === undefined || specSort === null)
        return;
    render.path = specSort.getAttribute('path');
}
function initRender(){
    window.addEventListener("scroll", function (ev) {
        if(document.documentElement.clientHeight + document.documentElement.scrollTop >= (render.height - 400)
            && !render.isRendering){
            render.isRendering = true;

            params["start"] = render.questionsOnPage;

            xhr.request({
                path: render.path + construct_get(params),
                method: 'GET',
                headers: {
                    'Cache-Control': 'max-age=604800'
                }
            }, function(response, error) {
                if (response) {
                    const array = JSON.parse(response);

                    if (array !== undefined && array.length > 0) {
                        Array.forEach(array, function (question) {
                            let div = document.createElement("div"),
                                tags = question.tags, 
                                tagsHtml = '',
                                rightImg = '';
                            div.classList.add('question');
                            Array.forEach(tags, function (tag) {
                                tagsHtml += '<a href="/tags/'+tag.id+'" class="chip" style="background: '+tag.color+'">'+tag.name+'</a>';
                            });
                            if(question.right === true){
                                rightImg = '<svg  aria-hidden="true" data-prefix="far" data-icon="check-circle" class="svg-inline--fa fa-check-circle fa-w-16 is-right-img" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="#4CAF50" d="M256 8C119.033 8 8 119.033 8 256s111.033 248 248 248 248-111.033 248-248S392.967 8 256 8zm0 48c110.532 0 200 89.451 200 200 0 110.532-89.451 200-200 200-110.532 0-200-89.451-200-200 0-110.532 89.451-200 200-200m140.204 130.267l-22.536-22.718c-4.667-4.705-12.265-4.736-16.97-.068L215.346 303.697l-59.792-60.277c-4.667-4.705-12.265-4.736-16.97-.069l-22.719 22.536c-4.705 4.667-4.736 12.265-.068 16.971l90.781 91.516c4.667 4.705 12.265 4.736 16.97.068l172.589-171.204c4.704-4.668 4.734-12.266.067-16.971z"></path></svg>';
                            }
                            div.innerHTML = '<div class="left-side">' +
                                '                                <div class="text">' +
                                '                                    <div class="title">' +
                                '                                        <a href="/questions/'+question.id+'">'+question.title+'</a>' +
                                '                                    </div>' +
                                '                                    <div class="info">' +
                                '                                        Задан <a class="blue-text text-darken-1">'+question.user.login+'</a>, <span>'+question.createDate+'</span>' + rightImg +
                                '                                    </div>' +
                                '                                </div>' +
                                '                                <div class="action">' +
                                '                                    <div class="tags">' +
                                 tagsHtml +
                                '                                    </div>' +
                                '                                </div>' +
                                '                            </div>' +
                                '                            <div class="right-side">' +
                                '                                <div class="stat-elem">' +
                                '                                    <span class="name">рейтинг</span>' +
                                '                                    <span class="num">'+question.rating+'</span>' +
                                '                                </div>' +
                                '                                <div class="stat-elem">' +
                                '                                    <span class="name">просмотры</span>' +
                                '                                    <span class="num">'+question.views+'</span>' +
                                '                                </div>' +
                                '                            </div>';

                            render.questionContainer.appendChild(div);
                        });
                    }
                    render.questionsOnPage += 10;
                    render.isRendering = false;
                } else if (error) {
                    render.isRendering = false;
                }
            });
        }
    });
}

initCategories("sort-content", "type-content");
initSpecialSort();
initRender();