package org.example.stage_back.dto;

import lombok.Data;
import org.example.stage_back.entities.User;

@Data
public class UserDTO {
    private Long id;
    private String email;
    
    public static UserDTO fromEntity(User user) {
        if (user == null) return null;
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
