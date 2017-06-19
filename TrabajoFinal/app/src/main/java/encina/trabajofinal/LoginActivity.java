package encina.trabajofinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button botonSesion;                    //Instancia para manejar el botonSesion
    private Button botonRegistrar;                //Instancia para manejar el botonRegistrar
    private TextInputLayout usuario;
    private TextInputLayout contraseña;
    private DatabaseHelper dbHelper;                            //Creo una variable de de tipo DatabaseHelper para despues instanciarla





    @Override
    protected void onCreate(Bundle savedInstanceState) { //creo la instancia para relacionar con el layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);          //relaciono la clase con el layout de login
       /*/ Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);  //creo una instancia para la barra creada en el layout
        setSupportActionBar(toolbar);                            //uso la instancia  /*/


        //Referencia al boton
        botonSesion = (Button) findViewById(R.id.btnSession);  //Asocio la instancia (referencia) con la interfaz(layout)
        usuario = (TextInputLayout) findViewById(R.id.userInput);
        contraseña = (TextInputLayout) findViewById(R.id.contraInput);
        dbHelper = new DatabaseHelper(this);                            //Instancio el manejador de bd en la variable dbHelper

        botonSesion.setOnClickListener(new View.OnClickListener(){   //Asocio un manejador de eventos para el boton (listener)
            public void onClick(View v){
                validarDatos();
            }
        });

        botonRegistrar = (Button)findViewById(R.id.btnRegistrar);
        botonRegistrar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){                                                   //el metodo que indica lo que se va a realizar cuando se usa el evento de click
                Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);     //asocio la instancia de redireccion de la activity actual a la de register
                startActivity(intent);                                                     //Realizo la redireccion

                //redireccion a nuevo activityRegister();
            }
        });
    }

    private boolean validarUsuario(String user){
        SQLiteDatabase bd = dbHelper.open();
        String consulta = "SELECT usuario FROM Usuarios WHERE usuario= '" + user + "';";
        //Si el usuario no existe se dispara una excepcion, por eso se usa el try y el catch
        try {
            Cursor fila = bd.rawQuery(consulta, null);
            if (fila.moveToNext()) {                   //si existe un usuario en la bd con el mismo nombre que el ingresado, se prosigue, sino se dispara una excepcion
                String userBd = fila.getString(0);                   //lo guardo en una variable
                if (user.equals(userBd)) {                           //Lo comparo con el ingresado por el usuario y le aviso que no esta disponible si son iguales
                    usuario.setError(null);        //Se setea el textInputLayout
                    bd.close();
                    return true;                                   //retorno falso
                } else {                                            //Si los usuarios no son iguales
                    usuario.setError("Usuario inexistente");
                    bd.close();
                    return false;                                    //retorno true porque paso la validacion
                }
            }else{                                            //No existe un usuario la bd asique el usuario es valido
                usuario.setError("Usuario inexistente");
                bd.close();
                return false;                                 //Paso la validacion
            }

        }catch(SQLException e){                        //Al no existir usuario en bd, se dispara una excepcion y es capturada
            usuario.setError("Usuario inexistente");                   //Entonces quiere decir que el usuario ingresado es valido (puede ser ingresado y se retorna true
            bd.close();
            return false;
        }
    }

    private boolean validarPass(String pass,String user){
        SQLiteDatabase bd = dbHelper.open();
        String consulta = "SELECT contrasenia FROM Usuarios WHERE usuario= '" + user + "';";
        //Si el usuario no existe se dispara una excepcion, por eso se usa el try y el catch
        try {
            Cursor fila = bd.rawQuery(consulta, null);
            if (fila.moveToNext()) {                   //si existe un usuario en la bd con el mismo nombre que el ingresado, se prosigue, sino se dispara una excepcion
                String passBd = fila.getString(0);                   //lo guardo en una variable
                if (pass.equals(passBd)) {                           //Lo comparo con el ingresado por el usuario y le aviso que no esta disponible si son iguales
                    contraseña.setError(null);        //Se setea el textInputLayout
                    bd.close();
                    return true;                                   //retorno verdadero
                } else {                                            //Si los usuarios no son iguales
                    contraseña.setError("La contraseña es incorrecta");
                    bd.close();
                    return false;                                    //retorno true porque paso la validacion
                }
            }else{                                            //No existe un usuario la bd asique el usuario es valido
                contraseña.setError("La contraseña es incorrecta");
                bd.close();
                return false;                                 //Paso la validacion
            }

        }catch(SQLException e){                        //Al no existir usuario en bd, se dispara una excepcion y es capturada
            contraseña.setError("La contraseña es incorrecta");                   //Entonces quiere decir que el usuario ingresado es valido (puede ser ingresado y se retorna true
            bd.close();
            return false;
        }
    }


    private void validarDatos(){

        String user = usuario.getEditText().getText().toString();
        String pass = contraseña.getEditText().getText().toString();

        boolean userValidacion = validarUsuario(user);
        boolean passValidacion = validarPass(pass,user);

        if (userValidacion && passValidacion) {
            Toast.makeText(this, "Usuario logueado", Toast.LENGTH_LONG).show();
            Intent intent= new Intent(LoginActivity.this, ItemListActivity.class);     //asocio la instancia de redireccion de la activity actual a la de register
            startActivity(intent);                                                     //Realizo la redireccion

        }
        else{
            Toast.makeText(this, "Hay campos erroneos", Toast.LENGTH_LONG).show();
        }

    }

}
