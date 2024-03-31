package com.patika.invoiceservice.dto;

import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateInvoiceDto {
   private Map<UUID, Double> orderPriceMap;
}
