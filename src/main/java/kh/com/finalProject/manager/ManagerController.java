package kh.com.finalProject.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kh.com.finalProject.board.BoardDTO;
import kh.com.finalProject.board.BoardService;
import kh.com.finalProject.member.MemberDTO;
import kh.com.finalProject.member.MemberService;
import kh.com.finalProject.publicdata.DataService;
import kh.com.finalProject.reply.ReplyDTO;
import kh.com.finalProject.reply.ReplyService;
import kh.com.finalProject.visit.VisitService;

@Controller
@RequestMapping("/manager")
public class ManagerController {

	@Autowired
	private MemberService mService;

	@Autowired
	private BoardService bService;

//	@Autowired
//	private ReportService rService;

	@Autowired
	private ReplyService replyService;

	@Autowired
	private DataService dService;

	@Autowired
	private VisitService vService;

	@Autowired
	private ManagerService service;

	// 관리자 main 페이지 이동
	@RequestMapping("/main.do")
	public String main(int currentPage, Model model) throws Exception {

		// 신규 가입자 조회
		List<MemberDTO> mlist = mService.dayMember();
		// 당일 추가 게시글 조회
		List<BoardDTO> blist = bService.dayBoard();
		// 공공데이터 갯수 조회
		List<Integer> publicdatalist = new ArrayList<>();

		// 오늘 방문자 수
		int todayCount = vService.todayCount();

		// 전체 방문자 수
		int totalCount = vService.totalCount();

		publicdatalist.add(dService.countAll());
		publicdatalist.add(dService.countAllFestival());
		publicdatalist.add(dService.countAllLeports());

		int recordTotalCnt = service.selectAllBoardCount();
		// 게시판 이전, 다음 버튼
		HashMap<String, Object> boardNaviMap = service.getPageNavi(recordTotalCnt, currentPage);
		// 게시판 페이지네이션
		List<BoardDTO> list = service.selectAll(currentPage);

		System.out.println("mlist : " + mlist);
		System.out.println("blist : " + blist);
		HashMap<String, Object> map = new HashMap<>();

		// 멤버 영역
		map.put("mlist", mlist);

		// 게시판 영역
		map.put("blist", blist);
		map.put("boardNaviMap", boardNaviMap);
		map.put("list", list);
		map.put("recordTotalCnt", recordTotalCnt);
		model.addAttribute("blist", blist);
		model.addAttribute("boardNaviMap", boardNaviMap);
		model.addAttribute("list", list);
		model.addAttribute("recordTotalCnt", recordTotalCnt);

		// 공공데이터 영역
		map.put("publicdatalist", publicdatalist);

		// 오늘 방문자수
		map.put("todayCount", todayCount);

		// 전체 방문자수
		map.put("totalCount", totalCount);
		model.addAttribute("map", map);
		return "manager/main";
	}

	// 회원 관리 페이지 이동
	@RequestMapping("/member.do")
	public String memeber(int currentPage, Model model) throws Exception {

		HashMap<String, Object> naviMap = mService.getMemberPageNavi(currentPage);
		model.addAttribute("naviMap", naviMap);
		model.addAttribute("currentPage", currentPage);
		return "/manager/member";
	}

	// 전체 게시판 조회
	@RequestMapping("/toManagerBoard.do")
	public String toBoard(Model model, int currentPage) throws Exception {
		// 자유게시판 게시글 총 갯수
		int recordTotalCnt = service.selectAllBoardCount();

		// 자유게시판 페이지네이션
		HashMap<String, Object> naviMap = service.getPageNavi(recordTotalCnt, currentPage);
		List<BoardDTO> list = service.selectAll(currentPage);

		model.addAttribute("naviMap", naviMap);
		model.addAttribute("list", list);
		model.addAttribute("recordTotalCnt", recordTotalCnt);

		return "manager/main";
	}

	// 개시판 관리 페이지 이동
	@RequestMapping("/board.do")
	public String board() {
		return "/manager/board";
	}

	// 신고 관리 페이지 이동
	@RequestMapping("/report.do")
	public String report() {
		return "/manager/report";
	}

	// 댓글관리 페이지 이동
	@RequestMapping("/reply.do")
	public String reply() {
		return "/manager/reply";
	}

	// 회원 전체 조회
	@RequestMapping("/toMember.do")
	@ResponseBody()
	public List<MemberDTO> toMember(int currentPage) throws Exception {

		System.out.println("toMember.do 도착");
		List<MemberDTO> list = mService.mSelectAll(currentPage);
		System.out.println(list);

		return list;
	}

	// 회원 삭제
	@RequestMapping("/memberDelete.do")
	@ResponseBody()
	public String delete(String id) throws Exception {
		mService.delete(id);
		int rs = mService.delete(id);
		System.out.println(rs);
		if (rs == 0) {
			return "success";
		} else {
			return "fail";
		}
	}

}
