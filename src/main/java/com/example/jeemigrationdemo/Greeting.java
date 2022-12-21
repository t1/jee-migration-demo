package com.example.jeemigrationdemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Greeting {
    String hello;
    String who;

    public String toString() {return hello + ", " + who + "!";}
}
