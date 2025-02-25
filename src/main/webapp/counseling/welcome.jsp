<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
 integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
 integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous">
</script>
<style>
.carousel-caption {
    position: absolute;
    bottom: 0;
    left: 15px;
    right: 15px;
    padding-bottom: 20px;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
    background-color: rgba(0, 0, 0, 0.3);
}
.carousel-item img {
    max-width: 100%;
    max-height: 100%;
}
.carousel {
    max-width: 60%;
    margin: auto;
}
</style>
<title>상담 관리 홈페이지</title>
</head>
<body>
    <jsp:include page="menu.jsp"/>

    <div id="carouselExampleIndicators" class="carousel slide mt-5" data-bs-ride="carousel">
        <div class="carousel-indicators">
            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1" aria-label="Slide 2"></button>
            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2" aria-label="Slide 3"></button>
        </div>
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img src="${pageContext.request.contextPath}/counseling/image/counseling_photo.jpg" class="d-block w-100 img-fluid" alt="...">
                <div class="carousel-caption d-none d-md-block">
                    <h5>상담</h5>
                    <p>상담자가 도움을 필요로 하는 사람에게 전문적 지식과 기능을 가지고 내담자 자신과 환경에 대한 이해를 증진시키며,<br>합리적이고 현실적이며 효율적인 행동양식을 증진시키거나 의사결정을 내릴 수 있도록 원조하는 활동.<hr>(교육학용어사전, 1995. 6. 29., 서울대학교 교육연구소)</p>
                </div>
            </div>
            <div class="carousel-item">
                <img src="${pageContext.request.contextPath}/counseling/image/counseling_photo2.jpg" class="d-block w-100 img-fluid" alt="...">
                <div class="carousel-caption d-none d-md-block">
                    <h5>개인 상담</h5>
                    <p>개인이 지니고 있는 여러 가지 형태의 문제를 개별적 면담을 통해서 해결하는 상담의 한 형태.<br>개인상담에서는 단순히 정보나 지식을 제공하기보다는 감정·태도·동기·행동성향 등의 명료화와 변화의 촉진이 그 초점이 된다.<hr>(교육학용어사전, 1995. 6. 29., 서울대학교 교육연구소)</p>
                </div>
            </div>
            <div class="carousel-item">
                <img src="${pageContext.request.contextPath}/counseling/image/counseling_photo3.jpg" class="d-block w-100 img-fluid" alt="...">
                <div class="carousel-caption d-none d-md-block">
                    <h5>집단 상담</h5>
                    <p>개인이 지니고 있는 여러 가지 문제를 소집단의 경험을 통하여 해결하는 상담의 한 형태.<br>집단상담에는 내담자에게 정보나 지식을 제공하는 것보다는 개인의 감정·태도·동기·가치·행동의 구체화와 이들의 변화를 촉진함에 중점을 둔다.<hr>(교육학용어사전, 1995. 6. 29., 서울대학교 교육연구소)</p>
                </div>
            </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
        </button>
    </div>
</body>
</html>
