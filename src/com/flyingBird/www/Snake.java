package com.flyingBird.www;

import java.util.LinkedList;

public class Snake {
    private LinkedList<Node> body;
    private Direction direction;
    private boolean isLiving;

    public boolean isLiving() {
        return isLiving;
    }

    public void setLiving(boolean living) {
        isLiving = living;
    }

    public Snake(){
        direction = Direction.LEFT;
        isLiving = true;
        initSanke();
    }

    private void initSanke() {
        body = new LinkedList<>();
        body.add(new Node(16, 16));
        body.add(new Node(17, 16));
        body.add(new Node(18, 16));
        body.add(new Node(19, 16));
        body.add(new Node(20, 16));
        body.add(new Node(21, 16));
//        body.add(new Node(22, 16));
//        body.add(new Node(23, 16));
//        body.add(new Node(24, 16));
//        body.add(new Node(25, 16));
//        body.add(new Node(26, 16));
//        body.add(new Node(27, 16));
//        body.add(new Node(28, 16));
//        body.add(new Node(29, 16));


    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void move(){
        if (isLiving) {
            Node head = body.getFirst();
            switch (direction) {
                case UP:
                    body.addFirst(new Node(head.getX(), head.getY()-1));
                    break;
                case DOWN:
                    body.addFirst(new Node(head.getX(), head.getY()+1));
                    break;
                case LEFT:
                    body.addFirst(new Node(head.getX()-1, head.getY()));
                    break;
                case RIGHT:
                    body.addFirst(new Node(head.getX()+1, head.getY()));
                    break;
            }
            body.removeLast();

            head = body.getFirst();
            if (head.getX() < 0 || head.getY() < 0 || head.getX() > 40 || head.getY() > 40) {
                isLiving = false;
            }

            for (int i = 1; i < body.size(); i++) {
                Node node = body.get(i);
                if(head.getX() == node.getX() && head.getY() == node.getY()) {
                    isLiving = false;
                }
            }
        }

    }

    public LinkedList<Node> getBody() {
        return body;
    }

    public void setBody(LinkedList<Node> body) {
        this.body = body;
    }

    public void eat(Node food) {
        Node head = body.getFirst();
        switch (direction) {
            case UP:
                body.addFirst(new Node(head.getX(), head.getY()-1));
                break;
            case DOWN:
                body.addFirst(new Node(head.getX(), head.getY()+1));
                break;
            case LEFT:
                body.addFirst(new Node(head.getX()-1, head.getY()));
                break;
            case RIGHT:
                body.addFirst(new Node(head.getX()+1, head.getY()));
                break;
        }
    }
}
