package kr.news.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import kr.controller.Action;
import kr.news.dao.NewsDAO;
import kr.news.vo.NewsVO;
import kr.util.FileUtil;

public class ModifyAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		
		MultipartRequest multi = FileUtil.createFile(request);
		
		NewsVO vo = new NewsVO();
		vo.setNum(Integer.parseInt(multi.getParameter("num")));
		vo.setTitle(multi.getParameter("title"));
		vo.setWriter(multi.getParameter("writer"));
		vo.setPasswd(multi.getParameter("passwd"));
		vo.setEmail(multi.getParameter("email"));
		vo.setArticle(multi.getParameter("article"));
		vo.setFilename(multi.getFilesystemName("filename")); //조심(파일명 읽어올 땐 getFilesystemName)
		
		NewsDAO dao = NewsDAO.getInstance();
		//비밀번호 인증을 위해 한 건의 레코드를 자바빈에 담아서 반환
		NewsVO db_news = dao.getNews(vo.getNum());
		boolean check = false;
		if(db_news!=null) {
			check = db_news.isCheckedPassword(vo.getPasswd());
		}
		if(check) { //비밀번호 일치
			dao.updateNews(vo);
			if(vo.getFilename()!=null) { 
				//새 파일로 교체할 때 원래 파일 제거
				FileUtil.removeFile(request, db_news.getFilename());
			}
			request.setAttribute("num", vo.getNum());
		}else { //비밀번호 불일치
			if(vo.getFilename()!=null) {
				//인증 오류일 경우 업로드된 파일 제거
				FileUtil.removeFile(request, vo.getFilename());
			}
		}
		
		//UI 처리를 위해 check 저장
		request.setAttribute("check", check);
		
		//JSP경로 반환
		return "/WEB-INF/views/modify.jsp";
	}

}
