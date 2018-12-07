
const render = {
    userContainer: document.querySelector('.user-container'),
    usersOnPage: 10,
    path: '/services/users',
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

            params["start"] = render.usersOnPage;

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
                        Array.forEach(array, function (user) {
                            let div = document.createElement("a");
                            div.href = '/users/' + user.id;
                            div.classList.add('user');
                            div.innerHTML = '<div class="ava"><img src="/'+user.userImage+'" /></div>' +
                                '                            <div class="info">' +
                                '                                <div class="login">'+user.login+'</div>' +
                                '                                <div class="rating">' +
                                '                                    <svg aria-hidden="true" data-prefix="fas" data-icon="star" class="svg-inline--fa fa-star fa-w-18" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path fill="#ffcb05" d="M259.3 17.8L194 150.2 47.9 171.5c-26.2 3.8-36.7 36.1-17.7 54.6l105.7 103-25 145.5c-4.5 26.3 23.2 46 46.4 33.7L288 439.6l130.7 68.7c23.2 12.2 50.9-7.4 46.4-33.7l-25-145.5 105.7-103c19-18.5 8.5-50.8-17.7-54.6L382 150.2 316.7 17.8c-11.7-23.6-45.6-23.9-57.4 0z"></path></svg>' +
                                '                                    <span>'+user.rating+'</span>' +
                                '                                </div>' +
                                '                            </div>';

                            render.userContainer.appendChild(div);
                        });
                    }
                    render.usersOnPage += 10;
                    render.isRendering = false;
                } else if (error) {
                    render.isRendering = false;
                }
            });
        }
    });
}

initCategories('sort-content', 'type-content');
initSpecialSort();
initRender();