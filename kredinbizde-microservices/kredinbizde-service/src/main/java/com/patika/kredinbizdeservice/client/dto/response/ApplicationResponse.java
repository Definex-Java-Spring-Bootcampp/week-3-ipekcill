package com.patika.kredinbizdeservice.client.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ApplicationResponse {
    private LocalDateTime createDate;
    private ApplicationStatus applicationStatus;
    private Long productId;
    private Long userId;
}
