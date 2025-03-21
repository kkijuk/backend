package umc.kkijuk.server.record.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.record.controller.response.FileResponse;
import umc.kkijuk.server.record.domain.File;
import umc.kkijuk.server.record.domain.FileType;
import umc.kkijuk.server.record.domain.Record;
import umc.kkijuk.server.record.dto.FileReqDto;
import umc.kkijuk.server.record.dto.UrlReqDto;
import umc.kkijuk.server.record.repository.FileRepository;
import umc.kkijuk.server.record.repository.RecordRepository;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    private final RecordRepository recordRepository;
    private final FileRepository fileRepository;
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.s3.bucket-path}")
    private String bucketPath;

    @Override
    public Map<String, String> getSignUrl (Long memberId, String fileName){

        if (fileRepository.existsByMemberIdAndFileTitle(memberId, fileName)) {
            throw new IllegalArgumentException("이미 존재하는 파일 이름입니다: " + fileName);
        }

        String keyName = bucketPath + "/" + memberId + "/" +  UUID.randomUUID().toString() + "-" + fileName;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .serverSideEncryption("AES256") // sse-s3 암호화 적용
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();

        String presignedUrl = s3Presigner.presignPutObject(presignRequest).url().toString();

        Map<String, String> response = new HashMap<>();
        response.put("signedURL",presignedUrl);
        response.put("keyName",keyName);
        return response;
    }

    @Override
    @Transactional
    public FileResponse saveFile(Long memberId, Long recordId, FileReqDto request) {
        log.info("Saving file for memberId: {}, recordId: {}, request: {}", memberId, recordId, request);

        if (fileRepository.existsByMemberIdAndFileTitle(memberId, request.getTitle())) {
            throw new IllegalArgumentException("이미 존재하는 파일 이름입니다: " + request.getTitle());
        }

        File file = File.builder()
                .memberId(memberId)
                .fileType(FileType.File)
                .fileTitle(request.getTitle())
                .fileLinkTitle(request.getLinkTitle())
                .keyName(request.getKeyName())
                .build();

        updateRecordTimestamp(memberId);

        return new FileResponse(fileRepository.save(file));
    }


    @Override
    public Map<String, String> getDownloadUrl(Long memberId, String fileName) {

        File file = fileRepository.findByMemberIdAndFileTitle(memberId, fileName)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일을 찾을 수 없습니다: " + fileName));
        String keyName = file.getKeyName();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))  // URL 유효 기간 설정
                .getObjectRequest(getObjectRequest)
                .build();

        String presignedUrl = s3Presigner.presignGetObject(presignRequest).url().toString();

        Map<String, String> response = new HashMap<>();
        response.put("presignedURL", presignedUrl);
        response.put("keyName", keyName);
        return response;
    }

    @Override
    @Transactional
    public FileResponse deleteFile(Long memberId, String fileName){
        File file = fileRepository.findByMemberIdAndFileTitle(memberId, fileName)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일이 존재하지 않습니다: " + fileName));

        s3Client.deleteObject(builder -> builder.bucket(bucketName).key(file.getKeyName()).build());
        fileRepository.delete(file);

        updateRecordTimestamp(memberId);

        return new FileResponse(file);
    }

    @Override
    @Transactional
    public FileResponse renameFile(Long memberId, String oldFileName, String newFileName) {

        File existingFile = fileRepository.findByMemberIdAndFileTitle(memberId, oldFileName)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일이 존재하지 않습니다: " + oldFileName));

        if (fileRepository.existsByMemberIdAndFileTitle(memberId, newFileName)) {
            throw new IllegalArgumentException("이미 존재하는 파일 이름입니다: " + newFileName);
        }

        // 새로운 keyName 생성
        String newKeyName = bucketPath + "/" + memberId + "/" + UUID.randomUUID().toString() + "-" + newFileName;

        // S3에서 기존 파일을 새로운 이름으로 복사
        CopyObjectRequest copyRequest = CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .sourceKey(existingFile.getKeyName())
                .destinationBucket(bucketName)
                .destinationKey(newKeyName)
                .serverSideEncryption("AES256") // sse-s3 암호화 적용
                .build();
        s3Client.copyObject(copyRequest);

        // 기존 파일 삭제
        s3Client.deleteObject(builder -> builder.bucket(bucketName).key(existingFile.getKeyName()).build());

        File updatedFile = File.builder()
                .id(existingFile.getId())
                .memberId(existingFile.getMemberId())
                .fileType(existingFile.getFileType())
                .fileTitle(newFileName)
                .fileLinkTitle(existingFile.getFileLinkTitle())
                .keyName(newKeyName)
                .build();

        fileRepository.save(updatedFile);

        updateRecordTimestamp(memberId);

        return new FileResponse(updatedFile);
    }



    @Override
    @Transactional
    public FileResponse saveUrl(Long memberId, Long recordId, UrlReqDto urlReqDto){
        if (fileRepository.existsByMemberIdAndUrlTitle(memberId, urlReqDto.getUrlTitle())) {
            throw new IllegalArgumentException("이미 존재하는 URL 제목입니다: " + urlReqDto.getUrlTitle());
        }
//        Record record = recordRepository.findById(recordId)
//                .orElseThrow(() -> new ResourceNotFoundException("Record", recordId));

        File file = File.builder()
                .memberId(memberId)
                .fileType(FileType.URL)
                .urlTitle(urlReqDto.getUrlTitle())
                .url(urlReqDto.getUrl())
                .build();
        fileRepository.save(file);
        updateRecordTimestamp(memberId);
        return new FileResponse(file);
    }

    @Override
    @Transactional
    public FileResponse deleteUrl(Long memberId, UrlReqDto urlReqDto){
        File file = fileRepository.findByMemberIdAndUrlTitle(memberId, urlReqDto.getUrlTitle())
                .orElseThrow(() -> new IllegalArgumentException("해당 URL이 존재하지 않습니다: " + urlReqDto.getUrlTitle()));
        fileRepository.delete(file);
        updateRecordTimestamp(memberId);
        return new FileResponse(file);

    }

    private void updateRecordTimestamp(Long memberId) {
        Record record = recordRepository.findByMemberId(memberId);
        if (record == null) {
            throw new ResourceNotFoundException("Record", memberId);
        }
        record.updateTimestamp();
        recordRepository.save(record);
    }
}
