window.xhr = {

    headers: function (xhr) {

        if(xhr instanceof XMLHttpRequest) {

            var header = document.getElementById("_csrf_header").getAttribute("content");
            var token = document.getElementById("_csrf").getAttribute("content");

            xhr.setRequestHeader(header, token);
            xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest")
        }
    },

    file: function (path, file, ajax) {

        xhr.open("POST", path, true);

        xhr.headers(ajax);

        var formData = new FormData();
        formData.append("file", file);

        ajax.send(formData);

    },

    get: function () {
        var ajax = new XMLHttpRequest();
        xhr.headers(xhr);
        return ajax;
    }

};
