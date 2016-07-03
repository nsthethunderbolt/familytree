<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Family Tree</title>
</head>
<body>
hi
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.1/jquery.min.js"></script>
<script type="text/javascript">
    $(function () {
        console.log("page loaded");
        var arr = {};
        arr["id"] = 2;
        $.ajax({
            url: "http://localhost:8080/getTree",
            //data:JSON.stringify(arr),
            contentType: "application/json",
            datatype: 'json',
            //type:"post",
            success: function (resp) {
                console.log("response recieved");
                console.log(resp);
                printTree(resp, -1);
            }
        });
        function sop(name, l) {
            for (var i = 0; i < l; i++) {
                console.log("->");
            }
            console.log(name);
        }

        function printTree(obj, l) {

            l++;

            for (var i = 0; i < obj.children.length; i++) {
                var m = obj.children[i];
                console.log("\n");
                sop(m.nickName, l);
                if (m.spouse)
                    console.log("-" + m.spouse.nickname);
                if (m.children && m.children.length > 0) {
                    printTree(m, l);
                }

            }

        }

    });
</script>
</body>
</html>