package com.vn.iphoneshop.dto.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ApiResponse<T>{
    @Builder.Default
    private int code = 200;
    private Object errors;
    private String message;
    private String timestamp;
    private boolean hasError;
    private T data;
}
