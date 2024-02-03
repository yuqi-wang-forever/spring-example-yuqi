package wang.yuqi.springsecurityyuqi.dto;

import lombok.*;


public record AuthenticationRequest(String username,
        String password) {

}
