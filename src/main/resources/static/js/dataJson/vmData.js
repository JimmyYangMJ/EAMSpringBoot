const vmData = {
    isCollapse : true,
    testData: '',
    assetImportForm: {
        assetName: '',
        department: '', // 数组尾部为最终结果
        charge: '' // 数组
    },
    assetNameList: [],
    assetUpdate: {
        id: {},
        data: [],
        form: {},
        currentStage: '接收报修信息',
        nextStage: ''
    },
    departmentOptions: [{
        value: 'jishufuwushiyebu',
        label: '技术服务事业部',
        children: [{
            value: 'weixiuzhongxing',
            label: '维修中心'
        }]
    }, {
        value: 'yunyujisuanshiyebu',
        label: '云与计算事业部',
        children: [{
            value: 'yunjishuyunwei',
            label: '云技术运维'
        }]
    }],
    staffOptions: [{
        value: 'jishufuwushiyebu',
        label: '技术服务事业部',
        children: [{
            value: 'weixiuzhongxing',
            label: '维修中心',
            children: [{
                value: 'zhangsan',
                label: '员工2'
            }]
        }]
    }, {
        value: 'yunyujisuanshiyebu',
        label: '云与计算事业部',
        children: [{
            value: 'yunjishuyunwei',
            label: '云技术运维',
            children: [{
                value: 'zhangsan',
                label: '员工1'
            }]
        }]
    }],
    tables: [],    // 表格数据
    tableData: [], // 列名
    pagination: {
        pageSize: 10,
        sum: 0,
        currentPage: 1

    },
    message: "未上传",
    formInline: {
        table: ''
    },
    form: {},
    dialogFormVisible: false,
    dialogUpdateVisible: false,
    formValues: [],
    forms: {},
    formKeys: [],
    formLabelWidth: '200px'

};