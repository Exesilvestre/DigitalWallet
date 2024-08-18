package com.example.accounts_server.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.util.Arrays;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class APIErrorEntity {
    private String handledBy;
    private String exception;
    private HttpStatusCode oriStatus;
    private HttpStatusCode status;
    private String uri;
    private String localizedMessage;
    private List<String> messages;

    public APIErrorEntity(
            String handledBy,
            String exception,
            HttpStatusCode oriStatus,
            HttpStatusCode status,
            String uri,
            String localizedMessage,
            List<String> messages
    ) {
        super();
        this.handledBy = handledBy;
        this.exception = exception;
        this.oriStatus = oriStatus;
        this.status = status;
        this.uri = uri;
        this.localizedMessage = localizedMessage;
        this.messages = messages;
    }

    public APIErrorEntity(
            String handledBy,
            String exception,
            HttpStatusCode oriStatus,
            HttpStatusCode status,
            String uri,
            String localizedMessage,
            String message
    ) {
        this(handledBy, exception, oriStatus, status, uri, localizedMessage, Arrays.asList(message));
    }

}
