function searchMusic() {
    var key = document.getElementById("keyword").value;
    $.ajax({
        url: 'http://172.19.240.204:3000/search?keywords=' + key,
        data: {},
        dataType: 'json',
        type: 'get',
        success: function (result) {
            document.getElementById('songtable').innerHTML = '<thead>' +
                '            <tr>' +
                '                <td>音乐列表</td>' +
                '                <td>操作</td>' +
                '            </tr>' +
                '            </thead>';
            var songs = result.result.songs;
            for (var i in songs) {
                if (songs[i].fee === 0){
                    var name=songs[i].name + ' - ' + songs[i].artists[0].name;
                    var str = '<td>' + name + '</td>';
                    str = str + '<td>' + '<button class=\"btn btn-primary\" type=\"button\" onclick=\"javascript:musicChange(' + songs[i].id + ')\">播放</button>' + '</td>';
                    str = str + '<td>' + '<button class=\"btn btn-primary\" type=\"button\" onclick=\"javascript:favourite(' + songs[i].id +',\''+name+ '\')\">收藏</button>' + '</td>';
                    str = str + '<td>' + '<a href=\"/whisper/new/' + songs[i].id + '/' + name + '\">' + '<button class=\"btn btn-primary\" type=\"button\">创建悄悄话</button>' + '</a>' + '</td>';
                    str = '<tr>' + str + '</tr>';
                    $("#songtable").append(str)
                }
            }

        },
        error: function (err) {
            location.reload(true);
            alert('搜索失败！');
        }
    });

}

function musicChange(id) {
    var url = "//music.163.com/outchain/player?type=2&id=" + id + "&auto=1&&height=66";
    document.getElementById('music_player').src = url;

}

function favourite(id,name){
    if(!(id)) {
        alert('请输入全部字段！');
        return;
    }
    $.ajax({
        url : '/music/'+id+'/favorite?musicName='+name,
        data : {
        },
        dataType : 'json',
        type : 'post',
        success: function (result) {
            alert('收藏成功！');

        },
        error:function(err){
            location.reload(true);
            alert('收藏失败！');
        }
    });

}
function unfavourite(id) {
    if (!(id)) {
        alert('请输入全部字段！');
        return;
    }
    $.ajax({
        url: '/music/' + id + '/unfavorite',
        data: {},
        dataType: 'json',
        type: 'post',
        success: function (result) {
            location.reload(true);

        },
        error: function (err) {
            location.reload(true);
            alert('取消收藏失败！');
        }
    });
}