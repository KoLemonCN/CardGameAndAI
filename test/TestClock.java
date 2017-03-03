package test;

import game.util.Clock;

public class TestClock {
	public static void main(String[] args) {
		Clock clock = new Clock();
		
		clock.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clock.stop();
		
		System.out.println("getNanoTimeInterval: " + clock.getNanoTimeInterval()/Clock.getNanoToMillis());
		
	}
}
