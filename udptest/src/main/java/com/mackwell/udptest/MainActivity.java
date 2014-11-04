package com.mackwell.udptest;

import java.util.List;
import java.util.concurrent.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    private final String TAG = "Main Activity";

	private final String FIND = "FIND";
	private final String SETT = "SETT";
	private final String SETC = "SETC";
	
	
	private UDPClient udpClient = null;


    private EditText ipEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        ipEditText = (EditText) findViewById(R.id.editText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
	public void search(View view)
	{
		
		System.out.println("send udp package");
		
		udpClient = new UDPClient(FIND,"255.255.255.255");
		
		udpClient.search();

	}
	
	public void openServer(View view)
	{
		System.out.println("-----Open Server-----");
		ExecutorService exec = Executors.newCachedThreadPool();
		UDPServer server = new UDPServer();
		exec.execute(server);
		
		
	}
	
	public void printPanelList(View view)
	{
		List<byte[]> panelList = udpClient.getPanelList();
		for(int i=0; i<panelList.size();i++)
		{
			System.out.print("\n");
			byte[] list =  panelList.get(i);
			for(int j=0; j<list.length;j++)
			{
				
				System.out.print(list[j]+ " ");
				
			}
			
		}
		
	}
	
	// ford and pull test	
	
	public void reset(View view)
	{
		Log.d(TAG, "Reset");
		
		if(udpClient==null) {
			udpClient = new UDPClient(FIND,"255.255.255.255");
		}
		udpClient.reset();
		
	}

    public void singleIpSearch(View view)
    {
        Log.d(TAG,"SingleIpSearch");
        if(udpClient==null) {
            udpClient = new UDPClient(FIND,"255.255.255.255");
        }
        udpClient.singleIpSearch(ipEditText.getText().toString());


    }

}
