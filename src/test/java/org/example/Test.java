package org.example;

import org.otter.gsmpdu.SmsPduDecode;
import org.otter.gsmpdu.beans.SmsInfo;

public class Test {
    public static void main(String[] args) throws Exception{
        String pdu = "07911326040000F0040B911346610089F60000208062917314080CC8F71D14969741F977FD07";

        SmsInfo si =SmsPduDecode.decode(pdu);

        System.out.println(pdu);
        System.out.println("----------------------------------");
        System.out.println("message center："+si.getSmscNumber());
        System.out.println("sender："+si.getSenderNumber());
        System.out.println("send time："+si.getTimeStamp());
        System.out.println("ems message id："+si.getSplitId());
        System.out.println("message index and count："+si.getSplitIndex()+"/"+si.getSplitCount());
        System.out.println("content："+si.getContent());
    }
}
