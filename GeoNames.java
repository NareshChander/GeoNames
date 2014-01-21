package Android.CompetencyCheckpoint3_Task2;
import android.app.Activity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpEntity;
import org.json.JSONObject;
import org.json.JSONArray;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GeoNames extends Activity implements OnClickListener

{
/** Called when the activity is first created. */
     Button clear;
     TextView tv;
     EditText s,n,e,w;
@Override
public void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    tv=(TextView)findViewById(R.id.final_results);
    clear=(Button)findViewById(R.id.btnClear);
     n = (EditText) findViewById(R.id.north);
     e = (EditText) findViewById(R.id.east);
     w = (EditText) findViewById(R.id.west);
     s = (EditText) findViewById(R.id.south);

     clear.setOnClickListener(this);
}

public void GetInfo(View v)
{
     float val=0;
     boolean flag1=true;
     boolean flag2=true;
     String outOfRange="";
     String invalidValue="";
     
//    EditText n = (EditText) findViewById(R.id.north);
//    EditText e = (EditText) findViewById(R.id.east);
//    EditText w = (EditText) findViewById(R.id.west);
//    EditText s = (EditText) findViewById(R.id.south);
    
            tv.setText("");

try
{
    val = Float.valueOf(n.getText().toString()); 
}
catch(NumberFormatException nex)
{
    invalidValue+="\nInvalid value for North.";
     flag2=false;
}
if(flag2==true)
{
if (val < -90 || val > 90) 
    {
        outOfRange+="\nOut of range value for North.";
        flag1=false;
    }
}

try
{
    val = Float.valueOf(s.getText().toString()); 
}
catch(NumberFormatException nex)
{
        invalidValue+="\nInvalid value for South.";
        flag2=false;
}
if(flag2==true)
{
if (val < -90 || val > 90) 
    {
        outOfRange+="\nOut of range value for South.";
        flag1=false;
    }
}

try
{
    val = Float.valueOf(e.getText().toString()); 
}
catch(NumberFormatException nex)
{
        invalidValue+="\nInvalid value for East.";
        flag2=false;
}
if(flag2==true)
{
if (val < -180 || val > 180) 
    {
        outOfRange+="\nOut of range value for East.";
        flag1=false;
    }
}

try
{
    val = Float.valueOf(w.getText().toString()); 
}
catch(NumberFormatException nex)
{
        invalidValue+="\nInvalid value for West.";
        flag2=false;
}
if(flag2==true)
{
if (val < -180 || val > 180) 
    {
        outOfRange+="\nOut of range value for West.";
        flag1=false;
    }
}



if(flag1==false)
    Toast.makeText(getApplicationContext(),outOfRange, Toast.LENGTH_LONG).show();  

if(flag2==false)
    Toast.makeText(getApplicationContext(),invalidValue, Toast.LENGTH_LONG).show(); 

if(flag1==true && flag2==true)
{
    try
    {
    String url= 
    //"http://api.geonames.org/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username=ennnceee&maxRows=1";
        "http://api.geonames.org/earthquakesJSON?"
            + "north="+n.getText().toString()
            +"&south="+s.getText().toString()
            + "&east="+e.getText().toString()
            + "&west="+w.getText().toString()
            + "&username=ennnceee"
            + "&maxRows=1";
    HttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet(url);
    StringBuilder builder = new StringBuilder();
    try
        {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            BufferedReader  reader  =  new  BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) 
                {
                    builder.append(line);
                }
            String result = builder.toString();
            this.DisplayInfo(result);
        }
    catch(ClientProtocolException ee)
        {
            ee.printStackTrace();
        }
    catch(IOException ee)
        {
            ee.printStackTrace();
        }
    }
    catch(Exception eee)
        {
        }
}
}


private void DisplayInfo(String result)
    {
        try
            {
            String s="";
            String noData="No Earthquakes reported in the given area.";
            JSONObject jsonObject = new JSONObject(result);
            JSONArray arr = new JSONArray(jsonObject.getString("earthquakes"));
            for(int a = 0; a<arr.length(); a++)
                {
                JSONObject element = arr.getJSONObject(a);

                s+="Magnitude: ";
                s+=element.getString("magnitude")+ ("\n");
                s+="Date/Time : ";
                s+=element.getString("datetime")+ ("\n");
//                s+="Country: ";
//                s+=element.getString("eqid")+ ("\n");
                }
                    //TextView tv = (TextView) findViewById(R.id.final_results);
                    if(arr.length()==0)
                        tv.setText(noData);
                    else
                        tv.setText(s);
            }
        catch (Exception e) 
        {
                Toast.makeText(this,"Connection Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
        }
    }

    public void onClick(View v) 
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    if(v == clear)
    {
        tv.setText("");
        n.setText("");
        s.setText("");
        e.setText("");
        w.setText("");
    }
    }
  }
