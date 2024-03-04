/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.loans;

import com.cloudpesa.digipesa.forms.HomePageForm;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.SpanLabel;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.processing.Result;
import com.codename1.ui.Button;
import static com.codename1.ui.CN.getCurrentForm;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.util.StringUtil;
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
public class MobileLoansForm 
{
    private String SaccoID = "", PhoneNumber, LoanType="";
    private String Key = Storage.getInstance().readObject("Key").toString();
    private TextField AmountToET, AmountToETDV,AmountToETMOB,AmountToETFLASH, resp;
    double AmountSTR = 0.0;
    private static String result = "";
    private String myUrl, Reply;
    public void loansScreen(){
        showScreen("Mobile Loans", createMobileLoansScreen());
    }
    public void checkLimit() throws UnsupportedEncodingException, IOException{
        
        PhoneNumber = Storage.getInstance().readObject("PhoneNumber").toString();
        SaccoID = Storage.getInstance().readObject("SaccoID").toString();
        
        myUrl = "https://suresms.co.ke:45322/capi/AdvanceEligibility";
        
        Hashtable hash =new Hashtable();
        hash.put("phone", PhoneNumber);
        hash.put("SaccoID", SaccoID);
        hash.put("period", "1");   
        hash.put("loantype", "Eloan");   
        hash.put("Key", Key);   

        final Result res=Result.fromContent(hash);
        final String checkthis=res.toString();
        
        ConnectionRequest request=new ConnectionRequest()
        {
            public void buildRequestBody(OutputStream thisstream) throws IOException
            {
                Writer andika=null;
                andika=new OutputStreamWriter(thisstream,"UTF-8");
                andika.write(checkthis);
                andika.flush();
                andika.close();
            }
        };
        request.setUrl(myUrl);
        request.setContentType("application/json");                    
        request.addRequestHeader("Accept","application/json");
        request.setPost(true);
        request.setWriteRequest(true);
        ///----------loading......
        InfiniteProgress inftprogress = new InfiniteProgress();
        Dialog dlg_progress = new Dialog();
        dlg_progress.setDialogUIID("dlg_progress");
        dlg_progress.setLayout(new BorderLayout());
        
        Label lbl_progress = new Label("  please wait");
        lbl_progress.getStyle().setFgColor(0x000000, false);
        lbl_progress.getStyle().setBgTransparency(0);
        dlg_progress.addComponent(BorderLayout.CENTER, FlowLayout.encloseCenterBottom(inftprogress, lbl_progress));
        dlg_progress.setTransitionInAnimator(CommonTransitions.createEmpty());
        dlg_progress.setTransitionOutAnimator(CommonTransitions.createEmpty());
        dlg_progress.showPacked(BorderLayout.CENTER, false);
        request.setDisposeOnCompletion(dlg_progress);

        NetworkManager.getInstance().addToQueueAndWait(request);
        request.setDisposeOnCompletion(dlg_progress);
        
        byte[] dataa = request.getResponseData();
        if (dataa != null) 
        {
            Reader reader = new InputStreamReader(new ByteArrayInputStream(dataa), "UTF-8");
            int chr;
            StringBuffer sb = new StringBuffer();
            String str_server_response = "";
            while ((chr = reader.read()) != -1)
            {
                sb.append((char) chr);
            }
            str_server_response = sb.toString(); 

            if(request.getResponseCode() == 200 || request.getResponseCode() == 201)
            {
                JSONParser json_parser = new JSONParser();
                Map map_response = json_parser.parseJSON(new InputStreamReader(new ByteArrayInputStream(dataa), "UTF-8"));

                String response  = map_response.get("ResponseBody").toString();
                System.out.println("Response = "+response);
                
                Command ok = new Command("Proceed");
                Command cancel = new Command("Cancel");
                
                if (response == null) {
                    Dialog.show("", "Failed to connect to server.Please retry....:", "Close", "Retry");
                }
                else if (response.equals("")) 
                {
                    Dialog.show("Error!!", "Error occured while processing your request", "Close", "Retry");
                }
                else if(!response.isEmpty()||!request.equals(""))
                {
                    if (response.toLowerCase().trim().equalsIgnoreCase("1")) 
                    {
                        Dialog.show("Declined!", "You do not qualify for this loan.To qualify, you must be a member for last six months.", "Close", "Retry");
                    } 
                    else if (response.toLowerCase().trim().equalsIgnoreCase("2")) 
                    {
                        Dialog.show("Declined!", "You do not qualify for this loan.You have an outstanding e-loan.", "Close", "Retry");
                    } 
                    else if (response.toLowerCase().trim().equalsIgnoreCase("3")) 
                    {
                        Dialog.show("Declined!", "You do not qualify for this loan.You have oustanding loan arrears.", "Close", "Retry");
                    } 
                    else if (response.toLowerCase().trim().equalsIgnoreCase("0")) 
                    {
                        Dialog.show("Declined!", "You do not qualify for this loan.Your free deposits is below the set minimum", "Close", "Retry");
                    }
                    else 
                    {
                        response = StringUtil.replaceAll(response, ",", "");
                        double amt =Double.parseDouble(response.toString()); 
                        if(Dialog.show("Success", "You qualify for a mobile Loan of up to KES " + amt + ", payable in 30 Days,interest rate of 6%  p.m", ok, cancel)  == ok)
                        {
                            createLoanScreen(amt);
                        }


                    }
                }
            }
            else
            {
                Dialog.show("", "Connect to Server", "Close", "Retry");
            }
        }
    }
    public void ApplyLoan(String amount) throws UnsupportedEncodingException, IOException{
        
        PhoneNumber = Storage.getInstance().readObject("PhoneNumber").toString();
        SaccoID = Storage.getInstance().readObject("SaccoID").toString();
        
        myUrl = "https://suresms.co.ke:45322/capi/PostAdvance";
        
        Hashtable hash =new Hashtable();
        hash.put("phone", PhoneNumber);
        hash.put("SaccoID", SaccoID);
        hash.put("period", "1");   
        hash.put("loantype", "Eloan");   
        hash.put("amount", amount);   

        final Result res=Result.fromContent(hash);
        final String checkthis=res.toString();
        
        ConnectionRequest request=new ConnectionRequest()
        {
            public void buildRequestBody(OutputStream thisstream) throws IOException
            {
                Writer andika=null;
                andika=new OutputStreamWriter(thisstream,"UTF-8");
                andika.write(checkthis);
                andika.flush();
                andika.close();
            }
        };
        request.setUrl(myUrl);
        request.setContentType("application/json");                    
        request.addRequestHeader("Accept","application/json");
        request.setPost(true);
        request.setWriteRequest(true);
        ///----------loading......
        InfiniteProgress inftprogress = new InfiniteProgress();
        Dialog dlg_progress = new Dialog();
        dlg_progress.setDialogUIID("dlg_progress");
        dlg_progress.setLayout(new BorderLayout());
        
        Label lbl_progress = new Label("  please wait");
        lbl_progress.getStyle().setFgColor(0x000000, false);
        lbl_progress.getStyle().setBgTransparency(0);
        dlg_progress.addComponent(BorderLayout.CENTER, FlowLayout.encloseCenterBottom(inftprogress, lbl_progress));
        dlg_progress.setTransitionInAnimator(CommonTransitions.createEmpty());
        dlg_progress.setTransitionOutAnimator(CommonTransitions.createEmpty());
        dlg_progress.showPacked(BorderLayout.CENTER, false);
        request.setDisposeOnCompletion(dlg_progress);

        NetworkManager.getInstance().addToQueueAndWait(request);
        request.setDisposeOnCompletion(dlg_progress);
        
        byte[] dataa = request.getResponseData();
        if (dataa != null) 
        {
            Reader reader = new InputStreamReader(new ByteArrayInputStream(dataa), "UTF-8");
            int chr;
            StringBuffer sb = new StringBuffer();
            String str_server_response = "";
            while ((chr = reader.read()) != -1)
            {
                sb.append((char) chr);
            }
            str_server_response = sb.toString(); 

            if(request.getResponseCode() == 200 || request.getResponseCode() == 201)
            {
                JSONParser json_parser = new JSONParser();
                Map map_response = json_parser.parseJSON(new InputStreamReader(new ByteArrayInputStream(dataa), "UTF-8"));

                String response  = map_response.get("ResponseBody").toString();
                System.out.println("Response = "+response);
                
                Command ok = new Command("Proceed");
                Command cancel = new Command("Cancel");
                
                if (response == null) {
                    Dialog.show("", "Failed to connect to server.Please retry....:", "Close", "Retry");
                }
                else if (response.equals("")) 
                {
                    Dialog.show("Error!!", "Error occured while processing your request", "Close", "Retry");
                }
                else if(request.equals("true"))
                {
                    showLoanSuccessForm(amount);
                }
                else{
                    Dialog.show("Request Failed", "Please retry.", "Close", "Retry");
                }
            }
            else
            {
                Dialog.show("", "Error Connecting to Server", "Close", "Retry");
            }
        }
    }
    private Container createMobileLoansScreen(){
        
        Container appLoan = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Label bottomSpace = new Label();
        Container tab1 = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        tab1.addComponent(BorderLayout.east(new Label("")).
                add(BorderLayout.WEST, new Label("  DigiPesa E loan", "profileName")));
        tab1.add(new SpanLabel("This product is meant to facilitate any member’s immediate short-term\n" +
                        "financial needs.\n" +
                        "Approval is immediate\n" +
                        "Repayment period is up to 6 months.\n" +
                        "This product attracts an interest rate of 5% p.m on reducing balance\n" +
                        "Any member who has saved consistently for six months or any member\n" +
                        "with an active loan, qualifies for this if their outstanding loan is less than\n" +
                        "their savings.",  "detailHead"));
        appLoan.addComponent(tab1);
        
        return appLoan;
    }
    private void createLoanScreen(double amtlimit){
        
        
        TextField txt_Amt = new TextField("", " Enter Amount", 20, TextField.NUMERIC);
        Button btn_PayFosa = new Button("Apply Loan", "transactionButtons");
        
        Container cnt_fosaContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        cnt_fosaContainer.addComponent(new Label("Enter Amount", "FlagButton"));
        cnt_fosaContainer.addComponent(txt_Amt);
        cnt_fosaContainer.addComponent(btn_PayFosa);
        cnt_fosaContainer.setUIID("ContaWra");
        
        btn_PayFosa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if(txt_Amt.getText().equals("")){
                    Dialog.show("", "Please Enter the Amount", "Close", "Retry");
                }
                else{
                    String Amount = StringUtil.replaceAll(txt_Amt.getText(), ",","");
                    if(Double.parseDouble(Amount) < 100){
                        Dialog.show("", "Amount should be more than KES 100.", "Close", "Retry");
                    }
                    else if(Double.parseDouble(Amount)> amtlimit){
                        Dialog.show("SORRY!!", "Your mobile Loan Request has been declined.Amount should be less than KES " + amtlimit + ".", "Close", "Retry");
                    }
                    else{
                    }
                }
            }
        });
                
    }
    public void showLoanSuccessForm(String loanAmt)
    {
        Form successForm = new Form();
        successForm.setLayout(new LayeredLayout());
        
        Form lastForm = getCurrentForm();
        Command backCommand = Command.create("", FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, UIManager.getInstance().getComponentStyle("cartTitleCommand")),
                e-> lastForm.showBack());
        successForm.setBackCommand(backCommand);
                
        Font materialFont = FontImage.getMaterialDesignFont();
        int w = Display.getInstance().getDisplayWidth()/2;
        
        FontImage accountImage = FontImage.createFixed("\ue86c", materialFont, 0x19b019, w, w);
        Label iconPlaceholder = new Label("","ProfilePic");
        Label notesLabel = new Label(accountImage, "ProfilePic");
        Component.setSameHeight(notesLabel, iconPlaceholder);
        Component.setSameWidth(notesLabel, iconPlaceholder);
        Label bottomSpace = new Label();
        
        Container tab1 = BorderLayout.centerAbsolute(BoxLayout.encloseY
        (
                iconPlaceholder,
                new Label("Success", "Success"),
                new SpanLabel("Loan Request of Ksh."+loanAmt+" has been submitted Successfully and is being processed.",  "SuccessBody"),
                bottomSpace
        ));
        successForm.add(tab1);
        
        Button doneButton = new Button("DONE");
        doneButton.setUIID("SuccessButton");
        doneButton.addActionListener(e -> new HomePageForm().showMain());
        
        successForm.add(BorderLayout.south(doneButton));
        successForm.addShowListener(e -> 
        {
            iconPlaceholder.getParent().replace(iconPlaceholder, notesLabel, CommonTransitions.createFade(1500));
        });
        successForm.showBack();
    }
    protected void showScreen(String title, Component content){
            Form depoForm = new Form(title, new BorderLayout());
            content.setUIID("ComponentContainer");
            Toolbar toolbar = depoForm.getToolbar();
            
            Button aplnw = new Button("APPLY NOW", "transactionButtons");
            Button checkelig = new Button("CHECK ELIGIBILITY", "transactionButtons");

            Form previous = getCurrentForm();
            toolbar.setBackCommand("", Toolbar.BackCommandPolicy.AS_ARROW,
                e -> previous.showBack());
            
            Command ok = new Command("Proceed");
            Command cancel = new Command("Cancel");
        
            depoForm.add(BorderLayout.CENTER, content);
            depoForm.add(BorderLayout.SOUTH, checkelig);
            
            checkelig.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent evt) 
                {
                    try {
                        checkLimit();
                    } catch (IOException ex) {
                    }
                }
            });
            
            depoForm.show();
    }
}
