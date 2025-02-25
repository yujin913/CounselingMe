package counseling;

import java.sql.Date;
import java.time.LocalDateTime;

// 사용자 엔티티 클래스
public class User {
    private int id;
    private String email;
    private String name;
    private String password;
    private String phoneNumber;
    private Date birthDate;
    private String address;
    private boolean isCounselor;
    private boolean counselorRequested;
    private LocalDateTime createdAt;
    

    public int getId() {
    	return id;
    }
    
    public void setId(int id) {
    	this.id = id;
    }

    public String getEmail() {
    	return email; 
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }

    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }

    public String getPassword() {
    	return password;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }

    public String getPhoneNumber() {
    	return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
    	this.phoneNumber = phoneNumber;
    }

    public Date getBirthDate() {
    	return birthDate;
    }
    
    public void setBirthDate(Date birthDate) {
    	this.birthDate = birthDate;
    }

    public String getAddress() {
    	return address;
    }
    
    public void setAddress(String address) {
    	this.address = address;
    }

    public boolean isCounselor() {
    	return isCounselor;
    }

    public void setCounselor(boolean isCounselor) {
    	this.isCounselor = isCounselor;
    }

    public boolean isCounselorRequested() {
    	return counselorRequested;
    }

    public void setCounselorRequested(boolean counselorRequested) {
    	this.counselorRequested = counselorRequested;
    }

    public LocalDateTime getCreatedAt() {
    	return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
    	this.createdAt = createdAt;
    }
}
