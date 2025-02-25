<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 가입 페이지</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
 integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>

<jsp:include page="./menu.jsp"/>

<div class="container mt-5">
    <% if (request.getParameter("error") != null) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <% String error = request.getParameter("error");
               if ("passwordMismatch".equals(error)) { %>
                비밀번호가 일치하지 않습니다.
            <% } else if ("emailExists".equals(error)) { %>
                이미 존재하는 이메일입니다.
            <% } else { %>
                알 수 없는 오류가 발생했습니다.
            <% } %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>
    <form action="${pageContext.request.contextPath}/control?action=processJoin" method="post">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-body">
                        <h2 class="text-center mb-4">회원가입</h2>
                        <div class="mb-3">
                            <label for="email" class="form-label">이메일</label>
                            <input type="email" class="form-control" id="email" name="email" required autofocus>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">패스워드</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <div class="mb-3">
                            <label for="passwordConfirm" class="form-label">패스워드 확인</label>
                            <input type="password" class="form-control" id="passwordConfirm" name="passwordConfirm" required>
                        </div>
                        <div class="mb-3">
                            <label for="name" class="form-label">이름</label>
                            <input type="text" class="form-control" id="name" name="name" required>
                        </div>
                        <div class="mb-3">
                            <label for="phone" class="form-label">전화번호</label>
                            <input type="text" class="form-control" id="phone" name="phone">
                        </div>
                        <div class="mb-3">
                            <label for="address" class="form-label">주소</label>
                            <input type="text" class="form-control" id="address" name="address">
                        </div>
                        <div class="mb-3">
                            <label for="dob" class="form-label">생년월일</label>
                            <input type="date" class="form-control" id="dob" name="dob" required>
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">회원가입하기</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
