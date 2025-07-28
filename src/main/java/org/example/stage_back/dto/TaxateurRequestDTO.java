package org.example.stage_back.dto;
public class TaxateurRequestDTO {
    private String email;
    private String password;
    private Long portId;

    public Long getPortId() {
        return portId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Getters et setters
}