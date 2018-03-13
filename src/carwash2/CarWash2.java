package carwash2;
import java.util.*;
//Car wash simulation, Allen Williams - Linear Data Structures
//Program uses 2 parallel queues for arrival times and service times
//instead of a Customer class holding these variables because the program
//specifications require that a queue hold "arrival times".
class Queue<T> {
        private node head;
        private node tail;
        private int size;
        final int MAXSIZE=8;
        public Queue() {}
        private class node {
            private T value;
            private node link;
            private node back;
            node() {}
            node(T value) {this.value = value;}
            public T getValue() {return this.value;}
            public node getLink() {return this.link;}
            public void setLink(node link) {this.link = link;}
            public node getBack() {return this.back;}
            public void setBack(node back) {this.back = back;}
        }
        public boolean isFull() {return(size == MAXSIZE);}
        public boolean isEmpty() {return(size == 0);}
        public void enqueue(T toAdd) {
            //Preconditions : size < MAXSIZE
            node temp = new node(toAdd);
            if (size==0) tail = temp;
            else head.setBack(temp);
            temp.setLink(head);
            head = temp;
            size++;
        }
        public T dequeue() {
            node temp;
            temp = tail;
            tail = tail.getBack();
            size--;
            return temp.getValue();
        }
        public T peek() {return tail.getValue();}
    }
public class CarWash2 {
    public static void main(String[] args) {
        int hours, minutes, now, end, nextArrival, serviceTime, numArrived,
                numQueued, numServed, currFinish, idleTime, numBypassed,
                totalWait, maxWait, startCars;
        double avgWait;
        Queue<Integer> arrivalTimes = new Queue<>();
        Queue<Integer> serviceTimes = new Queue<>();
        Random Rand   = new Random();
        Scanner stdin = new Scanner(System.in);
        
        for(int i = 0; i < 45; i++) System.out.print("\u203e");
        System.out.println("\nParameters");
        for(int i = 0; i < 45; i++) System.out.print("_");
        System.out.print("\n\nHours\t: ");
        hours = Integer.parseInt(stdin.next());
        System.out.print("Minutes\t: ");
        minutes = Integer.parseInt(stdin.next());
        System.out.print("Cars in queue initially: ");
        startCars = Integer.parseInt(stdin.next());
        for (int i = 0; i < startCars; i++) {
            arrivalTimes.enqueue(0);
            serviceTimes.enqueue(Rand.nextInt(4) + 2);
        }
        now         = 0;
        end         = (hours*60 + minutes);
        numArrived  = 0;
        numQueued   = 0;
        numServed   = 0;
        idleTime    = 0;
        currFinish  = -1;
        totalWait   = 0;
        maxWait     = 0;
        nextArrival = (Rand.nextInt(5)+1);
        if (!arrivalTimes.isEmpty()) currFinish = serviceTimes.peek();
        while (now < end) {
            if (now == nextArrival) {
                if (!arrivalTimes.isFull()){
                    arrivalTimes.enqueue(now);
                    serviceTime = Rand.nextInt(4) + 2;
                    if (serviceTimes.isEmpty()) currFinish = now + serviceTime;
                    serviceTimes.enqueue(serviceTime);
                    numQueued++;
                }
                nextArrival = (now + Rand.nextInt(5)+1);
                numArrived++;
            }
            if (currFinish == now) {
                numServed++;
                arrivalTimes.dequeue();
                serviceTimes.dequeue();
                if(!arrivalTimes.isEmpty()) {
                    currFinish = now + serviceTimes.peek();
                    totalWait += (now - arrivalTimes.peek());
                    if ((now - arrivalTimes.peek()) > maxWait) 
                        maxWait = now - arrivalTimes.peek();
                }
            }
            if(arrivalTimes.isEmpty()) idleTime++;
            now++;
        }
        avgWait = (totalWait*1.0/numServed);
        numBypassed = numArrived - numQueued;
        System.out.println();
        for (int i = 0; i < 45; i++) System.out.print("\u203e");
        System.out.println("\nSimulation Statistics");
        for (int i = 0; i < 45; i++) System.out.print("_");
        System.out.println("\nCustomers Served \t: " + numServed);
        System.out.println("Minutes Idle \t\t: " + idleTime);
        System.out.println("Bypassed Car Wash \t: " + numBypassed);
        System.out.printf("Average Wait Time \t: %.2f Minutes\n", avgWait);
        System.out.println("Maximum Wait Time \t: " + maxWait);
    }
}
