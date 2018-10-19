<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>home</title>
</head>
<body>

 <pre>
     hello world!
        ${value1}
        <#-- ${value2}  #注释:  如果不存在变量，强制为空 -->
        ${value2 !'我是默认值'}
        $!{value3}

    <#-- 循环语句-->
     <#list colors as color>
     ${color}
     </#list>

     <#list map?keys as key>
         key:${key}:value:${map[key]}
     </#list>

      <#list map?keys as key>
          key:${key}:value:${map[key]}
      </#list>

     username:${user.getUsername()}
     username:${user.username}

     <#include "head.html" />
</pre>


</body>
</html>