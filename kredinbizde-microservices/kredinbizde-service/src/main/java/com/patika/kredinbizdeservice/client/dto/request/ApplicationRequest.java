package com.patika.kredinbizdeservice.client.dto.request;

import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApplicationRequest {
    private Long userId;
    private Long productId;
}
