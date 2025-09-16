package com.anonymous63.lms.mapper;

import com.anonymous63.lms.dto.request.BookTransactionReqDto;
import com.anonymous63.lms.dto.response.BookTransactionResDto;
import com.anonymous63.lms.entity.BookTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookTransactionMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "bookId", source = "book.id")
    BookTransactionResDto toBookTransactionResDto(BookTransaction bookTransaction);

    BookTransaction toEntity(BookTransactionReqDto reqDto);
}
