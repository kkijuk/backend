package umc.kkijuk.server.unitTest.tag.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import umc.kkijuk.server.common.domian.exception.OwnerMismatchException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.tag.domain.Tag;
import umc.kkijuk.server.tag.dto.TagRequestDto;
import umc.kkijuk.server.tag.dto.TagResponseDto;
import umc.kkijuk.server.tag.repository.TagRepository;
import umc.kkijuk.server.tag.service.TagServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    @Mock
    private TagRepository tagRepository;
    @InjectMocks
    private TagServiceImpl tagService;
    private Long testMemberId = 333L;
    private Member testMember;
    private Member otherMember;
    private Tag testTag1;
    private Tag testTag2;
    @BeforeEach
    void init() {
        testMember = Member.builder()
                .id(testMemberId)
                .email("test@naver.com")
                .phoneNumber("010-1234-5678")
                .birthDate(LocalDate.of(2024, 7, 31))
//                .password("test")
                .userState(State.ACTIVATE)
                .build();

        otherMember = Member.builder()
                .id(2L)
                .email("test2@naver.com")
                .phoneNumber("010-2345-5678")
                .birthDate(LocalDate.of(2024, 7, 31))
//                .password("test")
                .userState(State.ACTIVATE)
                .build();

        testTag1 = Tag.builder()
                .id(1L)
                .memberId(testMemberId)
                .name("test tag1")
                .build();

        testTag2 = Tag.builder()
                .id(2L)
                .memberId(testMemberId)
                .name("test tag2")
                .build();
    }
    @Test
    @DisplayName("[create] 새로운 tag 만들기 정상 요청")
    void testCreateTag() {
        //given
        TagRequestDto.CreateTagDto requestDto = TagRequestDto.CreateTagDto.builder()
                .tagName("test tag1")
                .build();
        when(tagRepository.save(any(Tag.class))).thenReturn(testTag1);
        //when
        Tag tag = tagService.createTag(testMember,requestDto);
        //then
        assertAll(
                () -> assertThat(tag.getMemberId().equals(testMemberId)),
                () -> assertThat(tag.getId().equals(1L)),
                () -> assertThat(tag.getName().equals("test tag1"))
        );
        verify(tagRepository).save(any(Tag.class));
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    @DisplayName("[delete] tag 삭제하기 정상 요청")
    void testDeleteTag() {
        //given
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(testTag1));
        //when
        tagService.delete(testMember,1L);
        //then
        verify(tagRepository, times(1)).delete(testTag1);
    }

    @Test
    @DisplayName("[delete] 없는 리소스 요청의 경우 ResourceNotFoundException 발생")
    void testDeleteTagResourceNotFoundException(){
        //given
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> {
            tagService.delete(testMember, 5L);
        });
        verify(tagRepository, never()).delete(any(Tag.class));
    }

    @Test
    @DisplayName("[delete] 다른 사용자의 요청의 경우 OwnerMismatchException 발생")
    void testDeleteOwnerMismatchException() {
        //given
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag1));
        //when
        //then
        assertThrows(OwnerMismatchException.class, () -> {
           tagService.delete(otherMember,1L);
        });
        verify(tagRepository, never()).delete(any(Tag.class));
    }

    @Test
    @DisplayName("[findAllTags] 모든 태그 조회하기 정상 요청")
    void testFindAllTags(){
        //given
        List<Tag> tagList = Arrays.asList(testTag1, testTag2);
        when(tagRepository.findAllTagByMemberId(testMemberId)).thenReturn(tagList);
        //when
        TagResponseDto.ResultTagDtoList allTags = tagService.findAllTags(testMember);
        //then
        assertAll(
                () -> assertThat(allTags).isNotNull(),
                () -> assertThat(allTags.getTagList()).hasSize(tagList.size()),
                () -> assertThat(allTags.getTagList()).extracting("tagName")
                        .contains(tagList.get(0).getName(),tagList.get(1).getName())
        );
        verify(tagRepository, times(1)).findAllTagByMemberId(testMemberId);
    }


}
