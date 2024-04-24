package com.andrei.aws_basics.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
}
