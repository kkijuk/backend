package umc.kkijuk.server.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RefreshTokenRequest {
  private String refreshToken;
}
