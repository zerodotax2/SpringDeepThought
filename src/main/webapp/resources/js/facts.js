
const render = {
    factsContainer: document.querySelector('#facts-container'),
    factsOnPage: 10,
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
                size: 10,
                factsOnPage: render.factsOnPage
            };
            Array.forEach(Object.keys(render.params), function (param) {
                content[param] = render.params[param];
            });
            xhr.request({
                path: '/services/facts',
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
initRender();
initCategories("sort-content", "type-content");