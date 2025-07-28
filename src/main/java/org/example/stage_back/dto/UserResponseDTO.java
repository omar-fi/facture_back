package org.example.stage_back.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Integer id;
    private String email;
    private String role;
    private String portNom;
    private Long portId;
}
