package com.example.semster3thirdapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Country {
  String country;
  String name;

  double probability;

  public Country(String country, String name, double probability) {
    this.country = country;
    this.name = name;
    this.probability = probability;
  }

  @Override
  public String toString() {
    return "Country{" +
        "country='" + country + '\'' +
        ", name='" + name + '\'' +
        ", probability=" + probability +
        '}';
  }
}
