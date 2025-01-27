package com.example.visitmanagement;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_id, et_pw, et_name,et_phone, et_belong;
    private Button btn_register, btn_upload;
    private TextView tv_photo;
    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    String TAG = "RegisterActivity";

    Uri uri;//이미지

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //값 찾아주기
        et_id=findViewById(R.id.et_id);
        et_pw=findViewById(R.id.et_pw);
        et_name=findViewById(R.id.et_name);
        et_phone=findViewById(R.id.et_phone);
        et_belong=findViewById(R.id.et_belong);

//        tv_photo=findViewById(R.id.tv_photo);
        btn_upload = findViewById(R.id.btn_upload);

        imageview = (ImageView) findViewById(R.id.imageView);

        //업로드 버튼 클릭
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
//                startActivityForResult(intent, 1);
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        //회원가입 버튼 클릭 시 수행
        btn_register= findViewById(R.id.btn_register);
        btn_register.setOnClickListener(view -> {
            //editText에 입력되어 있는 값 get
            String ID=et_id.getText().toString();
            String Password=et_pw.getText().toString();
            String Name=et_name.getText().toString();
            String Phone_Num=et_phone.getText().toString();
            String Belonging=et_belong.getText().toString();

//            Intent intent=new Intent(RegisterActivity.this, Mainpage.class);
//            startActivity(intent);

            Response.Listener<String> responseListener= response -> {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    boolean success=jsonObject.getBoolean("success");
                    if(success){
                        String image=jsonObject.getString("profileimage");
                        Toast.makeText(getApplicationContext(),"회원가입을 완료하였습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegisterActivity.this, menu.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            };
            // 서버로 Volley를 이용해서 요청을 함.
            RegisterRequest registerRequest = new RegisterRequest(ID, Password, Name, Phone_Num, Belonging, uri, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
            queue.add(registerRequest);
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            imageview.setImageURI(uri);

        }
    }

}