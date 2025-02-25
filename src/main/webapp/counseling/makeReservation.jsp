<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상담 예약</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script>
    function submitForm() {
        document.getElementById('reservationForm').submit();
    }
</script>
</head>
<body>
<jsp:include page="./menu.jsp"/>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <h2 class="text-center mb-4">상담 예약</h2>
            <form id="reservationForm" action="${pageContext.request.contextPath}/control" method="get">
                <input type="hidden" name="action" value="makeReservation">
                <input type="hidden" name="counselorId" value="${param.counselorId}">
                <div class="mb-3">
                    <label for="counselorName" class="form-label">상담사 이름</label>
                    <input type="text" class="form-control" id="counselorName" name="counselorName" value="${counselorName}" readonly style="background-color: #e9ecef;">
                </div>
                <div class="mb-3">
                    <label for="date" class="form-label">상담 날짜</label>
                    <input type="date" class="form-control" id="date" name="date" onchange="submitForm()" value="${selectedDate}">
                </div>
            </form>
            <form action="${pageContext.request.contextPath}/control?action=processMakeReservation" method="post">
                <input type="hidden" name="counselorId" value="${param.counselorId}">
                <input type="hidden" name="date" value="${selectedDate}">
                <input type="hidden" name="userId" value="${sessionScope.userId}">
                <div class="mb-3">
                    <label class="form-label">상담 시간대</label><br>
                    <c:set var="timeSlots" value="${'morning,afternoon,evening'}"/>
                    <div class="btn-group d-flex flex-wrap" role="group" aria-label="Time slots">
                        <c:forEach var="slot" items="${timeSlots}">
                            <c:set var="disabled" value="${reservedSlots.contains(slot) ? 'disabled' : ''}"/>
                            <input type="radio" class="btn-check" name="timeSlot" id="slot-${slot}" value="${slot}" ${disabled}>
                            <label class="btn btn-outline-primary flex-fill mb-2" for="slot-${slot}">
                                <c:choose>
                                    <c:when test="${slot == 'morning'}">오전 (10:00 ~ 12:00)</c:when>
                                    <c:when test="${slot == 'afternoon'}">오후 (13:00 ~ 15:00)</c:when>
                                    <c:when test="${slot == 'evening'}">저녁 (15:00 ~ 17:00)</c:when>
                                </c:choose>
                            </label>
                        </c:forEach>
                    </div>
                </div>
                <div class="d-grid">
                    <button type="submit" class="btn btn-primary">상담 예약</button>
                </div>
                <div class="d-grid mt-2">
                    <a href="${pageContext.request.contextPath}/control?action=myPage" class="btn btn-secondary">취소</a>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
