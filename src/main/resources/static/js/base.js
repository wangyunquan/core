

var bloghost="http://"+window.location.host;
var blog={
    /**
     * 提示 warning danger success
     * @param message
     * @param modeltype
     */
    nofify:	function(message,modeltype){
        $.notify({
                title: '<strong>提示</strong>',
                message: message
            },{
                type: modeltype,
                placement:{from:'top',align:'center'},
                animate: {
                    enter: 'animated bounceInDown',
                    exit: 'animated bounceOutUp'
                }
            }
        );
    },
    /**
     * 文章喜欢
     * @param articleid 评论id
     * @param obj 点赞dom 对象
     */
    articleSupport:function(articleid,obj){
        if(!blog.islogin()){
            blog.nofify("您还未登录,请先登录!","danger");
            return;
        }
        blogAlert.showLoading("Loading...");
        $.ajax({
            type: "post",
            url: bloghost+"/font/articleSupport.do",
            data: {id:articleid},
            success: function(msg){
                blogAlert.closeLoading();
                var str=$(obj).text().replace(/\D|\W/,"");
                var total=parseInt(str);
                if(msg=="+"){
                    blog.nofify("点赞成功!","success");
                    $(obj).html('<span class="glyphicon glyphicon-heart"></span>&nbsp;'+(total+1));
                    $(obj).addClass("active");
                    $(obj).find("span").css({"color":"#e78170"});
                    $(obj).attr("data-original-title","取消喜欢");
                }else if(msg=="-"){
                    blog.nofify("已取消点赞!","success");
                    $(obj).html('<span class="glyphicon glyphicon-heart"></span>&nbsp;'+(total-1));
                    $(obj).removeClass("active");
                    $(obj).find("span").css({"color":"black"});
                    $(obj).attr("data-original-title","喜欢");
                }
                else{
                    blog.nofify(msg,"danger");
                }

            }
        });
    },
    /**
     * 评论点赞
     * @param cid 评论id
     * @param obj 点赞dom 对象
     */
    articleCommentSupport:function(cid,obj){
        if(!blog.islogin()){
            blog.nofify("您还未登录,请先登录!","danger");
            return;
        }
        $.ajax({
            type: "post",
            url: bloghost+"/font/commentSupport.do",
            data: {commentId:cid},
            success: function(msg){
                blogAlert.closeLoading();
                var node=$(obj).children("span");
                var str=$(node).text().replace(/\D|\W/,"");
                var total=parseInt(str);
                if(msg=="+"){
                    blog.nofify("点赞成功!","success");
                    $(node).text("("+(total+1)+")");
                    $(node).css("color","red");
                }else if(msg=="-"){
                    blog.nofify("已取消点赞!","success");
                    $(node).text("("+(total-1)+")")
                    $(node).css("color","black");
                }
                else{
                    blog.nofify(msg,"danger");
                }

            }
        });
    },
    userLogin:function(){
        window.location='https://51so.info/pub/login.do?url='+ window.location;
    },
    userLogout:function(){
        blogAlert.confirm("是否退出系统?",function(){
            window.location='http://51so.info/font/logout.do';
        });
    },
    /**
     * 删除自己的评论
     * @param cid
     */
    delArticleComment:function(cid){
        if(!blog.islogin()){
            blog.nofify("您还未登录,请先登录!","danger");
            return;
        }
        blogAlert.confirm('你确定要删除选中记录吗？', function(result){
            if(result){
                blogAlert.showLoading("正在处理...");
                $.ajax({
                    type: "post",
                    url: bloghost+"/font/delComments.do",
                    data: {id:cid},
                    success: function(msg){
                        blogAlert.closeLoading();
                        if(msg=="success"){
                            blog.nofify("操作成功!","success");
                            $("#"+cid).remove();
                        }else{
                            blog.nofify(msg,"danger");
                        }

                    }
                });
            }
        });
    },
    /**
     * 是否已经登录
     * @returns {Boolean}
     */
    islogin:function(){
        var regx=/^((https|http)?:\/\/)51so\.info\/admin.*/;
        if(regx.test(window.location+"")){return true;}
        var result=$("#menu_user_info");
        return result.length>0;
    },
    faceBox:function(callback){
        var url=bloghost+'/pub/emojify_dialog.do?callback='+callback;
        $.fancybox.open({
            href: url,
            type: 'iframe',
            padding: 10,
            scrolling: 'no',
            fitToView: true,
            width: 450,
            height: 320,
            autoSize: false,
            closeClick: false,
            iframe: {
                scrolling :'no',
                preload : true
            }
        });
    }

};


var article = {
    /**
     * 删除文章
     * @param id
     */
    delArticle: function (id) {
        var lis = new Array();
        if (id != '' && id.length == 32) {
            lis.push(id);
        } else {
            //选择模式
            var chks = $("input[name='ar_chk']:checked");
            if (chks.length == 0) {
                blogAlert.alert("请选择要删除的文章", "red");
                return;
            }
            for (var i = 0; i < chks.length; i++) {
                lis.push(chks[i].value);
            }
        }
        blogAlert.confirm("你确定要删除选中记录吗？", function (result) {
            if (result) {
                blogAlert.showLoading("正在处理请稍后...");
                $.ajax({
                    type: "post",
                    url: "/admin/del_article.do",
                    data: {ids: lis},
                    success: function (msg) {
                        blogAlert.closeLoading();
                        bootbox.alert(msg);
                        if (msg.indexOf("成功") != -1) {
                            $("#sform").submit();
                        }

                    }
                });
            }
        });
    }
    /**
     * 删除标签
     * @param labid
     */
    , dellable: function (labid) {
        blogAlert.confirm(
            "是否删除此标签?",
            function (result) {
                if (result) {
                    $.ajax({
                        type: "get",
                        url: "/admin/del_article_lable.do",
                        data: {id: labid},
                        success: function (msg) {
                            if (msg == "success") {
                                location.reload();
                            } else {
                                alert(msg);
                            }
                        }
                    });
                }
            }
        )
    }
    /**
     * 取消置顶
     */
    , canclefixedTop: function (id) {
        blogAlert.showLoading("正中处理");
        var array = new Array();
        array.push(id);
        $.ajax({
            type: "post",
            url: "/admin/cancleFixedTop.do",
            data: {ids: array},
            success: function (msg) {
                blogAlert.closeLoading();
                if (msg == "success") {
                    location.reload();
                } else {
                    bootbox.alert(msg);
                }

            }
        });
    }

    /**
     * 改变文章类型
     */
    , updateCategory: function (data) {
        var chks = $("input[name='ar_chk']:checked");
        if (chks.length == 0) {
            blogAlert.error("请选择要分类的文章");
            return;
        }
        swal({
            title: '请选择文章类型',
            input: 'select',
            inputOptions: data,
            showCancelButton: true,
            confirmButtonColor: '#42c02e',
            cancelButtonColor: '#d33',
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            blogAlert.showLoading("正在处理,请稍后.");
            var lis = new Array();
            for (var i = 0; i < chks.length; i++) {
                lis.push(chks[i].value);
            }
            $.ajax({
                type: "post",
                url: "/admin/update_articletype.do",
                data: {ids: lis, category: result},
                success: function (msg) {
                    blogAlert.closeLoading();
                    if (msg == "success") {
                        location.reload();
                    } else {
                        blogAlert.alert(msg, "blue");
                    }

                }
            });
        })
    }
    /**
     * 置顶
     */
    , fixedTop: function (id) {
        blogAlert.showLoading("正中处理");
        var array = new Array();
        array.push(id);
        $.ajax({
            type: "post",
            url: "/admin/fixedTop.do",
            data: {ids: array},
            success: function (msg) {
                blogAlert.closeLoading();
                if (msg == "success") {
                    location.reload();
                } else {
                    bootbox.alert(msg);
                }

            }
        });
    }
    /**
     * 抓取图片
     */
    , crawImg: function (id) {
        blogAlert.showLoading("正中处理");
        $.ajax({
            type: "post",
            url: "/admin/outsideImgResTransformation.do",
            data: {id: id},
            success: function (msg) {
                blogAlert.closeLoading();
                if (msg == "success") {
                    blogAlert.success("操作成功");
                } else {
                    blogAlert.error(msg);
                }

            }
        });
    }
};

function allchk(obj) {
    var chk = obj.checked;
    if (chk) {
        $("input[name='ar_chk']").prop("checked", true);
    } else {
        $("input[name='ar_chk']").prop("checked", false);
    }
}
function rowclick(row) {
    var checked = $(row.cells[0]).children(":checkbox").prop("checked");
    $(row.cells[0]).children(":checkbox").prop("checked", checked ? false : true);
}


var blogAlert = {
    dialog: null,
    alert: function (msg) {
        swal({type: 'warning', text: msg, timer: 2000});
    },
    confirm: function (msg, funcallback) {
        swal({
            //title: '是否继续',
            text: msg,
            type: 'question',
            showCancelButton: true,
            confirmButtonColor: '#42c02e',
            cancelButtonColor: '#d33',
            confirmButtonText: '是',
            cancelButtonText: '否'
        }).then(funcallback)
    },
    error:function(title,text){
        swal(
            title, text, 'error'
        )
    },
    success:function(title,text){
        swal(
            title, text, 'success'
        )
    },
    success:function(title,text,funcallback){
        swal({
            title: title,
            text: text,
            type: 'success'
        }).then(
            funcallback
        )
    },
    /**
     *
     * @param title
     * @param inputType text, email, password, number, tel, range, textarea, select, radio, checkbox, file and url.
     * @param inputVal 默认值
     * @param callback
     */
    prompt: function (title,inputType,inputVal,callback) {
        swal({
            title: title,
            input: inputType,
            inputValue:inputVal,
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            confirmButtonColor: '#42c02e',
            cancelButtonColor: '#d33',
            showLoaderOnConfirm: true,
            //preConfirm: callback,
            allowOutsideClick: false
        }).then(callback);
    }, showLoading: function (msg) {
        swal({
            title: '',
            text: msg,
            width:'300px',
            imageUrl: '/www/showloading/loading.gif',
            showConfirmButton:false,
            showLoaderOnConfirm:true,
            allowOutsideClick:false
        })
        /* blogAlert.dialog = bootbox.dialog({
             message: '<p class="text-center"><img src="/www/showloading/loading.gif"/>&nbsp;' + msg + '</p>',
             size: 'small',
             closeButton: false
         });*/
    },
    closeLoading: function () {
        /* if (null == blogAlert.dialog)return;
         blogAlert.dialog.modal('hide');*/
        swal.close();
    },
    /**
     *
     * @param message
     * @param modeltype warning danger success
     */
    notify: function (message, modeltype) {
        $.notify({
                title: '<strong>提示</strong>',
                message: message
            }, {
                type: modeltype,
                placement: {from: 'top', align: 'center'},
                animate: {
                    enter: 'animated bounceInDown',
                    exit: 'animated bounceOutUp'
                }
            }
        );
    }
};

var Article= {
    AdvancedMode: function (obj) {
        if ($(obj).text() == "高级") {
            $("#div_summary").show();
            //$("#div_password").show();
            //$("#div_private").show();
            //$("#div_discuss").show();
            //$("#div_post_type").show();
            $(obj).text("精简");
        } else {
            $("#div_summary").hide();
            //$("#div_password").hide();
            //$("#div_private").hide();
            //$("#div_discuss").hide();
            //$("#div_post_type").hide();
            $(obj).text("高级");
        }
    }
}