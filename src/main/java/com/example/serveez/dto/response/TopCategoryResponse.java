package com.example.serveez.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopCategoryResponse {
    private String categoryId;
    private String categoryName;
    private Long bookingsCount;
}
