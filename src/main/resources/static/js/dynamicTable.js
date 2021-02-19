/**
 *  BsTable动态表格生成
 */
DataQuery.sqlExecute = function (){

    var sql = $('#sql').val();
    var connectInfo = $('#connectInfo').val();

    $('#DataQueryTable').bootstrapTable({
        ajax: function (request) {
            $.ajax({
                type: "GET",
                url: Feng.ctxPath + "/dataQuery/list" + "/" + sql + "/" + connectInfo,
                contentType: "application/json;charset=utf-8",
                dataType: "json",
                json: 'callback',
                success: function (json) {

                    var dynamicHeader = [];
                    dynamicHeader.push({
                        field: "state",
                        check: true
                    });

                    for (var i = 0; i<(Object.keys(json[0])).length; i++) {
                        var property = (Object.keys(json[0]))[i];
                        //console.log(property);
                        dynamicHeader.push({
                            "title": property,
                            "field": property,
                            switchable: true,
                            sortable: true
                        });
                    }

                    //console.log(Object.keys(json[0]));

                    $('#DataQueryTable').bootstrapTable('destroy').bootstrapTable({
                        data: json,
                        toolbar: '#toolbar',
                        cache: false,
                        striped: true,
                        sidePagination: "client",
                        sortOrder: "desc",
                        pageSize: 25,
                        pageNumber: 1,
                        pageList: "[25, 50, 100, All]",
                        showToggle: true,
                        showColumns: true,
                        showExport: true,
                        exportDataType: "basic",
                        pagination: true,
                        strictSearch: true,
                        search: true,
                        columns: dynamicHeader
                    });
                },
                error: function () {
                    alert("SQL查询错误，请输入正确的SQL语句！");
                    location.reload();
                }
            });
        }
    });
};