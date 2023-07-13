### GSM/GPRS之二-短信pdu详细解析

文章来源 (https://blog.csdn.net/liht_1634/article/details/124179486)

一、概论

对于短信的接收和发送，在传送时都会变成统一标准的16进制码，这里以接收为例，从RIL接收的是一个pud怎么转换成大家都可以认识的字符串了？

-------------------------------------------------------------------------------------------

二、解析

RIL中收到new SMS，调用android.telephony.SmsMessage.newFromCMT() decode PDU，结果放入SmsMessage中。android.telephony.SmsMessage.newFromCMT() 根据电话的类型，用com.android.internal.telephony.gsm.SmsMessage或com.android.internal.telephony.cdma.SmsMessage的newFromCMT()真正的实现PDU decode。

解析pdu

对于RIL接收到到的pdu是一串16进制的数串，其含义会在下文中进行描述

0891 683110802105F0 24 0D91 688102200982F6

00 08    21800381602423   044F60597D

1）短信中心内容解析 服务中心地址(Service Center Address，短信中心号码)

08 代表的是后面8个字节都是短信中心的内容

91代表的类型，91是国际类型，81或者A1表示是国内，如果是91就需要在后面的号码上‘+’

683110802105F0为短信中心号码，+8613010812500

------------------------------

2）pdu第一个字节解析 PDU类型(Protocol Data Unit Type)

24 代表的含义，其2进制表示0010 0100

接收的时候这8位分别表示以下含义：
<table cellspacing="0" style="margin-left:5.4pt;"><tbody><tr><td style="background-color:#ffffff;width:28.5pt;"> <p style="text-align:center;"><span style="color:#333333;">BIT</span></p> </td><td style="background-color:#ffffff;width:61.75pt;"> <p style="text-align:center;"><span style="color:#92d050;">7</span></p> </td><td style="background-color:#ffffff;width:69.2pt;"> <p style="text-align:center;"><span style="color:#ffc000;">6</span></p> </td><td style="background-color:#ffffff;width:66.35pt;"> <p style="text-align:center;"><span style="color:#ff0000;">5</span></p> </td><td style="background-color:#ffffff;width:65.9pt;"> <p style="text-align:center;"><span style="color:#333333;">4</span></p> </td><td style="background-color:#ffffff;width:63.45pt;"> <p style="text-align:center;"><span style="color:#333333;">3</span></p> </td><td style="background-color:#ffffff;width:67.95pt;"> <p style="text-align:center;"><span style="color:#c00000;">2</span></p> </td><td style="background-color:#ffffff;width:53.1pt;"> <p style="text-align:center;"><span style="color:#0000ff;">1</span></p> </td><td style="background-color:#ffffff;width:62.15pt;"> <p style="text-align:center;"><span style="color:#0000ff;">0</span></p> </td></tr><tr><td style="background-color:#ffffff;width:28.5pt;"> <p style="text-align:center;"><span style="color:#333333;">参数</span></p> </td><td style="background-color:#ffffff;width:61.75pt;"> <p style="text-align:center;"><span style="color:#92d050;">RP</span></p> </td><td style="background-color:#ffffff;width:69.2pt;"> <p style="text-align:center;"><span style="color:#ffc000;">UDHI</span></p> </td><td style="background-color:#ffffff;width:66.35pt;"> <p style="text-align:center;"><span style="color:#ff0000;">SRI</span></p> </td><td style="background-color:#ffffff;width:65.9pt;"> <p style="text-align:center;"><span style="color:#333333;">-</span></p> </td><td style="background-color:#ffffff;width:63.45pt;"> <p style="text-align:center;"><span style="color:#333333;">-</span></p> </td><td style="background-color:#ffffff;width:67.95pt;"> <p style="text-align:center;"><span style="color:#c00000;">MMS</span></p> </td><td style="background-color:#ffffff;width:53.1pt;"> <p style="text-align:center;"><span style="color:#0000ff;">MTI</span></p> </td><td style="background-color:#ffffff;width:62.15pt;"> <p style="text-align:center;"><span style="color:#0000ff;">MTI</span></p> </td></tr></tbody></table>
发送的时候分别表示以下含义

发送的PDU，典型为11H:
<table cellspacing="0" style="margin-left:5.4pt;"><tbody><tr><td style="background-color:#ffffff;width:27.95pt;"> <p style="text-align:center;"><span style="color:#333333;">BIT</span></p> </td><td style="background-color:#ffffff;width:64pt;"> <p style="text-align:center;"><span style="color:#92d050;">7</span></p> </td><td style="background-color:#ffffff;width:77.35pt;"> <p style="text-align:center;"><span style="color:#ffc000;">6</span></p> </td><td style="background-color:#ffffff;width:61.7pt;"> <p style="text-align:center;"><span style="color:#7030a0;">5</span></p> </td><td style="background-color:#ffffff;width:56.65pt;"> <p style="text-align:center;"><span style="color:#00b050;">4</span></p> </td><td style="background-color:#ffffff;width:62.55pt;"> <p style="text-align:center;"><span style="color:#00b050;">3</span></p> </td><td style="background-color:#ffffff;width:77.35pt;"> <p style="text-align:center;"><span style="color:#00b0f0;">2</span></p> </td><td style="background-color:#ffffff;width:64.8pt;"> <p style="text-align:center;"><span style="color:#0000ff;">1</span></p> </td><td style="background-color:#ffffff;width:45.95pt;"> <p style="text-align:center;"><span style="color:#0000ff;">0</span></p> </td></tr><tr><td style="background-color:#ffffff;width:27.95pt;"> <p style="text-align:center;"><span style="color:#333333;">参数</span></p> </td><td style="background-color:#ffffff;width:64pt;"> <p style="text-align:center;"><span style="color:#92d050;">RP</span></p> </td><td style="background-color:#ffffff;width:77.35pt;"> <p style="text-align:center;"><span style="color:#ffc000;">UDHI</span></p> </td><td style="background-color:#ffffff;width:61.7pt;"> <p style="text-align:center;"><span style="color:#7030a0;">SRR</span></p> </td><td style="background-color:#ffffff;width:56.65pt;"> <p style="text-align:center;"><span style="color:#00b050;">VPF</span></p> </td><td style="background-color:#ffffff;width:62.55pt;"> <p style="text-align:center;"><span style="color:#00b050;">VPF</span></p> </td><td style="background-color:#ffffff;width:77.35pt;"> <p style="text-align:center;"><span style="color:#00b0f0;">RD</span></p> </td><td style="background-color:#ffffff;width:64.8pt;"> <p style="text-align:center;"><span style="color:#0000ff;">MTI</span></p> </td><td style="background-color:#ffffff;width:45.95pt;"> <p style="text-align:center;"><span style="color:#0000ff;">MTI</span></p> </td></tr></tbody></table>

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

------------------------------

3）发送方地址

0d 代表后面13，后面的字节数是[2+(Length+1)]/2=8，后面8个字节是发送方的地址；

91 代表的是目的号码类型。

688102200982F6：+8618200290286，发送方的地址

------------------------------

4）TP-Protocol-Identifier(TP-PID) 发送方地址(Originator Address)

01：TS 23.040 9.2.3.9

------------------------------

5）TP-Data-Coding-Scheme 协议标识(Protocol-Identifier)

08：0000 1000

表示数据编码方法和消息类别。一般为00H默认7位编码，等级号0。UCS2编码0等级为08H，可以传输中文。
<table cellspacing="0" style="margin-left:5.4pt;width:278pt;"><tbody><tr><td style="background-color:#ffffff;width:8.8pt;"> <p style="text-align:center;">7</p> </td><td style="background-color:#ffffff;width:8.8pt;"> <p style="text-align:center;">6</p> </td><td style="background-color:#ffffff;width:8.8pt;"> <p style="text-align:center;">5</p> </td><td style="background-color:#ffffff;width:8.8pt;"> <p style="text-align:center;">4</p> </td><td style="background-color:#ffffff;width:35.2pt;"> <p style="text-align:center;">3</p> </td><td style="background-color:#ffffff;width:16pt;"> <p style="text-align:center;">&nbsp;2</p> </td><td style="background-color:#ffffff;width:89.2pt;"> <p style="text-align:center;">&nbsp; &nbsp; &nbsp;1 &nbsp; &nbsp; &nbsp;&nbsp;</p> </td><td style="background-color:#ffffff;width:102.4pt;"> <p style="text-align:center;">0</p> </td></tr><tr><td colspan="4" style="background-color:#ffffff;width:35.2pt;"> <p style="text-align:center;">编码组</p> </td><td style="background-color:#ffffff;width:35.2pt;"> <p style="text-align:center;">保留</p> </td><td style="background-color:#ffffff;width:16pt;"> <p style="text-align:center;">x</p> </td><td style="background-color:#ffffff;width:89.2pt;"> <p style="text-align:center;">x</p> </td><td style="background-color:#ffffff;width:102.4pt;"> <p style="text-align:center;">x</p> </td></tr></tbody></table>
具体如下：
<table cellspacing="0" style="margin-left:3.9pt;width:524.9pt;"><tbody><tr><td style="width:45pt;"> <p>bit7-6</p> </td><td style="width:115.5pt;"> <p>bit5</p> </td><td style="width:84.85pt;"> <p>bit4</p> </td><td style="width:150.85pt;"> <p>bit3-2</p> </td><td style="width:128.7pt;"> <p>bit1-0</p> </td></tr><tr><td style="width:45pt;"> <p>00</p> </td><td style="width:115.5pt;"> <p>0:文本未压缩</p> </td><td style="width:84.85pt;"> <p style="margin-left:0;">0：表示bit1,</p> <p style="margin-left:0;">0是保留，没有消息类别；</p> </td><td style="width:150.85pt;"> <p>0&nbsp;&nbsp;&nbsp;0 &nbsp; 默认字母表7bit</p> </td><td style="width:128.7pt;"> <p>0&nbsp;&nbsp;&nbsp;&nbsp;0&nbsp;&nbsp;&nbsp;&nbsp; Class0</p> </td></tr><tr><td style="width:45pt;"> <p></p> </td><td style="width:115.5pt;"> <p>1：用<a href="https://so.csdn.net/so/search?q=GSM&amp;spm=1001.2101.3001.7020" target="_blank" class="hl hl-1" data-report-click="{&quot;spm&quot;:&quot;1001.2101.3001.7020&quot;,&quot;dest&quot;:&quot;https://so.csdn.net/so/search?q=GSM&amp;spm=1001.2101.3001.7020&quot;,&quot;extra&quot;:&quot;{\&quot;searchword\&quot;:\&quot;GSM\&quot;}&quot;}" data-tit="GSM" data-pretit="gsm">GSM</a>标准压缩</p> </td><td style="width:84.85pt;"> <p>1：表示有</p> </td><td style="width:150.85pt;"> <p>0&nbsp;&nbsp;&nbsp;1 &nbsp; &nbsp;8bit数据</p> </td><td style="width:128.7pt;"> <p>0&nbsp;&nbsp;&nbsp;&nbsp;1&nbsp;&nbsp;&nbsp;&nbsp; Class1</p> </td></tr><tr><td style="width:45pt;"> <p></p> </td><td style="width:115.5pt;"> <p></p> </td><td style="width:84.85pt;"> <p></p> </td><td style="width:150.85pt;"> <p>1&nbsp;&nbsp;&nbsp;0 &nbsp; &nbsp;UCS2编码</p> </td><td style="width:128.7pt;"> <p>1&nbsp;&nbsp;&nbsp;&nbsp;0&nbsp;&nbsp;&nbsp;&nbsp; Class2</p> </td></tr><tr><td style="width:45pt;"> <p></p> </td><td style="width:115.5pt;"> <p></p> </td><td style="width:84.85pt;"> <p></p> </td><td style="width:150.85pt;"> <p>1 &nbsp;1 &nbsp; &nbsp; 保留</p> </td><td style="width:128.7pt;"> <p>1&nbsp;&nbsp;&nbsp;&nbsp;1&nbsp;&nbsp;&nbsp;&nbsp; Class3</p> </td></tr><tr><td style="width:45pt;"> <p></p> </td><td style="width:115.5pt;"> <p></p> </td><td style="width:84.85pt;"> <p></p> </td><td style="width:150.85pt;"> <p></p> </td><td style="width:128.7pt;"> <p>Bit1 Bit0&nbsp;&nbsp;消息类别</p> </td></tr><tr><td style="width:45pt;"> <p>00</p> </td><td style="width:115.5pt;"> <p>0</p> </td><td style="width:84.85pt;"> <p>0</p> </td><td style="width:150.85pt;"> <p>10</p> </td><td style="width:128.7pt;"> <p>00</p> </td></tr></tbody></table>
Class0：短消息直接显示到用户终端

Class1：短消息存储在SIM卡上

Class2：短消息必须存储在SIM卡上，禁止直接传输到中断。

Class3：短消息存贮在用户设备上。

------------------------------

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

------------------------------

7）UserDataHeader - UDH  用户数据长度(User Data Length(Amount of Characters))

04 4F60597D

04：UDL用户数据长度，包含用户数据和用户数据头的长度

1、 如果用户用默认7位编码。

1） 如果没有用户数据头，此数字标示7bit的字符个数。

2） 如果有用户数据头，此数字表示包括用户数据头（包含补丁在内）在内的7bit个数。即7bit个数加上头部长度在加1（补丁）

2、 如果用户用8位编码

表示用户数据区的字节数，有数据头信息，包括在内。

3、 如果为UCS2编码，则是用户数据区的字节数

注意：由于前面pdu的第一个字节里面已经标示是否有头，如果有头紧接的一个字节为头的长度，然后剩余的是用户数据的长度

------------------------------

8）UD 用户数据(User Data)(短消息内容)

4F60597D：就是用户数据

-------------------------------------------------------------------------------------------

三、编码与解码
------------------------------------------------

1、 英文编码
缺省的GSM字符集为7位编码，ASCII码为8位编码，编码就是将8位ASCII编码转换为7位编码。
例如：1234 编码后得到31D98C06
2进制表示：
8位编码 00110001 00110010 00110011 00110100
7位编码 00110001 11011001 10001100 00000110
即，将ASCII8位编码的Bit8去掉，依次将下7位编码的后几位逐次移到前面，形成新的8位编码。
------------------------------------------------
2、 英文解码
简单地说就是将7位字符编码转换为8为字符编码。
------------------------------------------------
3、 中文编码
中文编码较为简单，就是将GB2312的中文编码转换为代码页为CP936的Unicode编码即可。
------------------------------------------------
4、 中文解码
将代码页为CP936的Unicode编码转换为GB2312的中文编码即可。
