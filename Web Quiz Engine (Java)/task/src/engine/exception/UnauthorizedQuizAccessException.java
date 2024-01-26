package engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Unauthorized access to quiz")
public class UnauthorizedQuizAccessException extends RuntimeException {}
