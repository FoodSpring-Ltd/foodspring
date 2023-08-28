package com.juaracoding.foodspring.utils;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/24/2023 2:08 PM
@Last Modified 8/24/2023 2:08 PM
Version 1.0
*/

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.Objects;


@Builder
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageProperty {
    private String sortBy;
    private String sortType;
    private Integer page;
    private Integer limit;

    private static Integer defaultLimit = 10;
    private static Direction defaultDirection = Direction.ASC;
    private static Integer defaultPage = 0;
    private String defaultSortBy = "updatedAt";


    public String getSortBy() {
        return sortBy;
    }
    /**
     * Returns Sort.Direction. It will return Sort.Direction.ASC if not set.
     * */
    public Direction getSortType() {
        if (Objects.isNull(sortType)) {
            sortType = defaultDirection.toString();
        }
        return Sort.Direction.fromString(sortType);
    }

    public String getSortTypeString() {
        return sortType;
    }

    /**
     * Returns integer page or 0 if not set.
     * */
    public Integer getPage() {
        if (Objects.isNull(page)) {
            return defaultPage;
        }
        return page - 1;
    }

    /**
     * Returns integer limit or 10 if not set.
     * */
    public Integer getLimit() {
        if (Objects.isNull(limit)) {
            return defaultLimit;
        }
        return limit;
    }

    /**
     * Return a Pageable object constructed from the properties of PageProperty.
     * SortBy column is set to updatedAt by default when sortBy property is not set.
     * You can change the defaultSortBy column to match your requirements.
    * */
    public Pageable getPageable() {
        return PageRequest.of(getPage(),
                getLimit(),
                Sort.by(getSortType(), Objects.isNull(getSortBy()) ? defaultSortBy : getSortBy()));
    }
}
