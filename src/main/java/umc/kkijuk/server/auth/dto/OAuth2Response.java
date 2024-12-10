package umc.kkijuk.server.auth.dto;

public interface OAuth2Response {
  String getProvider();

  String getProviderId();

  String getEmail();

}
