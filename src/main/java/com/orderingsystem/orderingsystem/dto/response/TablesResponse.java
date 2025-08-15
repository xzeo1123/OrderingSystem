package com.orderingsystem.orderingsystem.dto.response;

import com.orderingsystem.orderingsystem.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TablesResponse {

    private Integer id;

    private Integer number;

    private Status status;
}
