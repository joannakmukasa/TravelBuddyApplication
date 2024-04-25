/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.Scanner;

/**
 *
 * @author joann
 */
public class Client {

    public static void main(String[] args) {
        // TODO code application logic here
        Client c = new Client();
        c.start();
        c.menu();
    }
    
    
    Users u = new Users();
    Trips t = new Trips();
    UserManager um = new UserManager();
    int choice;
    Scanner sc = new Scanner(System.in);
    
    // function that calls the sign up and log in functions
    public void start(){
        System.out.println("Welcome to the Travel Buddy App");
        System.out.println("1. Log In");
        System.out.println("2. Sign Up");
        System.out.print("Choice: ");
        choice = sc.nextInt();
        System.out.println("");
        if (choice == 1) {
            u.login();
            menu();
        }
        else if (choice == 2) {
            u.signUp();
            menu();
        }
    }
    
    
    // menu function to navigate application
    public void menu(){
        System.out.println("");
        System.out.println("Menu");
        System.out.println("1. Go on a Trip");
        System.out.println("2. Propose a Trip");
        System.out.print("Choice: ");
        choice = sc.nextInt();
        System.out.println("");
        switch (choice) {
            case 1:
                System.out.println("1. View all available trips");
                System.out.println("2. Search for a trip");
                System.out.print("Choice: ");
                choice = sc.nextInt();
                System.out.println("");
                switch(choice){
                    case 1:
                        t.viewTrips();
                        menu();
                        break;
                    case 2:
                        t.queryTrips();
                        break;
                }
                menu();
                break;
            case 2:
                System.out.println("");
                t.addTrip();
            break;
                
            default:
                System.out.println("Invalid Selection");
                menu();
                break;
        }
    }
    
}
