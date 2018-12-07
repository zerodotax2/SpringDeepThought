'use strict';
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


initCategories("sort-content", "type-content");
initSpecialSort();