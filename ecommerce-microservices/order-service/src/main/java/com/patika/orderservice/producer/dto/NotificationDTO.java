package com.patika.orderservice.producer.dto;

import com.patika.orderservice.producer.enums.NotificationType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NotificationDTO {

    private NotificationType notificationType;
    private String message;
    private String to;

}
