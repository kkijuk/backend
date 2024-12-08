//package umc.kkijuk.server.unitTest.tag.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
//import umc.kkijuk.server.login.controller.dto.LoginInfo;
//import umc.kkijuk.server.member.domain.Member;
//import umc.kkijuk.server.member.service.MemberServiceImpl;
//import umc.kkijuk.server.tag.controller.TagController;
//import umc.kkijuk.server.tag.controller.response.TagResponse;
//import umc.kkijuk.server.tag.domain.Tag;
//import umc.kkijuk.server.tag.dto.TagRequestDto;
//import umc.kkijuk.server.tag.dto.TagResponseDto;
//import umc.kkijuk.server.tag.dto.converter.TagConverter;
//import umc.kkijuk.server.tag.service.TagServiceImpl;
//
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TagControllerTest {
//    @InjectMocks
//    private TagController tagController;
//    @Mock
//    private TagServiceImpl tagService;
//    @Mock
//    private MemberServiceImpl memberService;
//    public final Long requestMemberId = 1L;
//    public LoginInfo loginInfo;
//    public Tag testTag1;
//    public Tag testTag2;
//    private Member requestMember;
//
//    @BeforeEach
//    void init() {
//        requestMember = Member.builder()
//                .id(requestMemberId)
//                .build();
//        loginInfo = LoginInfo.from(requestMember);
//        testTag1 = Tag.builder()
//                .id(1L)
//                .name("test tag1")
//                .build();
//        testTag2 = Tag.builder()
//                .id(2L)
//                .name("test tag2")
//                .build();
//    }
//
//    @Test
//    @DisplayName("[create] 새로운 tag 생성")
//    void testCreate() {
//        //given
//        TagRequestDto.CreateTagDto requestDto = TagRequestDto.CreateTagDto.builder()
//                .tagName("test tag1")
//                .build();
//
//        when(memberService.getById(requestMemberId)).thenReturn(requestMember);
//        when(tagService.createTag(requestMember, requestDto)).thenReturn(testTag1);
//
//        //when
//        TagResponse<TagResponseDto.ResultTagDto> resultResponse = tagController.create(loginInfo, requestDto);
//        TagResponseDto.ResultTagDto resultData = resultResponse.getData();
//        //then
//        assertAll(
//                () -> assertThat(resultResponse.getStatus()).isEqualTo(HttpStatus.OK.value()),
//                () -> assertThat(resultResponse.getMessage()).isNotNull(),
//                () -> assertEquals(resultData.getTagName(), "test tag1"),
//                () -> assertEquals(resultData.getId(), 1L)
//        );
//    }
//
//    @Test
//    @DisplayName("[create] 없는 사용자의 요청")
//    void testCreateResourceNotFoundException() {
//        //given
//        final Long NotExistingMemberId = 9999L;
//        LoginInfo NotExistLoginInfo = LoginInfo.builder().memberId(NotExistingMemberId).build();
//
//        TagRequestDto.CreateTagDto requestDto = TagRequestDto.CreateTagDto.builder()
//                .tagName("test tag1")
//                .build();
//
//        when(memberService.getById(NotExistingMemberId)).thenThrow(new ResourceNotFoundException("Member", NotExistingMemberId));
//
//        //when
//        //then
//        assertThrows(ResourceNotFoundException.class, () -> {
//            tagController.create(NotExistLoginInfo, requestDto);
//        });
//    }
//
//    @Test
//    @DisplayName("[delete] 존재하는 tag 삭제")
//    void testDelete() {
//        //given
//        when(memberService.getById(requestMemberId)).thenReturn(requestMember);
//        doNothing().when(tagService).delete(requestMember, 1L);
//        //when
//        TagResponse<Object> resultResponse = tagController.delete(loginInfo, 1L);
//        //then
//        assertAll(
//                () -> assertEquals(HttpStatus.OK.value(), resultResponse.getStatus()),
//                () -> assertNotNull(resultResponse.getMessage())
//        );
//    }
//
//    @Test
//    @DisplayName("[delete] 없는 사용자의 요청")
//    void testDeleteResourceNotFoundException() {
//        //given
//        final Long NotExistingMemberId = 9999L;
//        LoginInfo NotExistLoginInfo = LoginInfo.builder().memberId(NotExistingMemberId).build();
//        when(memberService.getById(NotExistingMemberId))
//                .thenThrow(new ResourceNotFoundException("Member", NotExistingMemberId));
//        //when
//        //then
//        assertThrows(ResourceNotFoundException.class, () -> {
//            tagController.delete(NotExistLoginInfo, 1L);
//        });
//    }
//
//    @Test
//    @DisplayName("[read] tag 조회하기")
//    void testRead() {
//        //given
//        List<Tag> tagList = Arrays.asList(testTag1, testTag2);
//        TagResponseDto.ResultTagDtoList result = TagConverter.toResultTagDtoList(tagList);
//
//        when(memberService.getById(requestMemberId)).thenReturn(requestMember);
//        when(tagService.findAllTags(requestMember)).thenReturn(result);
//
//        //when
//        TagResponse<TagResponseDto.ResultTagDtoList> resultResponse = tagController.read(loginInfo);
//        TagResponseDto.ResultTagDtoList resultData = resultResponse.getData();
//        //then
//        assertAll(
//                () -> assertEquals(HttpStatus.OK.value(), resultResponse.getStatus()),
//                () -> assertNotNull(resultResponse.getMessage()),
//                () -> assertNotNull(resultData),
//                () -> assertEquals(2, resultData.getCount()),
//                () -> assertEquals("test tag1", resultData.getTagList().get(0).getTagName()),
//                () -> assertEquals("test tag2", resultData.getTagList().get(1).getTagName())
//        );
//    }
//
//    @Test
//    @DisplayName("[read] 없는 사용자의 요청")
//    void testReadResourceNotFoundException() {
//        //given
//        final Long NotExistingMemberId = 9999L;
//        LoginInfo NotExistLoginInfo = LoginInfo.builder().memberId(NotExistingMemberId).build();
//        when(memberService.getById(NotExistingMemberId))
//                .thenThrow(new ResourceNotFoundException("Member", NotExistingMemberId));
//        //when
//        //then
//        assertThrows(ResourceNotFoundException.class, () -> {
//            tagController.read(NotExistLoginInfo);
//        });
//
//    }
//}
