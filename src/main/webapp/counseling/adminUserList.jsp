<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>사용자 목록</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<jsp:include page="./menu.jsp"/>
<div class="container mt-5">
    <h2 class="text-center mb-4">사용자 목록</h2>

    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <table class="table table-bordered">
        <thead>
        <tr>
            <th>ID</th>
            <th>이메일</th>
            <th>이름</th>
            <th>전화번호</th>
            <th>주소</th>
            <th>생년월일</th>
            <th>가입일</th>
            <th>상담사 여부</th>
            <th>상담 권한 신청</th>
            <th>액션</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.id}</td>
                <td>${user.email}</td>
                <td>${user.name}</td>
                <td>${user.phoneNumber}</td>
                <td>${user.address}</td>
                <td>${user.birthDate}</td>
                <td>${user.createdAt}</td>
                <td>${user.counselor ? "예" : "아니오"}</td>
                <td>${user.counselorRequested ? "예" : "아니오"}</td>
                <td>
                    <c:if test="${!user.counselor}">
                        <form action="${pageContext.request.contextPath}/control?action=grantCounselor" method="post" style="display:inline;">
                            <input type="hidden" name="userId" value="${user.id}"/>
                            <button type="submit" class="btn btn-success btn-sm">상담 권한 부여</button>
                        </form>
                    </c:if>
                    <c:if test="${user.counselor}">
                        <form action="${pageContext.request.contextPath}/control?action=revokeCounselor" method="post" style="display:inline;">
                            <input type="hidden" name="userId" value="${user.id}"/>
                            <button type="submit" class="btn btn-danger btn-sm">상담 권한 회수</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
