/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.wendani;

import com.cloudpesa.digipesa.signin.LoginForm;
import com.codename1.io.Storage;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.Resources;
import static com.codename1.ui.util.Resources.getGlobalResources;
import com.codename1.ui.util.UITimer;

/**
 *
 * @author Festus
 */
public class SplashScreen extends Form
{
    protected com.codename1.ui.Container gui_Container_1 = new com.codename1.ui.Container(new BoxLayout(BoxLayout.Y_AXIS));
    protected com.codename1.ui.Container gui_Container_2 = new com.codename1.ui.Container(new FlowLayout());
    protected com.codename1.ui.Label gui_Label_1 = new com.codename1.ui.Label();
    private Boolean bool_pinCode_exist;
    
    public SplashScreen()
    {
        initGuiBuilderComponents(Resources.getGlobalResources());
        getTitleArea().setUIID("Container");
        int size = Display.getInstance().convertToPixels(0.5f);
        Image logoImage = getGlobalResources().getImage("Codename_One.png").scaledWidth(Math.round(Display.getInstance().getDisplayWidth()));
        gui_Label_1.getAllStyles().setMargin(0, 0, 0, 0);
        gui_Label_1.getAllStyles().setPadding(0, 0, 0, 0);
        gui_Label_1.setIcon(logoImage);
        
        bool_pinCode_exist = Storage.getInstance().exists("00032pinCode");
        UITimer.timer(3000, false, this, () -> 
        {
            new LoginForm().show();
        });
    }
    
    private void initGuiBuilderComponents(Resources resourceObjectInstance) 
    {
        setLayout(new BorderLayout());
        setInlineStylesTheme(resourceObjectInstance);
        setUIID("Splash");
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("");
        setName("SplashForm");
        ((com.codename1.ui.layouts.BorderLayout)getLayout()).setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER);
                gui_Container_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_1.setName("Container_1");
        addComponent(com.codename1.ui.layouts.BorderLayout.CENTER, gui_Container_1);
                gui_Container_2.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_2.setName("Container_2");
        ((com.codename1.ui.layouts.FlowLayout)gui_Container_2.getLayout()).setAlign(CENTER);
                gui_Label_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_1.setName("Label_1");
        gui_Label_1.setIcon(resourceObjectInstance.getImage("Codename_One.png"));
        gui_Container_1.addComponent(gui_Container_2);
        gui_Container_1.addComponent(gui_Label_1);
    }
}
