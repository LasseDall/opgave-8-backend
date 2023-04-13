package com.example.semster3thirdapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Gender {
  String gender;
  String name;
  int count;
  double probability;

  @Override
  public String toString() {
    return "Gender{" +
        "gender='" + gender + '\'' +
        ", name='" + name + '\'' +
        ", count=" + count +
        ", probability=" + probability +
        '}';
  }
}

