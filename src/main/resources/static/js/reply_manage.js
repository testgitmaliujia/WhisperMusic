function delReply(id){
    if(!(id)) {
        alert('请输入全部字段！');
        return;
    }
    $.ajax({
        url : '/reply/'+id+'/delete',
        data : {
        },
        dataType : 'json',
        type : 'post',
        success: function (result) {
            location.reload(true);
            alert('删除成功！');
        },
        error:function(err){
            location.reload(true);
            alert('删除失败！');
        }
    });

}

function stickReply(id){
    if(!(id)) {
        alert('请输入全部字段！');
        return;
    }
    $.ajax({
        url : '/reply/'+id+'/stick',
        data : {
        },
        dataType : 'json',
        type : 'post',
        success: function (result) {
            location.reload(true);
            alert('置顶成功！');
        },
        error:function(err){
            location.reload(true);
            alert('操作失败！');
        }
    });

}

function unstickReply(id){
    if(!(id)) {
        alert('请输入全部字段！');
        return;
    }
    $.ajax({
        url : '/reply/'+id+'/unstick',
        data : {
        },
        dataType : 'json',
        type : 'post',
        success: function (result) {
            location.reload(true);
            alert('操作成功！');
        },
        error:function(err){
            location.reload(true);
            alert('操作失败！');
        }
    });

}