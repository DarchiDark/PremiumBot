package org.avangard.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@Getter
@AllArgsConstructor
public class Profile {
    private long uuid;
    private Group group;
    @NotNull
    private Date expireTime;
}
