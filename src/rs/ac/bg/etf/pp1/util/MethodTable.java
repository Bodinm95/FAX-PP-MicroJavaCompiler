package rs.ac.bg.etf.pp1.util;

import java.util.ArrayList;
import java.util.List;

import rs.etf.pp1.mj.runtime.Code;

public class MethodTable {

	public List<Byte> table;

	public MethodTable() {
		table = new ArrayList<Byte>();
	}

	public void addWordToStaticData(int value, int address) {
		table.add(new Byte((byte)Code.const_));
		table.add(new Byte((byte)((value >> 16) >> 8)));
		table.add(new Byte((byte)(value >> 16)));
		table.add(new Byte((byte)(value >> 8)));
		table.add(new Byte((byte)value));
		table.add(new Byte((byte)Code.putstatic));
		table.add(new Byte((byte)(address >> 8)));
		table.add(new Byte((byte)address));
	}

	public void addNameTerminator() {
		addWordToStaticData(-1, Code.dataSize++);
	}

	public void addTableTerminator() {
		addWordToStaticData(-2, Code.dataSize++);
	}

	public void addFunctionAddress(int functionAddress) {
		addWordToStaticData(functionAddress, Code.dataSize++);
	}

	public void addFunctionEntry(String name, int functionAddressInCodeBuffer) {
		for (int i = 0; i < name.length(); i++)
			addWordToStaticData(name.charAt(i), Code.dataSize++);
		addNameTerminator();
		addFunctionAddress(functionAddressInCodeBuffer);
	}
}
