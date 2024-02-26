package com.bookdetails;

import java.time.LocalDate;

public class Author {
    String name;
    LocalDate dateOfBirth;
    String region;

    public Author(String name, LocalDate dateOfBirth, String region) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.region = region;
    }
    public Author(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getRegion() {
        return region;
    }
}
