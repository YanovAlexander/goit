var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        stompClient.subscribe('/topic/retrieveTree', function (retrieveTree) {
            if ($("#jstree").jstree(true).settings) {
                $("#jstree").jstree(true).settings.core.data = JSON.parse(retrieveTree.body);
                $('#jstree').jstree(true).refresh();
            } else {
                $('#jstree').jstree({
                    'core': {
                        "data": JSON.parse(retrieveTree.body),
                        "check_callback": true
                    },
                    "plugins": ["contextmenu"],
                    "contextmenu": {
                        "items": customMenu
                    }
                });
            }
        });
        stompClient.send("/app/root-tree", {});
        stompClient.subscribe("/topic/retrieveFile", function (res) {
            var file = new File([atob(JSON.parse(res.body).bytes)], JSON.parse(res.body).text);
            saveAs(file);
        })
    });
}

function customMenu(node) {
    var items = {
        "Create": {
            "label": "Create",
            "action": function (data) {
                var ref = $.jstree.reference(data.reference);
                sel = ref.get_selected();
                if (!sel.length) {
                    return false;
                }
                sel = sel[0];
                sel = ref.create_node(sel, {"type": "file"});
                if (sel) {
                    ref.edit(sel);
                }
            }
        },
        "Upload_file": {
            "label": "Upload file",
            "action": function (data) {
                $("#file-input").trigger('click');
                document.getElementById('file-input').addEventListener("change", uploadFile, false);
            }
        },
        "Rename": {
            "label": "Rename",
            "action": function (data) {
                var inst = $.jstree.reference(data.reference);
                obj = inst.get_node(data.reference);
                inst.edit(obj);
            }
        },
        "Delete": {
            "label": "Delete",
            "action": function (data) {
                var ref = $.jstree.reference(data.reference),
                    sel = ref.get_selected();
                if (!sel.length) {
                    return false;
                }
                var node = ref.get_node(sel);
                var path = node.original.text;
                deleteNode(retrievePath(node, path));
                ref.delete_node(sel);
            }
        },
        "Download": {
            "label": "Download",
            "action": function (data) {
                var ref = $.jstree.reference(data.reference),
                    sel = ref.get_selected();
                if (!sel.length) {
                    return false;
                }
                var node = ref.get_node(sel);
                var path = node.original.text;
                stompClient.send("/app/download-node", {}, path);
            }
        }
    };

    if (node.icon != "jstree-file") {
        delete items.Download;
    }

    if (node.icon == "jstree-file") {
        delete items.Upload_file;
    }

    return items
}

function retrievePath(node, path) {
    if (node.parent != "#") {
        var parent_node = $("#jstree").jstree().get_node(node.parent);
        path = parent_node.text + "/" + path;
        return retrievePath(parent_node, path);
    }
    return path;
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function deleteNode(nodePath) {
    stompClient.send("/app/delete-node", {}, nodePath);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
});

function uploadFile() {
    var file = document.getElementById('file-input').files[0];
    var reader = new FileReader();
    reader.readAsDataURL(file);
    var sel = $("#jstree").jstree("get_selected");
    if (!sel.length) {
        return false;
    }
    var node = $("#jstree").jstree().get_node(sel);
    var path = node.original.text;
    reader.onload = function (e) {
        var result = e.target.result;
        var base64 = result.split("base64,");
        var fileName = document.getElementById('file-input').files[0].name;
        body = {"base64": base64[1], "fileName": fileName, "parentPath": retrievePath(node, path)};
        stompClient.send("/app/upload-file", {}, JSON.stringify(body));
    };
}

