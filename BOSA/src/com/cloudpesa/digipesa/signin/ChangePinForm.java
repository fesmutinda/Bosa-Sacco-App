/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.signin;

import com.cloudpesa.digipesa.forms.HomePageForm;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.ToastBar;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.processing.Result;
import com.codename1.ui.Button;
import static com.codename1.ui.CN.getCurrentForm;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextComponent;
import com.codename1.ui.Toolbar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.layouts.TextModeLayout;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Festus
 */
public class ChangePinForm {
    private String str_server_response, str_response;
    
    public void changepinScreen() 
    {
        showScreen("Change PIN", createTextComponentContainer());
    }
    private Container createTextComponentContainer() 
    {
        Container textContainer = new Container(new LayeredLayout());
        // Set UIID for background image.
        textContainer.setUIID("InputContainer");
        Container textFields = new Container(new TextModeLayout(6, 1));

        TextComponent currentPIN = new TextComponent().labelAndHint("Current Pin").constraint(TextArea.NUMERIC | TextArea.PASSWORD);
        TextComponent newPIN = new TextComponent().labelAndHint("New PIN").constraint(TextArea.NUMERIC | TextArea.PASSWORD);
        TextComponent confirmPIN = new TextComponent().labelAndHint("Confirm PIN").constraint(TextArea.NUMERIC | TextArea.PASSWORD);

        textFields.add(currentPIN);
        textFields.add(newPIN);
        textFields.add(confirmPIN);

        Button saveButton = new Button("CHANGE PIN", "transactionButtons");
        
        Validator val = new Validator();
        val.setValidationFailureHighlightMode(Validator.HighlightMode.UIID);
        val.addSubmitButtons(saveButton)
                .addConstraint(currentPIN, new LengthConstraint(4))
                .addConstraint(newPIN, new LengthConstraint(4))
                .addConstraint(confirmPIN, new LengthConstraint(4));

        saveButton.addActionListener(ee ->
        {
            if(!newPIN.getText().equals(confirmPIN.getText()))
            {
                Dialog.show("", "Your new PIN and confirmation PIN does not match", "Close", "Retry");
            }
            else if(!currentPIN.getText().equals("2032"))
            {
                Dialog.show("", "Please Enter Your Current PIN correctly", "Close", "Retry");
            }
            else
            {
            }
        });

        textFields.add(saveButton);
        Container textFieldsAndSaveButton = BorderLayout.center(textFields);
        textFieldsAndSaveButton.setUIID("TextComponents");

        return BorderLayout.center(textFieldsAndSaveButton);
    }
     protected void showScreen(String title, Component content){
        Form depoForm = new Form(title, new BorderLayout());
        content.setUIID("ComponentContainer");
        Toolbar toolbar = depoForm.getToolbar();

        Form previous = getCurrentForm();
        toolbar.setBackCommand("", Toolbar.BackCommandPolicy.AS_ARROW,
            e -> previous.showBack());

        depoForm.add(BorderLayout.CENTER, content);
        depoForm.show();
    }
}
