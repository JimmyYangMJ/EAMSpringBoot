require.config({
    paths: {
        echarts: 'http://echarts.baidu.com/build/dist'
    }
});
var course = {
    "status": 0,
    "msg": "查询成功",
    "data":[
        {
            "id": 1,
            "node": 1,
            "humidity": 23,
            "times": "2019-09-22- 20:40:56"
        },
        {
            "id": 3,
            "node": 1,
            "humidity": 45,
            "times": "2019-09-22- 20:41:54"
        },

        {
            "id": 3,
            "node": 1,
            "humidity": 65,
            "times": "2019-09-23- 20:41:54"
        }
    ]
};
var status = course.status;
if(status == 1){  //  没有相应记录
    alert("没有相应记录")
}
var node = course.data;
var nodeTime = new Array();
var nodeHumidity = new Array();
for (var i = 0; i < 3; i++) {
    nodeTime[i] = node[i].times;
    nodeHumidity[i] = node[i].humidity;
}

// 使用
require(
    [
        'echarts',
        'echarts/chart/line' ,// 使用柱状图就加载bar模块，按需加载
        'echarts/chart/bar'
    ],
    function (ec) {
        // 基于准备好的dom，初始化echarts图表
        var myChart = ec.init(document.getElementById('echart1'));

        option = {
            title : {
                text: '土壤水势',
                subtext: '虚构'
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['节点一','节点二']
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data: nodeTime
                    // data : ['00：00','1：00','2：00','3：00','4：00','5：00','6：00','7:00','8:00',
                    //     '9：00','10：00','11：00','12：00','13：00','14：00','15：00','16:00','17:00'
                    // ,'18:00', '19：00','20：00','21：00','22：00','23:00']
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    axisLabel : {
                        formatter: '{value} pa'
                    }
                }
            ],
            series : [
                {
                    name:'节点一',
                    type:'line',
                    data:nodeHumidity,
                    // data:[11, 50, 15, 13, 12, 13, 10,9],
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name: '平均值'}
                        ]
                    }
                },
                {
                    name:'节点二',
                    type:'line',
                    data:[1, -2, 2, 5, 3, 2, 0,4],
                    markPoint : {
                        data : [
                            {name : '周最低', value : -2, xAxis: 1, yAxis: -1.5}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name : '平均值'}
                        ]
                    }
                }
            ]
        };

        // 为echarts对象加载数据
        myChart.setOption(option);
    }
);