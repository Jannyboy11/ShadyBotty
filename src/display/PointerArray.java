package display;

public class PointerArray<O> {
	private final Object[] array;
	private int pointer = 0;
	
	public PointerArray(int size) {
		array = new Object[size];
		for (int i =0; i < array.length; i ++)
			array[i] = null;
	}

	/**
	 * @return the array
	 */
	public O[] getArray() {
		return (O[])array;
	}
	
	public void setNext(O next) {
		array[pointer] = next;
		pointer = (pointer+1) % array.length;
	}
	
	public O[] getOrdered() {
		Object[] result = new Object[array.length];
		for (int i = 0; i < array.length; i ++) {
			result[i] = array[(pointer+i) % array.length];
		}
		return (O[])result;
	}
	
	public String orderedString(String split) {
		O[] ordered = getOrdered();
		String result = "";
		for (int i = 0; i < ordered.length-1; i++)
			result += ordered[i] == null? "" :  ordered[i].toString() + split ;
		result += ordered[ordered.length-1] == null? "" :  ordered[ordered.length-1].toString();
		return result;
	}
}
