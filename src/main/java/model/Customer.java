package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dob;
    private Credentials credentials;

    public String toStringSimplified() {
        return id + " " + firstName + " " + lastName.charAt(0);
    }
}