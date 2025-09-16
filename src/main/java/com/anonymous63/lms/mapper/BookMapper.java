package com.anonymous63.lms.mapper;

import com.anonymous63.lms.dto.request.BookReqDto;
import com.anonymous63.lms.dto.response.BookResDto;
import com.anonymous63.lms.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "addedByUserId", source = "addedBy.id")
    BookResDto toBookResDto(Book book);

    Book toEntity(BookReqDto reqDto);
}
