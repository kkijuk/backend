package umc.kkijuk.server.common.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import umc.kkijuk.server.common.domian.exception.*;
import umc.kkijuk.server.common.domian.response.ErrorResponse;
import umc.kkijuk.server.common.domian.exception.ConfirmPasswordMismatchException;
import umc.kkijuk.server.common.domian.response.ErrorResultResponse;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResponse resourceNotFoundException(ResourceNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ReviewRecruitMismatchException.class)
    public ErrorResponse ReviewRecruitMatchException(ReviewRecruitMismatchException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(RecruitOwnerMismatchException.class)
    public ErrorResponse RecruitOwnerMismatchException(RecruitOwnerMismatchException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidTagNameException.class)
    public ErrorResponse InvalidTagNameException(InvalidTagNameException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConfirmPasswordMismatchException.class)
    public ErrorResponse ConfirmPasswordMismatchException(ConfirmPasswordMismatchException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FieldUpdateException.class)
    public ErrorResponse FieldUpdateException(FieldUpdateException e) {
        return new ErrorResponse(e.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidMemberDataException.class)
    public ErrorResponse InvalidMemberDataException(InvalidMemberDataException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CurrentPasswordMismatchException.class)
    public ErrorResponse CurrentPasswordMismatchException(CurrentPasswordMismatchException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MemberEmailNotFoundException.class)
    public ErrorResponse EmailNotFoundException(MemberEmailNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(CareerValidationException.class)
    public ErrorResponse CareerValidationException(CareerValidationException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResultResponse<?>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResultResponse.createFail(bindingResult));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public ErrorResponse handleInvalidFormatExceptions(InvalidFormatException exception) {
        return new ErrorResponse("올바른 형식이 아닙니다.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OwnerMismatchException.class)
    public ErrorResponse OwnerMismatchException(OwnerMismatchException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MemberAlreadyExistsException.class)
    public ErrorResponse MemberAlreadyExistsException(MemberAlreadyExistsException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CertificationNumberMismatchException.class)
    public ErrorResponse CertificationNumberMismatchException(CertificationNumberMismatchException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailNotResistedException.class)
    public ErrorResponse EmailNotResistedException(EmailNotResistedException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RecruitTagNotFoundException.class)
    public ErrorResponse RecruitTagNotFoundException(RecruitTagNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RecruitTagAlreadyExistException.class)
    public ErrorResponse RecruitTagAlreadyExistException(RecruitTagAlreadyExistException e) {
        return new ErrorResponse(e.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ErrorResponse EmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RecordNotFoundException.class)
    public ErrorResponse RecordNotFoundException(RecordNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailMismatchException.class)
    public ErrorResponse EmailMismatchException(EmailMismatchException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidBirthDateException.class)
    public ErrorResponse InvalidBirthDateException(InvalidBirthDateException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomAuthException.class)
    public ErrorResponse handleCustomAuthException(CustomAuthException exception) {
        return new ErrorResponse(exception.getErrorStatus().getMessage());
    }
}
