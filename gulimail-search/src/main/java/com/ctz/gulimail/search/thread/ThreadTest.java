package com.ctz.gulimail.search.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThreadTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*Thread01 thread01 = new Thread01();
        thread01.run();*/
//        new Thread(new Runnable01()).start();
        FutureTask<Integer> integerFutureTask = new FutureTask<>(new Callable01());
//        new Thread(integerFutureTask).start();
        Integer integer = integerFutureTask.get();
        System.out.println(integer);
    }

    public static class Thread01 extends Thread {
        @Override
        public void run() {
            System.out.println("abc");
        }
    }

    public static class Runnable01 implements Runnable {
        @Override
        public void run() {
            System.out.println("cba");
        }
    }

    public static class Callable01 implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("aaa");
            return 100;
        }
    }
}
