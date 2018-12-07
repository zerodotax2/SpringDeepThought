const render = {
    articlesContainer: document.getElementById('articles_container'),
    path: '/services/articles',
    isRendering: false,
    articlesOnPage: 10,
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

        if(document.documentElement.clientHeight + document.documentElement.scrollTop >= (render.height - 200)
            && !render.isRendering){
            render.isRendering = true;

            params["start"] = render.articlesOnPage;
            params["size"] = 10;

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
                        Array.forEach(array, function (article) {
                            let div = document.createElement("div"),
                                tags = article.tags,
                                tagsHtml = '';
                            Array.forEach(tags, function (tag) {
                               tagsHtml += '<a href="/tags/'+tag.id+'" class="chip" style="background: '+tag.color+'">'+tag.name+'</a>';
                            });
                            div.classList.add("card" ,"hoverable" ,"col" ,"z-depth-2", "s12");
                            div.innerHTML = '<div class="card-image row" >' +
                                '                                <a href="/articles/'+article.id+'"><img class="articleImage" src="/'+article.articleImage+'"/></a>' +
                                '                            </div>' +
                                '                            <div class="card-content">' +
                                '                                <h5 class="card-title grey-text text-darken-2">'+article.title+'</h5>' +
                                '                                <div class="tags">' +
                                    tagsHtml +
                                '                                </div>' +
                                '                            </div>' +
                                '                            <div class="card-action">' +
                                '                                <div class="views action-stat">' +
                                '                                    <h6 class="grey-text">'+article.views+'</h6>' +
                                '                                    <svg aria-hidden="true" data-prefix="far" data-icon="eye" class="svg-inline--fa fa-eye fa-w-18" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path fill="#adb5bd" d="M569.354 231.631C512.97 135.949 407.81 72 288 72 168.14 72 63.004 135.994 6.646 231.631a47.999 47.999 0 0 0 0 48.739C63.031 376.051 168.19 440 288 440c119.86 0 224.996-63.994 281.354-159.631a47.997 47.997 0 0 0 0-48.738zM288 392c-102.556 0-192.091-54.701-240-136 44.157-74.933 123.677-127.27 216.162-135.007C273.958 131.078 280 144.83 280 160c0 30.928-25.072 56-56 56s-56-25.072-56-56l.001-.042C157.794 179.043 152 200.844 152 224c0 75.111 60.889 136 136 136s136-60.889 136-136c0-31.031-10.4-59.629-27.895-82.515C451.704 164.638 498.009 205.106 528 256c-47.908 81.299-137.444 136-240 136z"></path></svg>' +
                                '                                </div>' +
                                // '                                <div class="comments action-stat">' +
                                // '                                    <h6 class="grey-text">'+article.comments+'</h6>' +
                                // '                                    <svg aria-hidden="true" data-prefix="fas" data-icon="comment" class="svg-inline--fa fa-comment fa-w-16" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="#adb5bd" d="M256 32C114.6 32 0 125.1 0 240c0 49.6 21.4 95 57 130.7C44.5 421.1 2.7 466 2.2 466.5c-2.2 2.3-2.8 5.7-1.5 8.7S4.8 480 8 480c66.3 0 116-31.8 140.6-51.4 32.7 12.3 69 19.4 107.4 19.4 141.4 0 256-93.1 256-208S397.4 32 256 32z"></path></svg>' +
                                // '                                </div>' +
                                '                                <a href="/articles/'+article.id+'" class="btn col s5 l3 red waves-effect waves-light">Читать</a>' +
                                '                            </div>';

                            render.articlesContainer.appendChild(div);
                        });
                    }
                        render.articlesOnPage += 10;
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