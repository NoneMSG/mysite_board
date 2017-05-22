<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/include/header.jsp" />
		<div id="content">
			<div id="board">
				<form id="search_form" action="a" method="post">
					<input type="text" id="kwd" name="kwd" value="">
					<input type="submit" value="찾기">
					<input type="hidden" value="search">
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
					
					<c:forEach items="${blist }" var="bvo" varStatus="status">		
					<tr>
						<td>[${status.count }]</td>
						<td class="left">
							<a href="${pageContext.servletContext.contextPath }/board?a=viewform&bno=${bvo.no}">${bvo.title }</a>
						</td>
						<td>${authUser.name }</td>
						<td>${bvo.hit }</td>
						<td>${bvo.regDate }</td>
						<td><form></form></td>
						<td><a href="" class="del">삭제</a></td>
					</tr>
					</c:forEach>
					
					<tr>
						<td>3</td>
						<td class="left" style="padding-left:${vo.depth * 20 }px">
							<img src="${pageContext.request.contextPath }/assets/images/reply.png">
							<a href="">두 번째 글의 답글 입니다.</a>
						</td>
						<td>안대혁</td>
						<td>3</td>
						<td>2015-10-02 12:04:12</td>
						<td>
							<a href="" class="del">삭제</a>
						</td>
					</tr>
					<tr>
						<td>2</td>
						<td class="left" style="padding-left:40px">
							<img src="${pageContext.request.contextPath }/assets/images/reply.png">
							<a href="">답글의 답글 입니다.</a>
						</td>
						<td>안대혁</td>
						<td>3</td>
						<td>2015-10-02 12:04:12</td>
						<td><a href="" class="del">삭제</a></td>
					</tr>
					
				</table>
				<div class="pager">
					<ul>
						<li><a href="">◀</a></li>
						<li><a href="">1</a></li>
						<li><a href="">2</a></li>
						<li class="selected">3</li>
						<li><a href="">4</a></li>
						<li><a href="">5</a></li>
						<li><a href="">▶</a></li>
					</ul>
				</div>				
				<div class="bottom">
					<a href="${pageContext.servletContext.contextPath }/board?a=writeform" id="new-book">글쓰기</a>
				</div>				
			</div>
		</div>
		<c:import url="/WEB-INF/views/include/navigation.jsp">
			<c:param name="menu" value="board"/>
		</c:import>
		<c:import url="/WEB-INF/views/include/footer.jsp" />
	</div>
</body>
</html>