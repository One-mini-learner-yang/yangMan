<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh_CN">
<%@include file="/WEB-INF/include-head.jsp"%>
<link rel="stylesheet" href="css/pagination.css"/>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<link rel="stylesheet" href="ztree/zTreeStyle.css"/>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
    $(function () {
        window.pageNum=1;
        window.pageSize=6;
        window.keyWord="";
        generatePage();

        $("#searchBtn").click(function(){
            window.keyWord=$("#keyWordInput").val();
            generatePage();
        });
        $("#showAddModalBtn").click(function () {
            $("#addModal").modal("show");
        });
        $("#saveRoleBtn").click(function () {
            var roleName=$.trim($("#addRoleName").val());
            $.ajax({
                "url": "role/save.json",
                "type": "post",
                "data": {
                    "name":roleName
                },
                "async": false,
                "dataType": "json",
                "success":function(response){
                    var result=response.result;
                    if (result=="SUCCESS"){
                        layer.msg("操作成功");
                        window.pageNum=99999999;
                        generatePage();
                    }
                    if (result=="FAILED"){
                        layer.msg("操作失败");
                    }
                },
                "error":function (response) {
                    layer.msg(response.status+" "+response.statusText);
                }
            });
            $("#addRoleName").val("");
            $("#addModal").modal("hide");
        });
        $("#rolePageBody").on("click",".pencilBtn",function () {
            $("#editModal").modal("show");
            var roleName=$(this).parent().prev().text();
            window.roleId=this.id;
            $("#editRoleName").val(roleName);
        });
        $("#updateRoleBtn").click(function () {
            var roleName=$("#editRoleName").val();
            $.ajax({
                "url": "role/update.json",
                "type": "post",
                "data": {
                    "id":window.roleId,
                    "name":roleName
                },
                "async": false,
                "dataType": "json",
                "success":function(response){
                    var result=response.result;
                    if (result=="SUCCESS"){
                        layer.msg("操作成功");
                        $("#editModal").modal("hide");
                        window.pageNum=99999999;
                        generatePage();
                    }
                    if (result=="FAILED"){
                        layer.msg("操作失败");
                    }
                },
                "error":function (response) {
                    $("#editModal").modal("hide");
                    layer.msg(response.status+" "+response.statusText);
                }
            });

        });
        $("#removeRoleBtn").click(function () {
            var requestBody=JSON.stringify(window.roleIdArray);
            $.ajax({
                "url": "role/delete/by/role/id/array.json",
                "type": "post",
                "data":requestBody,
                "contentType":"application/json;charset=UTF-8",
                "dataType": "json",
                "success":function(response){
                    var result=response.result;
                    if (result=="SUCCESS"){
                        layer.msg("操作成功");
                        $("#confirmModal").modal("hide");
                        generatePage();
                    }
                    if (result=="FAILED"){
                        layer.msg("操作失败");
                    }
                },
                "error":function (response) {
                    $("#confirmModal").modal("hide");
                    layer.msg(response.status+" "+response.statusText);
                }
            });
        });
        $("#assignBtn").click(function () {
            var authArray=[];
            var zTreeObj=$.fn.zTree.getZTreeObj("authTreeDemo");
            var checkedNodes=zTreeObj.getCheckedNodes();
            for(var i=0;i<checkedNodes.length;i++){
                var checkedNode=checkedNodes[i];
                var authId=checkedNode.id;
                authArray.push(authId);
            }
            var requestBody={
                "authIdArray":authArray,
                "roleId":[window.roleId]
            };
            requestBody=JSON.stringify(requestBody);
            $.ajax({
                "url":"assign/do/role/assign/auth.json",
                "type":"post",
                "data":requestBody,
                "contentType":"application/json;charset=UTF-8",
                "dataType":"json",
                "success":function (response) {
                    var result=response.result;
                    if (result=="SUCCESS"){
                        layer.msg("操作成功！");
                    }
                    if (result=="FAIL"){
                        layer.msg("操作失败"+response.message);
                    }
                },
                "error":function (response) {
                    layer.msg(response.status+" "+response.statusText)
                }
            });
            $("#assignModal").modal("hide");
        });
        $("#rolePageBody").on("click",".removeBtn",function(){
            var roleName=$(this).parent().prev().text();
            var roleArray=[{
                roleId:this.id,
                roleName:roleName
            }];
            showConfirmModal(roleArray);
        });
        $("#rolePageBody").on("click",".checkBtn",function(){
            window.roleId=this.id;
            $("#assignModal").modal("show");
            fillAuthTree();
        });
        $("#summaryBox").click(function () {
            var currentStatus=this.checked;
            $(".itemBox").prop("checked",currentStatus);
        });
        $("#rolePageBody").on("click",".itemBox",function () {
            var checkedBoxCount=$(".itemBox:checked").length;
            var totalCount=$(".itemBox").length;
            $("#summaryBox").prop("checked",checkedBoxCount==totalCount);
        });
        $("#batchRemoveBtn").click(function () {
            var roleArray = [];
            //遍历
            $(".itemBox:checked").each(function () {
                var roleId = this.id;
                var roleName = $(this).parent().next().text();

                roleArray.push({
                    roleId: roleId,
                    roleName: roleName
                });
            })
            if (roleArray.length == 0) {
                layer.msg("请选择一个角色");
                return;
            }
            if (roleArray.length == 5) {
                $("#summaryBox").prop("checked", false);
            }
            showConfirmModal(roleArray);
        });
    });
    function showConfirmModal(roleArray) {
        $("#confirmModal").modal("show");
        $("#roleNameDiv").empty();
        window.roleIdArray=[];
        //。length没写 记得教训
        for (var i=0;i<roleArray.length;i++){
            var role=roleArray[i];
            var roleName = role.roleName;
            $("#roleNameDiv").append(roleName+"<br/>");
            var roleId = role.roleId;
            window.roleIdArray.push(roleId);
        }
    }
</script>
<script type="text/javascript">
    //执行分页，生成页面效果
    function generatePage() {
        // 1.获取数据
        var pageInfo=getPageInfoRemote();
        // 2.填充表格
        fillTableBody(pageInfo);
    }
    function fillAuthTree(){
        var ajaxReturn = $.ajax({
            "url":"assign/get/all/auth.json",
            "type":"post",
            "dataType":"json",
            "async":false,

        });
        if (ajaxReturn.status!=200){
            layer.msg("出错，"+ajaxReturn.status+" "+ajaxReturn.statusText);
            return;
        }
        var authList=ajaxReturn.responseJSON.data;
        // 创建json对象存储zTree所做的设置
        // 交给ztree组装
        var setting = {
            "data": {
                "simpleData": {
                    //开启简单json功能
                    "enable": true,
                    //设置父节点
                    // "pIdKey": "categoryId"
                },
                "key": {
                    //显示的名
                    "name": "title"
                }
            },
            "check":{
                "enable":true

            }
        };

        // 生成初始化树结构
        // ,不要写成.
        $.fn.zTree.init($("#authTreeDemo"),setting,authList);
        //默认节点展开
        var zTreeObj=$.fn.zTree.getZTreeObj("authTreeDemo");
        zTreeObj.expandAll(true);
        // 查询已分配的Auth的id组成的数组
        ajaxReturn=$.ajax({
            "url":"assign/get/assign/auth/id/by/role/id.json",
            "type":"post",
            "data":{
                "roleId":window.roleId
            },
            "dataType":"json",
            "async":false
        });
        if (ajaxReturn.status!=200){
            layer.msg("出错，"+ajaxReturn.status+" "+ajaxReturn.statusText)
            return;
        }
        // 从响应结果中获取authArray
        var authArray=ajaxReturn.responseJSON.data;
        //alert(authArray);
        // 把对应的树形结构勾选
        // 遍历一下数组
        for (var i=0;i<authArray.length;i++){
            var authId=authArray[i];
            // 根据id查询树形结构中的对应节点
            var treeNode = zTreeObj.getNodeByParam("id",authId);
            console.log(treeNode);
            // 将treeNode设置为被勾选
            var checked=true;//节点勾选
            var checkTypeFlag=false;//不联动，避免父节点勾选，回显多选内容
            zTreeObj.checkNode(treeNode,checked,checkTypeFlag);
        }

    };
    function getPageInfoRemote() {
        // 调用$.ajax()函数发送请求并接受$.ajax()函数的返回值
        var ajaxResult = $.ajax({
            "url": "role/get/page/info.json",
            "type": "post",
            "data": {
                "pageNum": window.pageNum,
                "pageSize": window.pageSize,
                "keyWord": window.keyWord
            },
            "async": false,
            "dataType": "json"
        });
        console.log(ajaxResult);
        // 判断当前响应状态码是否为 200
        var statusCode = ajaxResult.status;
        // 如果当前响应状态码不是 200，说明发生了错误或其他意外情况，显示提示消息，让 当前函数停止执行
        if (statusCode != 200) {
            layer.msg("失败！响应状态码=" + statusCode + " 说明信息=" + ajaxResult.statusText);
            return null;
        }
        console.log(ajaxResult);
        // 如果响应状态码是 200，说明请求处理成功，获取pageInfo
        var resultEntity = ajaxResult.responseJSON;
        // 从 resultEntity 中获取 result 属性
        var result = resultEntity.result;
        // 判断 result 是否成功
        if (result == "FAILED") {
            layer.msg(resultEntity.message);
            return null;
        }
        // 确认 result 为成功后获取
        pageInfo
        var pageInfo = resultEntity.data;
        // 返回 pageInfo

        return pageInfo;

    }

    function fillTableBody(pageInfo) {
        // 清除 tbody 中的旧的内容
        $("#rolePageBody").empty();
        // 这里清空是为了让没有搜索结果时不显示页码导航条
        $("#Pagination").empty();
        // 判断 pageInfo 对象是否有效
        if (pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length == 0) {
            $("#rolePageBody").append("<tr><td colspan='4' align='center'>抱歉！没有查询到您搜 索的数据！</td></tr>");
            return;
        }
        // 使用 pageInfo 的 list 属性填充 tbody
        for(var i = 0; i < pageInfo.list.length; i++) {
            var role = pageInfo.list[i];
            var roleId = role.id;
            var roleName = role.name;
            var numberTd = "<td>" + (i + 1) + "</td>";
            var checkboxTd = "<td><input id='"+roleId+"' class='itemBox' type='checkbox'></td>";
            var roleNameTd = "<td>" + roleName + "</td>";
            var checkBtn = "<button id='"+roleId+"' type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>";

            var pencilBtn = "<button id='"+roleId+"' type='button' class='btn btn-primary btn-xs pencilBtn'><i class=' glyphicon glyphicon-pencil '></i></button>";
            var removeBtn = "<button id='"+roleId+"' type='button' class='btn btn-danger btn-xs removeBtn'><i class=' glyphicon glyphicon-remove '></i></button>";
            var buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>";
            var tr = "<tr>" + numberTd + checkboxTd + roleNameTd + buttonTd + "</tr>";
            $("#rolePageBody").append(tr);
        }

// 生成分页导航条
        generateNavigator(pageInfo);


    }
    //生成分页页码导航条
    function generateNavigator(pageInfo) {
        //总记录数
        var totalRecord=pageInfo.total;
        var properties = {
            num_edge_entries: 3, //边缘页数
            num_display_entries: 5, //主体页数
            callback: paginationCallBack,
            items_per_page:pageInfo.pageSize, //每页显示1项
            current_page: pageInfo.pageNum-1,//Pagination内部使用pageIndex来管理页面
            prev_text:"上一页",
            next_text:"下一页"
        }
        $("#Pagination").pagination(totalRecord,properties)
    }
    function paginationCallBack(pageIndex,jQuery) {
        //根据pageIndex计算pageNum
        window.pageNum = pageIndex+1;
        //调用分页函数
        generatePage();
        //由于每个按钮都是超链接，所以这里取消超链接的默认行为
        return false;
    }
</script>
<body>
    <%@include file="/WEB-INF/include-nav.jsp"%>
    <div class="container-fluid">
        <div class="row">
            <%@include file="/WEB-INF/include-sidebar.jsp"%>
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                    </div>
                    <div class="panel-body">
                        <form class="form-inline" role="form" style="float:left;">
                            <div class="form-group has-feedback">
                                <div class="input-group">
                                    <div class="input-group-addon">查询条件</div>
                                    <input id="keyWordInput" class="form-control has-success" type="text" placeholder="请输入查询条件">
                                </div>
                            </div>
                            <button id="searchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                        </form>
                        <button id="batchRemoveBtn" type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                        <button id="showAddModalBtn" type="button" class="btn btn-primary" style="float:right;"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                        <br>
                        <hr style="clear:both;">
                        <div class="table-responsive">
                            <table class="table  table-bordered">
                                <thead>
                                <tr>
                                    <th width="30">#</th>
                                    <th width="30"><input id="summaryBox" type="checkbox"></th>
                                    <th>名称</th>
                                    <th width="100">操作</th>
                                </tr>
                                </thead>
                                <tbody id="rolePageBody"></tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="6" align="center">
                                            <div id="Pagination" class="pagination"></div>
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<%@include file="/WEB-INF/modal-role-add.jsp"%>
<%@include file="/WEB-INF/modal-role-edit.jsp"%>
<%@include file="/WEB-INF/modal-role-confirm.jsp"%>
<%@include file="modal-role-assign-auth.jsp"%>
</body>
</html>


