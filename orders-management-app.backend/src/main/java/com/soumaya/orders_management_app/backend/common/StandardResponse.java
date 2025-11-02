package com.soumaya.orders_management_app.backend.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StandardResponse {
    private int id;
    private String message;
}
