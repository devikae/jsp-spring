
<%@page import="com.study.exception.BizNotEffectedException"%>
<%@page import="com.study.exception.BizNotFoundException"%>
<%@page import="com.study.free.service.FreeBoardServiceImpl"%>
<%@page import="com.study.free.service.IFreeBoardService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.study.free.vo.FreeBoardVO"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.SQLException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/inc/header.jsp"%>
<title>자유게시판 - 글 보기</title>
</head>
<body>
	<%@ include file="/WEB-INF/inc/top.jsp"%>

<%
	int boNo = Integer.parseInt(request.getParameter("boNo"));
	IFreeBoardService freeBoardService = new FreeBoardServiceImpl();
	
	try{
		FreeBoardVO freeBoard = freeBoardService.getBoard(boNo);
		
		freeBoardService.increaseHit(boNo); 
		// NOT EFFECTED (업데이트 되지 않았을때)
		
		request.setAttribute("freeBoard", freeBoard); 
		// NOT FOUND
		
	}catch(BizNotFoundException | BizNotEffectedException bnf){
		request.setAttribute("bnf", bnf);		
	}
%>
	

	

	<c:if test="${bnf ne null }">
	<div class="alert alert-warning">
			해당 글이 존재하지 않습니다.
			또는 조회수증가 실패했습니다.
	</div>	
		<a href="freeList.jsp" class="btn btn-default btn-sm">
		<span class="glyphicon glyphicon-list" aria-hidden="true"></span>
		&nbsp;목록
	</a>
	</c:if>

	<c:if test="${bnf eq null }">

		<div class="container">
			<div class="page-header">
				<h3>
					자유게시판 - <small>글 보기</small>
				</h3>
			</div>
			<table class="table table-striped table-bordered">
				<tbody>
					<tr>
						<th>글번호</th>
						<td>${freeBoard.boNo }</td>
					</tr>
					<tr>
						<th>글제목</th>
						<td>${freeBoard.boTitle }</td>
					</tr>
					<tr>
						<th>글분류</th>
						<td>${freeBoard.boCategory }</td>
					</tr>
					<tr>
						<th>작성자명</th>
						<td>${freeBoard.boWriter }</td>
					</tr>
					<!-- 비밀번호는 보여주지 않음  -->
					<tr>
						<th>내용</th>
						<td>
						${freeBoard.boContent }
					</td>
					</tr>
					<tr>
						<th>등록자 IP</th>
						<td>${freeBoard.boIp }</td>
					</tr>
					<tr>
						<th>조회수</th>
						<td>${freeBoard.boHit }</td>
					</tr>
					<tr>
						<th>최근등록일자</th>
						<td>${freeBoard.boRegDate ne null ? freeBoard.boModDate : freeBoard.boRegDate}</td>
<!-- 						수정을 안했다면 null 수정했다면 등록날짜 // null이 아니면 수정날짜 null이면 등록날짜-->
					</tr>
					<tr>
						<th>삭제여부</th>
						<td>${freeBoard.boDelYn }</td>
					</tr>
					<tr>
						<td colspan="2">
							<div class="pull-left">
								<a href="freeList.jsp" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-list" aria-hidden="true"></span> &nbsp;&nbsp;목록
								</a>
							</div>
							<div class="pull-right">
								<a href="freeEdit.jsp?boNo=${freeBoard.boNo }" class="btn btn-success btn-sm"> <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> &nbsp;&nbsp;수정
								</a>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- container -->
	</c:if>

</body>
</html>






