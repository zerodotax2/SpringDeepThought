
const render = {
    tagsContainer: document.querySelector('#tags-container'),
    tagsOnPage: 10,
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
                path: '/services/tags',
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
                        Array.forEach(array, function (tag) {
                            let div = document.createElement("a");
                            div.href = '/tags/'+tag.id;
                            div.classList.add('card', 'tag-card', 'hoverable', 'col', 's12','l4');
                            div.innerHTML = '<div class="card-title" style="background: '+tag.color+';">' +
                                                            tag.name +
                                '                            </div>' +
                                '                            <div class="card-content">' +
                                '                                <p>'+tag.description+'</p>' +
                                '                            </div>' +
                                '                            <div class="card-action">' +
                                '                                <div class="tag-num">' +
                                '                                    <svg aria-hidden="true" data-prefix="fas" data-icon="pen-alt" class="svg-inline--fa fa-pen-alt fa-w-16" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="#7E7E7E" d="M497.94 74.17l-60.11-60.11c-18.75-18.75-49.16-18.75-67.91 0l-56.55 56.55 128.02 128.02 56.55-56.55c18.75-18.75 18.75-49.15 0-67.91zm-246.8-20.53c-15.62-15.62-40.94-15.62-56.56 0L75.8 172.43c-6.25 6.25-6.25 16.38 0 22.62l22.63 22.63c6.25 6.25 16.38 6.25 22.63 0l101.82-101.82 22.63 22.62L93.95 290.03A327.038 327.038 0 0 0 .17 485.11l-.03.23c-1.7 15.28 11.21 28.2 26.49 26.51a327.02 327.02 0 0 0 195.34-93.8l196.79-196.79-82.77-82.77-84.85-84.85z"></path></svg>' +
                                '                                    <span>'+tag.articles+'</span>' +
                                '                                </div><div class="tag-num">' +
                                '                                <svg aria-hidden="true" data-prefix="fas" data-icon="question" class="svg-inline--fa fa-question fa-w-12" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512"><path fill="#7E7E7E" d="M202.021 0C122.202 0 70.503 32.703 29.914 91.026c-7.363 10.58-5.093 25.086 5.178 32.874l43.138 32.709c10.373 7.865 25.132 6.026 33.253-4.148 25.049-31.381 43.63-49.449 82.757-49.449 30.764 0 68.816 19.799 68.816 49.631 0 22.552-18.617 34.134-48.993 51.164-35.423 19.86-82.299 44.576-82.299 106.405V320c0 13.255 10.745 24 24 24h72.471c13.255 0 24-10.745 24-24v-5.773c0-42.86 125.268-44.645 125.268-160.627C377.504 66.256 286.902 0 202.021 0zM192 373.459c-38.196 0-69.271 31.075-69.271 69.271 0 38.195 31.075 69.27 69.271 69.27s69.271-31.075 69.271-69.271-31.075-69.27-69.271-69.27z"></path></svg>' +
                                '                                <span>'+tag.questions+'</span>' +
                                '                            </div><div class="tag-num">' +
                                '                                <svg aria-hidden="true" data-prefix="fas" data-icon="puzzle-piece" class="svg-inline--fa fa-puzzle-piece fa-w-18" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path fill="#7E7E7E" d="M519.442 288.651c-41.519 0-59.5 31.593-82.058 31.593C377.409 320.244 432 144 432 144s-196.288 80-196.288-3.297c0-35.827 36.288-46.25 36.288-85.985C272 19.216 243.885 0 210.539 0c-34.654 0-66.366 18.891-66.366 56.346 0 41.364 31.711 59.277 31.711 81.75C175.885 207.719 0 166.758 0 166.758v333.237s178.635 41.047 178.635-28.662c0-22.473-40-40.107-40-81.471 0-37.456 29.25-56.346 63.577-56.346 33.673 0 61.788 19.216 61.788 54.717 0 39.735-36.288 50.158-36.288 85.985 0 60.803 129.675 25.73 181.23 25.73 0 0-34.725-120.101 25.827-120.101 35.962 0 46.423 36.152 86.308 36.152C556.712 416 576 387.99 576 354.443c0-34.199-18.962-65.792-56.558-65.792z"></path></svg>' +
                                '                                <span>'+tag.problems+'</span>' +
                                '                            </div>' +
                                '                            </div>';
                            render.tagsContainer.appendChild(div);
                        });
                    }
                    render.tagsOnPage += 10;
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
