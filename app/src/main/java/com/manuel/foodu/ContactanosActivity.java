package com.manuel.foodu;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ContactanosActivity extends AppCompatActivity {
    TextInputLayout textInputLayout_nombre, textInputLayout_correo, textInputLayout_mensaje;
    RadioGroup radioGroup;
    MaterialRadioButton materialRadioButton, materialRadioButton_queja, materialRadioButton_sugerencia;
    final String correoProyecto = "proyectosmariorecio@gmail.com", contrasenaProyecto = "MR1704002053CV";
    String nombre, correo, mensaje;
    static String asunto;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactanos);
        setTitle("Quejas y sugerencias");
        textInputLayout_nombre = findViewById(R.id.textInputLayout_nombre);
        textInputLayout_correo = findViewById(R.id.textInputLayout_correo);
        textInputLayout_mensaje = findViewById(R.id.textInputLayout_mensaje);
        materialRadioButton_queja = findViewById(R.id.radio_button_queja);
        materialRadioButton_sugerencia = findViewById(R.id.radio_button_sugerencia);
        radioGroup = findViewById(R.id.radio_group_opciones);
        Objects.requireNonNull(textInputLayout_nombre.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (textInputLayout_nombre.getEditText().getText().toString().isEmpty()) {
                    textInputLayout_nombre.setError("El Nombre es obligatorio");
                } else {
                    textInputLayout_nombre.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        Objects.requireNonNull(textInputLayout_correo.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!validarEmail(charSequence.toString())) {
                    textInputLayout_correo.setError("Formato de correo electrónico inválido");
                } else {
                    textInputLayout_correo.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        Objects.requireNonNull(textInputLayout_mensaje.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (textInputLayout_mensaje.getEditText().getText().toString().isEmpty()) {
                    textInputLayout_mensaje.setError("El Mensaje es obligatorio");
                } else {
                    textInputLayout_mensaje.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        MaterialCardView materialCardView_enviar = findViewById(R.id.card_view_enviar);
        materialCardView_enviar.setOnClickListener(view -> {
            int radioID = radioGroup.getCheckedRadioButtonId();
            materialRadioButton = findViewById(radioID);
            nombre = Objects.requireNonNull(textInputLayout_nombre.getEditText()).getText().toString().trim();
            correo = Objects.requireNonNull(textInputLayout_correo.getEditText()).getText().toString().trim();
            mensaje = Objects.requireNonNull(textInputLayout_mensaje.getEditText()).getText().toString().trim();
            if (!evaluarCampos(nombre) && !validarEmail(correo) && !evaluarCampos(mensaje) && (!materialRadioButton_queja.isChecked() || !materialRadioButton_sugerencia.isChecked())) {
                Snackbar.make(view, "Complete los campos", Snackbar.LENGTH_SHORT).show();
            } else {
                if (evaluarCampos(nombre)) {
                    if (validarEmail(correo)) {
                        if (evaluarCampos(mensaje)) {
                            if (materialRadioButton_queja.isChecked() || materialRadioButton_sugerencia.isChecked()) {
                                asunto = materialRadioButton.getText().toString().trim();
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                Properties properties = new Properties();
                                properties.put("mail.smtp.host", "smtp.gmail.com");
                                properties.put("mail.smtp.socketFactory.port", "465");
                                properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                                properties.put("mail.smtp.auth", "true");
                                properties.put("mail.smtp.port", "465");
                                session = Session.getDefaultInstance(properties, new Authenticator() {
                                    @Override
                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        return new PasswordAuthentication(correoProyecto, contrasenaProyecto);
                                    }
                                });
                                try {
                                    Message message = new MimeMessage(session);
                                    message.setFrom(new InternetAddress(correo, nombre + " (" + correo + ")"));
                                    message.setSubject(asunto + "s de FoodU");
                                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoProyecto));
                                    message.setContent(mensaje, "text/html; charset=utf-8");
                                    new SendMail().execute(message);
                                } catch (Exception e) {
                                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Snackbar.make(view, "No olvides elegir entre Queja o Sugerencia", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            textInputLayout_mensaje.setError("El Mensaje es obligatorio");
                        }
                    } else {
                        textInputLayout_correo.setError("El Correo electrónico es obligatorio");
                    }
                } else {
                    textInputLayout_nombre.setError("El Nombre es obligatorio");
                }
            }
        });
    }

    public boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public boolean evaluarCampos(String valor) {
        return !valor.equals("");
    }

    @SuppressLint("StaticFieldLeak")
    public class SendMail extends AsyncTask<Message, String, String> {
        public ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ContactanosActivity.this, "Procesando...", "Por favor, espere un momento", true, false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Éxito";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s.equals("Éxito")) {
                if (ContactanosActivity.asunto.equals("Queja")) {
                    Toast.makeText(ContactanosActivity.this, "Gracias por sus quejas", Toast.LENGTH_SHORT).show();
                } else if (ContactanosActivity.asunto.equals("Sugerencia")) {
                    Toast.makeText(ContactanosActivity.this, "Gracias por sus sugerencias", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(ContactanosActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(ContactanosActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}