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

public class WriteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		
		if( session == null ) {
			WebUtils.redirect( request.getContextPath() + "/board", request, response);
			return;
		}
		UserVo authUser = (UserVo)session.getAttribute( "authUser" );
		if( authUser == null ) {
			WebUtils.redirect( request.getContextPath() + "/board", request, response);
			return;
		}
		
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		if(title==null||content==null){
			title=""; content="";
		}
		String gno = request.getParameter("gno");
		String ono = request.getParameter("ono");
		String dep = request.getParameter("depth");
		
		if( "".equals( title ) || "".equals( content ) ) {
			WebUtils.redirect( request.getContextPath() + "/board", request, response );
			return;			
		}
		
		BoardDao dao = new BoardDao();
		BoardVo vo = new BoardVo();
		
		vo.setTitle(title);
		vo.setContent(content);
		vo.setGroupNo(0);
		vo.setUserNo(authUser.getNo());
		
		if( gno != null ) {
			System.out.println("if(gno!+null)");
			int groupNo = Integer.parseInt( gno );
			int orderNo = Integer.parseInt( ono );
			int depth = Integer.parseInt( dep );
			
			// 같은 그룹의 orderNo 보다 큰 글 들의 order_no 1씩 증가
			dao.increaseGroupOrder(groupNo, orderNo);
			
			vo.setGroupNo(groupNo);
			vo.setOrderNo(orderNo+1);
			vo.setDepth(depth+1);
		}
		System.out.println(title);
		System.out.println(content);
		System.out.println(gno);
		dao.insert(vo);
		
		WebUtils.redirect(request.getContextPath() + "/board", request, response);
	}

}
