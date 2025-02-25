<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="counseling.Board"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.time.LocalDateTime"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시판 목록</title>
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
        .table th:nth-child(2), .table td:nth-child(2) {
            width: 40%;
        }
        .btn-small {
            padding: 0.25rem 0.5rem;
            font-size: 0.875rem;
        }
        .rounded-table {
            border-radius: 0.5rem;
        }
    </style>
</head>
<body>
<jsp:include page="./menu.jsp"/>
<div class="container mt-5">
    <h2 class="text-center mb-4">게시판 목록</h2>
    <div class="table-responsive rounded-table">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>번호</th>
                <th>제목</th>
                <th>작성자</th>
                <th>등록일</th>
                <th>조회수</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Board> boards = (List<Board>) request.getAttribute("boards");
                if (boards != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    for (Board board : boards) {
                        String formattedDate = board.getCreatedAt().toLocalDateTime().format(formatter);
            %>
                <tr>
                    <td><%= board.getId() %></td>
                    <td><a href="<%= request.getContextPath() %>/control?action=viewBoard&id=<%= board.getId() %>" class="no-link-style"><%= board.getTitle() %></a></td>
                    <td><%= board.getUserName() %></td>
                    <td><%= formattedDate %></td>
                    <td><%= board.getViewCount() %></td>
                </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </div>
    <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-3">
        <a href="${pageContext.request.contextPath}/control?action=addBoard" class="btn btn-primary btn-small">게시물 등록</a>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
        crossorigin="anonymous"></script>
</body>
</html>
