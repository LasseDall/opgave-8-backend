package com.example.semster3thirdapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Age {
  int age;
  String name;
  int count;

  @Override
  public String toString() {
    return "Age{" +
        "age=" + age +
        ", name='" + name + '\'' +
        ", count=" + count +
        '}';
  }
}
