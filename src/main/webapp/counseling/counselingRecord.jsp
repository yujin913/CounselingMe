<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="counseling.CounselingRecord" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상담 기록</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
 integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<jsp:include page="./menu.jsp"/>
<div class="container mt-5">
    <h2 class="text-center mb-4">상담 기록</h2>
    <form action="${pageContext.request.contextPath}/control?action=saveCounselingRecord" method="post">
        <input type="hidden" name="reservationId" value="<%= request.getAttribute("reservationId") %>">
        <div class="mb-3">
            <label for="counselingContent" class="form-label">상담 내용</label>
            <textarea class="form-control" id="counselingContent" name="counselingContent" rows="10"><%= (request.getAttribute("counselingRecord") != null) ? ((CounselingRecord) request.getAttribute("counselingRecord")).getContent() : "" %></textarea>
        </div>
        <div class="d-grid">
            <button type="submit" class="btn btn-primary">저장</button>
        </div>
        <div class="d-grid mt-2">
            <a href="${pageContext.request.contextPath}/control?action=counselingManagement" class="btn btn-secondary">취소</a>
        </div>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous"></script>
</body>
</html>
