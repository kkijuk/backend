package umc.kkijuk.server.career.controller.response;

import lombok.*;
import java.util.List;


public class FindTagResponse {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class TagResponse {
        private Long tagId;
        private String tagName;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class SearchTagResponse{
        private int detailCount;
        private List<TagResponse> tagList;
    }
}
