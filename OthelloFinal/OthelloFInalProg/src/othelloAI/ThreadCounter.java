package othelloAI;

public class ThreadCounter {
	static int count=0;
	static double[][] logic;
	static long end_time=0;
	
	public static void plus(){
		setCount(getCount() + 1);
	}
	
	public static void minus(){
		setCount(getCount() - 1);
	}

	public static void setCount(int count) {
		ThreadCounter.count = count;
	}

	public static int getCount() {
		return count;
	}
	public static void setLogic(double[][] logic_temp){
		logic=logic_temp;
	}
	
	public static double getLogic(int x, int y) {
		return logic[x][y];
	}

	public static void setEndTime(long end_time1) {
		end_time=end_time1;
	}
	
	public static long getEndTime(){
		return end_time;
	}
	
}
