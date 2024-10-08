package umc.kkijuk.server.career.controller.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class CareerResponse<T> {
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public CareerResponse(final String message,T data){
        this.message = message;
        this.data = data;
    }
    public static <T> CareerResponse<T> success(String message, T data){
        return new CareerResponse<>(message,data);
    }
}
