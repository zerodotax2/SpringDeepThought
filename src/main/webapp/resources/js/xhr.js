window.xhr = {

    file: function (path, file, callback) {

        let formData = new FormData();
        formData.append("file", file);

        xhr.request({path: path, method: "POST", content: formData}, function(response, error){
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
            } else if (ajax.status >= 400) {
                callback(undefined, ajax.responseText);
            }

            callback(ajax.responseText);
        };
        ajax.open(options.method || "GET", options.path || "/", true);
        ajax.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        ajax.setRequestHeader(appConf.csrfHeader, appConf.csrfToken);
        if(options.headers){
            for(let header in options.headers){
                ajax.setRequestHeader(header.name, header.value)
            }
        }
        ajax.send(options.content || null);
    }
};
