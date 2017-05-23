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

public class ReplyFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		if( session == null ) {
			WebUtils.redirect( request.getContextPath() + "/board", request, response );
			return;
		}
		UserVo authUser = (UserVo)session.getAttribute( "authUser" );
		if( authUser == null ) {
			WebUtils.redirect( request.getContextPath() + "/board", request, response );
			return;
		}
		Long bno = Long.parseLong(request.getParameter("bno"));
		BoardVo boardVo = new BoardDao().get(bno);
		boardVo.setNo(bno);
		request.setAttribute("replyVo", boardVo);
		WebUtils.forward("/WEB-INF/views/board/reply.jsp", request, response);
	}

}
