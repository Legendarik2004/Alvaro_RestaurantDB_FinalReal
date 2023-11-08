package model;

import lombok.Data;

@Data
public class User {
    private final int id;
    private final String nombre;
    private final String password;
}
