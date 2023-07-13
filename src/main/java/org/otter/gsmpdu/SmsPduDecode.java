package org.otter.gsmpdu;

import org.otter.gsmpdu.beans.SmsInfo;
import org.otter.gsmpdu.beans.SmsType;
import org.otter.gsmpdu.decoder.EightBitDecoder;
import org.otter.gsmpdu.decoder.IDecoder;
import org.otter.gsmpdu.decoder.SevenBitDecoder;
import org.otter.gsmpdu.decoder.UnicodeDecoder;
import org.otter.utils.HexString;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

/// <summary>
/// 短信结构。
/// </summary>
public class SmsPduDecode {

    //#region 编解码方法
    static String byteReverse(byte[] raw) {
        String bstr = HexString.bytesToHex(raw);
        StringBuilder buf = new StringBuilder();
        int len = bstr.length();
        int i = 0;
        while (i < len) {
            int fisrtIdx = i + 1;
            if (fisrtIdx < len) {
                buf.append(bstr.charAt(fisrtIdx));
            }
            buf.append(bstr.charAt(i));
            i += 2;
        }
        int bufLen = buf.length() - 1;
        char lastChar = buf.charAt(bufLen);
        if (lastChar == 'f' || lastChar == 'F') {
            buf.deleteCharAt(bufLen);
        }
        return buf.toString();
    }

    static String getNumber(byte type, byte[] number) {
        switch (type & 0x70) {
            case 0x10:
                return "+" + byteReverse(number);
            case 0x50:
                return SevenBitDecoder.converString(number);
            default:
                return byteReverse(number);
        }
    }

    static SmsType getSmsType(byte firstOctet) {
        int num = firstOctet & 3;
        int num2 = firstOctet & 0x40;
        if (num == 3 && num2 == 64) return SmsType.EMS_SUBMIT;
        return SmsType.valueOf(num + num2);
    }

    static IDecoder getEncodingScheme(byte encodingScheme) throws Exception {
        switch (encodingScheme & 0x0c) {
            case 0:
                return new SevenBitDecoder();
            case 0x04:
                return new EightBitDecoder();
            case 0x08:
                return new UnicodeDecoder();
            default:
                throw new Exception("未知PDU消息编码方案");
        }
    }

    public static SmsInfo decode(String pdu) throws Exception {
        ByteArrayInputStream ms = new ByteArrayInputStream(HexString.hexToBytes(pdu));
        return decode(ms);
    }

    public static SmsInfo decode(ByteArrayInputStream reader) throws Exception {
        SmsInfo sms = new SmsInfo();

        /*
        1）短信中心内容解析 服务中心地址(Service Center Address，短信中心号码)
            08 代表的是后面8个字节都是短信中心的内容
            91代表的类型，91是国际类型，81或者A1表示是国内，如果是91就需要在后面的号码上‘+’
            683110802105F0为短信中心号码，+8613010812500
         */
        int smscLength = reader.read();
        int smscType = reader.read();
        byte[] smscNumber = new byte[(smscLength - 1)];
        reader.read(smscNumber);
        sms.setSmscNumber(getNumber((byte) smscType, smscNumber));
        /*
        2）pdu第一个字节解析 PDU类型(Protocol Data Unit Type)
            24 代表的含义，其2进制表示0010 0100
            接收的时候这8位分别表示以下含义：
            BIT 7   6    5  4 3  2   1   0
            参数 RP UDHI SRI - - MMS MTI MTI
            发送的时候分别表示以下含义
            发送的PDU，典型为11H:
            BIT 7   6    5   4   3  2   1   0
            参数 RP UDHI SRR VPF VPF RD MTI MTI
            MTI 2bit：消息类型，00表示收，01表示发
            MMS 1bit：短消息服务中心是否有更多短消息等待移动台。1有，0无。默认为1。
            SRI 1bit：状态报告标示。0不需要状态返回到移动设备。1需要。默认为0。
            UDHI 1bit：用户数据头标示。0用户数据没有头信息，1有。一般为0。
            RP  1bit：是否有回复路径的标示。1有，0没有。一般为0。
            VPF 2bit：有效期限格式。00 VP不存在； 10 VP区存在用一个字节表示，是相对值；
            01保留；      11存在，半个字节表示，绝对值。
            RD  1bit：重复信元丢弃。0通知服务中心碰到同源、同目的地、同样的MR（短消息序号）的短消息接受；
            1抛弃，此时将在短消息提交报告中返回一个适当的FCS值。
            SRR 2bit：状态报告要求。
         */
        int typeCode = reader.read();
        SmsType type = getSmsType((byte) typeCode); // 类型

        if (type != SmsType.SMS_RECEIVED && type != SmsType.EMS_RECEIVED) {
            throw new Exception("不支持的短信类型" + typeCode);
        }

        /*
        3）发送方地址
         0d 代表后面13，后面的字节数是[2+(Length+1)]/2=8，后面8个字节是发送方的地址；
         91 代表的是目的号码类型。
         688102200982F6：+8618200290286，发送方的地址
         */
        int senderLength = reader.read(); // 发信人号码长度
        int senderType = reader.read();// 发信人号码类型
        byte[] senderNumber = new byte[(int) Math.ceil(senderLength / 2F)];
        reader.read(senderNumber); // 发信人号码
        sms.setSenderNumber(getNumber((byte) senderType, senderNumber));
        /*
        4）TP-Protocol-Identifier(TP-PID) 发送方地址(Originator Address)
            01：TS 23.040 9.2.3.9
         */
        int TP_PID = reader.read(); // 协议ID
        /*
        5）TP-Data-Coding-Scheme 协议标识(Protocol-Identifier)
            08：0000 1000
            表示数据编码方法和消息类别。一般为00H默认7位编码，等级号0。UCS2编码0等级为08H，可以传输中文。
         */
        int TP_DCS = reader.read(); // 编码方案
        /*
           6）时间 服务中心的时间戳(Service Center Timestamp)
                短信中心下发的时间戳，这个编码和长度固定
                    21800381602423
                    21: 年份,12
                    80：月,08
                    03：日,30
                    81:小时，18
                    60：分钟,06
                    12：秒,21
                    23：时区
         */
        byte[] timeStamp = new byte[7];
        reader.read(timeStamp);// 发送时间戳
        String timeStampStr = byteReverse(timeStamp); // 发送时间戳
        sms.setTimeStamp(timeStampStr);

        /*
        7）UserDataHeader - UDH  用户数据长度(User Data Length(Amount of Characters))
            04 4F60597D
            04：UDL用户数据长度，包含用户数据和用户数据头的长度
            1、 如果用户用默认7位编码。
                1） 如果没有用户数据头，此数字标示7bit的字符个数。
                2） 如果有用户数据头，此数字表示包括用户数据头（包含补丁在内）在内的7bit个数。即7bit个数加上头部长度在加1（补丁）
            2、 如果用户用8位编码
                表示用户数据区的字节数，有数据头信息，包括在内。
            3、 如果为UCS2编码，则是用户数据区的字节数
         */
        int TP_UDL = reader.read();

        // 长短信分割，建议使用Redis保存每一条短信，用一个原子增长标记收到所有短信后合并
        if (type == SmsType.EMS_RECEIVED) // 也可以这样判断  (type & 0x40) == 0x40
        {
            int TP_UDHL = reader.read(); // 用户数据头长度
            byte[] header = new byte[TP_UDHL];
            reader.read(header); // 用户数据头
            if (TP_UDHL >= 5) {
                byte headerId = header[0]; // Header ID
                byte headerLength = header[1]; // 头长度
                if (headerLength >= 3) // 一般长度3字节，ETL公司的分割ID是2字节
                {
                    sms.setSplitId(HexString.bytesToHex(Arrays.copyOfRange(header, 0, headerLength - 2))); // 分割ID，需要跳过ID和头长度，至倒数第2个字节
                    sms.setSplitCount(header[headerLength - 2]); // 一共多少条短信
                    sms.setSplitIndex(header[headerLength - 1]); // 当前是第几条
                }
            }
        }
        /*
        8）UD 用户数据(User Data)(短消息内容)
         */
        int remainingDataLen = reader.available();
        //fixing remaining data length is less than head describe length.
        if(remainingDataLen<TP_UDL){
            TP_UDL = remainingDataLen;
        }
        byte[] dataBuf = new byte[TP_UDL];
        reader.read(dataBuf); // 短信内容
        sms.setContent(getEncodingScheme((byte) TP_DCS).decode(dataBuf));

        return sms;
    }

}