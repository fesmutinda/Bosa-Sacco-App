/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.forms;

import com.codename1.ui.BrowserComponent;
import static com.codename1.ui.CN.getCurrentForm;
import com.codename1.ui.Form;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;

/**
 *
 * @author Festus
 */
public class FindUmojaWendani {
    public void showWebView() 
    {
	Form form = new Form();
	BrowserComponent bc = new BrowserComponent();
	form.setLayout(new BorderLayout());
	form.add(BorderLayout.CENTER, bc);
	bc.setURL("https://www.uws.co.ke/");
	
        Toolbar toolbar = form.getToolbar();

            Form previous = getCurrentForm();
            toolbar.setBackCommand("", Toolbar.BackCommandPolicy.AS_ARROW,
                e -> previous.showBack());
        
	form.show();
    }
}
