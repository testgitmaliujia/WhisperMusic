function delCollect(id){
    if(!(id)) {
        alert('请输入全部字段！');
        return;
    }
    $.ajax({
        url : '/whisper/'+id+'/unfavorite',
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

