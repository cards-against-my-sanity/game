package dev.jacobandersen.cams.game.security.controller.advice;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class WsExceptionHandlerControllerAdvice {
    @MessageExceptionHandler(AccessDeniedException.class)
    @SendToUser("/queue/errors")
    public String handleAccessDeniedException(AccessDeniedException ex) {
        return String.format("You do not have permission to perform this action: %s", ex.getMessage());
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public String handleGeneralException(Exception ex) {
        return String.format("An error has occurred: %s", ex.getMessage());
    }
}