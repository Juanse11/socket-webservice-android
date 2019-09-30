package com.server.objects;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2019-09-30T00:53:43")
@StaticMetamodel(ChatMessage.class)
public class ChatMessage_ { 

    public static volatile SingularAttribute<ChatMessage, Long> Id;
    public static volatile SingularAttribute<ChatMessage, String> time;
    public static volatile SingularAttribute<ChatMessage, String> message;
    public static volatile SingularAttribute<ChatMessage, String> username;

}