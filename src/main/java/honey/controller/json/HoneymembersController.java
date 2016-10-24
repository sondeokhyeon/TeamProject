package honey.controller.json;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import honey.dao.MemberFileDao;
import honey.service.HoneymembersService;
import honey.util.FileUploadUtil;
import honey.vo.HoneyMembers;
import honey.vo.JsonResult;
import honey.vo.MemberFile;

// 시작하기 전에 우선 나는 컨트롤러와 서비스의 역할에 대해 생각한게 
// 디비에 직접적으로 접속해서 crud 기능을 하는 것은 서비스 부분이 담당하고, 그외의 세션이나 쿠키등의 생성과 스크립트와 값을 주고
// 받는 부분에 대해서는 컨트롤러가 담당한다고 생각하고 작업을 진행한다.
@Controller
@RequestMapping({"/mainpage/", "/writepage/", "/adminpage/","/membership/","/resultOfSearch/"})
public class HoneymembersController {
	@Autowired HoneymembersService hMembersService;
	@Autowired ServletContext sc;
	@Autowired MemberFileDao memberFileDao;
	
	@RequestMapping(path="joinMember")
	public Object joinMember(HoneyMembers members) throws Exception {
		try {
			// 서비스에 있는 메서드를 호출했다.
			hMembersService.singUpMembers(members);
			return JsonResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.fail(e.getMessage());
		}
	}

	@RequestMapping(path="unregisteMember")
	public Object unregister(HttpSession session) throws Exception {
		//session.invalidate();
		// 지금 세션을 무효화 시키면 어떻하지?
		// 기존에 서비스 구현 전에는 어떻게 이게 동작을 했지??
		// 회원탈퇴가 된 후에 세션을 무효화시키는게 맞는거 아닌가??
		// 회원탈퇴 완료 후 세션을 무효화 시키니 정상동작한다.

		try {
			HoneyMembers hMembers= (HoneyMembers)session.getAttribute("member");
			hMembersService.unregister(hMembers.getMemberNo());
			session.invalidate();
			return JsonResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.fail(e.getMessage());
		}
	}


	@RequestMapping(path="userInfoDetail")
	public Object userInfoDetail (HttpSession session) throws Exception {
		// 이 메서드는 DB에서 작업할 일이 없어서 service 클래스에서 따로 구현할 필요가 없을 것 같다.
		try {
			HoneyMembers hMembers = (HoneyMembers)session.getAttribute("member");

			if(hMembers == null) {
				System.out.println("해당 회원 정보가 없습니다.");
			}
			return JsonResult.success(hMembers);
		} catch (Exception e) {
			return JsonResult.fail(e.getMessage());
		}
	}

	@RequestMapping(path="userStatusUpdate")
	public Object userStatusUpdate(HoneyMembers hmember, HttpSession session) throws Exception {
		try {
			// 파라미터값으로 브라우저에서 입력했던 값인 hmember 객체를 받아서 이 hmember 객체에 회원번호를 셋팅하기 위해
			// 세션도 파라미터로 받았다
			// 세션에 담긴 회원 번호를 얻기 위해 새로 vo 객체를 하나 생성해서 세션에 있는 모든 값을 담은 후
			// 그 중 회원번호만을 hmember 객체에 셋팅했다.
			HoneyMembers honeymembers =(HoneyMembers)session.getAttribute("member");
			hmember.setMemberNo(honeymembers.getMemberNo());
			hMembersService.memberInfoUpdate(hmember);
			hmember.setPassword(honeymembers.getPassword());
			session.setAttribute("member", hmember);
			return JsonResult.success();
		} catch(Exception e) {
			e.printStackTrace();
			return JsonResult.fail(e.getMessage());
		}
	}

	@RequestMapping(path="changePassword")
	public Object changePassword(HoneyMembers hMember, HttpSession session) throws Exception {
		try {
			HoneyMembers honeyMemebers = (HoneyMembers)session.getAttribute("member");
			hMember.setMemberNo(honeyMemebers.getMemberNo());
			hMembersService.modifyPassword(hMember);
			hMember.setEmail(honeyMemebers.getEmail());
			hMember.setMemberNo(honeyMemebers.getMemberNo());
			hMember.setNickname(honeyMemebers.getNickname());
			hMember.setPhone(honeyMemebers.getPhone());
			hMember.setUserBirthDay(honeyMemebers.getUserBirthDay());
			hMember.setUserName(honeyMemebers.getUserName());
			session.setAttribute("member", hMember);
			return JsonResult.success();
		} catch(Exception e) {
			e.printStackTrace();
			return JsonResult.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "/profileFileUpload", method = RequestMethod.POST)
	@ResponseBody
	public String upload(MultipartHttpServletRequest req, 
			HttpServletResponse res, HttpSession session) throws IOException {
		String uploadDir = sc.getRealPath("/upload") + "/";
		Iterator<String> itr =  req.getFileNames();
		/* Iterator : 모든 컬랙션으로부터 정보를 얻을 수 있는 인터페이스
		 */
		MultipartFile mpf = req.getFile(itr.next());
		String originFileName = mpf.getOriginalFilename();

		String newFilename = null;
		if (mpf != null && ! mpf.isEmpty()) {
			newFilename = FileUploadUtil.getNewFilename(originFileName);
			mpf.transferTo(new File(uploadDir + newFilename));
			MemberFile profileFile = new MemberFile();
			profileFile.setFilename(newFilename);
			HoneyMembers honeymembers =(HoneyMembers)session.getAttribute("member");
			profileFile.setMemberNo(honeymembers.getMemberNo());

			memberFileDao.prifileFileinsert(profileFile);
		}

		return "{\"code\":\"1\", \"msg\":\"file upload success.\"}";
	}
	
	@RequestMapping(path="userProfileFileLoder")
	public Object profileFileLoder (HttpSession session) {
		HoneyMembers honeyMember = (HoneyMembers)session.getAttribute("member");
		MemberFile memberFile = new MemberFile();
		try {
			memberFile.setFilename(hMembersService.getProfileFileName(honeyMember.getMemberNo()));
			System.out.println(memberFile.getFilename());
			return JsonResult.success(memberFile.getFilename());
		} catch(Exception e) {
			e.printStackTrace();
			return JsonResult.fail(e.getMessage());
		}

	}




}