<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="counseling.Reservation" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상담 관리</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
 integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<jsp:include page="./menu.jsp"/>
<div class="container mt-5">
    <h2 class="text-center mb-4">상담 관리</h2>
    <div class="table-responsive">
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>피상담자 이름</th>
                    <th>상담 날짜</th>
                    <th>시작 시간</th>
                    <th>종료 시간</th>
                    <th>상태</th>
                    <th>기록</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Reservation> completedReservations = (List<Reservation>) request.getAttribute("completedReservations");
                    if (completedReservations != null) {
                        for (Reservation reservation : completedReservations) {
                %>
                    <tr>
                        <td><%= reservation.getUserName() %></td>
                        <td><%= reservation.getReservationDate() %></td>
                        <td><%= reservation.getStartTime() %></td>
                        <td><%= reservation.getEndTime() %></td>
                        <td><%= reservation.getStatus() %></td>
                        <td>
                            <a href="<%= request.getContextPath() %>/control?action=counselingRecord&reservationId=<%= reservation.getId() %>" class="btn btn-primary btn-sm">기록</a>
                        </td>
                    </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous"></script>
</body>
</html>
