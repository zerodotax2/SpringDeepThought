'use strict';

window.xhr = {

    file: function (path, file, callback) {

        let formData = new FormData();
        formData.append("file", file);

        xhr.request({path: path, method: "POST",
            content: formData}, function(response, error){
            if(error){
                callback(response)
            }else if(response){
                callback(undefined, error);
            }
        });
    },
    request: function (options, callback) {
        const ajax = new XMLHttpRequest();
        ajax.onreadystatechange = function (e) {
            if (ajax.readyState !== 4) {
                return;
            }if (ajax.status >= 400) {
                callback(undefined, ajax.responseText, ajax.status);
                return;
            }

            callback(ajax.responseText, undefined, ajax.status);
        };
        ajax.open(options.method || "GET", options.path || "/", true);
        ajax.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        ajax.setRequestHeader(appConf.csrfHeader, appConf.csrfToken);


        if(options.headers){
            let headers = Object.keys(options.headers);
            for(let i = 0; i < headers.length; i++){
                ajax.setRequestHeader(headers[i], options.headers[headers[i]]);
            }
        }
        ajax.send(options.content || null);
    }
};
