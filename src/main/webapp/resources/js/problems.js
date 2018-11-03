
const render = {
    problemContainer: document.querySelector('.problems-container'),
    problemsOnPage: 10,
    rendering: false,
    height: Math.max(
        document.body.scrollHeight, document.body.offsetHeight,
        document.documentElement.clientHeight, document.documentElement.scrollHeight,
        document.documentElement.offsetHeight),
    params: get_parameters()
};
function initRender(){
    window.addEventListener("scroll", function (ev) {
        if(document.documentElement.clientHeight + document.documentElement.scrollTop >= (render.height - 400)
            && !render.isRendering){
            render.isRendering = true;
            const content ={
                start: 10
            };
            Array.forEach(Object.keys(render.params), function (param) {
                content[param] = render.params[param];
            });
            xhr.request({
                path: '/services/problems',
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Cache-Control': 'max-age=604800'
                },
                content: JSON.stringify(content)
            }, function(response, error) {
                if (response) {
                    const array = JSON.parse(ajax.responseText);

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
initRender();