package com.hr.service;

import com.hr.pojo.Member;

public interface MemberSerivce {

    Member findByTelephone(String telephone);
    void addMember(Member member);
}
