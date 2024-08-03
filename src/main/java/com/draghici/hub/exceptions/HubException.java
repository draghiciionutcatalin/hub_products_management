package com.draghici.hub.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HubException {

    String errorMessage;
    int errorCode;
}
