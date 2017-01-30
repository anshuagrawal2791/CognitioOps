package com.cognitio.cognitio;

/**
 * Created by anshu on 03/12/16.
 */

public class school {


    String id,cr,zone,name,phone,city,email,address,person_met,designation,status,visit,comments,cr_name,cr_phone;

    public school(String id, String cr, String zone, String name, String phone, String city, String email, String address, String person_met, String designation, String status, String visit, String comments, String cr_name, String cr_phone) {
        this.id = id;
        this.cr = cr;
        this.zone = zone;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.email = email;
        this.address = address;
        this.person_met = person_met;
        this.designation = designation;
        this.status = status;
        this.visit = visit;
        this.comments = comments;
        this.cr_name = cr_name;
        this.cr_phone = cr_phone;
    }

    public String getId() {
        return id;
    }

    public String getCr() {
        return cr;
    }

    public String getZone() {
        return zone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPerson_met() {
        return person_met;
    }

    public String getDesignation() {
        return designation;
    }

    public String getStatus() {
        return status;
    }

    public String getVisit() {
        return visit;
    }

    public String getComments() {
        return comments;
    }

    public String getCr_name() {
        return cr_name;
    }

    public String getCr_phone() {
        return cr_phone;
    }
}
