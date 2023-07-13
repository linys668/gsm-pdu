package org.otter.gsmpdu.beans;

/// <summary>
/// 短信类型。
/// </summary>
public enum SmsType {
    /// <summary>
    /// 普通短信。
    /// </summary>
    SMS_RECEIVED(0),
    SMS_STATUS_REPORT(2),
    SMS_SUBMIT(1),
    /// <summary>
    /// EMS短信。
    /// </summary>
    EMS_RECEIVED(0x40),
    EMS_SUBMIT(65);

    private final int typeCode;

    SmsType(int tc) {
        typeCode = tc;
    }

    public static SmsType valueOf(int tc){
        for(SmsType st : SmsType.values()) {
            if(st.typeCode == tc) {
                return st;
            }
        }
        return null;
    }

    public int getTypeCode() {
        return typeCode;
    }
}
