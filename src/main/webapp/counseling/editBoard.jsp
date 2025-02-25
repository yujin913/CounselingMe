<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="counseling.Board" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시물 수정</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>
<body>
<jsp:include page="./menu.jsp"/>
<div class="container mt-5">
    <%
        Board board = (Board) request.getAttribute("board");
    %>
    <h2 class="text-center mb-4">게시물 수정</h2>
    <form action="${pageContext.request.contextPath}/control?action=updateBoard" method="post">
        <input type="hidden" name="id" value="<%= board.getId() %>">
        <div class="mb-3">
            <label for="title" class="form-label">제목</label>
            <input type="text" class="form-control" id="title" name="title" value="<%= board.getTitle() %>" required>
        </div>
        <div class="mb-3">
            <label for="content" class="form-label">내용</label>
            <textarea class="form-control" id="content" name="content" rows="10" required><%= board.getContent() %></textarea>
        </div>
        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
            <button type="submit" class="btn btn-primary">수정</button>
            <a href="${pageContext.request.contextPath}/control?action=viewBoard&id=<%= board.getId() %>" class="btn btn-secondary">취소</a>
        </div>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
        crossorigin="anonymous"></script>
</body>
</html>
