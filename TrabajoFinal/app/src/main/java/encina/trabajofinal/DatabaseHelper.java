package encina.trabajofinal;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper  {

    private static final String DATABASE_NAME = "BD";   //nombre de la bd (string)
    private static final int DATABASE_VERSION = 1;      //version de la bd (puede actualizarse mediante metodos)

    //Creacion de una tabla en la bd
    private static final String DATABASE_CREATE = "CREATE TABLE Usuarios (id integer primary key autoincrement not null, usuario varchar not null, email varchar not null, contrasenia varchar not null); ";

    private SQLiteDatabase bd;                        //Instancia de la clase manejadora de la bd

    public DatabaseHelper(Context context) {              //Constructor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);   //Herencia del helper, contexto (contexto de accion para el helper, nombre de la bd, factory null, version de la bd)
    }

    public void onCreate(SQLiteDatabase db){ //metodo llamado automaticamente cuando se crea una instancia de SQLiteOpenHelper
        try{
            db.execSQL(DATABASE_CREATE);   //Ejecuta la query que crea la tabla de usuarios
        } catch(SQLException e){           //Si se produce una excepcion de sql se imprime un metodo
            e.printStackTrace();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int olvdVersion, int newVersion){ //metodo ejecutado si se identifica que se tiene una version antigua de la base de datos
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        onCreate(db);
    }

    public SQLiteDatabase open(){           //Metodo para poder escribir datos en la bd
        bd = this.getWritableDatabase();

        return bd;
    }

    public SQLiteDatabase read(){          //Metodo para leer datos de la bd
        bd = this.getReadableDatabase();

        return bd;
    }

    public void close(){       //Metodo para cerrar la bd
        bd.close();
    }
}
