package org.openxdata.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import org.openxdata.server.admin.model.User;

/**
 *
 * @author victor
 */
public class UserSummary extends BaseModel {

	private static final long serialVersionUID = -483245623678099579L;
	
	private User user;

    public UserSummary() {
    }

    public UserSummary(User user) {
        setUser(user);
    }

    public UserSummary(String id, String userName) {
        setId(id);
        setName(userName);
    }

    public void setId(String id) {
        set("id", id);
    }

    public String getId() {
        return get("id");
    }

    public void setName(String userName) {
        set("name", userName);
    }

    public String getName() {
        return get("name");
    }
    
    public void setFirstName(String firstName) {
        set("firstName", firstName);
    }

    public String getFirstName() {
        return get("firstName");
    }
    
    public void setMiddleName(String middleName) {
        set("middleName", middleName);
    }

    public String getMiddleName() {
        return get("middleName");
    }
    
    public void setLastName(String lastName) {
        set("lastName", lastName);
    }

    public String getLastName() {
        return get("lastName");
    }
    
    public void setStatus(String status) {
        set("status", status);
    }

    public String getStatus() {
        return get("status");
    }
    
    public void setEmail(String email) {
        set("email", email);
    }

    public String getEmail() {
        return get("email");
    }
    
    public void setPhoneNo(String phoneNo) {
        set("phoneNo", phoneNo);
    }

    public String getPhoneNo() {
        return get("phoneNo");
    }

    public void setUser(User user) {
        this.user = user;
        updateUser(user);
    }

    public User getUser() {
        return user;
    }

    public void updateUser(User user) {
        setId(String.valueOf(user.getId()));
        setName(user.getName());
        setFirstName(user.getFirstName());
        setMiddleName(user.getMiddleName());
        setLastName(user.getLastName());
        setStatus(user.getStatusType());
        setEmail(user.getEmail());
        setPhoneNo(user.getPhoneNo());
    }
}