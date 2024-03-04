/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.transactions;

import com.cloudpesa.digipesa.forms.HomePageForm;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.processing.Result;
import com.codename1.ui.Button;
import static com.codename1.ui.CN.getCurrentForm;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.list.GenericListCellRenderer;
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
import java.util.List;
import java.util.Map;

/**
 *
 * @author Festus
 */
public class DepostLoans {
    private String myUrl, Reply;
    protected ComboBox cbo_accs = new ComboBox("","");
    protected ComboBox cbo_loans = new ComboBox("","","");
    private String [] str_acoptions = new String[1];  //here i needed to declare an empty arraay...yenye nkiload details itazishikilia, then kuna mahali naitumia kwa picker.....
    
    public void reloansScreen() 
    {
        try {
            loadLoans();
        } catch (IOException ex) {
        }
    }
    public void loadLoans() throws UnsupportedEncodingException, IOException
    {
        int w = Display.getInstance().getDisplayWidth()-1;
        myUrl = "https://suresms.co.ke:45322/capi/CurrentLoans";
        
        Hashtable hash =new Hashtable();
        hash.put("phone", "");
        hash.put("SaccoID", "");
        hash.put("Key", "");           
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
        dlg_progress.setWidth(w);
        
        Label lbl_progress = new Label("  Loading Loans");
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
                
                String allloans_response = map_response.get("ResponseBody").toString();
                
                allloans_response = StringUtil.replaceAll(allloans_response, ", ", "**");
                allloans_response = StringUtil.replaceAll(allloans_response, "[", "");
                allloans_response = StringUtil.replaceAll(allloans_response, "]", "");
                
                if (allloans_response.equals("")||allloans_response.equals("[]")){
                    Dialog.show("", "No Loans Found", "Close", "Retry");
                }
                else{
                    System.out.println("Available Loans: " + allloans_response);

                    List<String> loan_items = StringUtil.tokenize(allloans_response, "**");

                    String [] str_acoptions = new String[loan_items.size()];
                    StringBuilder sb_options = new StringBuilder();

                    for (int i = 0; i < loan_items.size(); i++) 
                    {
                        String item = loan_items.get(i);

                        String loanID = StringUtil.tokenize(item, ":::").get(0);
                        String loanName = StringUtil.tokenize(item, ":::").get(1);
                        str_acoptions[i] = loanID;// + ": "+loanName;

                        sb_options.append(str_acoptions[i]);

                    }
                    ComboBox cbo_loans = new ComboBox(str_acoptions);
        
                    TextField txt_Amt = new TextField("", " Enter Amount", 20, TextField.NUMERIC);
                    Button btn_PayFosa = new Button("Deposit", "cBtns");
                    btn_PayFosa.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            if(txt_Amt.getText().equals("")){
                                Dialog.show("", "Please Enter the Amount", "Close", "Retry");
                            }
                            String Amount = StringUtil.replaceAll(txt_Amt.getText(), ",","");
                            if(Double.parseDouble(Amount) < 50){
                                Dialog.show("", "You Cannot transfer less than Ksh. 50", "Close", "Retry");
                            }
                            else{
                                try {transact(cbo_loans.getSelectedItem().toString(), Amount.toString());} catch (IOException ex) {}
                            }
                        }
                    });
                    
                    Container demoContainer = BorderLayout.center(BoxLayout.encloseY(new SpanLabel("Select Loan", "FlagButton"), cbo_loans, new SpanLabel("Enter Amount", "FlagButton"), txt_Amt));
                    demoContainer.add(BorderLayout.SOUTH, btn_PayFosa);
                    demoContainer.setUIID("Wrapper");
                    
                    showScreen("Loan Repayment", BoxLayout.encloseY(demoContainer));
                }
            }
            else
            {
            }
        }
    }

    public void transact(String Account, String Amount) throws IOException
    {
        String url = "https://suresms.co.ke:4242/mobileapi/api/Deposits";
        String PhoneNumber = Storage.getInstance().readObject("WendaniPhoneNumber").toString();
        String Token = Storage.getInstance().readObject("WendaniToken").toString();
        
        Hashtable hash = new Hashtable();
        hash.put("mobile_no", PhoneNumber);
        hash.put("amount", Amount);
        hash.put("acc_to", Account);

        final Result res = Result.fromContent(hash);
        final String checkthis = res.toString();
        
        ConnectionRequest request = new ConnectionRequest()
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
        request.setUrl(url);
        request.setContentType("application/json");                    
        request.addRequestHeader("Accept","application/json");
        request.addRequestHeader("Authorization", "Basic dXNlcm5hbWU6cGFzc3dvcmQ=");
        request.addRequestHeader("Token", Token);
        request.setPost(true);
        request.setWriteRequest(true);
        ///----------loading......
        InfiniteProgress inftprogress = new InfiniteProgress();
        Dialog dlg_progress = new Dialog();
        dlg_progress.setDialogUIID("dlg_progress");
        dlg_progress.setLayout(new BorderLayout());
        
        Label lbl_progress = new Label("  Please Wait...");
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
            if(request.getResponseCode() == 200 || request.getResponseCode() == 201)
            {
                String response = new String(dataa);
                if (response.contains("\"Message\"")) {
                    Dialog.show("Oops!!!", "Cannot connect to the server", "Ok", "Retry");
                } else {
                    JSONParser json_parser = new JSONParser();
                    Map map_response = json_parser.parseJSON(new InputStreamReader(new ByteArrayInputStream(dataa), "UTF-8"));
                    String status_code = map_response.get("status_code").toString();
                    String description = map_response.get("Description").toString();
                    String Success = map_response.get("Success").toString();

                    System.out.println("Description: " + description);
                    if(Success.equals("true"))
                    {
                        Dialog.show("Success!!!", "Please wait for a pop up from Safaricom and enter your Service PIN.", "Close", "Retry");
                        new HomePageForm().BosaDemoHome();
                    }
                    else
                    {
                        Dialog.show("Sorry", description, "Close", "Retry");
                    }
                }

            }
            else
            {
                System.out.println("Request was not successful");
            }
        }
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
