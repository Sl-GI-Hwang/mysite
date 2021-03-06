<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	pageContext.setAttribute("cn", "\n"); 
 	pageContext.setAttribute("br", "<br/>"); 
 %>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath }/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"/>
		<div id="content">
			<div id="board" class="board-form">
				<table class="tbl-ex">
					<tr>
						<th colspan="2">글보기</th>
					</tr>
					<tr>
						<td class="label">제목</td>
						<td>${readboard.title }</td>
					</tr>
					<tr>
						<td class="label">내용</td>
						<td>
							<div class="view-content">
							${fn:replace(readboard.contents, cn, br)}
							</div>
						</td>
					</tr>
				</table>
				<div class="bottom">
					<a href="${pageContext.servletContext.contextPath }/board?a=board">글목록</a>
					<c:choose>
					<c:when test='${authUser.no != null }'>
						<a href="${pageContext.servletContext.contextPath }/board?a=reply&no=${readboard.no }&gNo=${readboard.gNo}&oNo=${readboard.oNo}&depth=${readboard.depth}&currentPage=${currentPage}">답글달기</a>
					</c:when>
					<c:when test='${authUser.no == readboard.userNo }'>
						<a href="${pageContext.servletContext.contextPath }/board?a=modify&no=${readboard.no }&userNo=${readboard.userNo}&currentPage=${currentPage}">글수정</a>
						<a href="${pageContext.servletContext.contextPath }/board?a=deleteform&no=${readboard.no }&gNo=${readboard.gNo}&oNo=${readboard.oNo}&depth=${readboard.depth}">삭제</a>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"/>
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>