<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
			<div id="board">
				<form id="search_form" action="${pageContext.servletContext.contextPath }/board/search" method="post">
					<input type="text" id="kwd" name="kwd" value="">
					<input type="submit" value="찾기">
					<input type="hidden" name="currentPage" value="1">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>	
					<c:set var="count" value='${fn:length(list) }' />
					<c:forEach items='${list }' var='vo' varStatus='status'>
						<tr>
							<td>${pvo.boardCount- status.index -(10*(pvo.currentPage-1))}</td>
							<c:choose>
								<c:when test="${vo.depth > 0}">
									<c:choose>
										<c:when test='${vo.status == 0 }'>
											<td class="label" style="padding-left:${30*vo.depth-20}px;text-align:left">
											<img src='${pageContext.servletContext.contextPath }/assets/images/reply.png'/>	
											삭제된 페이지입니다 </td>
										</c:when>
										<c:otherwise>
											<td class="label" style="padding-left:${30*vo.depth-20}px;text-align:left">
											<img src='${pageContext.servletContext.contextPath }/assets/images/reply.png'/>
											<a href="${pageContext.servletContext.contextPath }/board/read/no=${vo.no}&currentPage=1">${vo.title}</a></td>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test='${vo.status == 0 }'>
											<td class="label">삭제된 페이지입니다</td>
										</c:when>
										<c:otherwise>
											<td class="label" >
											<a href="${pageContext.servletContext.contextPath }/board/read/no=${vo.no}&currentPage=1">${vo.title}</a></td>
											<!--a href="${pageContext.servletContext.contextPath }/board/readboard&no=${vo.no}&userNo=${vo.userNo}&currentPage=${pvo.currentPage}">${vo.title}</a></td-->
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
							<td>${vo.name }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							<c:choose>
								<c:when test='${authUser.no == vo.userNo && vo.status == 1 }'>
									<td><a href="${pageContext.servletContext.contextPath }/board/delete/no=${vo.no }&gNo=${vo.gNo}&oNo=${vo.oNo}&depth=${vo.depth}" class="del">삭제</a></td>
								</c:when>
								<c:otherwise>
									<td> </td>
								</c:otherwise>
							</c:choose>
						</tr>						
					</c:forEach>		
				</table>
				
				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						<c:if test="${ pvo.firstPage > 10 }"> 
							<li><a href="${pageContext.servletContext.contextPath }/board/list?kwd=${param.kwd}&currentPage=<c:out value="${pvo.firstPage-1}"/>">◀</a></li>
						</c:if>
						<c:forEach var="counter" begin="${pvo.firstPage}" end="${pvo.firstPage+9}"> 
							<c:if test="${ counter <= pvo.pageCount }"> 
                            	<c:choose> 
                                	<c:when test="${ counter == pvo.currentPage }"> 
                                    	<a href="${pageContext.servletContext.contextPath }/board/list?kwd=${param.kwd}&currentPage=<c:out value="${counter}"/>"><span class="point"><c:out value="${counter}"/></span></a> 
                                    </c:when> 
                                    <c:when test="${ counter != pvo.currentPage }"> 
                                        <a href="${pageContext.servletContext.contextPath }/board/list?kwd=${param.kwd}&currentPage=<c:out value="${counter}"/>">[ <c:out value="${counter}"/> ]</a> 
                                    </c:when> 
                                </c:choose> 
                             </c:if> 
                         </c:forEach>
                        <c:if test="${ pvo.firstPage+9 < pvo.pageCount  }"> 
                        	<li><a href="${pageContext.servletContext.contextPath }/board/list?kwd=${param.kwd}&currentPage=<c:out value="${pvo.firstPage+10}"/>">▶</a></li> 
                        </c:if>  
					</ul>
				</div>					
				<!-- pager 추가 -->
				<c:choose>
					<c:when test='${empty authUser }'>
					</c:when>
					<c:otherwise>
						<div class="bottom">
							<a href="${pageContext.servletContext.contextPath }/board/write" id="new-book">글쓰기</a>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board"/>
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>