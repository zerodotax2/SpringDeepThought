'use strict';
const userId = document.getElementById('user_id').getAttribute('content'),
    ownerId = document.getElementById('owner_id').getAttribute('content'),
    href = location.href,
    articleId = href.substring(href.lastIndexOf('/')+1, href.length);

let voted = false;
function initRating() {
    const ratePanel = document.querySelector('.article-footer .article-rating'),
        up = ratePanel.firstElementChild,
        down = ratePanel.lastElementChild,
        num = ratePanel.querySelector('span');
    up.addEventListener('click', function () {
        changeRate(1);
    });
    down.addEventListener('click', function () {
      changeRate(-1);
    });
    function changeRate(rate) {
        if(voted){
            return;
        }else if(userId === '-1'){
	    modal.error('Вы не зарегистрированы, чтобы голосовать');
	    return;
	}else if(ownerId === userId){
            modal.error('Нельзя голосовать за свою статью');
            return;
        }
        xhr.request({
            path: '/services/articles/rating',
            method: 'POST',
            headers: {
                'Content-Type':'application/json'
            },
            content: JSON.stringify({
                id: articleId,
                rate: rate
            })
        }, function (response, error, status) {
            if(response){
                voted = true;
                if(status === 208) {
                    modal.info("Вы уже голосовали");
                    return;
                }
                num.innerText = Number(num.innerText) + Number(rate);
            }
        })
    }
}

const comment = {
    letters: 0
};
function initComments() {
    const addCommentPanel = document.querySelector('.add-comment'),
        writer = addCommentPanel.querySelector('.writer'),
        commentTextArea = writer.querySelector('textarea'),
        commentLetterCounter = writer.querySelector('.counter span'),
        postButton = writer.querySelector('.post-comment'),
        postError = addCommentPanel.querySelector('.add-comment-error'),
        comments = document.querySelectorAll('.comment'),
        rateButtons = document.querySelectorAll('.comment .rate-comment');
    comment.writer = writer;
    commentTextArea.addEventListener('input', function () {
        let value = commentTextArea.value;
        comment.letters = value.length;
        if(comment.letters > 1000){
            commentTextArea.value = value.substring(0, 1000);
            return;
        }
        commentLetterCounter.innerText = 1000 - comment.letters;
    });
    postButton.addEventListener('click', function (e) {
        const value = commentTextArea.value;
        if(value.length < 5 || value.length > 1000){
            return;
        }
        xhr.request({
            method: 'POST',
            path: '/services/articles/comments',
            headers: {
                'Content-Type':'application/json'
            },
            content: JSON.stringify({
                text: value.substring(0, 1000),
                id: articleId
            })
        }, function (response, error) {
            if(response){
                const newComment = JSON.parse(response),
                    div = document.createElement('div');
                postError.style.display = 'none';
                div.classList.add('comment');
                div.setAttribute('user', newComment.user.id);
                div.setAttribute('id', 'comment-'+newComment.id);
                div.innerHTML = '<div class="left-side">' +
                    '                            <img class="ava" src="'+'/'+newComment.user.userImage+'"/>' +
                    '                            <div class="rating">' +
                    '                                <svg  class="rate-comment" content="'+newComment.id+':1" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512"><path fill="#A9A9A9" d="M288.662 352H31.338c-17.818 0-26.741-21.543-14.142-34.142l128.662-128.662c7.81-7.81 20.474-7.81 28.284 0l128.662 128.662c12.6 12.599 3.676 34.142-14.142 34.142z"></path></svg>' +
                    '                                <span class="rate">'+newComment.rating+'</span>' +
                    '                                <svg  class="rate-comment" content="'+newComment.id+':-1" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512"><path fill="#A9A9A9" d="M31.3 192h257.3c17.8 0 26.7 21.5 14.1 34.1L174.1 354.8c-7.8 7.8-20.5 7.8-28.3 0L17.2 226.1C4.6 213.5 13.5 192 31.3 192z"></path></svg>' +
                    '                            </div>' +
                    '                        </div>' +
                    '                        <div class="right-side">' +
                    '                            <div class="top-info">' +
                    '                               <div class="left">' +
                    '                                <div class="user-info">' +
                    '                                    <div class="login">'+newComment.user.login+'</div>' +
                    '                                    <div class="user-rating">' +
                    '                                        <svg aria-hidden="true" data-prefix="fas" data-icon="star" class="svg-inline--fa fa-star fa-w-18" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path fill="#ffcb05" d="M259.3 17.8L194 150.2 47.9 171.5c-26.2 3.8-36.7 36.1-17.7 54.6l105.7 103-25 145.5c-4.5 26.3 23.2 46 46.4 33.7L288 439.6l130.7 68.7c23.2 12.2 50.9-7.4 46.4-33.7l-25-145.5 105.7-103c19-18.5 8.5-50.8-17.7-54.6L382 150.2 316.7 17.8c-11.7-23.6-45.6-23.9-57.4 0z"></path></svg>' +
                    '                                        <span>'+newComment.user.rating+'</span>' +
                    '                                    </div>' +
                    '                                </div>' +
                    '                                <div class="date">' +
                    '                                    <svg aria-hidden="true" data-prefix="far" data-icon="calendar-alt" class="svg-inline--fa fa-calendar-alt fa-w-14" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path fill="#adb5bd" d="M148 288h-40c-6.6 0-12-5.4-12-12v-40c0-6.6 5.4-12 12-12h40c6.6 0 12 5.4 12 12v40c0 6.6-5.4 12-12 12zm108-12v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm96 0v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm-96 96v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm-96 0v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm192 0v-40c0-6.6-5.4-12-12-12h-40c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12zm96-260v352c0 26.5-21.5 48-48 48H48c-26.5 0-48-21.5-48-48V112c0-26.5 21.5-48 48-48h48V12c0-6.6 5.4-12 12-12h40c6.6 0 12 5.4 12 12v52h128V12c0-6.6 5.4-12 12-12h40c6.6 0 12 5.4 12 12v52h48c26.5 0 48 21.5 48 48zm-48 346V160H48v298c0 3.3 2.7 6 6 6h340c3.3 0 6-2.7 6-6z"></path></svg>' +
                    '                                    <span>'+newComment.createDate+'</span></div>' +
                    '                                   </div>' +
                    '                              <div class="right"></div>' +
                    '                            </div>' +
                    '                            <div class="comment-text">'+newComment.text+'</div>' +
                    '                        </div>';
                addCommentPanel.parentNode.insertBefore(div, addCommentPanel.nextSibling);
                const rates = addCommentPanel.nextElementSibling.querySelectorAll('.rate-comment');
                for(let i = 0; i < rates.length; i++){
                    commentRate(rates[i]);
                }
            }else if(error){
                postError.style.display = 'block';
            }
        });
    });
    for (let i = 0; i < rateButtons.length; i++){
        commentRate(rateButtons[i]);
    }
    for(let i = 0; i < comments.length; i++){
        if(comments[i].getAttribute('user') === userId){
            editComment(comments[i]);
        }
    }
}

    function editComment(comment) {
        const insertPanel = comment.querySelector('.top-info .right'),
            commentId = comment.getAttribute('id').split('-')[1],
            edit = document.createElement('i'),
            remove = document.createElement('i');

        edit.setAttribute('content', commentId);
        remove.setAttribute('content', commentId);
        edit.addEventListener('click', function (e) {
           const commentId = e.currentTarget.getAttribute('content'),
                comment = document.querySelector('#comment-'+commentId+' .right-side'),
                text = comment.querySelector('.comment-text');
           if(comment.classList.contains('comment-edit')){
               comment.querySelector('.comment-change').style.display = 'none';
               text.style.display = 'block';
               comment.classList.remove('comment-edit');
               return;
           }
           comment.classList.add('comment-edit');
           text.style.display = 'none';
           if(comment.querySelector('.comment-change') === null){
              let commentChange = document.createElement('div');
              commentChange.classList.add('comment-change');
              commentChange.innerHTML = '<textarea id="comment-change"></textarea>' +
                  '                                <div class="actions">' +
                  '                                    <div class="counter">Осталось символов: <span>1000</span></div>' +
                  '                                    <div class="save">Сохранить</div>' +
                  '                                </div>';
              comment.appendChild(commentChange);
           }else{
               comment.querySelector('.comment-change').style.display = 'block';
           }
           let commentChange = comment.querySelector('.comment-change'),
                counter = commentChange.querySelector('.counter span'),
                save = commentChange.querySelector('.save'),
                area = commentChange.querySelector('textarea');
           area.innerText = text.innerText;
           counter.innerText = 1000 - text.innerText.substring(0, 1000).length;
           area.addEventListener('input', function () {
              if(area.value.length >= 1000){
                  area.value = area.value.substring(0, 1000);
              }
              counter.innerText = 1000 - area.value.length;
           });
           save.addEventListener('click', function () {
              const value = area.value;
              if(value.length < 10 || value.length > 1000){return;}
               xhr.request({
                   path: '/services/articles/comments',
                   method: 'PUT',
                   headers: {
                       'Content-Type':'application/json'
                   },
                   content: JSON.stringify({
                       id: commentId,
                       text: value
                   })
               }, function (response) {
                   if(response){
                       text.innerText = value;
                       commentChange.style.display = 'none';
                       text.style.display = 'block';
                   }
               })
           });
        });
        remove.addEventListener('click', function (e) {
            const commentId = e.currentTarget.getAttribute('content'),
                deleteBtn = modal.createDefaultButton('Удалить', modal.warningColor),
                declineBtn = modal.createDefaultButton('Отмена', modal.infoColor);
            deleteBtn.addEventListener('click', function () {
                xhr.request({
                    path: '/services/articles/comments',
                    method: 'DELETE',
                    headers: {
                        'Content-Type':'application/json'
                    },
                    content: JSON.stringify({
                        id: commentId
                    })
                }, function (response) {
                    if(response){
                        const removedComment = document.getElementById('comment-'+commentId);
                        removedComment.parentNode.removeChild(removedComment);
                    }
                });
            });
            modal.customActions(deleteBtn, declineBtn);
            modal.warning('Вы действительно хотите удалить комментарий?');
        });
        edit.innerHTML = '<svg role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="#E5E8EA" d="M290.74 93.24l128.02 128.02-277.99 277.99-114.14 12.6C11.35 513.54-1.56 500.62.14 485.34l12.7-114.22 277.9-277.88zm207.2-19.06l-60.11-60.11c-18.75-18.75-49.16-18.75-67.91 0l-56.55 56.55 128.02 128.02 56.55-56.55c18.75-18.76 18.75-49.16 0-67.91z"></path></svg>';
        remove.innerHTML = '<svg role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 352 512"><path fill="#D6DADE" d="M242.72 256l100.07-100.07c12.28-12.28 12.28-32.19 0-44.48l-22.24-22.24c-12.28-12.28-32.19-12.28-44.48 0L176 189.28 75.93 89.21c-12.28-12.28-32.19-12.28-44.48 0L9.21 111.45c-12.28 12.28-12.28 32.19 0 44.48L109.28 256 9.21 356.07c-12.28 12.28-12.28 32.19 0 44.48l22.24 22.24c12.28 12.28 32.2 12.28 44.48 0L176 322.72l100.07 100.07c12.28 12.28 32.2 12.28 44.48 0l22.24-22.24c12.28-12.28 12.28-32.19 0-44.48L242.72 256z"></path></svg>';
        insertPanel.appendChild(edit);
        insertPanel.appendChild(remove);
    }
    function commentRate(rateButton){
        rateButton.addEventListener('click', function (e) {
            let target = e.currentTarget,
                content = target.getAttribute('content').split(':'),
                commentId = content[0],
                rate = content[1],
                commentOwnerId = target.parentElement.parentElement.parentElement.getAttribute('user');
            if(userId === '-1'){
		modal.error('Вы не зарегистрированы, чтобы голосовать');
		return;
	    }else if(commentOwnerId === userId){
                modal.error('Нельзя голосовать за свой комментарий');
                return;
            }
            xhr.request({
                path: '/services/articles/comments/rating',
                method: 'POST',
                headers: {
                    'Content-Type':'application/json'
                },
                content: JSON.stringify({
                    id: commentId,
                    rate: rate
                })
            }, function (response, error, status) {
                if(response){
                    if(status === 208){
                        modal.info('Вы уже голосовали');
                        return;
                    }
                    let num = target.parentElement.querySelector('.rate');
                    num.innerText = Number(num.innerText) + Number(rate);
                }
            })
        });
    }

initRating();
initComments();