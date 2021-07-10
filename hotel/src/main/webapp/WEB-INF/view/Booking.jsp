<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bookings info</title>
    <style type="text/css"> /* знаю что стили в хедере это фу фу, но ведь не о том урок) */
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
    <h2>Booking(s)</h2>
    <br>
    <table>
        <tr>
            <th>Id</th>
            <th>Room id</th>
            <th>User id</th>
            <th>Start date</th>
            <th>End date</th>
        </tr>
        <c:forEach var="booking" items="${bookings}">
            <tr>
                <td>${booking.id}</td>
                <td>${booking.room.id}</td>
                <td>${booking.user.id}</td>
                <td>${booking.startDate}</td>
                <td>${booking.endDate}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
