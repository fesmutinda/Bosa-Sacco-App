/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.wendani.account;

import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.Storage;
import static com.codename1.ui.CN.getCurrentForm;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Festus
 */
public class Ministatement {
    public void showThis(){
        showDemo("Ministatements", createContentPane());
    }
    
    public Container createContentPane() {
        Container demoContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS), "DemoContainer");
        demoContainer.setScrollableY(true);
        demoContainer.add(createStatements());

        return demoContainer;
    }
    
    public Container createStatements() 
    {
        Container statementContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        String str_content = (String) Storage.getInstance().readObject("Statements.json");
        Log.p("Reading >>>>>>>>>>>>>>\n"+str_content+"\n<<<<<<<<<<< .json from storage.", 1);

        byte[] bytes_content = str_content.getBytes();

        JSONParser json_content = new JSONParser();

        try(Reader is_content = new InputStreamReader(new ByteArrayInputStream(bytes_content), "UTF-8"))
        {
            Map<String, Object> data = json_content.parseJSON(is_content); 
            //Get the transactions
            ArrayList transactions = (ArrayList)data.get("transactions");
            System.out.println("transactions : " + transactions);

            for(int i=0; i<transactions.size(); i++){
                Map<String, Object> transaction = (Map<String, Object>)transactions.get(i);

                String transDate = (String)transaction.get("transDate");
                String description = (String)transaction.get("description");
                String amount = (String)transaction.get("amount");
                
                statementContainer.add(createStatement(amount, description, transDate));
            }
        }
        catch(IOException err) 
        {
                Log.p("Error: "+err, 3);
        }
        
        statementContainer.setScrollableY(true);
        statementContainer.setScrollVisible(false);
        return statementContainer;
    }
    private Container createStatement(String amount, String description, String transDate){
        Container loanGuaranteedContainer = BoxLayout.encloseY(new Label(""));
        loanGuaranteedContainer.setUIID("LabelContainer");
        
        loanGuaranteedContainer.addComponent(BorderLayout.east(new Label(""+amount, "detailHead")).
                add(BorderLayout.WEST, new Label(description, "detailHead")));
        loanGuaranteedContainer.add(new Label(transDate, "detailHead"));
        
        return BoxLayout.encloseY(loanGuaranteedContainer);
    }
    protected void showDemo(String title, Component content){
        Form demoForm = new Form(title, new BorderLayout());
        content.setUIID("ComponentDemoContainer");
        Toolbar toolbar = demoForm.getToolbar();

        Form previous = getCurrentForm();
        toolbar.setBackCommand("", Toolbar.BackCommandPolicy.AS_ARROW,
            e -> previous.showBack());

        demoForm.add(BorderLayout.CENTER, content);
        demoForm.show();
    }
}
