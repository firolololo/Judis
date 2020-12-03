package com.stellar.judis.model;

import com.stellar.judis.protocol.ProtocolEnum;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/12/3 13:49
 */
public class Message {
    private MessageTypeEnum messageType;
    private ProtocolEnum protocolType;
    private String id;
    private String body;

    public MessageTypeEnum getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageTypeEnum messageType) {
        this.messageType = messageType;
    }

    public ProtocolEnum getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolEnum protocolType) {
        this.protocolType = protocolType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
