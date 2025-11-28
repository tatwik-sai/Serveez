package com.example.serveez.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingsOverTimeResponse {
    private Integer rangeDays;
    private List<TimePoint> points;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimePoint {
        private String date;
        private Long bookingsCount;
    }
}
