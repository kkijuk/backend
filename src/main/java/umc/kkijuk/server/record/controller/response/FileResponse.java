package umc.kkijuk.server.record.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import umc.kkijuk.server.record.domain.File;
import umc.kkijuk.server.record.domain.FileType;

@Data
@Getter
@Builder
@AllArgsConstructor
public class FileResponse {
    private Long id;
    private FileType fileType;
    private String fileTitle;
    private String fileLinkTitle;
    private String keyName;
    private String urlTitle;
    private String url;

    public FileResponse(File file){
        this.id = file.getId();
        this.fileType = file.getFileType();
        this.fileTitle = file.getFileTitle();
        this.fileLinkTitle = file.getFileLinkTitle();
        this.keyName = file.getKeyName();
        this.urlTitle = file.getUrlTitle();
        this.url = file.getUrl();
    }
}
