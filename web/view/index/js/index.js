$(document).ready(function() {


    $('.date').datepicker({
        autoclose: true, //自动关闭
        beforeShowDay: $.noop,    //在显示日期之前调用的函数
        calendarWeeks: false,     //是否显示今年是第几周
        clearBtn: true,          //显示清除按钮
        daysOfWeekDisabled: [],   //星期几不可选
        endDate: Infinity,        //日历结束日期
        forceParse: true,         //是否强制转换不符合格式的字符串
        format: 'yyyy-mm-dd',     //日期格式
        keyboardNavigation: true, //是否显示箭头导航
        language: 'zh-CN',           //语言
        minViewMode: 0,
        orientation: "auto",      //方向
        rtl: false,
        startDate: -Infinity,     //日历开始日期
        startView: 0,             //开始显示
        todayBtn: false,          //今天按钮
        todayHighlight: true,    //今天高亮
        weekStart: 1              //星期几是开始
    });
    $('#f-start-date').datepicker('setDate',new Date());
    $('#f-end-date').datepicker('setDate',getFormatDate(new Date(),7));
    $('#init-date').datepicker('setDate',getFormatDate(new Date(),-60));
    $('#st-start-date').datepicker('setDate',getFormatDate(new Date(),-30));

    $('input').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    });

    $('.spinner .btn:first-of-type').on('click', function() {
        $('.spinner input').val( parseInt($('.spinner input').val(), 10) + 1);
    });
    $('.spinner .btn:last-of-type').on('click', function() {
        $('.spinner input').val( parseInt($('.spinner input').val(), 10) - 1);
    });

    $('#future-rainfall-content').hide();


    $('#future-rainfall-radios1').on('ifChecked', function(){
        $('#future-rainfall-content').show();
    });

    $('#future-rainfall-radios2').on('ifChecked', function () {
        $('#future-rainfall-content').hide();
    });

    $("#add-rainfall-checkbox").on('ifChecked', function () {
        var startTime = $('#f-start-time-value').val();
        var row = {"id":1,"time":startTime,"sub_basin1":1,"sub_basin2":2,"sub_basin3":3,"sub_basin4":4};
        $('#future-rainfall-table').bootstrapTable("updateRow",{index: 0, row: row});
    });

    $("#add-rainfall-checkbox").on('ifUnchecked', function () {
        var startTime = $('#f-start-time-value').val();
        var next24h = $("#next24h").val() == "" ? "null" : $("#next24h").val();
        var item = {"id":1,"time":startTime};
        var areaList = area.area;
        for(var i=0;i<areaList.length-2;i++){
            $.extend(item,$.parseJSON("{\""+ areaList[i+2].field +"\":"+next24h+"}"));
        }
        $('#future-rainfall-table').bootstrapTable("updateRow",{index: 0, row: item});
    });

    var area = {"area":[{"field":"id","title":"序号"},{"field":"time","title":"时间"},{"field":"sub_basin1","title":"子流域1","editable": {
            "type": 'text',
        "title": '子流域1',
        validate: function (v) {
        // if (!v) return '用户名不能为空';
    }
}}, {"field":"sub_basin2","title":"子流域2"},{"field":"sub_basin3","title":"子流域3"},{"field":"sub_basin4","title":"子流域4"}]};

    $("#load-data-one-key,#data-import").click(function(){

        $('#future-rainfall-table').bootstrapTable("load",loadFutureRainFall());
        $('#future-water-table').bootstrapTable("load",loadFutureWater());

    });

    $('#future-rainfall-table').bootstrapTable({
        //url: 'data.json',
        // columns : area.area
        columns : [{
            field: 'id',
            title: '序号'
        }, {
            field: 'time',
            title: '时间'
        }, {
            field: 'sub_basin1',
            title: '子流域1',
            editable: {
                type: 'text',
                title: '子流域1',
                mode: "inline",
                emptytext: 0,
                highlight: false
            }
        }, {
            field: 'sub_basin2',
            title: '子流域2'
        }, {
            field: 'sub_basin3',
            title: '子流域3'
        }, {
            field: 'sub_basin4',
            title: '子流域4'
        }],
        /*data: [{
            id: 1,
            time: '2018-05-05',
            sub_basin1: '3',
            sub_basin2: '3',
            sub_basin3: '3'
        }, {
            id: 2,
            time: '2018-05-06',
            sub_basin1: '3',
            sub_basin2: '3',
            sub_basin3: '3'
        },{
            id: 3,
            time: '2018-05-07',
            sub_basin1: '3',
            sub_basin2: '4',
            sub_basin3: '5'
        },{
            id: 4,
            time: '2018-05-07',
            sub_basin1: '3',
            sub_basin2: '4',
            sub_basin3: '5'
        },{
            id: 5,
            time: '2018-05-07',
            sub_basin1: '3',
            sub_basin2: '4',
            sub_basin3: '5'
        }],*/
        striped: true
    });
    // $('#future-rainfall-table').bootstrapTable("load",loadFutureRainFall());

    var title = {"area":[{"field":"id","title":"序号"},{"field":"time","title":"时间"},{"field":"reservoir1","title":"水库1"},
            {"field":"reservoir2","title":"水库2"},{"field":"reservoir3","title":"水库3"},{"field":"reservoir4","title":"水库4"},{"field":"reservoir5","title":"水库5"}
            ,{"field":"reservoir6","title":"水库6"},{"field":"reservoir7","title":"水库7"},{"field":"reservoir8","title":"水库8"},{"field":"reservoir9","title":"水库9"}]};

    $('#future-water-table').bootstrapTable({
        //url: 'data.json',
        columns : title.area,
        /*data: [{
            id: 1,
            time: '2018-05-05',
            reservoir1: '3',
            reservoir2: '3',
            reservoir3: '3'
        }, {
            id: 2,
            time: '2018-05-06',
            reservoir1: '3',
            reservoir2: '3',
            reservoir3: '3'
        },{
            id: 3,
            time: '2018-05-07',
            reservoir1: '3',
            reservoir2: '4',
            reservoir3: '5'
        },{
            id: 4,
            time: '2018-05-07',
            reservoir1: '3',
            reservoir2: '4',
            reservoir3: '5'
        },{
            id: 5,
            time: '2018-05-07',
            reservoir1: '3',
            reservoir2: '4',
            reservoir3: '5'
        }],*/
        striped: true
    });

    function loadFutureRainFall() {

        var startTime = $('#f-start-time-value').val();
        var endTime = $('#f-end-time-value').val();
        /*if(startTime == "" || endTime == ""){
            alert("请选择预测开始时间和结束时间");
            return;
        }*/
        var rowData = [];
        var rows = dayBetwennTwoDate(startTime,endTime);
        var next24h = $("#next24h").val() == "" ? "null" : $("#next24h").val();
        var next48h = $("#next48h").val() == "" ? "null" : $("#next48h").val();
        var next72h = $("#next72h").val() == "" ? "null" : $("#next72h").val();
        var next3d = $("#next3d").val() == "" ? "null" : $("#next3d").val();
        for(var i=0;i<rows.length;i++){
            var item = {"id":i+1,"time":rows[i]};
            var areaList = area.area;
            if(i == 0){
                for(var j=0;j<areaList.length-2;j++){
                    $.extend(item,$.parseJSON("{\""+ areaList[j+2].field +"\":"+next24h+"}"));
                }
            } else if(i == 1){
                for(var j=0;j<areaList.length-2;j++){
                    $.extend(item,$.parseJSON("{\""+ areaList[j+2].field +"\":"+next48h+"}"));
                }
            } else if(i == 2){
                for(var j=0;j<areaList.length-2;j++){
                    $.extend(item,$.parseJSON("{\""+ areaList[j+2].field +"\":"+next72h+"}"));
                }
            } else {
                for(var j=0;j<areaList.length-2;j++){
                    $.extend(item,$.parseJSON("{\""+ areaList[j+2].field +"\":"+next3d+"}"));
                }
            }
            rowData.push(item);
        }
        return rowData;

    }

    function loadFutureWater() {

        var startTime = $('#f-start-time-value').val();
        var endTime = $('#f-end-time-value').val();
        var rows = dayBetwennTwoDate(startTime,endTime);
        var rowData = [];
        var value = 0;
        for(var i=0;i<rows.length;i++){
            var item = {"id":i+1,"time":rows[i]};
            var titleList = title.area;
            for(var j=0;j<titleList.length-2;j++){
                $.extend(item,$.parseJSON("{\""+ titleList[j+2].field +"\":"+ value +"}"));
            }
            rowData.push(item);
        }
        return rowData;

    }

    // 返回两个时间段每天日期的数组
    function dayBetwennTwoDate(start,end){
        // start = start.replace(new RegExp("-","gm"),"/") + " 00:00:00";
        // end = end.replace(new RegExp("-","gm"),"/") + " 00:00:00";
        var startMSeconds = Date.parse(new Date(start));
        var endMSeconds = Date.parse(new Date(end));
        var result = [];
        while(startMSeconds <= endMSeconds){
            result.push(new Date(startMSeconds).format("yyyy-MM-dd"));
            startMSeconds += 24 * 3600 * 1000;
        }
        return result;
    }

    // 获取基于某一天的之前或者之后的时间
    function getFormatDate(start,days){
        var startMSeconds = Date.parse(start);
        var resultMSeconds = startMSeconds + days * (24 * 3600 * 1000);
        return new Date(resultMSeconds);
    }

    // 时间格式化函数（可放在一个公共的js中）
    Date.prototype.format = function(format) {
        var date = {
            "M+": this.getMonth() + 1,
            "d+": this.getDate(),
            "h+": this.getHours(),
            "m+": this.getMinutes(),
            "s+": this.getSeconds(),
            "q+": Math.floor((this.getMonth() + 3) / 3),
            "S+": this.getMilliseconds()
        };
        if (/(y+)/i.test(format)) {
            format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
        }
        for (var k in date) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1
                    ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
            }
        }
        return format;
    }

});



