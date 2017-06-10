package my.app.yuval.intel_managment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMail extends Activity {

    private Activity activityReference;
    Button send;
    EditText edtsubject;
    EditText edtmsg;
    String workerID;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        activityReference = this;
        setContentView(R.layout.mail_dialog);
        Intent intent = getIntent();
        workerID = intent.getStringExtra("WorkerID");
        mail = intent.getStringExtra("Mail");


        edtsubject = (EditText) findViewById(R.id.edtSubject);
        edtmsg = (EditText) findViewById(R.id.edttxt2);
        send = (Button)findViewById(R.id.SendButton);

        Initedt(edtsubject, "Subject");
        Initedt(edtmsg, "Message...");

        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                String sub = edtsubject.getText().toString();
                String msg = edtmsg.getText().toString();

                if(sub.equals("Subject") || sub.length() < 1)
                {
                    Toast.makeText(activityReference, "Please enter a subject", Toast.LENGTH_LONG).show();
                    return;
                }

                if(msg.equals("Message...") || msg.length() < 1)
                {
                    Toast.makeText(activityReference, "Please enter a meesage", Toast.LENGTH_LONG).show();
                    return;
                }


                if(mail.equals("NOT EXIST"))
                {
                    Toast.makeText(activityReference, "Worker ID doesn't exist", Toast.LENGTH_LONG).show();
                    return;
                }

                String[] TO = {mail};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT ,sub);
                emailIntent.putExtra(Intent.EXTRA_TEXT, msg);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(activityReference,
                            "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    return;
                }
                return;

            }
        });

    }

    private void Initedt(final EditText edt,final String value)
    {
        edt.setOnTouchListener( new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edt.getText().toString().equals(value)){
                    edt.setText("");
                }
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && TextUtils.isEmpty(edt.getText().toString())){
                    edt.setText(value);
                } else if (hasFocus && edt.getText().toString().equals(value)){
                    edt.setText("");
                }
            }
        });


    }

}