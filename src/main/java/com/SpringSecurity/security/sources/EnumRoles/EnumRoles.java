package com.SpringSecurity.security.sources.EnumRoles;





import org.springframework.stereotype.Component;

import java.util.Set;
@Component
public class EnumRoles {
  Set<String> setRoles = Set.of("ROLE_ADMIN","ROLE_USER");

    public Set<String> getSetRoles() {
        return setRoles;
    }

}
