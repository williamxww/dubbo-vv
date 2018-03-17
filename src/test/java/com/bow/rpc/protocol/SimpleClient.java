package com.bow.rpc.protocol;

import java.io.IOException;

import Demo.PrinterPrx;
import Demo.PrinterPrxHelper;
import Ice.Util;

/**
 * @author wwxiang
 * @since 2018/3/8.
 */
public class SimpleClient {

	private Ice.Communicator communicator;

	public PrinterPrx getProxy() {
		Ice.Properties properties = Util.createProperties();
		Ice.InitializationData initData = new Ice.InitializationData();
		initData.properties = properties;
		communicator = Util.initialize(initData);
		Ice.ObjectPrx proxy = communicator.stringToProxy("Printer:tcp -h 127.0.0.1 -p 10000");
		PrinterPrx printer = PrinterPrxHelper.checkedCast(proxy);
		return printer;

	}

	public static void main(String[] args) throws IOException {
		SimpleClient client = new SimpleClient();
		PrinterPrx printer = client.getProxy();
		while (true) {
			System.in.read();
			printer.printString("vv");
            System.out.println("Send 1 msg.");
        }

	}
}
