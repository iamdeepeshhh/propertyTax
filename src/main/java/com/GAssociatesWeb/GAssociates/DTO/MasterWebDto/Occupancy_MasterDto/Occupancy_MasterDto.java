package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.Occupancy_MasterDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Occupancy_MasterDto {
    private Integer occupancy_id;
    private String occupancy;
    private String occupancy_marathi;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
