//生产树形结构
function generateTree() {
    $.ajax({
        "url":"menu/get/whole/tree.json",
        "type":"post",
        "dataType":"json",
        "success":function (response) {
            var result=response.result;
            if (result=="SUCCESS"){
                var setting={
                    "view":{
                        "addDiyDom":myAddDiyDom,
                        "addHoverDom":myAddHoverDom,
                        "removeHoverDom":myRemoveHoverDom
                    },
                    "data":{
                        "url":"shabi"
                    }
                };
                var zNodes=response.data;
                $(document).zTree.init($("#treeDemo"),setting,zNodes);
            }
            if(result=="FAIL"){
                layer.msg(response.message);
            }
        }
    });
};
//鼠标移入范围是删除按钮组
function myRemoveHoverDom(treeId,treeNode) {
    var btnGroupId=treeNode.tId+"_btnGrp";
    $("#"+btnGroupId).remove()
};
//鼠标移入范围是添加按钮组
function myAddHoverDom(treeId,treeNode) {


    //定id删除的时候定位
    var btnGroupId=treeNode.tId+"_btnGrp";

    if ($("#"+btnGroupId).length>0){
        return;
    }
    //准备各个按钮的HTML标签onclick='showAddModal(this)'
    var addBtn="<a id='"+treeNode.id+"' class='addBtn btn btn-info dropdown-toggle btn-xs '  style='margin-left:10px;padding-top:0px;' href='#' title='insert'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var editBtn="<a id='"+treeNode.id+"' class='editBtn btn btn-info dropdown-toggle btn-xs ' style='margin-left:10px;padding-top:0px;' href='#' title='edit'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>"
    var removeBtn="<a id='"+treeNode.id+"' class='removeBtn btn btn-info dropdown-toggle btn-xs ' style='margin-left:10px;padding-top:0px;' href='#' title='delete'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    // 获取当前节点的级别数据
    var level=treeNode.level;
    var btnHTML="";
    //各级别不同操作，按钮的不同
    if (level ==0){

        btnHTML=addBtn;
    }
    if (level ==1){
        btnHTML=addBtn+" "+editBtn;
        var length=treeNode.children.length;
        if (length == 0){
            btnHTML=btnHTML+" "+removeBtn;
        }
    }
    if (level ==2){

        btnHTML=editBtn+" "+removeBtn;
    }
    var anchorId=treeNode.tId+"_a";
    $("#"+anchorId).after("<span id='"+btnGroupId+"'>"+btnHTML+"</span>")
};
//修改默认图标
function myAddDiyDom(treeId,treeNode){
    console.log(treeId);
    console.log(treeNode);
    //控制span标签
    var spanId=treeNode.tId+"_ico";
    //删除旧class
    //添加新的class
    $("#"+spanId)
        .removeClass()
        .addClass(treeNode.icon);
};