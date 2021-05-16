package chess.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

public class RequiredParameterValidationException extends HandledException {
    private static final String ERROR_MESSAGE = "인자 값을 검증하는 과정에서 오류가 발생했습니다.";
    private final Map<String, String> causes;

    public RequiredParameterValidationException(Map<String, String> causes) {
        super(ERROR_MESSAGE);
        this.causes = causes;
    }

    public static RequiredParameterValidationException from(BindingResult bindingResult) {
        Map<String, String> fieldAndCauseMap = bindingResult.getAllErrors().stream()
                .collect(Collectors.toMap(error -> ((FieldError) error).getField(), DefaultMessageSourceResolvable::getDefaultMessage));
        return new RequiredParameterValidationException(fieldAndCauseMap);
    }

    public Map<String, String> getCauses() {
        return causes;
    }
}
