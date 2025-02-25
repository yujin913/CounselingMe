<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>상담사 정보 수정</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<jsp:include page="./menu.jsp"/>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <h2 class="text-center mb-4">상담사 정보 수정</h2>
            <form action="${pageContext.request.contextPath}/control?action=processUpdateCounselor" method="post" enctype="multipart/form-data">
                <div class="mb-3">
                    <label for="specialty" class="form-label">전문 분야</label>
                    <input type="text" class="form-control" id="specialty" name="specialty" value="${counselor.specialty}" required>
                </div>
                <div class="mb-3">
                    <label for="photo" class="form-label">사진 업로드</label>
                    <input type="file" class="form-control" id="photo" name="photo">
                </div>
                <div class="mb-3">
                    <label for="gender" class="form-label">성별</label>
                    <select class="form-control" id="gender" name="gender" required>
                        <option value="남성" ${counselor.gender == '남성' ? 'selected' : ''}>남성</option>
                        <option value="여성" ${counselor.gender == '여성' ? 'selected' : ''}>여성</option>
                        <option value="기타" ${counselor.gender == '기타' ? 'selected' : ''}>기타</option>
                    </select>
                </div>
                <div class="d-grid">
                    <button type="submit" class="btn btn-primary">수정</button>
                </div>
                <div class="d-grid mt-2">
                    <a href="${pageContext.request.contextPath}/control?action=myPage" class="btn btn-secondary">취소</a>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>
