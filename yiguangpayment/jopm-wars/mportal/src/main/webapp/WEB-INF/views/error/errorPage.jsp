<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<title>操作失败</title>
</head>

<body>
	<h2>操作失败. [${errorMsg}]</h2>
	<p>
		<a href="${preUrl}">返回</a>
	</p>
</body>
</html>