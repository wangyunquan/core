
(function ($, window, document, undefined) {

    window.CommonTable = function (tableId, searchDiv, dataurl) {
        this.tableId = tableId;

        this.searchDiv = searchDiv;
        this.data = null;
        this.loaded = false;
        this.dataurl = dataurl;
        this.serverCallback = null;

        // 用作缓存一些数据
        var dataCache = $("#dataCache" + tableId);
        if (dataCache.length == 0) {
            dataCache = $("<div></div>");
            dataCache.attr("id", "dataCache" + tableId);
            $(document.body).append(dataCache);
        }
        this.dataCache = dataCache;

        //
        var searchButton = $("#" + searchDiv + " button[data-btn-type='search']");
        this.searchButton = searchButton;
        var resetButton = $("#" + searchDiv + " button[data-btn-type='reset']");
        this.resetButton = resetButton;
        // 表格横向自适应
        $("#" + this.tableId).css("width", "100%");
        // 初始化表格
        this.initTable(tableId, searchDiv,dataurl);



    }


    CommonTable.prototype.initTable = function (tableId, searchDiv,dataurl) {
        this.data = this.getServerData(null,tableId, dataurl);
        this.dataCache.data("data", this.data);
        // console.log(JSON.stringify(this.data));
        var that = this;

        // alert(JSON.stringify(columns));
        var allowPaging = 20;
        var rowId = "rowid";
        this.table = $('#' + tableId).DataTable($.extend({
            "paging": allowPaging, // 分页
            "lengthChange": allowPaging, // 每页记录数可选项
            "lengthMenu": [[10, 20, 50, -1], [10, 20, 50, "全部"]],
            "searching": false, // 过滤
            "ordering": true, // 排序
            "rowId": rowId,
            //"dom": '<"top"iflp<"clear">>rt<"bottom"iflp<"clear">>',
            "info": allowPaging, // 分页明细
            "autoWidth": false,
            //"stateSave" : true,// 这样就可以在删除返回时，保留在同一页上
            "processing": true,// 是否显示取数据时的那个等待提示
            "pagingType": "full_numbers",// 分页样式
            "language": { // 中文支持
                "sUrl": basePath + "/json/zh_CN.json"
            },
            "displayLength": that.data.size,// 每页记录条数，默认为10
            "serverSide": true,
            "ajaxDataProp": "data",
            "ajaxSource": dataurl,
            "fnServerData": $.proxy(that.fillDataTable, that),
            "fnInitComplete": $.proxy(that.fnInitComplete, that),
            "singleSelect": true,  //单选
            "aoColumns": columns

        }, that.config));


        if (this.searchButton) {
            this.searchButton.click(function () {
                that.table.page('first').draw(false);
                // 执行查询的回调函数
                if (that.searchButton.data("callback")) {
                    eval(that.searchButton.data("callback"));
                }
            });
        }

        if (this.resetButton) {
            this.resetButton.click(function () {
                //清除查询条件
                that.clearSearchDiv(that.searchDiv);
                //清除排序、分页、重置初始长度
                that.table.order([]).page.len(10).draw();
                if (that.resetButton.data("callback")) {
                    eval(that.resetButton.data("callback"));
                }
            });
        }
    }

    /**
     * 获取服务器中的数据
     *
     * @param pageInfo
     *            分页信息
     * @param tableId
     *            table的ID
     */
    CommonTable.prototype.getServerData = function (pageInfo, tableId,dataurl) {
        var dataCache = $("#dataCache" + tableId);
        var reqParam = {

            pageName: window.document.location.pathname,
            pageInfo: pageInfo,
            query: null,
            sortInfo: dataCache.data("sortInfo"),
            conditions: this.fnGetConditions(this.searchDiv)
        };
        dataCache.data("pageInfo", pageInfo);
        var retData = null;
        console.log("reqObj:");
        console.log(reqParam);
        console.log(JSON);
        //注释以上部分，统一用ajaxPost处理，以便处理session超时（ajax请求超时）
        ajaxPost(dataurl, {"reqObj": this.toJSONString(reqParam)}, function (result, status) {
            retData = result;
        });


        return retData;
    }


    // 获取查询框中的查询数据

    CommonTable.prototype.fnGetConditions = function (searchDiv) {
        var searchDiv = $("#" + searchDiv);
        var conditions = [];
        if (searchDiv !== null && searchDiv.length > 0) {
            var ele = searchDiv.find(':input[name]');
            ele.each(function (i) {
                if ($(this).attr("readonly") == "readonly" || $(this).attr("disabled") == "disabled")
                    return;
                var map = {};
                var key = $(this).attr("name");
                var isExist = false;
                for (var j = 0; j < conditions.length; j++) {
                    if (key == conditions[j].key) {
                        isExist = true;
                        map = conditions[j];
                        break;
                    }
                }
                var value = $(this).val();
                var type = $(this).attr("type");
                if ((type && (type.toLowerCase() == 'checkbox' || type.toLowerCase() == 'radio'))) {
                    if ($(this).attr("checked") != "checked") {
                        value = "";
                    }
                }
                    map.key = key;
                    map.value = value;
                    conditions.push(map);

            });
        } else {
            // no search conditions found.
        }
        //console.log("conditons:"+JSON.stringify(conditions));
        return conditions;
    }





    /**
     * 换页、排序、查询按钮调用此方法
     *
     * @param sSource
     *            服务器请求方法
     * @param aoData
     *            基本信息
     * @param fnCallback
     *            重绘dataTable的回调函数
     * @param oSettings
     *            dataTable全局配置
     */
    CommonTable.prototype.fillDataTable = function (sSource, aoData, fnCallback, oSettings) {
        var result = this.data;
        var map = oSettings.oAjaxData;
        var dataCache = $("#dataCache" + oSettings.sTableId);
        if (this.loaded) {// 换页
            var pageInfo = {};
            pageInfo.pageSize = map.iDisplayLength;
            pageInfo.pageNum = map.iDisplayStart % map.iDisplayLength == 0 ? map.iDisplayStart / map.iDisplayLength + 1
                : map.iDisplayStart / map.iDisplayLength;
            // console.log(dataCache.data("getServerData"));
            // 构造排序
            var columnNames = map.sColumns.split(',');
            var sortArr = [];
            for (var i = 0; i < map.iSortingCols; i++) {
                if (map["iSortCol_" + i] != 0)// 过滤掉rowIndex的排序
                    sortArr.push(columnNames[map["iSortCol_" + i]] + " " + map["sSortDir_" + i]);
            }
            dataCache.data("sortInfo", sortArr.join());
            result = this.getServerData(pageInfo, oSettings.sTableId);
            this.data = result;

        } else {// 首次加载
            result = this.data;
            this.loaded = true;
        }
        var obj = {};
        obj['data'] = result.rows;
        obj["iTotalRecords"] = result.pageInfo.count;
        obj["iTotalDisplayRecords"] = result.pageInfo.count;
        fnCallback(obj);
        //序号排序
        $("table.table thead tr").each(function () {
            $(this).find("th").eq(0).removeClass("sorting_asc").addClass("sorting_disabled");
        });
        //加载完成以后做一些其他处理
        if (this.serverCallback) {
            this.serverCallback.call(this, oSettings);
        }


    }


    CommonTable.prototype.toJSONString = function (value) {
        var _array_tojson = Array.prototype.toJSON;
        delete Array.prototype.toJSON;
        var r = JSON.stringify(value);
        Array.prototype.toJSON = _array_tojson;
        return r;
    }

})(jQuery, window, document);