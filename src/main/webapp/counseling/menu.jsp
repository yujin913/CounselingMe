<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
 integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<style>
  .navbar-text-custom {
    background-color: rgba(211, 211, 211, 0.5);
    padding: 0.5rem 1rem;
    border-radius: 0.25rem;
    margin-right: 0.5rem;
    display: flex;
    align-items: center;
  }
  .navbar-text-custom strong {
    margin-left: 0.25rem;
  }
</style>
</head>
<body>
<%
    String userId = null;
    String userName = null;
    Boolean isAdmin = false;
    Boolean isCounselor = false;
    if (session != null) {
        Object sessionUserId = session.getAttribute("userId");
        Object sessionUserName = session.getAttribute("userName");
        Object sessionIsAdmin = session.getAttribute("isAdmin");
        Object sessionIsCounselor = session.getAttribute("isCounselor");
        if (sessionUserId != null) {
            userId = sessionUserId.toString();
        }
        if (sessionUserName != null) {
            userName = sessionUserName.toString();
        }
        if (sessionIsAdmin != null) {
            isAdmin = (Boolean) sessionIsAdmin;
        }
        if (sessionIsCounselor != null) {
            isCounselor = (Boolean) sessionIsCounselor;
        }
    }
%>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <div class="container-fluid">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/control?action=welcome">
      <img src="${pageContext.request.contextPath}/counseling/image/counseling-icon.png" alt="Logo" width="30" height="30" class="d-inline-block align-top">
      상담 관리 시스템
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav">
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/control?action=welcome">홈</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/control?action=counselorList">상담사 목록</a>
        </li>
        <% if (isAdmin) { %>
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/control?action=userList">사용자 목록</a>
          </li>
        <% } %>
        <% if (isCounselor) { %>
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/control?action=counselingManagement">상담 관리</a>
          </li>
        <% } %>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/control?action=boardList">게시판</a>
        </li>
      </ul>
      <ul class="navbar-nav ms-auto">
        <% if (userId != null) { %>
          <li class="nav-item">
            <span class="navbar-text navbar-text-custom">안녕하세요, <strong><%= userName %></strong>님</span>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/control?action=myPage">마이페이지</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/control?action=logout">로그아웃</a>
          </li>
        <% } else { %>
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/control?action=login">로그인</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/control?action=join">회원가입</a>
          </li>
        <% } %>
      </ul>
    </div>
  </div>
</nav>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous"></script>
</body>
</html>
