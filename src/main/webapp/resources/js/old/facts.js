
const render = {
    factsContainer: document.querySelector('#facts-container'),
    factsOnPage: 10,
    path: '/services/facts',
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

            params["start"] = render.factsOnPage;

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
                        Array.forEach(array, function (factElement) {
                            let div = document.createElement("a"),
                                factElementTags = factElement.tags,
                                tagsHTML = '';
                            Array.forEach(factElementTags, function (factElementTag) {
                                tagsHTML += '<a href="/tags/'+factElementTag.id+'" class="chip" style="background: '+factElementTag.color+'">'+factElementTag.name+'</a>';
                            });
                            div.classList.add('factElement', 'z-depth-1-half', 'hoverable');
                            div.innerHTML = '<div class="content">' +
                                        factElement.text+
                                '                            </div>' +
                                '                            <div class="tags">' +
                                                    tagsHTML +
                                '                            </div>';
                            render.factsContainer.appendChild(div);
                        });
                    }
                    render.factsOnPage += 10;
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