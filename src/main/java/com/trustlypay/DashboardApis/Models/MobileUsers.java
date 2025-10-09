package com.trustlypay.DashboardApis.Models;

import jakarta.persistence.*;



@Entity
@Table(name = "mobileUsers")
public class MobileUsers {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mobileUserId;

    @Column(unique = true, nullable = false)
    private String mobileUserName;

    @Column(nullable = false)
    private String password;

    public int getMobileUserId() {
        return mobileUserId;
    }

    public void setMobileUserId(int mobileUserId) {
        this.mobileUserId = mobileUserId;
    }

    public String getMobileUserName() {
        return mobileUserName;
    }

    public void setMobileUserName(String mobileUserName) {
        this.mobileUserName = mobileUserName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MobileUsers(int mobileUserId, String mobileUserName, String password) {
        this.mobileUserId = mobileUserId;
        this.mobileUserName = mobileUserName;
        this.password = password;
    }
}
