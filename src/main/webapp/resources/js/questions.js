'use strict';
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

initCategories("sort-content", "type-content");
initSpecialSort();