package testCases;

import static reusableActions.SignIn.signIn;
import static reusableActions.SignOut.signOut;
import static utility.Constant.testSheetName;
import static utility.Constant.testSheetPath;
import static utility.Constant.uname;
import static utility.ExcelUtils.setExcelFile;
import static utility.SauceConnect.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

public class TC02OpenProjectInClarityGannt {
	final String TCName="TC02OpenProjectInClarityGannt";
	 final int TCNum=2;
	 final String BrowserVersion="11";
	 final String BrowserName="IE";
	 Boolean status=true;
	    
	    @Before
	    public void setUp() throws Exception {
	    	uname="svct411";
	    //       wd = new FirefoxDriver();
	    	// wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	    	
	    	 //Set connection and Update job info	
	    	setConnection(TCName,TCNum,BrowserName,BrowserVersion);
	    	
	        //open excel sheet
	       setExcelFile(testSheetPath+TCName+".xlsx",testSheetName);
	    	
	    }
	  @Test
	    public void webDriver() throws Exception 
	    {
	    	//----sign in
	    	if(signIn(wd)==0)
	    		status=false;
	    	print("signed in");
	    	 new Actions(wd).moveToElement(wd.findElement(By.id("ppm_nav_app"))).build().perform();
	    	 print("entered");
	        wd.get("https://m.gisclarity-stage.ca.com/niku/nu#action:mainnav.work&classCode=project");
	         //wd.findElement(By.cssSelector(".ppm_umenu_section>a[title='Projects']")).click();
	         print("entered");
	    	//wd.get("http://claritystage.ca.com/niku/nu#action:mainnav.work&classCode=project");
	    List<WebElement> l1=wd.findElements(By.id("projmgr.projectProperties"));
	    for(int i=0;i<l1.size();i++){
	    	String t=l1.get(i).getAttribute("title");
	    	print(t);
	    	if(t.contains("PR04388"))
	    	{
	    		l1.get(i).click();
	    		break;
	    	}
	    }
	    if(!waitcheck("Project Name"))
	    	status=false;
	    String pwhandle=wd.getWindowHandle();
	    wd.findElement(By.xpath("//div[@class='ppm_workspace_title_row']//span[.='Open in Scheduler']")).click();
        wd.findElement(By.linkText("Clarity Gantt")).click();
       Set<String> st=wd.getWindowHandles();
       System.out.println(st.size());
       Set<String> windowSet = wd.getWindowHandles();  
       Iterator<String> windowIterator=windowSet.iterator();
       int windowNum=0;
       while((windowIterator.hasNext()))
       { 
    	   windowNum++;
    	   String windowHandle = windowIterator.next();
          if(windowNum==2) //move to new window
          {
        	  Thread.sleep(1000);
			  wd = wd.switchTo().window(windowHandle);
					  wd.manage().window().maximize();
					  wd.findElement(By.linkText("Project Management")).click();
					  if(!waitcheck("Estimating"))
					    	status=false;
					 wd.close();
					 wd.switchTo().window(pwhandle);
          }
       }
	    	
	    	if(signOut(wd)==0)
	    		status=false;
    	   
    }	
    
    @After
    public void tearDown() throws IOException {
   	if(status)
    client.jobPassed(jobID);
    	else
    		client.jobFailed(jobID);	 
     wd.quit();//close driver
    }
}
