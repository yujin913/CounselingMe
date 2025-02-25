<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="counseling.Board"%>
<%@ page import="counseling.BoardComment"%>
<%@ page import="java.util.List"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.time.LocalDateTime"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시물 상세</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <style>
        .no-link-style {
            color: inherit;
            text-decoration: none;
        }
        .no-link-style:hover {
            color: inherit;
            text-decoration: none;
        }
        .btn-small {
            padding: 0.25rem 0.5rem;
            font-size: 0.875rem;
        }
        .content-container {
            min-height: 300px;
            border: 1px solid #dee2e6;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 0.5rem;
        }
        .rounded-container {
            border-radius: 0.5rem;
            overflow: hidden;
            border: 1px solid #dee2e6;
            background-color: #ffffff;
        }
        .comment-container {
            margin-bottom: 1rem;
            padding: 10px;
            min-height: 50px;
        }
        .comment-form {
            display: flex;
            align-items: center;
            margin-bottom: 1rem;
        }
        .comment-form input {
            flex-grow: 1;
            margin-right: 0.5rem;
            height: 50px;
        }
        .comment-form button {
            flex-shrink: 0;
            height: 50px;
        }
        .comment-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
    </style>
</head>
<body>
<%
    Integer sessionUserId = (Integer) session.getAttribute("userId");
    if (sessionUserId == null) {
        response.sendRedirect(request.getContextPath() + "/control?action=login");
        return;
    }
%>
<jsp:include page="./menu.jsp"/>
<div class="container mt-5">
    <% if (request.getAttribute("alertMessage") != null) { %>
        <div class="alert alert-warning alert-dismissible fade show mt-3" role="alert">
            <%= request.getAttribute("alertMessage") %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>
    <%
        Board board = (Board) request.getAttribute("board");
        String formattedDate = "";
        if (board != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            formattedDate = board.getCreatedAt().toLocalDateTime().format(formatter);
        }
    %>
    <h2 class="text-center mb-4"><%= board.getTitle() %></h2>
    <div class="mb-3 text-center">
        <span>작성자: <%= board.getUserName() %></span> | 
        <span>등록일: <%= formattedDate %></span> | 
        <span>조회수: <%= board.getViewCount() %></span>
    </div>
    <div class="content-container">
        <p><%= board.getContent() %></p>
    </div>
    <div class="d-flex justify-content-between mt-4">
        <a href="${pageContext.request.contextPath}/control?action=boardList" class="btn btn-secondary btn-small">목록</a>
        <% if (board.getUserId() == sessionUserId || sessionUserId == 0) { %>
            <div class="d-flex">
                <a href="${pageContext.request.contextPath}/control?action=editBoard&id=<%= board.getId() %>" class="btn btn-primary btn-small me-2">수정</a>
                <form action="${pageContext.request.contextPath}/control?action=deleteBoard" method="post" onsubmit="return confirm('정말로 이 게시물을 삭제하시겠습니까?');">
                    <input type="hidden" name="boardId" value="<%= board.getId() %>">
                    <button type="submit" class="btn btn-danger btn-small">삭제</button>
                </form>
            </div>
        <% } %>
    </div>
    <div class="mt-4">
        <h5>댓글</h5>
        <form action="${pageContext.request.contextPath}/control?action=addBoardComment" method="post" class="comment-form">
            <input type="hidden" name="boardId" value="<%= board.getId() %>">
            <input type="text" name="content" class="form-control" placeholder="댓글을 입력하세요" required>
            <button type="submit" class="btn btn-primary btn-small">등록</button>
        </form>
        <%
            List<BoardComment> comments = (List<BoardComment>) request.getAttribute("comments");
            if (comments != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                for (BoardComment comment : comments) {
                    String commentFormattedDate = comment.getCreatedAt().toLocalDateTime().format(formatter);
        %>
                    <div class="rounded-container p-3 comment-container">
                        <div class="comment-header">
                            <div>
                                <strong><%= comment.getUserName() %></strong> <small><%= commentFormattedDate %></small>
                            </div>
                            <div>
                                <% if (comment.getUserId() == sessionUserId || sessionUserId == 0) { %>
                                    <form action="${pageContext.request.contextPath}/control?action=deleteBoardComment" method="post" onsubmit="return confirm('정말로 이 댓글을 삭제하시겠습니까?');" style="display:inline;">
                                        <input type="hidden" name="commentId" value="<%= comment.getId() %>">
                                        <input type="hidden" name="boardId" value="<%= board.getId() %>">
                                        <button type="submit" class="btn btn-danger btn-sm">삭제</button>
                                    </form>
                                <% } %>
                            </div>
                        </div>
                        <div><%= comment.getContent() %></div>
                    </div>
        <%
                }
            }
        %>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous">
</script>
</body>
</html>
