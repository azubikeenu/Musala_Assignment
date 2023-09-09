package com.azubike.ellipsis.droneapplication.exception.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorMessage {
    private Date timeStamp;
    private String message;
    private String path;
}
