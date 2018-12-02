package co.feits.ican;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity
{
    EditText etName,etDateofBirth,etEmail,etPassword;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        etName=findViewById(R.id.et_name);
        etDateofBirth=findViewById(R.id.et_date_of_birth);
        etEmail=findViewById(R.id.et_LoginEmail);
        etPassword=findViewById(R.id.et_LoginPassword);

        Button RegisterButton=findViewById(R.id.button_register);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(etName.getText().toString().isEmpty()
                        ||etDateofBirth.getText().toString().isEmpty()
                        ||etEmail.getText().toString().isEmpty()
                        ||etPassword.getText().toString().isEmpty())

                {
                    Toast.makeText(RegisterActivity.this, "Input field is missing", Toast.LENGTH_SHORT).show();
                    return;
                }


                else
                {
                    progressDialog= new ProgressDialog(RegisterActivity.this);
                    progressDialog.setTitle("Uploading Details");
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();
                    String email=etEmail.getText().toString();
                    String password=etPassword.getText().toString();
                    createUser(email,password);

                }

            }
        });
    }





    public void createUser(final String email, String password)
    {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("User");
                            Users users= new Users();
                            users.setName(etName.getText().toString());
                            users.setDateofBirth(etDateofBirth.getText().toString());
                            users.setEmail(email);

                            mRef.child(mAuth.getCurrentUser().getUid()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }


                    }
                });


    }



}
