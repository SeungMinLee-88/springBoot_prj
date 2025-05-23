package com.spring.reserve.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchCriteria {

    private String searchKey;
    private String searchValue;

}
