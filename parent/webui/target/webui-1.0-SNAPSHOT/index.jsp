<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript " src="layer/layer.js"></script>
    <script type="text/javascript">
        $(function(){
            $("#btn1").click(function(){
                var array=[5,8,12];
                var requestBody=JSON.stringify(array);
                $.ajax({
                    "url":"send/array.json",
                    "type":"post",
                    "data":requestBody,
                    "contentType":"application/json;charset=UTF-8",
                    // "data":{
                    //     "array":[5,8,12]
                    // },
                    "dataType":"json",
                    "success":function(response){
                        console.log(response)
                    },
                    "error":function(response){
                        alert(response)
                        console.log("error")
                    }
                });
            })
        });
    </script>
</head>
<body>
    <a href="test/ssm.html">测试SSM整合环境</a>
    <button id="btn1">Test RequestBody</button>

</body>
</html>