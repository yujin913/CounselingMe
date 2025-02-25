package counseling;

import java.sql.Timestamp;

// 상담 기록 엔티티 클래스
public class CounselingRecord {
    private int id;
    private int reservationId;
    private int userId;
    private int counselorId;
    private String content;
    private Timestamp updatedAt;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(int counselorId) {
        this.counselorId = counselorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
