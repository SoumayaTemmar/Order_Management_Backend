package com.soumaya.orders_management_app.backend.Models.CmdItem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CmdOptions {
    ATELIER("ATELIER"),
    SHOP("SHOP");

    private final String value;
}
