package counseling;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// 이미지 처리
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 15,    // 15 MB
        location = "c:/Temp"
)

// 메인 컨트롤러
@WebServlet("/control")
public class counselingController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private counselingDAO dao;

    public counselingController() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dao = new counselingDAO();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String view = "";

        if (action == null) {
            getServletContext().getRequestDispatcher("/control?action=welcome").forward(request, response);
        } else {
            switch (action) {
                // 기본
                case "welcome":
                    view = welcome(request, response);
                    break;

                // 회원
                case "join":
                    view = join(request, response);
                    break;
                case "processJoin":
                    view = processJoin(request, response);
                    break;
                case "login":
                    view = login(request, response);
                    break;
                case "processLogin":
                    view = processLogin(request, response);
                    break;
                case "logout":
                    view = logout(request, response);
                    break;
                case "myPage":
                    view = myPage(request, response);
                    break;
                case "updateUser":
                    view = updateUser(request, response);
                    break;
                case "deleteUser":
                    view = deleteUser(request, response);
                    break;

                // 관리자
                case "userList":
                    view = userList(request, response);
                    break;
                case "grantCounselor":
                    view = grantCounselor(request, response);
                    break;
                case "revokeCounselor":
                    view = revokeCounselor(request, response);
                    break;
                case "requestCounselor":
                    view = requestCounselor(request, response);
                    break;

                // 상담사
                case "counselorList":
                    view = counselorList(request, response);
                    break;
                case "addCounselor":
                    view = addCounselor(request, response);
                    break;
                case "processAddCounselor":
                    view = processAddCounselor(request, response);
                    break;
                case "updateCounselor":
                    view = updateCounselor(request, response);
                    break;
                case "processUpdateCounselor":
                    view = processUpdateCounselor(request, response);
                    break;
                case "editCounselorInfo":
                    view = editCounselorInfo(request, response);
                    break;

                // 상담 예약
                case "makeReservation":
                    view = makeReservation(request, response);
                    break;
                case "processMakeReservation":
                    view = processMakeReservation(request, response);
                    break;
                case "checkAvailableSlots":
                    view = checkAvailableSlots(request, response);
                    break;
                case "reservationList":
                    view = reservationList(request, response);
                    break;
                case "cancelReservation":
                    view = cancelReservation(request, response);
                    if (view == null) return; // sendRedirect가 사용된 경우
                    break;
                
                // 상담 관리
                case "counselingManagement":
                	view = counselingManagement(request, response);
                	break;
                case "counselingRecord":
                	view = counselingRecord(request, response);
                	break;
                case "saveCounselingRecord":
                	view = saveCounselingRecord(request, response);
                	break;
                	
                // 게시판
                case "boardList":
                	view = boardList(request, response);
                	break;
                case "addBoard":
                	view = addBoard(request, response);
                	break;
                case "processAddBoard":
                	view = processAddBoard(request, response);
                	break;
                case "viewBoard":
                	view = viewBoard(request, response);
                	break;
                case "editBoard":
                	view = editBoard(request, response);
                	break;
                case "updateBoard":
                	view = updateBoard(request, response);
                	break;
                case "deleteBoard":
                	view = deleteBoard(request, response);
                	break;
                
                // 게시판 댓글
                case "addBoardComment":
                	view = addBoardComment(request, response);
                	break;
                case "deleteBoardComment":
                	view = deleteBoardComment(request, response);
                	break;
                	
                default:
                    view = welcome(request, response);
                    break;
            }

            if (view != null) {
                getServletContext().getRequestDispatcher("/counseling/" + view).forward(request, response);
            }
        }
    }

    private String welcome(HttpServletRequest request, HttpServletResponse response) {
        return "welcome.jsp";
    }

    private String join(HttpServletRequest request, HttpServletResponse response) {
        return "join.jsp";
    }

    private String processJoin(HttpServletRequest request, HttpServletResponse response) {
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("passwordConfirm");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        Date dob = Date.valueOf(request.getParameter("dob"));

        if (!password.equals(passwordConfirm)) {
            request.setAttribute("error", "패스워드가 일치하지 않습니다.");
            return "join.jsp";
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setAddress(address);
        user.setBirthDate(dob);
        user.setPassword(hashedPassword); // 해싱된 비밀번호 저장
        user.setCounselor(false); // 기본값으로 상담사가 아님

        try {
            dao.addUser(user);
            return "login.jsp";
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate email")) {
                request.setAttribute("error", "중복된 이메일이 존재합니다. 다른 이메일을 사용하세요.");
                return "join.jsp";
            } else {
                e.printStackTrace();
                return "error.jsp";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

    private String login(HttpServletRequest request, HttpServletResponse response) {
        return "login.jsp";
    }

    private String processLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = dao.getUserByEmailAndPassword(email, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userName", user.getName());
                session.setAttribute("userPhone", user.getPhoneNumber());
                session.setAttribute("userAddress", user.getAddress());
                session.setAttribute("userDob", user.getBirthDate().toString());
                session.setAttribute("isAdmin", user.getId() == 0);  // 관리자 여부 저장
                session.setAttribute("isCounselor", user.isCounselor());  // 상담사 여부 저장

                String redirect = request.getParameter("redirect");
                if (redirect != null && !redirect.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/control?action=" + redirect + "&counselorId=" + request.getParameter("counselorId"));
                    return null;
                }

                return "welcome.jsp";
            } else {
                request.setAttribute("error", "올바른 이메일과 비밀번호를 입력했는지 다시 한번 확인해주세요.");
                return "login.jsp";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }


    private String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "welcome.jsp";
    }

    private String myPage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "login.jsp?error=notLoggedIn";
        }

        try {
            // 예약 상태 업데이트
            dao.updateReservationStatus(userId);

            User user = dao.getUserById(userId);
            List<Reservation> reservations;

            if (user.isCounselor()) {
                // 상담사인 경우 상담사 ID로 예약 내역 가져오기
                Counselor counselor = dao.getCounselorByUserId(userId);
                reservations = dao.getReservationsByCounselorId(counselor.getId());
            } else {
                // 일반 사용자일 경우 사용자 ID로 예약 내역 가져오기
                reservations = dao.getReservationsByUserId(userId);
            }

            // 사용자 정보 세션에 설정
            session.setAttribute("userName", user.getName());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userPhone", user.getPhoneNumber());
            session.setAttribute("userAddress", user.getAddress());
            session.setAttribute("userDob", user.getBirthDate().toString());
            session.setAttribute("isCounselor", user.isCounselor());

            // 예약 내역 요청 속성에 설정
            request.setAttribute("reservations", reservations);

            return "myPage.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

    private String updateUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "login.jsp?error=notLoggedIn";
        }

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        Date dob = Date.valueOf(request.getParameter("dob"));
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("passwordConfirm");

        if (!password.equals(passwordConfirm)) {
            request.setAttribute("error", "패스워드가 일치하지 않습니다.");
            return "myPage.jsp";
        }

        try {
            User user = dao.getUserById(userId);
            if (user != null) {
                user.setName(name);
                user.setEmail(email);
                user.setPhoneNumber(phone);
                user.setAddress(address);
                user.setBirthDate(dob);

                if (password != null && !password.isEmpty()) {
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    user.setPassword(hashedPassword);
                }

                dao.updateUser(user);
                session.setAttribute("userName", user.getName());
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userPhone", user.getPhoneNumber());
                session.setAttribute("userAddress", user.getAddress());
                session.setAttribute("userDob", user.getBirthDate().toString());

                return "myPage.jsp?success=update";
            } else {
                return "login.jsp?error=userNotFound";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

    private String deleteUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String userEmail = (String) session.getAttribute("userEmail");

        try {
            dao.deleteUser(userEmail);
            session.invalidate();
            return "welcome.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

    
    
    // 관리자
    private String userList(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<User> users = dao.getAllUsers();
            request.setAttribute("users", users);
            return "adminUserList.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

    private String grantCounselor(HttpServletRequest request, HttpServletResponse response) {
        int userId = Integer.parseInt(request.getParameter("userId"));

        try {
            User user = dao.getUserById(userId);
            if (user != null && !user.isCounselor()) {
                user.setCounselor(true);
                dao.updateUser(user, true);  // 상담사 권한 업데이트 메서드 호출
                request.setAttribute("message", "상담 권한이 성공적으로 부여되었습니다.");
                return userList(request, response); // 권한 부여 후 사용자 목록 페이지로 이동
            } else {
                request.setAttribute("error", "사용자를 찾을 수 없거나 이미 상담사입니다.");
                return "adminUserList.jsp";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

    private String requestCounselor(HttpServletRequest request, HttpServletResponse response) {
        return "addCounselor.jsp";
    }

    private String processAddCounselor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (Integer) session.getAttribute("userId");
        String specialty = request.getParameter("specialty");
        String gender = request.getParameter("gender");

        Part part = request.getPart("photo");
        String fileName = getFilename(part);
        if (fileName != null && !fileName.isEmpty()) {
            part.write("c:/Temp/img/" + fileName); // 고정 경로로 파일 저장
        }

        try {
            Counselor counselor = new Counselor();
            counselor.setUserId(userId);
            counselor.setSpecialty(specialty);
            counselor.setPhoto("/img/" + fileName);
            counselor.setGender(gender);
            dao.addCounselor(counselor);

            User user = dao.getUserById(userId);
            if (user != null) {
                user.setCounselor(true);
                // 비밀번호를 null로 설정하여 기존 비밀번호를 유지하도록 함
                user.setPassword(null);
                dao.updateUser(user);
            }

            return "myPage.jsp?success=requested";
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

    private String revokeCounselor(HttpServletRequest request, HttpServletResponse response) {
        int userId = Integer.parseInt(request.getParameter("userId"));

        try {
            User user = dao.getUserById(userId);
            if (user != null && user.isCounselor()) {
                if (dao.hasReservations(userId)) {
                    request.setAttribute("error", "예약 일정이 있어, 권한을 회수할 수 없습니다.");
                    return userList(request, response);
                }
                user.setCounselor(false);
                dao.updateUser(user, true); // 상담사 권한 업데이트 메서드 호출
                dao.revokeCounselor(userId); // 상담사 테이블에서 삭제
                request.setAttribute("message", "상담 권한이 성공적으로 회수되었습니다.");
                return userList(request, response); // 권한 회수 후 사용자 목록 페이지로 이동
            } else {
                request.setAttribute("error", "사용자를 찾을 수 없거나 이미 상담사 권한이 없습니다.");
                return "adminUserList.jsp";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }


    private String getFilename(Part part) {
        String fileName = null;
        String header = part.getHeader("content-disposition");
        int start = header.indexOf("filename=");
        fileName = header.substring(start + 10, header.length() - 1);
        return fileName;
    }

    
    
    // 상담사
    private String counselorList(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Counselor> counselors = dao.getAllCounselors();
            request.setAttribute("counselors", counselors);
            return "counselorList.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

    private String addCounselor(HttpServletRequest request, HttpServletResponse response) {
        return "addCounselor.jsp";
    }

    private String updateCounselor(HttpServletRequest request, HttpServletResponse response) {
        try {
            int counselorId = Integer.parseInt(request.getParameter("counselorId"));
            Counselor counselor = dao.getCounselorById(counselorId);
            if (counselor != null) {
                request.setAttribute("counselor", counselor);
                return "updateCounselor.jsp";
            } else {
                return "error.jsp";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

    private String processUpdateCounselor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (Integer) session.getAttribute("userId");
        String specialty = request.getParameter("specialty");
        String gender = request.getParameter("gender");

        Part part = request.getPart("photo");
        String fileName = getFilename(part);
        if (fileName != null && !fileName.isEmpty()) {
            part.write("c:/Temp/img/" + fileName);
        }

        try {
            Counselor counselor = new Counselor();
            counselor.setUserId(userId);
            counselor.setSpecialty(specialty);
            if (fileName != null && !fileName.isEmpty()) {
                counselor.setPhoto("/img/" + fileName);
            }
            counselor.setGender(gender);
            dao.updateCounselor(counselor);

            // 상담사 정보 수정 완료 알림 추가
            return "myPage.jsp?success=counselorUpdate";
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }


    private String editCounselorInfo(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        int userId = (Integer) session.getAttribute("userId");

        try {
            Counselor counselor = dao.getCounselorByUserId(userId);
            request.setAttribute("counselor", counselor);
            return "updateCounselor.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

    
    // 상담 예약
    private String makeReservation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/control?action=login&redirect=makeReservation&counselorId=" + request.getParameter("counselorId"));
            return null;
        }

        int counselorId = Integer.parseInt(request.getParameter("counselorId"));
        Date date = request.getParameter("date") != null && !request.getParameter("date").isEmpty() ? Date.valueOf(request.getParameter("date")) : null;

        try {
            List<String> reservedSlots = date != null ? dao.getReservedTimeSlots(counselorId, date) : new ArrayList<>();
            request.setAttribute("reservedSlots", reservedSlots);
            request.setAttribute("counselorId", counselorId);
            request.setAttribute("selectedDate", date);

            // 상담사 이름 가져오기
            String counselorName = dao.getCounselorNameById(counselorId);
            request.setAttribute("counselorName", counselorName);

            return "makeReservation.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

    private String checkAvailableSlots(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int counselorId = Integer.parseInt(request.getParameter("counselorId"));
            Date date = Date.valueOf(request.getParameter("date"));
            
            List<String> reservedSlots = dao.getReservedTimeSlots(counselorId, date);
            
            response.setContentType("application/json");
            Gson gson = new Gson();
            String json = gson.toJson(reservedSlots);
            response.getWriter().write(json);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }

    private String processMakeReservation(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            // 사용자 ID가 null인 경우 로그인 페이지로 리다이렉트
            return "login.jsp?error=notLoggedIn";
        }

        int counselorId = Integer.parseInt(request.getParameter("counselorId"));
        Date date = Date.valueOf(request.getParameter("date"));
        String timeSlot = request.getParameter("timeSlot");

        // 시간대에 따라 예약 시작 시간과 종료 시간을 설정합니다.
        Time startTime;
        Time endTime;

        switch (timeSlot) {
            case "morning":
                startTime = Time.valueOf("10:00:00");
                endTime = Time.valueOf("12:00:00");
                break;
            case "afternoon":
                startTime = Time.valueOf("13:00:00");
                endTime = Time.valueOf("15:00:00");
                break;
            case "evening":
                startTime = Time.valueOf("15:00:00");
                endTime = Time.valueOf("17:00:00");
                break;
            default:
                throw new IllegalArgumentException("Invalid time slot selected");
        }

        try {
            if (!dao.isTimeSlotAvailable(counselorId, date, startTime, endTime)) {
                throw new IllegalArgumentException("Selected time slot is not available");
            }

            Reservation reservation = new Reservation();
            reservation.setUserId(userId);
            reservation.setCounselorId(counselorId);
            reservation.setReservationDate(date);
            reservation.setStartTime(startTime);
            reservation.setEndTime(endTime);
            reservation.setStatus("예약됨"); // 상태 기본값 설정

            dao.addReservation(reservation);

            List<Reservation> reservations = dao.getReservationsByUserId(userId);
            request.setAttribute("reservations", reservations);
            return "myPage.jsp?success=reservation";
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

    private String reservationList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            // 사용자 ID가 null인 경우 로그인 페이지로 리다이렉트
            return "login.jsp?error=notLoggedIn";
        }

        try {
            List<Reservation> reservations = dao.getReservationsByUserId(userId);

            request.setAttribute("reservations", reservations); // 세션이 아닌 요청 속성에 저장
            return "reservationList.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

    private String cancelReservation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp?error=notLoggedIn");
            return null;
        }

        String reservationIdStr = request.getParameter("reservationId");

        int reservationId = Integer.parseInt(reservationIdStr);

        try {
            dao.cancelReservation(reservationId, userId);

            response.sendRedirect("control?action=myPage&tab=reservationList&success=cancel");
            return null; // sendRedirect 후 반환
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
            return null;
        }
    }
    
    
    // 상담 관리
    private String counselingManagement(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "login.jsp?error=notLoggedIn";
        }

        try {
            Counselor counselor = dao.getCounselorByUserId(userId);
            if (counselor == null) {
                return "error.jsp?message=No counselor found for user ID: " + userId;
            }

            int counselorId = counselor.getId();
            List<Reservation> completedReservations = dao.getCompletedReservationsByCounselorId(counselorId);
            request.setAttribute("completedReservations", completedReservations);
            return "counselingManagement.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            return "error.jsp";
        }
    }

 // 상담 기록 페이지로 이동
    private String counselingRecord(HttpServletRequest request, HttpServletResponse response) {
    	int reservationId = Integer.parseInt(request.getParameter("reservationId"));
    	try {
    		CounselingRecord record = dao.getCounselingRecordByReservationId(reservationId);
    		request.setAttribute("counselingRecord", record);
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return "error.jsp";
    	}
    	request.setAttribute("reservationId", reservationId);
    	return "counselingRecord.jsp";
    }

 // 상담 기록 저장
    private String saveCounselingRecord(HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();
    	Integer userId = (Integer) session.getAttribute("userId");

    	if (userId == null) {
    		return "login.jsp?error=notLoggedIn";
    	}

    	int reservationId = Integer.parseInt(request.getParameter("reservationId"));
    	String content = request.getParameter("counselingContent");

    	try {
    		Reservation reservation = dao.getReservationById(reservationId);
    		if (reservation == null) {
    			return "error.jsp?message=No reservation found for reservation ID: " + reservationId;
    		}

    		CounselingRecord record = new CounselingRecord();
    		record.setReservationId(reservationId);
    		record.setUserId(reservation.getUserId()); // userId 설정
    		record.setCounselorId(reservation.getCounselorId()); // 올바른 counselorId 설정
    		record.setContent(content);

    		dao.saveOrUpdateCounselingRecord(record);
    		response.sendRedirect(request.getContextPath() + "/control?action=counselingManagement&status=success");
    		return null; // sendRedirect를 호출했으므로 null을 반환하여 추가적인 포워딩을 방지
    	} catch (SQLException | IOException e) {
    		e.printStackTrace();
    		return "error.jsp";
    	}
    }

    
    // 게시판
    private String boardList(HttpServletRequest request, HttpServletResponse response) {
    	try {
    		List<Board> boards = dao.getAllBoards();
    		request.setAttribute("boards", boards);
    		return "boardList.jsp";
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return "error.jsp";
    	}
    }

	private String addBoard(HttpServletRequest request, HttpServletResponse response) {
	    HttpSession session = request.getSession();
	    Integer userId = (Integer) session.getAttribute("userId");

	    if (userId == null) {
	        return "login.jsp?error=notLoggedIn";
	    }

	    return "addBoard.jsp";
	}

	private String processAddBoard(HttpServletRequest request, HttpServletResponse response) {
	    HttpSession session = request.getSession();
	    Integer userId = (Integer) session.getAttribute("userId");

	    if (userId == null) {
	        return "login.jsp?error=notLoggedIn";
	    }

	    String title = request.getParameter("title");
	    String content = request.getParameter("content");

	    try {
	        Board board = new Board();
	        board.setUserId(userId);
	        board.setTitle(title);
	        board.setContent(content);
	        board.setCreatedAt(new Timestamp(System.currentTimeMillis()));
	        board.setViewCount(0);

	        dao.addBoard(board);
	        response.sendRedirect(request.getContextPath() + "/control?action=boardList&status=added");
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "error.jsp";
	    }
	}

	
	private String viewBoard(HttpServletRequest request, HttpServletResponse response) {
	    int boardId = Integer.parseInt(request.getParameter("id"));

	    try {
	        // 게시물 정보 가져오기
	        Board board = dao.getBoardById(boardId);
	        request.setAttribute("board", board);

	        // 조회수 증가
	        dao.incrementViewCount(boardId);

	        // 댓글 목록 가져오기
	        List<BoardComment> comments = dao.getCommentsByBoardId(boardId);
	        request.setAttribute("comments", comments);

	        return "boardView.jsp";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "error.jsp";
	    }
	}


	private String editBoard(HttpServletRequest request, HttpServletResponse response) {
	    int boardId = Integer.parseInt(request.getParameter("id"));

	    try {
	        Board board = dao.getBoardById(boardId);
	        request.setAttribute("board", board);
	        return "editBoard.jsp";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "error.jsp";
	    }
	}

	private String updateBoard(HttpServletRequest request, HttpServletResponse response) {
	    int boardId = Integer.parseInt(request.getParameter("id"));
	    String title = request.getParameter("title");
	    String content = request.getParameter("content");

	    try {
	        Board board = new Board();
	        board.setId(boardId);
	        board.setTitle(title);
	        board.setContent(content);
	        dao.updateBoard(board);
	        response.sendRedirect(request.getContextPath() + "/control?action=viewBoard&id=" + boardId);
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "error.jsp";
	    }
	}

	// 삭제 기능 추가
	private String deleteBoard(HttpServletRequest request, HttpServletResponse response) {
	    int boardId = Integer.parseInt(request.getParameter("boardId"));
	    try {
	        // 댓글이 있는지 확인
	        List<BoardComment> comments = dao.getCommentsByBoardId(boardId);
	        if (!comments.isEmpty()) {
	            request.setAttribute("alertMessage", "댓글이 생긴 게시물은 삭제할 수 없습니다.");
	            return viewBoardWithAlert(request, response);
	        }

	        dao.deleteBoard(boardId);
	        response.sendRedirect(request.getContextPath() + "/control?action=boardList");
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "error.jsp";
	    }
	}

	private String viewBoardWithAlert(HttpServletRequest request, HttpServletResponse response) {
	    int boardId = Integer.parseInt(request.getParameter("boardId"));

	    try {
	        // 게시물 정보 가져오기
	        Board board = dao.getBoardById(boardId);
	        request.setAttribute("board", board);

	        // 댓글 목록 가져오기
	        List<BoardComment> comments = dao.getCommentsByBoardId(boardId);
	        request.setAttribute("comments", comments);

	        return "boardView.jsp";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "error.jsp";
	    }
	}

	
	private String addBoardComment(HttpServletRequest request, HttpServletResponse response) {
	    HttpSession session = request.getSession();
	    Integer userId = (Integer) session.getAttribute("userId");

	    if (userId == null) {
	        return "login.jsp?error=notLoggedIn";
	    }

	    int boardId = Integer.parseInt(request.getParameter("boardId"));
	    String content = request.getParameter("content");

	    try {
	        BoardComment comment = new BoardComment();
	        comment.setBoardId(boardId);
	        comment.setUserId(userId);
	        comment.setContent(content);
	        dao.saveBoardComment(comment);
	        response.sendRedirect(request.getContextPath() + "/control?action=viewBoard&id=" + boardId);
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "error.jsp";
	    }
	}

	// 댓글 삭제 기능 추가
	private String deleteBoardComment(HttpServletRequest request, HttpServletResponse response) {
	    int commentId = Integer.parseInt(request.getParameter("commentId"));
	    int boardId = Integer.parseInt(request.getParameter("boardId"));
	    HttpSession session = request.getSession();
	    Integer userId = (Integer) session.getAttribute("userId");

	    if (userId == null) {
	        return "login.jsp?error=notLoggedIn";
	    }

	    try {
	        BoardComment comment = dao.getBoardCommentById(commentId);

	        // 댓글 작성자 또는 관리자인 경우에만 삭제
	        if (comment.getUserId() == userId || userId == 0) {
	            dao.deleteBoardComment(commentId);
	        }

	        response.sendRedirect(request.getContextPath() + "/control?action=viewBoard&id=" + boardId);
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "error.jsp";
	    }
	}

}
