<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="counseling.Counselor, counseling.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상담사 목록</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
 integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<style>
    .card-img-top {
        object-fit: cover;
        height: 300px;
    }
    .card {
        height: 100%;
        display: flex;
        flex-direction: column;
    }
    .card-body {
        flex-grow: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }
</style>
<script>
function checkLoginAndRedirect(counselorId) {
    <c:if test="${sessionScope.userId == null}">
        alert("상담 신청을 위해 로그인이 필요합니다.");
        window.location.href = "${pageContext.request.contextPath}/control?action=login&redirect=makeReservation&counselorId=" + counselorId;
    </c:if>
    <c:if test="${sessionScope.userId != null}">
        window.location.href = "${pageContext.request.contextPath}/control?action=makeReservation&counselorId=" + counselorId;
    </c:if>
}
</script>
</head>
<body>
<jsp:include page="./menu.jsp"/>
<div class="container mt-5">
    <h2 class="text-center mb-4">상담사 목록</h2>
    <div class="row">
        <c:forEach var="counselor" items="${counselors}">
            <div class="col-md-4">
                <div class="card mb-4">
                    <img src="${counselor.photo}" class="card-img-top" alt="Counselor Photo">
                    <div class="card-body">
                        <h5 class="card-title">${counselor.user.name}</h5>
                        <p class="card-text">
                            생년: <c:out value="${counselor.user.birthDate.toString().substring(0, 4)}"/><br>
                            성별: <c:out value="${counselor.gender}"/><br>
                            전문 분야: <c:out value="${counselor.specialty}"/><br>
                            주소: <c:out value="${counselor.user.address}"/>
                        </p>
                        <button class="btn btn-primary mt-auto" onclick="checkLoginAndRedirect(${counselor.id})">상담 신청</button>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
