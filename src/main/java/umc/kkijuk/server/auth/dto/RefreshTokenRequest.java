package umc.kkijuk.server.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
  private String email;
  private String refreshToken;
}
