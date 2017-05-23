package com.jx372.mysite.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jx372.mysite.dao.BoardDao;
import com.jx372.mysite.vo.BoardVo;
import com.jx372.mysite.vo.UserVo;
import com.jx372.web.action.Action;
import com.jx372.web.util.WebUtils;

public class ModifyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		HttpSession session = request.getSession();
		
		if( session == null ) {
			WebUtils.redirect( request.getContextPath() + "/board?a=list", request, response);
			return;
		}
		UserVo authUser = (UserVo)session.getAttribute( "authUser" );
		if( authUser == null ) {
			WebUtils.redirect( request.getContextPath() + "/board?a=list", request, response);
			return;
		}
		Long bno = Long.parseLong(request.getParameter("bno"));
		Long uno = authUser.getNo();
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		BoardVo boardVo = new BoardVo();
		boardVo.setNo(bno);
		boardVo.setUserNo(uno);
		boardVo.setTitle(title);
		boardVo.setContent(content);
		new BoardDao().modify(boardVo);
		
		WebUtils.redirect(request.getContextPath()+"/board", request, response);
	}

}
