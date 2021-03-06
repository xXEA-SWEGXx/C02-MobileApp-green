package net.fachtnaroe.co2_mobileapp_green;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.Web;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Form implements HandlesEventDispatching {

    private
    VerticalScrollArrangement Screen1;
    StatusBarTools statusBar;
    Web connection_toSomewhere;
    Button btn_doesSomething;
    Label msg_AllOK;

    static final int color_MAIN_BACKGROUND=0xFF000000; // black
    static final int color_MAIN_TEXT=0xFFFFFFFF; // white

    arduino_eeprom_data d1_Data=new arduino_eeprom_data();
    JSONObject d1_JSON=new JSONObject();
    Clock ticker=new Clock(this);

    protected void $define() {
        /* this next allows the app to use the full screen. In fact,
        seemingly anything makes this work at 100% except "Fixed" and the this.Sizing
        absent in the first place.
         */
        /* Cur seo isteach. Is cuma cén focal atá ann, níl gá leis */
        this.Sizing("Responsive");
        this.BackgroundColor(color_MAIN_BACKGROUND);
        Screen1 = new VerticalScrollArrangement(this);
        // each component, listed in order
        statusBar=new StatusBarTools(Screen1);

        statusBar.BGTransparentColor("#00000000");
        statusBar.BackgroundColor("#00000000");
        // now, how every component looks:
        Screen1.WidthPercent(100);
        Screen1.HeightPercent(100);
        Screen1.AlignHorizontal(Component.ALIGNMENT_NORMAL);
        Screen1.AlignVertical(Component.ALIGNMENT_CENTER);
        Screen1.BackgroundColor(color_MAIN_BACKGROUND);

        msg_AllOK=new Label(Screen1);
        msg_AllOK.WidthPercent(100);
        msg_AllOK.TextColor(color_MAIN_TEXT);
        msg_AllOK.HTMLFormat(true);
        msg_AllOK.Text("<br><br><br><br><br><br><h1 style='text-align: center;'>Howya, Mr O'Worrilt</h1>");

        // now, the events the components can respond to
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterSelecting");
        EventDispatcher.registerEventForDelegation(this, formName, "TimedOut"); // for network
        EventDispatcher.registerEventForDelegation(this, formName, "Timer"); // for updates
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        // finally, here is how the events are responded to
        dbg("dispatchEvent: " + formName + " [" +component.toString() + "] [" + componentName + "] " + eventName);
        if (eventName.equals("BackPressed")) {
            // this would be a great place to do something useful
            return true;
        }
        else if (eventName.equals("AfterSelecting")) {
        }
        else if (eventName.equals("Timer")) {
            if (component.equals(ticker)) {
                // turn off the timer while the event is being processed
                ticker.TimerEnabled(false);
                // process whatever the timer is for ...
                dbg("ticker has ticked");
                // turn the timer back on after the event is processed.
                ticker.TimerEnabled(true);
                // yeah, I turned it off and then back on again. But that's important, as ticks can collide
                return true;
            }
        }
        else if (eventName.equals("TimedOut")) {
            // do something
            return true;
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(connection_toSomewhere)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                handleNetworkResponse(component, status, textOfResponse);
                return true;
            }
        }
        else if (eventName.equals("Click")) {
            if (component.equals(btn_doesSomething)) {

                return true;
            }
         }
        return false;
    }

    void handleNetworkResponse(Component c, String status, String textOfResponse) {
        dbg(("<br><b>" + "some message here" + ":</b> " + textOfResponse + "<br>"));
        if (status.equals("200")) try {
                JSONObject parser = new JSONObject(textOfResponse);
                if (parser.getString("Status").equals("OK")) {
                    if (c.equals(connection_toSomewhere)) {
                    }
            }
        }
        catch(JSONException e){
                dbg("Android JSON exception (" + textOfResponse + ")");
            }
        else{
                dbg("Status is " + status);
            }
    }

    public static void dbg (String debugMsg) {
        System.err.print( "~~~> " + debugMsg + " <~~~\n");
    }
    public static boolean isNumeric(String string) {
        int intValue;
        if(string == null || string.equals("")) {
            return false;
        }
        try {
            intValue = Integer.parseInt(string);
            return true;
        }
        catch (NumberFormatException e) {
            //
        }
        return false;
    }
}
// Here be monsters:
