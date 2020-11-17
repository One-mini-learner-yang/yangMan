<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/11/5 0005
  Time: 20:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
        pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp"%>
<link rel="stylesheet" href="ztree/zTreeStyle.css"/>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowd/my-menu.js"></script>
<script type="text/javascript">
    $(function () {
        generateTree();
        $("#treeDemo").on("click",".addBtn",function () {
            window.pid=this.id;
            $("#menuAddModal").modal("show");
            return false;
        });
        $("#treeDemo").on("click",".editBtn",function () {
            window.id=this.id;
            $("#menuEditModal").modal("show");
            var zTreeObj=$.fn.zTree.getZTreeObj("treeDemo");
            var key="id";
            var value=window.id;
            var currentNode=zTreeObj.getNodeByParam(key,value);
            $("#editMenuName").val(currentNode.name);
            $("#editMenuUrl").val(currentNode.url);
            $("#menuEditModal [name=icon]").val([currentNode.icon]);
            return false;
        });
        $("#treeDemo").on("click",".removeBtn",function () {
            window.id=this.id;
            $("#menuConfirmModal").modal("show");
            var zTreeObj=$.fn.zTree.getZTreeObj("treeDemo");
            var key="id";
            var value=window.id;
            var currentNode=zTreeObj.getNodeByParam(key,value);
            console.log(currentNode);
            $("#removeNodeSpan").html("【<i class='" + currentNode.icon + "'></i>" + currentNode.name + "】");
            return false;
        });
        $("#menuSaveBtn").click(function () {
            var name=$("#menuName").val();
            var url=$("#menuUrl").val();
            var icon=$("#menuAddModal [name=icon]:checked").val();
            $.ajax({
                "url":"menu/save.json",
                "type":"post",
                "data":{
                    "pid":window.pid,
                    "name":name,
                    "url":url,
                    "icon":icon
                },
                "dataType":"json",
                "success":function (response) {
                    var result=response.result;
                    if (result=="SUCCESS"){
                        layer.msg("操作成功！");
                        $("#menuResetBtn").click();
                        $("#menuAddModal").modal("hide");
                        generateTree();
                    }
                    if(result == "FAILED") {
                        layer.msg("操作失败！"+response.message);
                        $("#menuResetBtn").click();
                        $("#menuAddModal").modal("hide");
                    }
                },
                "error":function(response) {
                    layer.msg(response.status+" "+response.statusText);
                    $("#menuResetBtn").click();
                    $("#menuAddModal").modal("hide");
                }
            });
        });
        $("#menuEditBtn").click(function () {
            var name=$("#editMenuName").val();
            var url=$("#editMenuUrl").val();
            var icon=$("#menuEditModal [name=icon]:checked").val();
            $.ajax({
                "url":"menu/update.json",
                "type":"post",
                "data":{
                    "id":window.id,
                    "name":name,
                    "url":url,
                    "icon":icon
                },
                "dataType":"json",
                "success":function (response) {
                    var result=response.result;
                    if (result=="SUCCESS"){
                        layer.msg("操作成功！");
                        $("#menuEditModal").modal("hide");
                        generateTree();
                    }
                    if(result == "FAILED") {
                        layer.msg("操作失败！"+response.message);
                        $("#menuEditModal").modal("hide");
                    }
                },
                "error":function(response) {
                    layer.msg(response.status+" "+response.statusText);
                    $("#menuEditModal").modal("hide");
                }
            });
        });
        $("#confirmBtn").click(function () {
            $.ajax({
                "url":"menu/remove.json",
                "type":"post",
                "data":{
                    "id":window.id
                },
                "dataType":"json",
                "success":function (response) {
                    var result=response.result;
                    if (result=="SUCCESS"){
                        layer.msg("操作成功！");
                        $("#menuConfirmModal").modal("hide");
                        generateTree();
                    }
                    if(result == "FAILED") {
                        layer.msg("操作失败！"+response.message);
                        $("#menuConfirmModal").modal("hide");
                    }
                },
                "error":function(response) {
                    layer.msg(response.status+" "+response.statusText);
                    $("#menuConfirmModal").modal("hide");
                }
            });
        });
    });
</script>
<body>
    <%@include file="/WEB-INF/include-nav.jsp"%>
    <div class="container-fluid">
        <div class="row">
            <%@include file="/WEB-INF/include-sidebar.jsp"%>
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                        <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal">
                            <i class="glyphicon glyphicon-question-sign"></i>
                        </div>
                    </div>
                    <div class="panel-body">
                        <%--ztree动态节点依附的静态节点--%>
                        <ul id="treeDemo" class="ztree"></ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
<%@include file="/WEB-INF/modal-menu-add.jsp"%>
<%@include file="/WEB-INF/modal-menu-edit.jsp"%>
<%@include file="/WEB-INF/modal-menu-confirm.jsp"%>
</body>
</html>
