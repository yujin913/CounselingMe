<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page session="true" %>
<%@ page import="java.util.List" %>
<%@ page import="counseling.Counselor" %>
<%@ page import="counseling.Reservation" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
 integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<style>
  .disabled-input {
    background-color: #e9ecef !important;
    pointer-events: none;
  }
  .nav-link.active {
    font-weight: bold;
    color: #495057;
  }
  .fixed-height td {
    height: 50px;
    vertical-align: middle;
  }
</style>
</head>
<body>
<jsp:include page="./menu.jsp"/>
<%
    Integer userId = (Integer) session.getAttribute("userId");
    String userName = (String) session.getAttribute("userName");
    String userEmail = (String) session.getAttribute("userEmail");
    String userPhone = (String) session.getAttribute("userPhone");
    String userAddress = (String) session.getAttribute("userAddress");
    String userDob = (String) session.getAttribute("userDob");
    Boolean isCounselor = (Boolean) session.getAttribute("isCounselor");
%>

<div class="container mt-5">
    <% if (request.getParameter("success") != null) { %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <% 
                String successParam = request.getParameter("success");
                if ("reservation".equals(successParam)) {
                    out.print("상담 예약 신청이 완료되었습니다.");
                } else if ("update".equals(successParam)) {
                    out.print("회원정보 수정이 완료되었습니다.");
                } else if ("cancel".equals(successParam)) {
                    out.print("예약이 정상적으로 취소되었습니다.");
                } else if ("counselorUpdate".equals(successParam)) {
                    out.print("상담사 정보 수정이 완료되었습니다.");
                } else if ("requested".equals(successParam)) {
                    out.print("상담 권한이 정상적으로 신청되었습니다.");
                }
            %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>
    <div class="row">
        <div class="col-md-3">
            <div class="list-group">
                <a href="#userInfo" class="list-group-item list-group-item-action active" aria-current="true" id="userInfo-tab" data-bs-toggle="list" role="tab" aria-controls="userInfo">회원정보 수정</a>
                <% if (isCounselor != null && isCounselor) { %>
                    <a href="#counselorInfo" class="list-group-item list-group-item-action" id="counselorInfo-tab" data-bs-toggle="list" role="tab" aria-controls="counselorInfo">상담사 정보 수정</a>
                <% } else { %>
                    <a href="#requestCounselor" class="list-group-item list-group-item-action" id="requestCounselor-tab" data-bs-toggle="list" role="tab" aria-controls="requestCounselor">상담 권한 신청</a>
                <% } %>
                <a href="#reservationList" class="list-group-item list-group-item-action" id="reservationList-tab" data-bs-toggle="list" role="tab" aria-controls="reservationList">예약 내역</a>
                <a href="${pageContext.request.contextPath}/control?action=deleteUser" class="list-group-item list-group-item-action text-danger">회원 탈퇴</a>
            </div>
        </div>
        <div class="col-md-9">
            <div class="tab-content">
                <div class="tab-pane fade show active" id="userInfo" role="tabpanel" aria-labelledby="userInfo-tab">
                    <form action="${pageContext.request.contextPath}/control?action=updateUser" method="post">
                        <div class="card">
                            <div class="card-body">
                                <h2 class="text-center mb-4">회원정보 수정</h2>
                                <div class="mb-3">
                                    <label for="email" class="form-label">이메일</label>
                                    <input type="email" class="form-control disabled-input" id="email" name="email" value="<%= userEmail %>" readonly>
                                </div>
                                <div class="mb-3">
                                    <label for="name" class="form-label">이름</label>
                                    <input type="text" class="form-control" id="name" name="name" value="<%= userName %>" required>
                                </div>
                                <div class="mb-3">
                                    <label for="phone" class="form-label">전화번호</label>
                                    <input type="text" class="form-control" id="phone" name="phone" value="<%= userPhone %>" required>
                                </div>
                                <div class="mb-3">
                                    <label for="address" class="form-label">주소</label>
                                    <input type="text" class="form-control" id="address" name="address" value="<%= userAddress %>" required>
                                </div>
                                <div class="mb-3">
                                    <label for="dob" class="form-label">생년월일</label>
                                    <input type="date" class="form-control" id="dob" name="dob" value="<%= userDob %>" required>
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label">패스워드</label>
                                    <input type="password" class="form-control" id="password" name="password" required>
                                </div>
                                <div class="mb-3">
                                    <label for="passwordConfirm" class="form-label">패스워드 확인</label>
                                    <input type="password" class="form-control" id="passwordConfirm" name="passwordConfirm" required>
                                </div>
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary">회원정보 수정</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane fade" id="counselorInfo" role="tabpanel" aria-labelledby="counselorInfo-tab">
                    <form action="${pageContext.request.contextPath}/control?action=processUpdateCounselor" method="post" enctype="multipart/form-data">
                        <div class="card">
                            <div class="card-body">
                                <h2 class="text-center mb-4">상담사 정보 수정</h2>
                                <div class="mb-3">
                                    <label for="specialty" class="form-label">전문 분야</label>
                                    <input type="text" class="form-control" id="specialty" name="specialty" value="<%= request.getAttribute("counselor") != null ? ((Counselor) request.getAttribute("counselor")).getSpecialty() : "" %>" required>
                                </div>
                                <div class="mb-3">
                                    <label for="photo" class="form-label">사진 업로드</label>
                                    <input type="file" class="form-control" id="photo" name="photo">
                                </div>
                                <div class="mb-3">
                                    <label for="gender" class="form-label">성별</label>
                                    <select class="form-control" id="gender" name="gender" required>
                                        <option value="남성" <%= request.getAttribute("counselor") != null && "남성".equals(((Counselor) request.getAttribute("counselor")).getGender()) ? "selected" : "" %>>남성</option>
                                        <option value="여성" <%= request.getAttribute("counselor") != null && "여성".equals(((Counselor) request.getAttribute("counselor")).getGender()) ? "selected" : "" %>>여성</option>
                                        <option value="기타" <%= request.getAttribute("counselor") != null && "기타".equals(((Counselor) request.getAttribute("counselor")).getGender()) ? "selected" : "" %>>기타</option>
                                    </select>
                                </div>
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary">수정</button>
                                </div>
                                <div class="d-grid mt-2">
                                    <a href="${pageContext.request.contextPath}/control?action=myPage" class="btn btn-secondary">취소</a>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane fade" id="requestCounselor" role="tabpanel" aria-labelledby="requestCounselor-tab">
                    <form action="${pageContext.request.contextPath}/control?action=processAddCounselor" method="post" enctype="multipart/form-data">
                        <div class="card">
                            <div class="card-body">
                                <h2 class="text-center mb-4">상담 권한 신청</h2>
                                <div class="mb-3">
                                    <label for="specialty" class="form-label">전문 분야</label>
                                    <input type="text" class="form-control" id="specialty" name="specialty" required>
                                </div>
                                <div class="mb-3">
                                    <label for="photo" class="form-label">사진 업로드</label>
                                    <input type="file" class="form-control" id="photo" name="photo" required>
                                </div>
                                <div class="mb-3">
                                    <label for="gender" class="form-label">성별</label>
                                    <select class="form-control" id="gender" name="gender" required>
                                        <option value="남성">남성</option>
                                        <option value="여성">여성</option>
                                        <option value="기타">기타</option>
                                    </select>
                                </div>
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary">신청</button>
                                </div>
                                <div class="d-grid mt-2">
                                    <a href="${pageContext.request.contextPath}/control?action=myPage" class="btn btn-secondary">취소</a>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane fade" id="reservationList" role="tabpanel" aria-labelledby="reservationList-tab">
                    <div class="card">
                        <div class="card-body">
                            <h2 class="text-center mb-4">예약 내역</h2>

                            <div class="container mt-5">
                                <table class="table table-bordered fixed-height">
                                    <thead>
                                        <tr>
                                            <% if (isCounselor != null && isCounselor) { %>
                                            <th>피상담자</th>
                                            <% } else { %>
                                            <th>상담사</th>
                                            <% } %>
                                            <th>날짜</th>
                                            <th>시작 시간</th>
                                            <th>종료 시간</th>
                                            <th>상태</th>
                                            <th>비고</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                            List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
                            if (reservations != null && !reservations.isEmpty()) {
                                for (Reservation reservation : reservations) {
                        %>
                                        <tr>
                                            <% if (isCounselor != null && isCounselor) { %>
                                            <td><%= reservation.getUserName() %></td>
                                            <% } else { %>
                                            <td><%= reservation.getCounselorName() %></td>
                                            <% } %>
                                            <td><%= reservation.getReservationDate() %></td>
                                            <td><%= reservation.getStartTime() %></td>
                                            <td><%= reservation.getEndTime() %></td>
                                            <td><%= reservation.getStatus() %></td>
                                            <td>
                                                <% if (!"상담 완료".equals(reservation.getStatus())) { %>
                                                <form action="${pageContext.request.contextPath}/control?action=cancelReservation" method="post" style="margin: 0;">
                                                    <input type="hidden" name="reservationId" value="<%= reservation.getId() %>"> <input type="hidden" name="userId" value="<%= userId %>">
                                                    <button type="submit" class="btn btn-danger btn-sm">취소</button>
                                                </form> <% } %>
                                            </td>
                                        </tr>
                                        <%
                                }
                            } else {
                        %>
                                        <tr>
                                            <td colspan="6" class="text-center">예약 내역이 없습니다.</td>
                                        </tr>
                                        <%
                            }
                        %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="tab-pane fade" id="defaultInfo" role="tabpanel" aria-labelledby="defaultInfo-tab">
                    <div class="card">
                        <div class="card-body">
                            <h2 class="text-center mb-4">회원 정보</h2>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous">
</script>
</body>
</html>
