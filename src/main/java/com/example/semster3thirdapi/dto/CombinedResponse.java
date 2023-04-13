package com.example.semster3thirdapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CombinedResponse {
  String name;
  String gender;
  double genderProbability;
  int age;
  int ageCount;
  String country;
  double countryProbability;

  public CombinedResponse(String name, String gender, double genderProbability, int age, int ageCount, String country, double countryProbability) {
    this.name = name;
    this.gender = gender;
    this.genderProbability = genderProbability;
    this.age = age;
    this.ageCount = ageCount;
    this.country = country;
    this.countryProbability = countryProbability;
  }

  @Override
  public String toString() {
    return "CombinedResponse{" +
        "name='" + name + '\'' +
        ", gender='" + gender + '\'' +
        ", genderProbability=" + genderProbability +
        ", age=" + age +
        ", ageCount=" + ageCount +
        ", country='" + country + '\'' +
        ", countryProbability=" + countryProbability +
        '}';
  }
}
