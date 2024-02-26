package com.persondetails;

import java.time.LocalDate;

public class Member extends Person{
    private MemberStatus memberStatus;
    private int borrowLimit;
    private LocalDate membership_startDate;
    private LocalDate membership_endDate;
    Member()
    {
        super();
        memberStatus = MemberStatus.ACTIVE;
        borrowLimit = 0;
    }

    public Member( MemberStatus memberStatus, int borrowLimit) {
        this.memberStatus = memberStatus;
        this.borrowLimit = borrowLimit;
    }

    public Member(String name, String gender, String emailAddress, String mobileNumber,String address, MemberStatus memberStatus, int borrowLimit) {
        super(name, Gender.valueOf(gender), emailAddress, mobileNumber,address);
        this.memberStatus = memberStatus;
        this.borrowLimit = borrowLimit;
    }

}
