package project.projectblog.util.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class RestApiException {

  private String errorMessage;
  private HttpStatus httpStatus;

    @Builder
  public RestApiException(String errorMessage, HttpStatus httpStatus) {
    this.errorMessage = errorMessage;
    this.httpStatus = httpStatus;
  }
}
