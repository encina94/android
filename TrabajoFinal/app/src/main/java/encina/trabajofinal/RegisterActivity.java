package encina.trabajofinal;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    //Creo instancias de los datos ingresados y botones para luego asociarlos al layout dentro del metodo onCreate
    private TextInputLayout usuario;
    private TextInputLayout email;
    private TextInputLayout contraseña;
    private Button botonRegistrarse;
    private DatabaseHelper dbHelper;                            //Creo una variable de de tipo DatabaseHelper para despues instanciarla

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Asocio las instancias de los datos ingresados por el usuario en los TextInputLayout
        usuario = (TextInputLayout) findViewById(R.id.usuarioInput);
        email = (TextInputLayout) findViewById(R.id.emailInput);
        contraseña = (TextInputLayout) findViewById(R.id.passInput);
        dbHelper = new DatabaseHelper(this);                            //Instancio el manejador de bd en la variable dbHelper

        botonRegistrarse = (Button) findViewById(R.id.btnRegistrarse);  //Asocio la instancia (referencia) con la interfaz(layout)
        botonRegistrarse.setOnClickListener(new View.OnClickListener() {   //Asocio un manejador de eventos para el boton (listener)
            public void onClick(View v) {
                validarDatos();

           //     prueba();
            }
        });
    }

    //Metodo booleano para validar el correo que se le pase
    //Este metodo va a ser llamado dentro de otro metodo cuando se haga click en el boton registrar
    private boolean validarEmail(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {  //Se aplica el patron para comprobar si el mail es valido
            email.setError("Correo electrónico inválido");        //Se muestra un msj en el TextInputLayout
            return false;
        } else {
            email.setError(null);                                 //Si es valido no se muestra nada y se retorna true
        }
        return true;
    }

    private boolean validarUsuario(String user) {
        SQLiteDatabase bd = dbHelper.open();
        String consulta = "SELECT usuario FROM Usuarios WHERE usuario= '" + user + "';";
        //Si el usuario no existe se dispara una excepcion, por eso se usa el try y el catch
        try {
            Cursor fila = bd.rawQuery(consulta, null);
            if (fila.moveToNext()) {                   //si existe un usuario en la bd con el mismo nombre que el ingresado, se prosigue, sino se dispara una excepcion
                String userBd = fila.getString(0);                   //lo guardo en una variable
                if (user.equals(userBd)) {                           //Lo comparo con el ingresado por el usuario y le aviso que no esta disponible si son iguales
                    usuario.setError("Usuario no disponible");        //Se setea el textInputLayout
                    bd.close();
                    return false;                                   //retorno falso
                } else {                                            //Si los usuarios no son iguales
                    usuario.setError(null);
                    bd.close();
                    return true;                                    //retorno true porque paso la validacion
                }
            }else{                                            //No existe un usuario la bd asique el usuario es valido
                usuario.setError(null);
                bd.close();
                return true;                                 //Paso la validacion
            }

        }catch(SQLException e){                        //Al no existir usuario en bd, se dispara una excepcion y es capturada
            usuario.setError(null);                   //Entonces quiere decir que el usuario ingresado es valido (puede ser ingresado y se retorna true
            bd.close();
            return true;
        }

        }


    private void validarDatos() {
        String correo = email.getEditText().getText().toString();
        String user = usuario.getEditText().getText().toString();
        String pass = contraseña.getEditText().getText().toString();

        boolean userValidacion = validarUsuario(user);
        boolean correoValidacion = validarEmail(correo);


        if (userValidacion && correoValidacion) {

            //Alta para guardar un registro
            ContentValues registro = new ContentValues();   //contenido de los valores que se van a guardar
            registro.put("usuario", user);                  //datos a guardar en las filas determinadas
            registro.put("email", correo);
            registro.put("contrasenia", pass);

            SQLiteDatabase bd = dbHelper.open();           //abro la bd

            //Aca inserto todos los datos
            bd.insert("Usuarios", null, registro);           //Inserto los datos guardados en registro en la tabla Usuarios

            //cierro la consulta de prueba
            dbHelper.close();                              //Cierro la bd
            //Mando un msj de que el registro se hizo con exito
            Toast.makeText(this, "Se guarda el registro", Toast.LENGTH_LONG).show();
            //Redirecciono a la pantalla de login una vez que se registro el usuario
            Intent intent= new Intent(RegisterActivity.this, LoginActivity.class);     //asocio la instancia de redireccion de la activity actual a la de register
            startActivity(intent);                                                     //Realizo la redireccion
        } else {
            Toast.makeText(this, "El registro no fue completado", Toast.LENGTH_LONG).show();
        }

    }

    /*/    private void prueba(){

        SQLiteDatabase bd = dbHelper.open();

        String consultita = "SELECT * FROM Usuarios WHERE usuario= 'fersen';";
        Cursor filaa = bd.rawQuery(consultita, null);
        if(filaa.moveToNext()){
            String uuuserbd  = filaa.getString(1);
            String emaaaaailbd = filaa.getString(2);
            String contraaaseniabd = filaa.getString(3);
        }
        //cierro la consulta de prueba
        dbHelper.close();

    }/*/

    //private void altaBd(usuario,email,contrasenia);   la idea es hacer una transaccion que ejecute el alta del registro

}




