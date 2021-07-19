<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Rooms info</title>
    <style type="text/css">
        TABLE {
            border-collapse: collapse;
        }
        TD, TH {
            padding: 3px;
            border: 1px solid black;
        }
        TH {
            background: #b0e0e6;
        }
    </style>
</head>
<body>
<h2>Room(s)</h2>
<br>
<table>
    <tr>
        <th>Id</th>
        <th>Renovation</th>
        <th>Price</th>
    </tr>
    <c:forEach var="room" items="${rooms}">
        <tr>
            <td>${room.id}</td>
            <td>${room.underRenovation}</td>
            <td>${room.price}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
