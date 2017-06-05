package com.kakaxi.fightdemo.bean;



import java.io.Serializable;

/**
 * Created by leixiaoliang  ddd on 2016/8/1.
 */
public class LoginBean implements Serializable{
        private String u_guid;
        private String avatar;
        private String nick_name;
        private int sex;
        private String birth;
        private String prov;
        private String city;
        private String district;
        private String address;
        private String mobile;
        private String summary;
        private String reg_time;

    @Override
    public String toString() {
        return "LoginBean{" +
                "u_guid='" + u_guid + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", sex=" + sex +
                ", birth='" + birth + '\'' +
                ", prov='" + prov + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", mobile='" + mobile + '\'' +
                ", summary='" + summary + '\'' +
                ", reg_time='" + reg_time + '\'' +
                ", token='" + token + '\'' +
                ", qr_code='" + qr_code + '\'' +
                '}';
    }

    private String token;
        private String qr_code;

    public String getU_guid() {
        return u_guid;
    }

    public void setU_guid(String u_guid) {
        this.u_guid = u_guid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

}
