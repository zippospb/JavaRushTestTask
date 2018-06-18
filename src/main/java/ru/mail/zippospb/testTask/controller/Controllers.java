package ru.mail.zippospb.testTask.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

class Controllers {
    static Pageable getPageRequest(int page, String sortBy, String order){
        Sort.Direction direction = order.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = new Sort(direction, sortBy);
        int pageNumber = page > 0 ? page - 1 : 0;
        return PageRequest.of(pageNumber, 10, sort);
    }
}
