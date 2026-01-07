package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import org.example.repo.DataRetriever;
public class Main {
    public static void main(String[] args) {
        DataRetriever dr = new DataRetriever();
        System.out.println(dr.findDishById(1).getName());
    }
}