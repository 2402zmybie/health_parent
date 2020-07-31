package com.hr.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hr.dao.MemberDao;
import com.hr.pojo.Member;
import com.hr.service.MemberSerivce;
import com.hr.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = MemberSerivce.class)
@Transactional
public class MemberServiceImpl implements MemberSerivce {

    @Autowired
    private MemberDao memberDao;

    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    public void addMember(Member member) {
        String password = member.getPassword();
        if(password != null) {
            //使用md5将明文密码加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }
}
