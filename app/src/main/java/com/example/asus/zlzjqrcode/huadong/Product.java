package com.example.asus.zlzjqrcode.huadong;

import java.io.Serializable;

/**
 * Created by fangzhu on 2015/4/23.
 */
public class Product implements Serializable {

    /**
     * 品种所需字段
     */

    private String name ;
    private String idcard;
    private String mobile;
    private String count;
    private String member_id;
    private String isshow;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Product(String name, String idcard, String mobile, String count, String member_id, String isshow) {
        this.name = name;
        this.idcard = idcard;
        this.mobile = mobile;
        this.count = count;
        this.member_id = member_id;
        this.isshow = isshow;
    }

    public String getIsshow() {
        return isshow;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow;
    }

    public Product() {
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }



    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", idcard='" + idcard + '\'' +
                ", mobile='" + mobile + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

}
