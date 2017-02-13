package mutelol;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionListener;
import java.awt.im.InputContext;
import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import javax.imageio.ImageIO;

import com.sun.glass.events.KeyEvent;


import com.sun.jna.Library;
import com.sun.jna.Native;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.ptr.PointerByReference;

import static mutelol.mutelol.Kernel32.*;
import static mutelol.mutelol.Psapi.*;
import static mutelol.mutelol.User32DLL.*;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


 
 // LeagueClientUxHelper.exe

public class mutelol extends javafx.application.Application {
	
	private boolean isStop = true;
	private boolean isMute = false;
	private int count = 0;
	private static final int MAX_TITLE_LENGTH = 1024;
	
    public static void main(String[] args) {
        launch(args);
    }

 
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        primaryStage.setTitle("MuteLoL");
               
        // ensure awt toolkit is initialized.
        java.awt.Toolkit.getDefaultToolkit();

        // app requires system tray support, just exit if there is no support.
        if (!java.awt.SystemTray.isSupported()) {
            System.out.println("No system tray support, application exiting.");
            Platform.exit();
        }

        // set up a system tray icon.
        java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
        //URL imageLoc = new URL("http://downloadicons.net/sites/default/files/smile-smilies-710.png");
        //java.awt.Image image = ImageIO.read(imageLoc);
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage("C:/Users/ijij6960/Dropbox/java/workspace/mutelol/src/mutelol/smile-smilies-710.png");
        PopupMenu popMenu= new PopupMenu();
        MenuItem item1 = new MenuItem("Status");
        MenuItem item2 = new MenuItem("Exit");
        ActionListener item2Listener = new ActionListener() {                
            @Override
            public void actionPerformed(java.awt.event.ActionEvent arg0) {
                // TODO Auto-generated method stub
                System.exit(0);                 
            }               
        };            
        item2.addActionListener(item2Listener);
        popMenu.add(item1);
        popMenu.addSeparator();
        popMenu.add(item2);
        
        java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image, "MuteLoL", popMenu);
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);
   
        Label label = new Label();
        label.setText("Initializing...");
        item1.setLabel("Initializing...");
  
        Button btn = new Button();
        btn.setText("Initialize...");
		
        btn.setOnAction(new EventHandler<ActionEvent>() {
         	
            @Override
            public void handle(ActionEvent event) {
                
                isStop = !isStop;
                isMute = false;
                
                if (isStop)
                {
                	btn.setText("Start");
                	label.setText("...");
                	item1.setLabel("...");
                	return;
                }
                else{
                	btn.setText("Working");
                }
                
            	Thread thread = new Thread() {
                	@Override public void run() {
                		
                		try {
                    		while(!isStop)
                    		{
                    			Thread.sleep(5000);
                    			
                    			if (isStop) break;	//반응성을 좋게 하기 위해..
                    			
                    			Platform.runLater(()->{
                    				
                    				
                    				String lolFileName = "League of Legends.exe";
                    				//String lolFileName = "notepad.exe";
                    				String lolWindowName = "League of Legends (TM) Client";
                    				
                    				try {
                        				if (isMute == false)
                        	    		{          
                        					System.out.println("Checking " + count);
                        					Platform.runLater ( () -> label.setText ("Checking LoL"));
                        					Platform.runLater ( () -> item1.setLabel("Checking LoL"));
                        					
                        					if (checkProcess(lolFileName))
                        					{
                        						if (checkActiveWindowTitle(lolWindowName))
                            	    			{
                        							Platform.runLater ( () -> label.setText ("Running LoL"));
                        							Platform.runLater ( () -> item1.setLabel("Running LoL"));
                        							
                        							RECT rect = new RECT();
                        						    GetClientRect(GetForegroundWindow(), rect);
                        						    System.out.println("GetClientRect : " + rect);
                        						    
                        						    POINT point = new POINT((rect.right-rect.left)/2, rect.bottom-2);
                        						    ClientToScreen(GetForegroundWindow(), point);
                        						    System.out.println("ClientToScreen : " + point);
                        						    
                        							Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
                            						System.out.println("해상도 : " + res.width + " x " + res.height); 
                            						
                        							//if (checkPixel((rect.right - rect.left)/2 + rect.left,rect.bottom-1,82,73,41))
                        							//if (checkPixel(res.width/2,res.height-2,82,73,41))
                            						if (checkPixel(point.x, point.y,82,73,41))
                        							{
                        								setMacro();
                        								isMute = true;
                        							}
                            	    			}
                        					}
                        	    		}
                        	    		else
                        	    		{
                        	    			System.out.println("Waiting " + count);
                        	    			Platform.runLater ( () -> label.setText ("Playing Game"));
                        	    			Platform.runLater ( () -> item1.setLabel("Playing LoL"));
                        	    			
                        	    			if (checkProcess(lolFileName) == false)
                        	    				isMute = false;
                        	    		}
                        				count = count + 1;
                    				} catch(Exception e) {e.printStackTrace();}
                    			});
                    		}
                		} catch(Exception e) {e.printStackTrace();}
                	};
                };
                thread.setDaemon(true);
                thread.start();
            }
        });
        
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
       
        vbox.getChildren().add(label);
        vbox.getChildren().add(btn);
        
        primaryStage.setScene(new Scene(vbox, 270, 130));
        primaryStage.hide();
        
        btn.fire();
    }
    
    
    Boolean checkProcess(String processName)
    {
		String filenameFilter = "/nh /fi \"Imagename eq "+processName+"\"";
		String tasksCmd = System.getenv("windir") +"/system32/tasklist.exe "+filenameFilter;

		Process p;
		try {
			p = Runtime.getRuntime().exec(tasksCmd);

			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

			ArrayList<String> procs = new ArrayList<String>();
			String line = null;
			while ((line = input.readLine()) != null) 
				procs.add(line);

			input.close();

			Boolean processFound = procs.stream().filter(row -> row.indexOf(processName) > -1).count() > 0;
			System.out.println("process found : " + processFound);
			return processFound;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
    }
    
    Boolean checkActiveWindowTitle(String windowName)
    {
    	char[] buffer = new char[MAX_TITLE_LENGTH * 2];
        GetWindowTextW(GetForegroundWindow(), buffer, MAX_TITLE_LENGTH);
        System.out.println("Active window title: " + Native.toString(buffer));
        
        if (Native.toString(buffer).equals(windowName))
        {
        	System.out.println("step0 : true");
        	return true;
        }
        System.out.println("step0 : false");
        return false;
    }
    
    Boolean checkPixel(int x, int y, int r, int g, int b) throws AWTException
    {
    	Robot rbt = new Robot();
    	Color pixel = rbt.getPixelColor(x, y);
    	System.out.println(x + "," + y + " rgb : " + pixel);
    	
    	if (pixel.getRed() == r && pixel.getGreen() == g && pixel.getBlue() == b)
    		return true;
    	return false;
    }
    
    
    void setMacro ()  throws AWTException
    {
  
    	/* ime 강제 변경 ... 동작하지 않음
    	Locale.setDefault(new Locale("en", "US", "WIN"));
    	System.out.println("default : "+Locale.getDefault());
    	Character.Subset[]  subset  =  {null};
    	
    	InputContext.getInstance().setCompositionEnabled(false);
    	System.out.println("getlocale : " + InputContext.getInstance().getLocale());
    	System.out.println("getInputMethodControlObject : " + InputContext.getInstance().getInputMethodControlObject());
    	
    	boolean b = InputContext.getInstance().selectInputMethod(Locale.ENGLISH);
        System.out.println("Set Language : " + b);
        */


    	Robot rbt = new Robot();
    	rbt.setAutoDelay(50);
    	
    	/*
    	rbt.keyPress(KeyEvent.VK_ENTER);
    	rbt.keyRelease(KeyEvent.VK_ENTER);
    	
    	// * 롤 클라이언트에서 클립보드를 지워버림
    	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    	String copyString = "/mute all";
    	Transferable transferable  = new StringSelection(copyString);
    	clipboard.setContents(transferable , null);

    	rbt.keyPress(KeyEvent.VK_CONTROL);
    	rbt.keyPress(KeyEvent.VK_V);
    	    	
    	rbt.keyRelease(KeyEvent.VK_V);
    	rbt.keyRelease(KeyEvent.VK_CONTROL);

    	rbt.keyPress(KeyEvent.VK_ENTER);
    	rbt.keyRelease(KeyEvent.VK_ENTER);
    	*/
    	
    	rbt.keyPress(KeyEvent.VK_ENTER);
    	rbt.keyRelease(KeyEvent.VK_ENTER);
    	
    	rbt.keyPress(KeyEvent.VK_DIVIDE);
    	rbt.keyRelease(KeyEvent.VK_DIVIDE);
    	
    	rbt.keyPress(KeyEvent.VK_M);
    	rbt.keyRelease(KeyEvent.VK_M);
    	rbt.keyPress(KeyEvent.VK_U);
    	rbt.keyRelease(KeyEvent.VK_U);
    	rbt.keyPress(KeyEvent.VK_T);
    	rbt.keyRelease(KeyEvent.VK_T);
    	rbt.keyPress(KeyEvent.VK_E);
    	rbt.keyRelease(KeyEvent.VK_E);
    	rbt.keyPress(KeyEvent.VK_SPACE);
    	rbt.keyRelease(KeyEvent.VK_SPACE);
    	rbt.keyPress(KeyEvent.VK_A);
    	rbt.keyRelease(KeyEvent.VK_A);
    	rbt.keyPress(KeyEvent.VK_L);
    	rbt.keyRelease(KeyEvent.VK_L);
    	rbt.keyPress(KeyEvent.VK_L);
    	rbt.keyRelease(KeyEvent.VK_L);
    	
    	
    	rbt.keyPress(KeyEvent.VK_ENTER);
    	rbt.keyRelease(KeyEvent.VK_ENTER);
    	
    	System.out.println("Set Macro");
    }
    
    static class Psapi {
        static { Native.register("psapi"); }
        public static native int GetModuleBaseNameW(Pointer hProcess, Pointer hmodule, char[] lpBaseName, int size);
    }

    static class Kernel32 {
        static { Native.register("kernel32"); }
        public static int PROCESS_QUERY_INFORMATION = 0x0400;
        public static int PROCESS_VM_READ = 0x0010;
        public static native int GetLastError();
        public static native Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle, Pointer pointer);
    }
    
    static class User32DLL {
        static { Native.register("user32"); }
        public static native int GetWindowThreadProcessId(HWND hWnd, PointerByReference pref);
        public static native HWND GetForegroundWindow();
        public static native int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);
        public static native void GetClientRect(HWND hWnd, RECT rect);
        public static native boolean ClientToScreen(HWND hWnd, POINT point);
    }
}