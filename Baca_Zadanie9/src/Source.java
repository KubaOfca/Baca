//Jakub Baran - 8

import java.util.Scanner;

public class Source {
    public static Scanner scn = new Scanner(System.in);
    public static Tree tree;
    public static int index;
    public static void main(String[] args) {
        int numberOfSets = scn.nextInt();
        for (int i = 0; i < numberOfSets; i++) {
            tree = new Tree();
            int numberOfCommands = scn.nextInt();
            scn.nextLine();
            for (int j = 0; j < numberOfCommands; j++) {
                runCommand(scn.next());
            }
        }
    }

    public static void runCommand(String command) {

        switch (command) {
            case "CREATE":
                String order = scn.next();
                int n = Integer.parseInt(scn.next());
                Person[] people = new Person[n];
                for (int i = 0; i < n; i++) {
                    people[i] = new Person(Integer.parseInt(scn.next()), scn.next(), scn.next());

                }
                index = n - 1;
                tree.create(order, n, people, Integer.MIN_VALUE, Integer.MAX_VALUE, people[n-1].priority);
                break;
            case "INORDER":
                tree.inorder();
                break;
            case "PREORDER":
                tree.preorder();
                break;
            case "POSTORDER":
                tree.postorder();
                break;
            case "HEIGHT":
                System.out.println(tree.height(tree.root));
                break;
        }
    }
}


class Node {
    public Person info; // element danych (klucz)
    public Node left; // lewy potomek węzła
    public Node right; // prawy lewy potomek węzła

    Node(Person info) {
        this.info = info;
        this.left = null;
        this.right = null;
    }
}

class Person {
    public int priority;
    public String name;
    public String surname;

    Person(int priority, String name, String surname) {
        this.priority = priority;
        this.name = name;
        this.surname = surname;
    }
}

class Tree {
    public Node root;

    public Tree() {
        root = null;
    }

    //--------------------------------------Modul kolejkowania----------------------------------------------



    //--------------------------------------Modul edycji----------------------------------------------

    public Node create(String order, int n, Person[] people, int min, int max, int priority) {
        Node node = null;

        if (Source.index >= 0) {
            if (order.equals("POSTORDER")) {
                if (priority > min && priority < max) {
                    node = new Node(people[Source.index]);
                    Source.index = Source.index - 1;
                    if(root == null){
                        root = node;
                    }
                    if(Source.index > 0){
                        node.right = create(order, n, people, priority, max, people[Source.index].priority);
                        node.left = create(order, n, people, min, priority, people[Source.index].priority);
                    }


                }

            }
        }

        return node;
    }

    //--------------------------------------Modul Raportowania----------------------------------------------

    public void inorder(){
        Stack stack = new Stack(10);
        Node p = root;
        while (p != null || !stack.isEmpty()){
            if(p != null){
                stack.push(p);
                p = p.left;
            }else{
                p = stack.pop();
                System.out.println(p.info.priority + " " + p.info.name + " " + p.info.surname);
                p = p.right;
            }

        }
    }

    public void preorder(){
        Stack stack = new Stack(100);
        Node p = root;
        while(p != null || !stack.isEmpty()){
            if(p != null){
                System.out.println(p.info.priority + " " + p.info.name + " " + p.info.surname);
                stack.push(p);
                p = p.left;
            }else{
                p = stack.pop();
                p = p.right;
            }
        }
    }

    public void postorder(){
        Stack stack = new Stack(10);
        Node p = root;

        if(p.right != null){
            stack.push(p.right);
            stack.push(p);
            p = p.left;
        }

        while (!stack.isEmpty()){
            while (p != null){
                if(p.right != null){
                    stack.push(p.right);
                }
                stack.push(p);
                p = p.left;
            }
            p = stack.pop();
            if(p.right == null){
                System.out.println(p.info.priority + " " + p.info.name + " " + p.info.surname);
                p = null;
            }else {
                if(p.right.equals(stack.top())){
                    stack.pop();
                    stack.push(p);
                    p = p.right;
                }else {
                    System.out.println(p.info.priority + " " + p.info.name + " " + p.info.surname);
                    p = null;
                }
            }
        }
    }

    public int height(Node p){
        if(p == null){
            return 0;
        }

        int left = height(p.left);
        int height = height(p.right);

        if(left > height){
            return left + 1;
        }else{
            return height + 1;
        }
    }
}

class Stack {
    public Node[] node;
    public int maxSize;
    public int currentSize;

    Stack(int maxSize) {
        this.maxSize = maxSize;
        this.node = new Node[maxSize];
        this.currentSize = 0;
    }

    public void push(Node element) {
        node[currentSize] = element;
        currentSize++;
    }

    public Node pop() {
        if (isEmpty()) {
            return null;
        } else {
            currentSize--;
            return node[currentSize];
        }
    }

    public Node top() {
        if (isEmpty()) {
            return null;
        } else {
            return node[currentSize - 1];
        }
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }
}