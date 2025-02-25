package counseling;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

// H2 데이터베이스 연동 DAO 클래스
public class counselingDAO {
    private final String JDBC_DRIVER = "org.h2.Driver";
    private final String JDBC_URL = "jdbc:h2:tcp://localhost:9092/~/counselingMe";
    private final String USERNAME = "choiyujin";
    private final String PASSWORD = "1234";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
    }

    // 회원
    public boolean isEmailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public void addUser(User user) throws SQLException {
        if (isEmailExists(user.getEmail())) {
            throw new SQLException("Duplicate email");
        }

        String sql = "INSERT INTO Users (email, name, password, phone_number, address, birth_date, is_counselor, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getAddress());
            pstmt.setDate(6, user.getBirthDate());
            pstmt.setBoolean(7, user.isCounselor());
            pstmt.executeUpdate();
        }
    }

    public User getUserByEmailAndPassword(String email, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setEmail(rs.getString("email"));
                        user.setName(rs.getString("name"));
                        user.setPhoneNumber(rs.getString("phone_number"));
                        user.setAddress(rs.getString("address"));
                        user.setBirthDate(rs.getDate("birth_date"));
                        user.setPassword(rs.getString("password"));
                        user.setCounselor(rs.getBoolean("is_counselor"));
                        return user;
                    }
                }
            }
        }
        return null;
    }
    
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE Users SET name = ?, phone_number = ?, address = ?, birth_date = ?";

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            sql += ", password = ?";
        }

        sql += " WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPhoneNumber());
            pstmt.setString(3, user.getAddress());
            pstmt.setDate(4, user.getBirthDate());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                pstmt.setString(5, user.getPassword());
                pstmt.setInt(6, user.getId());
            } else {
                pstmt.setInt(5, user.getId());
            }

            pstmt.executeUpdate();
        }
    }


    // 상담사 권한 업데이트 메서드
    public void updateUser(User user, boolean updateCounselorStatus) throws SQLException {
        String sql = "UPDATE Users SET is_counselor = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, user.isCounselor());
            pstmt.setInt(2, user.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteUser(String email) throws SQLException {
        String sql = "DELETE FROM Users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.executeUpdate();
        }
    }
    
    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT u.id, u.email, u.name, u.phone_number, u.address, u.birth_date, u.is_counselor, u.created_at, " +
                     "(SELECT COUNT(*) FROM counselors c WHERE c.user_id = u.id) AS counselorRequested " +
                     "FROM Users u WHERE u.id != 0 ORDER BY counselorRequested DESC, u.id ASC";
        List<User> users = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setAddress(rs.getString("address"));
                user.setBirthDate(rs.getDate("birth_date"));
                user.setCounselor(rs.getBoolean("is_counselor"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setCounselorRequested(rs.getInt("counselorRequested") > 0);
                users.add(user);
            }
        }
        return users;
    }



    // 관리자
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setName(rs.getString("name"));
                    user.setPhoneNumber(rs.getString("phone_number"));
                    user.setAddress(rs.getString("address"));
                    user.setBirthDate(rs.getDate("birth_date"));
                    user.setCounselor(rs.getBoolean("is_counselor"));
                    user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return user;
                }
            }
        }
        return null;
    }

    public boolean hasReservations(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations WHERE counselor_id = (SELECT id FROM counselors WHERE user_id = ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void revokeCounselor(int userId) throws SQLException {
        String sql = "DELETE FROM counselors WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }

        sql = "UPDATE Users SET is_counselor = FALSE WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }

    

    // 상담사
    public void addCounselor(Counselor counselor) throws SQLException {
        String sql = "INSERT INTO counselors (user_id, specialty, photo, gender) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, counselor.getUserId());
            pstmt.setString(2, counselor.getSpecialty());
            pstmt.setString(3, counselor.getPhoto());
            pstmt.setString(4, counselor.getGender());
            pstmt.executeUpdate();
        }
    }

    public Counselor getCounselorById(int id) throws SQLException {
        String sql = "SELECT c.*, u.id AS user_id, u.name, u.email, u.phone_number, u.address, u.birth_date, u.is_counselor " +
                     "FROM counselors c " +
                     "JOIN Users u ON c.user_id = u.id " +
                     "WHERE c.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Counselor counselor = new Counselor();
                    counselor.setId(rs.getInt("id"));
                    counselor.setUserId(rs.getInt("user_id"));
                    counselor.setSpecialty(rs.getString("specialty"));
                    counselor.setPhoto(rs.getString("photo"));
                    counselor.setGender(rs.getString("gender"));

                    User user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhoneNumber(rs.getString("phone_number"));
                    user.setAddress(rs.getString("address"));
                    user.setBirthDate(rs.getDate("birth_date"));
                    user.setCounselor(rs.getBoolean("is_counselor"));
                    
                    counselor.setUser(user); // User 객체 설정
                    return counselor;
                }
            }
        }
        return null;
    }


 // 상담사 이름 가져오기 메서드 추가
    public String getCounselorNameById(int counselorId) throws SQLException {
        String sql = "SELECT u.name FROM Users u JOIN counselors c ON u.id = c.user_id WHERE c.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, counselorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }
        return null;
    }


    public Counselor getCounselorByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM counselors WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Counselor counselor = new Counselor();
                    counselor.setId(rs.getInt("id"));
                    counselor.setUserId(rs.getInt("user_id"));
                    counselor.setSpecialty(rs.getString("specialty"));
                    counselor.setPhoto(rs.getString("photo"));
                    counselor.setGender(rs.getString("gender"));
                    return counselor;
                }
            }
        }
        return null;
    }
    
    public List<Counselor> getAllCounselors() throws SQLException {
        String sql = "SELECT c.*, u.id AS user_id, u.name, u.birth_date, u.address " +
                     "FROM counselors c " +
                     "JOIN Users u ON c.user_id = u.id " +
                     "WHERE u.is_counselor = TRUE";
        List<Counselor> counselors = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Counselor counselor = new Counselor();
                counselor.setId(rs.getInt("id"));
                counselor.setUserId(rs.getInt("user_id"));
                counselor.setSpecialty(rs.getString("specialty"));
                counselor.setPhoto(rs.getString("photo"));
                counselor.setGender(rs.getString("gender"));

                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setBirthDate(rs.getDate("birth_date"));
                user.setAddress(rs.getString("address"));
                counselor.setUser(user);

                counselors.add(counselor);
            }
        }
        return counselors;
    }


    public void updateCounselor(Counselor counselor) throws SQLException {
        String sql = "UPDATE counselors SET specialty = ?, photo = ?, gender = ? WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, counselor.getSpecialty());
            pstmt.setString(2, counselor.getPhoto());
            pstmt.setString(3, counselor.getGender());
            pstmt.setInt(4, counselor.getUserId());
            pstmt.executeUpdate();
        }
    }

    public List<Counselor> getAllCounselorsWithUserDetails() throws SQLException {
        String sql = "SELECT c.id AS counselor_id, c.user_id, c.specialty, c.photo, c.gender, " +
                     "u.email, u.name, u.phone_number, u.address, u.birth_date, u.is_counselor, u.created_at " +
                     "FROM counselors c JOIN Users u ON c.user_id = u.id";
        List<Counselor> counselors = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Counselor counselor = new Counselor();
                counselor.setId(rs.getInt("counselor_id"));
                counselor.setUserId(rs.getInt("user_id"));
                counselor.setSpecialty(rs.getString("specialty"));
                counselor.setPhoto(rs.getString("photo"));
                counselor.setGender(rs.getString("gender"));

                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setAddress(rs.getString("address"));
                user.setBirthDate(rs.getDate("birth_date"));
                user.setCounselor(rs.getBoolean("is_counselor"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                counselor.setUser(user); // User 객체 설정
                counselors.add(counselor);
            }
        }
        return counselors;
    }


 // 상담 예약 추가 메서드
    public void addReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO Reservations (user_id, counselor_id, reservation_date, start_time, end_time, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservation.getUserId());
            pstmt.setInt(2, reservation.getCounselorId());
            pstmt.setDate(3, reservation.getReservationDate());
            pstmt.setTime(4, reservation.getStartTime());
            pstmt.setTime(5, reservation.getEndTime());
            pstmt.setString(6, reservation.getStatus());
            pstmt.executeUpdate();
        }
    }


    public boolean isTimeSlotAvailable(int counselorId, Date date, Time startTime, Time endTime) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Reservations WHERE counselor_id = ? AND reservation_date = ? AND " +
                     "(? < end_time AND ? > start_time)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, counselorId);
            pstmt.setDate(2, date);
            pstmt.setTime(3, startTime);
            pstmt.setTime(4, endTime);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return false;
    }


    // 예약된 시간대 조회 메서드
    public List<String> getReservedTimeSlots(int counselorId, Date date) throws SQLException {
        String sql = "SELECT start_time, end_time FROM Reservations WHERE counselor_id = ? AND reservation_date = ?";
        List<String> reservedSlots = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, counselorId);
            pstmt.setDate(2, date);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Time startTime = rs.getTime("start_time");
                    Time endTime = rs.getTime("end_time");
                    if (startTime.equals(Time.valueOf("10:00:00")) && endTime.equals(Time.valueOf("12:00:00"))) {
                        reservedSlots.add("morning");
                    } else if (startTime.equals(Time.valueOf("13:00:00")) && endTime.equals(Time.valueOf("15:00:00"))) {
                        reservedSlots.add("afternoon");
                    } else if (startTime.equals(Time.valueOf("15:00:00")) && endTime.equals(Time.valueOf("17:00:00"))) {
                        reservedSlots.add("evening");
                    }
                }
            }
        }
        return reservedSlots;
    }

    // 상담 예약 목록 조회 메서드 (추가 기능)
    public List<Reservation> getAllReservations() throws SQLException {
        String sql = "SELECT * FROM Reservations";
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setUserId(rs.getInt("user_id"));
                reservation.setCounselorId(rs.getInt("counselor_id"));
                reservation.setReservationDate(rs.getDate("reservation_date"));
                reservation.setStartTime(rs.getTime("reservation_time"));
                reservation.setEndTime(rs.getTime("end_time"));
                reservation.setStatus(rs.getString("status"));
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public List<Reservation> getReservationsByUserId(int userId) throws SQLException {
        String sql = "SELECT r.id, r.reservation_date, r.start_time, r.end_time, r.status, cu.name AS counselor_name " +
                     "FROM Reservations r " +
                     "JOIN Counselors c ON r.counselor_id = c.id " +
                     "JOIN Users cu ON c.user_id = cu.id " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY r.reservation_date ASC, r.start_time ASC";  // 날짜와 시간 기준으로 정렬
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setReservationDate(rs.getDate("reservation_date"));
                    reservation.setStartTime(rs.getTime("start_time"));
                    reservation.setEndTime(rs.getTime("end_time"));
                    reservation.setStatus(rs.getString("status"));
                    reservation.setCounselorName(rs.getString("counselor_name"));
                    reservations.add(reservation);
                }
            }
        }
        return reservations;
    }

    public List<Reservation> getReservationsByCounselorId(int counselorId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.id, r.reservation_date, r.start_time, r.end_time, r.status, u.name AS user_name " +
                     "FROM Reservations r " +
                     "JOIN Users u ON r.user_id = u.id " +
                     "WHERE r.counselor_id = ? " +
                     "ORDER BY r.status DESC, r.reservation_date ASC, r.start_time DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, counselorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setReservationDate(rs.getDate("reservation_date"));
                    reservation.setStartTime(rs.getTime("start_time"));
                    reservation.setEndTime(rs.getTime("end_time"));
                    reservation.setStatus(rs.getString("status"));
                    reservation.setUserName(rs.getString("user_name"));
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }




    // 상담사 ID 존재 여부 확인 메서드
    public boolean isCounselorIdExists(int counselorId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Counselors WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, counselorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public int cancelReservation(int reservationId, int userId) throws SQLException {
        String sql = "DELETE FROM Reservations WHERE id = ? AND user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate();
        }
    }

    public void updateReservationStatus(int userId) throws SQLException {
        String sql = "UPDATE Reservations SET status = '상담 완료' WHERE user_id = ? AND reservation_date < CURRENT_DATE() OR (reservation_date = CURRENT_DATE() AND end_time < CURRENT_TIME()) AND status = '예약됨'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }

    public List<Reservation> getCompletedReservationsByCounselorId(int counselorId) throws SQLException {
        String sql = "SELECT r.id, r.user_id, r.reservation_date, r.start_time, r.end_time, u.name AS user_name " +
                     "FROM Reservations r " +
                     "JOIN Users u ON r.user_id = u.id " +
                     "WHERE r.counselor_id = ? AND r.status = '상담 완료'";
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, counselorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setUserId(rs.getInt("user_id"));
                    reservation.setReservationDate(rs.getDate("reservation_date"));
                    reservation.setStartTime(rs.getTime("start_time"));
                    reservation.setEndTime(rs.getTime("end_time"));
                    reservation.setUserName(rs.getString("user_name"));
                    reservation.setStatus("상담 완료");
                    reservations.add(reservation);
                }
            }
        }
        return reservations;
    }

    public void saveCounselingRecord(CounselingRecord record) throws SQLException {
        String sql = "INSERT INTO CounselingRecords (reservation_id, user_id, counselor_id, content) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, record.getReservationId());
            pstmt.setInt(2, record.getUserId()); // 추가된 필드
            pstmt.setInt(3, record.getCounselorId());
            pstmt.setString(4, record.getContent());
            pstmt.executeUpdate();
        }
    }

    public Reservation getReservationById(int reservationId) throws SQLException {
        String sql = "SELECT * FROM Reservations WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setUserId(rs.getInt("user_id"));
                    reservation.setCounselorId(rs.getInt("counselor_id"));
                    reservation.setReservationDate(rs.getDate("reservation_date"));
                    reservation.setStartTime(rs.getTime("start_time"));
                    reservation.setEndTime(rs.getTime("end_time"));
                    reservation.setStatus(rs.getString("status"));
                    return reservation;
                }
            }
        }
        return null;
    }

 // 상담 기록 저장 메서드
 public void saveOrUpdateCounselingRecord(CounselingRecord record) throws SQLException {
     String selectSql = "SELECT COUNT(*) FROM CounselingRecords WHERE reservation_id = ?";
     String insertSql = "INSERT INTO CounselingRecords (reservation_id, user_id, counselor_id, content, updated_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
     String updateSql = "UPDATE CounselingRecords SET content = ?, updated_at = CURRENT_TIMESTAMP WHERE reservation_id = ?";

     try (Connection conn = getConnection();
          PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
         
         selectPstmt.setInt(1, record.getReservationId());
         try (ResultSet rs = selectPstmt.executeQuery()) {
             rs.next();
             int count = rs.getInt(1);

             if (count == 0) {
                 try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                     insertPstmt.setInt(1, record.getReservationId());
                     insertPstmt.setInt(2, record.getUserId());
                     insertPstmt.setInt(3, record.getCounselorId());
                     insertPstmt.setString(4, record.getContent());
                     insertPstmt.executeUpdate();
                 }
             } else {
                 try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                     updatePstmt.setString(1, record.getContent());
                     updatePstmt.setInt(2, record.getReservationId());
                     updatePstmt.executeUpdate();
                 }
             }
         }
     }
 }

 // 특정 예약 ID로 상담 기록 조회 메서드
 public CounselingRecord getCounselingRecordByReservationId(int reservationId) throws SQLException {
     String sql = "SELECT * FROM CounselingRecords WHERE reservation_id = ?";
     try (Connection conn = getConnection();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setInt(1, reservationId);
         try (ResultSet rs = pstmt.executeQuery()) {
             if (rs.next()) {
                 CounselingRecord record = new CounselingRecord();
                 record.setId(rs.getInt("id"));
                 record.setReservationId(rs.getInt("reservation_id"));
                 record.setUserId(rs.getInt("user_id"));
                 record.setCounselorId(rs.getInt("counselor_id"));
                 record.setContent(rs.getString("content"));
                 record.setUpdatedAt(rs.getTimestamp("updated_at"));
                 return record;
             }
         }
     }
     return null;
 }

 
//Board 관련 메서드들

 public int getNextAvailableBoardId() throws SQLException {
	    String sql = "SELECT MIN(t1.id + 1) AS nextId FROM Board t1 LEFT JOIN Board t2 ON t1.id + 1 = t2.id WHERE t2.id IS NULL";
	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {
	        if (rs.next()) {
	            return rs.getInt("nextId");
	        }
	        return 1; // 기본값 1
	    }
	}

	public void addBoard(Board board) throws SQLException {
	    int nextId = getNextAvailableBoardId();
	    String sql = "INSERT INTO Board (id, user_id, title, content, created_at, view_count) VALUES (?, ?, ?, ?, ?, ?)";
	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, nextId);
	        pstmt.setInt(2, board.getUserId());
	        pstmt.setString(3, board.getTitle());
	        pstmt.setString(4, board.getContent());
	        pstmt.setTimestamp(5, board.getCreatedAt());
	        pstmt.setInt(6, board.getViewCount());
	        pstmt.executeUpdate();
	    }
	}


 public Board getBoardById(int id) throws SQLException {
	    String sql = "SELECT b.*, u.name AS user_name FROM Board b JOIN Users u ON b.user_id = u.id WHERE b.id = ?";
	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, id);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                Board board = new Board();
	                board.setId(rs.getInt("id"));
	                board.setUserId(rs.getInt("user_id"));
	                board.setTitle(rs.getString("title"));
	                board.setContent(rs.getString("content"));
	                board.setCreatedAt(rs.getTimestamp("created_at"));
	                board.setViewCount(rs.getInt("view_count"));
	                board.setUserName(rs.getString("user_name"));
	                return board;
	            }
	        }
	    }
	    return null;
	}

	public void incrementViewCount(int id) throws SQLException {
	    String sql = "UPDATE Board SET view_count = view_count + 1 WHERE id = ?";
	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, id);
	        pstmt.executeUpdate();
	    }
	}


 public List<Board> getAllBoards() throws SQLException {
	    String sql = "SELECT b.*, u.name AS user_name FROM Board b JOIN Users u ON b.user_id = u.id ORDER BY b.id DESC";
	    List<Board> boards = new ArrayList<>();
	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {
	        while (rs.next()) {
	            Board board = new Board();
	            board.setId(rs.getInt("id"));
	            board.setUserId(rs.getInt("user_id"));
	            board.setTitle(rs.getString("title"));
	            board.setContent(rs.getString("content"));
	            board.setCreatedAt(rs.getTimestamp("created_at"));
	            board.setViewCount(rs.getInt("view_count"));
	            board.setUserName(rs.getString("user_name"));
	            boards.add(board);
	        }
	    }
	    return boards;
	}

	


 public void updateBoard(Board board) throws SQLException {
	    String sql = "UPDATE Board SET title = ?, content = ? WHERE id = ?";
	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, board.getTitle());
	        pstmt.setString(2, board.getContent());
	        pstmt.setInt(3, board.getId());
	        pstmt.executeUpdate();
	    }
	}



 public void deleteBoard(int boardId) throws SQLException {
	    String sql = "DELETE FROM Board WHERE id = ?";
	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, boardId);
	        pstmt.executeUpdate();
	    }
	}

 public void addBoardComment(BoardComment comment) throws SQLException {
	    String sql = "INSERT INTO BoardComments (board_id, user_id, content, created_at) VALUES (?, ?, ?, ?)";
	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, comment.getBoardId());
	        pstmt.setInt(2, comment.getUserId());
	        pstmt.setString(3, comment.getContent());
	        pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
	        pstmt.executeUpdate();
	    }
	}

 public List<BoardComment> getCommentsByBoardId(int boardId) throws SQLException {
	    String sql = "SELECT bc.id, bc.board_id, bc.user_id, bc.content, bc.created_at, u.name AS user_name " +
	                 "FROM BoardComments bc " +
	                 "JOIN Users u ON bc.user_id = u.id " +
	                 "WHERE bc.board_id = ?";
	    List<BoardComment> comments = new ArrayList<>();
	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, boardId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                BoardComment comment = new BoardComment();
	                comment.setId(rs.getInt("id"));
	                comment.setBoardId(rs.getInt("board_id"));
	                comment.setUserId(rs.getInt("user_id"));
	                comment.setContent(rs.getString("content"));
	                comment.setCreatedAt(rs.getTimestamp("created_at"));
	                comment.setUserName(rs.getString("user_name"));
	                comments.add(comment);
	            }
	        }
	    }
	    return comments;
	}

	
	public void saveBoardComment(BoardComment comment) throws SQLException {
	    String sql = "INSERT INTO BoardComments (board_id, user_id, content) VALUES (?, ?, ?)";
	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, comment.getBoardId());
	        pstmt.setInt(2, comment.getUserId());
	        pstmt.setString(3, comment.getContent());
	        pstmt.executeUpdate();
	    }
	}
	
	public BoardComment getBoardCommentById(int commentId) throws SQLException {
        String sql = "SELECT bc.*, u.name AS user_name FROM BoardComments bc " +
                     "JOIN Users u ON bc.user_id = u.id WHERE bc.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, commentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    BoardComment comment = new BoardComment();
                    comment.setId(rs.getInt("id"));
                    comment.setBoardId(rs.getInt("board_id"));
                    comment.setUserId(rs.getInt("user_id"));
                    comment.setUserName(rs.getString("user_name"));
                    comment.setContent(rs.getString("content"));
                    comment.setCreatedAt(rs.getTimestamp("created_at"));
                    return comment;
                }
            }
        }
        return null;
    }
	
	public boolean hasComments(int boardId) throws SQLException {
	    String sql = "SELECT COUNT(*) FROM BoardComments WHERE board_id = ?";
	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, boardId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1) > 0;
	            }
	        }
	    }
	    return false;
	}

	// 댓글 삭제 메서드 추가
	public void deleteBoardComment(int commentId) throws SQLException {
	    String sql = "DELETE FROM BoardComments WHERE id = ?";
	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, commentId);
	        pstmt.executeUpdate();
	    }
	}

}
