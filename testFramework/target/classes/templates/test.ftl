<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>测试模板</title>
</head>
<body>
<h2>list指令的使用</h2>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>金额</td>
    </tr>
    <#list friends as stu>
        <tr>
            <td>${stu_index + 1}</td>
            <td>${stu.name}</td>
            <td>${stu.age}</td>
            <td>${stu.money}</td>
        </tr>
    </#list>
</table>
<h2>遍历map数据</h2>
第一个朋友的姓名：${stuMap['one'].name}
第一个朋友的姓名：${stuMap.one.name}
遍历
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>金额</td>
    </tr>
    <#list stuMap?keys as stu>
        <tr>
            <td>${stu_index + 1}</td>
            <td <#if stuMap[stu].name=='小李'>style="background: brown" </#if>>${stuMap[stu].name}</td>
            <td>${stuMap[stu].age}</td>
            <td>${stuMap[stu].money}</td>
        </tr>
    </#list>
</table>

<h2>空值的处理</h2>
生日：<#if fri1.birthday??>111</#if>
<h2>内建函数</h2>
${student.birthday?date}
</body>
</html>