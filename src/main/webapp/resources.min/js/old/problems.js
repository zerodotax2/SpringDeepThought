
const render = {
    problemContainer: document.querySelector('.problems-container'),
    problemsOnPage: 10,
    path: '/services/problems',
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

            params["start"] = render.problemsOnPage;

            xhr.request({
                path: render.path + construct_get(params),
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Cache-Control': 'max-age=604800'
                }
            }, function(response, error) {
                if (response) {
                    const array = JSON.parse(response);

                    if (array !== undefined && array.length > 0) {
                        Array.forEach(array, function (problem) {
                            let div = document.createElement("div"),
                                tags = question.tags,
                                tagsHtml = '';
                            div.classList.add('problem');
                            Array.forEach(tags, function (tag) {
                                tagsHtml += '<a href="/tags/'+tag.id+'" class="chip" style="background: '+tag.color+'">'+tag.name+'</a>';
                            });
                            div.innerHTML = '<div class="left-side">' +
                                '                                <a href="/problems/'+problem.id+'" class="title">'+problem.title+'</a>' +
                                '                                <div class="tags">' +
                                    tagsHtml +
                                '                                </div>' +
                                '                            </div>' +
                                '                            <div class="right-side">' +
                                '                                <div class="info">' +
                                '                                    <div class="info-element"><span class="header">Сложность</span><span class="difficult '+problem.difficult+'">'+problem.difficult+'</span></div>' +
                                '                                    <div class="info-element"> <span class="header">Решили</span> <span class="num">'+problem.decided+'</span></div>' +
                                '                                    <div class="info-element"> <span class="header">Рейтинг</span> <span class="num">'+problem.rating+'</span></div>' +
                                '                                </div>' +
                                '                            </div>';

                            render.problemContainer.appendChild(div);
                        });
                    }
                    render.problemsOnPage += 10;
                    render.isRendering = false;
                } else if (error) {
                    render.isRendering = false;
                }
            });
        }
    });
}

initCategories("sort-content", "type-content", "difficult-content");
initSpecialSort();
initRender();