package kr.news.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.controller.Action;
import kr.news.dao.NewsDAO;
import kr.news.vo.NewsVO;

public class ModifyAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		
		NewsVO vo = new NewsVO();
		vo.setTitle(request.getParameter("title"));
		vo.setWriter(request.getParameter("writer"));
		vo.setPasswd(request.getParameter("passwd"));
		vo.setEmail(request.getParameter("email"));
		vo.setArticle(request.getParameter("article"));
		vo.setFilename(request.getParameter("filename"));
		
		NewsDAO dao = NewsDAO.getInstance();
		NewsVO newsVO = dao.getNews(vo.getNum());
		boolean check = false;
		if(newsVO!=null) {
			check = newsVO.isCheckedPassword(vo.getPasswd());
		}
		if(check) {
			dao.updateNews(vo);
			request.setAttribute("num", vo.getNum());
		}
		request.setAttribute("check", check);
		
		//JSP경로 반환
		return "/WEB-INF/views/modify.jsp";
	}

}
