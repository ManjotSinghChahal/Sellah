package com.app.admin.sellah.controller.utils;
import java.util.Timer;
import java.util.TimerTask;

public class StopWatch {
    private long time_counter = 45;  // Seconds to set the Countdown from
    private Timer timer;

    public void startCountDown(){
        timer = new Timer();        // A thread of execution is instantiated
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println((--time_counter));

                if(time_counter == 0){
                    timer.cancel(); // timer is cancelled when time reaches 0
                }
            }
        },0,1000);
        // 0 is the time in second from when this code is to be executed
        // 1000 is time in millisecond after which it has to repeat
    }

    public void stopCountDown(){
        timer.cancel();
    }


    public static void main(String[] args){
        StopWatch s = new StopWatch();
        s.startCountDown();
    }
}
