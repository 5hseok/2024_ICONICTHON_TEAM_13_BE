package com.prochord.server.global.exception;


import com.prochord.server.global.exception.message.ErrorMessage;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
