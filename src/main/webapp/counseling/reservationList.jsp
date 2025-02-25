<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ page import="java.util.List" %>
<%@ page import="counseling.Reservation" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>예약 내역</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
 integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-5">
    <h2 class="text-center mb-4">예약 내역</h2>
    <table class="table table-bordered">
        <thead>
            <tr>
                <th>상담사</th>
                <th>날짜</th>
                <th>시작 시간</th>
                <th>종료 시간</th>
                <th>상태</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
                if (reservations != null && !reservations.isEmpty()) {
                    for (Reservation reservation : reservations) {
                        
                        System.out.println("JSP - Reservation: " + reservation);
            %>
                        <tr>
                            <td><%= reservation.getCounselorName() %></td>
                            <td><%= reservation.getReservationDate() %></td>
                            <td><%= reservation.getStartTime() %></td>
                            <td><%= reservation.getEndTime() %></td>
                            <td><%= reservation.getStatus() %></td>
                        </tr>
            <%
                    }
                } else {
            %>
                <tr>
                    <td colspan="6" class="text-center">예약 내역이 없습니다.</td>
                </tr>
            <%
                }
            %>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous"></script>
</body>
</html>
