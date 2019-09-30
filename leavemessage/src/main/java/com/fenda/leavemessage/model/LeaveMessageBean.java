package com.fenda.leavemessage.model;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/9/29
 * @Describe:
 */
public class LeaveMessageBean {
    int leaveMessageNumber;

    public LeaveMessageBean(int leaveMessageNumber) {
        this.leaveMessageNumber = leaveMessageNumber;
    }

    public void setLeaveMessageNumber(int leaveMessageNumber) {
        this.leaveMessageNumber = leaveMessageNumber;
    }

    public int getLeaveMessageNumber() {
        return leaveMessageNumber;
    }

    @Override
    public String toString() {
        return "LeaveMessageBean{" +
                "leaveMessageNumber=" + leaveMessageNumber +
                '}';
    }
}
