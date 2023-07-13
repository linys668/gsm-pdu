package org.otter.gsmpdu.beans;

/// <summary>
/// 短信结构。
/// </summary>
public class SmsInfo {
    /// <summary>
    /// 短信中心号码。
    /// </summary>
    private String smscNumber;

    /// <summary>
    /// 发件人号码。
    /// </summary>
    private String senderNumber;

    /// <summary>
    /// 发送时间。
    /// </summary>
    private String timeStamp;

    /// <summary>
    /// 短信编号。
    /// </summary>
    private String splitId;

    /// <summary>
    /// 短信总数。
    /// </summary>
    private int splitCount = 1;

    /// <summary>
    /// 当前第几条。
    /// </summary>
    private int splitIndex = 1;

    /// <summary>
    /// 短信正文。
    /// </summary>
    private String content;

    public String getSmscNumber() {
        return smscNumber;
    }

    public void setSmscNumber(String smscNumber) {
        this.smscNumber = smscNumber;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public int getSplitCount() {
        return splitCount;
    }

    public void setSplitCount(int splitCount) {
        this.splitCount = splitCount;
    }

    public int getSplitIndex() {
        return splitIndex;
    }

    public void setSplitIndex(int splitIndex) {
        this.splitIndex = splitIndex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}