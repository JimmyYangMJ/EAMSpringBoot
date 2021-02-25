var assetTableData = {
    isCollapse : true,
    assetImportForm: {
        assetName: '',
        department: '', // 数组尾部为最终结果
        charge: '' // 数组
    },
    assetUpdate: {
        id: {},
        data: [],
        form: {},
        currentStage: '接收报修信息',
        nextStage: ''
    },
    tables: [],    // 表格数据
    tableData: [], // 列名
    pagination: {
        pageSize: 10,
        sum: 0,
        currentPage: 1
    },
    message: "未上传",
    formInline: {  // 查询表单
        table: '', // 选中表
        assetNameList: [] // 表集合
    },
    form: {},
    dialogFormVisible: false,
    dialogUpdateVisible: false,
    formValues: [],
    forms: {},
    formKeys: [],
    formLabelWidth: '200px'

};